package ir.farsirib.shenavarlib.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;

import ir.farsirib.shenavarlib.StandOutWindow;
import ir.farsirib.shenavarlib.Utils;
import ir.farsirib.shenavarlib.constants.StandOutFlags;
import ir.farsirib.R;

import java.util.LinkedList;
import java.util.Queue;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_PHONE;

public class Window extends FrameLayout{
	@Override
	public void onConfigurationChanged(Configuration newConfiguration){
		super.onConfigurationChanged(newConfiguration);
		StandOutWindow.StandOutLayoutParams params = getLayoutParams();
		DisplayMetrics mDisplayMetrics = mContext.getResources().getDisplayMetrics();
		if(data.getBoolean(WindowDataKeys.IS_MAXIMIZED)){
			edit().setSize(1f, 1f).setPosition(0, 0).commit();
		}
	};

	public static final int VISIBILITY_GONE = 0;
	public static final int VISIBILITY_VISIBLE = 1;
	public static final int VISIBILITY_TRANSITION = 2;

	static final String TAG = "Window";
	public Class<? extends StandOutWindow> cls;
	public int id;
	public int type;
	public int visibility;
	public boolean focused;
	public StandOutWindow.StandOutLayoutParams originalParams;
	public int flags;
	public TouchInfo touchInfo;
	public Bundle data;
	int displayWidth, displayHeight;
	private final StandOutWindow mContext;
	private LayoutInflater mLayoutInflater;
	private VideoView videoview;
	private VideoView mVideoView2 = null;
	private int pauseTime = -1;
	private Uri uri;

	public Window(Context context) {
		super(context);
		mContext = null;
	}

	@SuppressLint("ClickableViewAccessibility")
	public Window(final StandOutWindow context, final int id) {
		super(context);
		context.setTheme(context.getThemeStyle());

		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);

		this.cls = context.getClass();
		this.id = id;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			type = TYPE_APPLICATION_OVERLAY;
		else
			type = TYPE_PHONE;
		this.originalParams = context.getParams(id, type,this);
		this.flags = context.getFlags(id);
		this.touchInfo = new TouchInfo();
		touchInfo.ratio = (float) originalParams.width / originalParams.height;
		this.data = new Bundle();
		DisplayMetrics metrics = mContext.getResources()
				.getDisplayMetrics();
		displayWidth = metrics.widthPixels;
		displayHeight = (int) (metrics.heightPixels - 25 * metrics.density);
		View content;
		FrameLayout body;
		if (Utils.isSet(flags, StandOutFlags.FLAG_DECORATION_SYSTEM)) {
			content = getSystemDecorations();
			body = (FrameLayout) content.findViewById(R.id.body);
		} else {
			content = new FrameLayout(context);
			content.setId(R.id.content);
			body = (FrameLayout) content;
		}
		addView(content);
		body.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean consumed = false;
				consumed = context.onTouchHandleMove(id, Window.this, v, event)
						|| consumed;
				consumed = context.onTouchBody(id, Window.this, v, event)
						|| consumed;
				return consumed;
			}
		});
		context.createAndAttachView(id, body);
		if (body.getChildCount() == 0) {
			throw new RuntimeException(
					"You must attach your view to the given frame in createAndAttachView()");
		}
		if (!Utils.isSet(flags,
				StandOutFlags.FLAG_FIX_COMPATIBILITY_ALL_DISABLE)) {
			fixCompatibility(body);
		}
		if (!Utils.isSet(flags,
				StandOutFlags.FLAG_ADD_FUNCTIONALITY_ALL_DISABLE)) {
			addFunctionality(body);
		}
		setTag(body.getTag());
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		StandOutWindow.StandOutLayoutParams params = getLayoutParams();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (mContext.getFocusedWindow() != this) {
				mContext.focus(id);
			}
		}
		if (event.getPointerCount() >= 2
				&& Utils.isSet(flags,
				StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE)
				&& (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
			touchInfo.scale = 1;
			touchInfo.dist = -1;
			touchInfo.firstWidth = params.width;
			touchInfo.firstHeight = params.height;
			return true;
		}
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_OUTSIDE:
				if (mContext.getFocusedWindow() == this) {
					mContext.unfocus(this);
				}
				mContext.onTouchBody(id, this, this, event);
				break;
		}
		if (event.getPointerCount() >= 2
				&& Utils.isSet(flags,
				StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE)) {
			float x0 = event.getX(0);
			float y0 = event.getY(0);
			float x1 = event.getX(1);
			float y1 = event.getY(1);
			double dist = Math
					.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_MOVE:
					if (touchInfo.dist == -1) {
						touchInfo.dist = dist;
					}
					touchInfo.scale *= dist / touchInfo.dist;
					touchInfo.dist = dist;
					edit().setAnchorPoint(.5f, .5f)
							.setSize(
									(int) (touchInfo.firstWidth * touchInfo.scale),
									(int) (touchInfo.firstHeight * touchInfo.scale))
							.commit();
					break;
			}
			mContext.onResize(id, this, this, event);
		}
		return true;
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (mContext.onKeyEvent(id, this, event)) {
			Log.d(TAG, "Window " + id + " key event " + event
					+ " cancelled by implementation.");
			return false;
		}
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_BACK:
					mContext.unfocus(this);
					return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	public boolean onFocus(boolean focus) {
		if (!Utils.isSet(flags, StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE)) {
			if (focus == focused) {
				// window already focused/unfocused
				return false;
			}
			focused = focus;
			if (mContext.onFocusChange(id, this, focus)) {
				Log.d(TAG, "Window " + id + " focus change "
						+ (focus ? "(true)" : "(false)")
						+ " cancelled by implementation.");
				focused = !focus;
				return false;
			}
			if (!Utils.isSet(flags,
					StandOutFlags.FLAG_WINDOW_FOCUS_INDICATOR_DISABLE)) {
				View content = findViewById(R.id.content);
				if (focus) {
					content.setBackgroundResource(R.drawable.border_focused);
				} else {
					if (Utils
							.isSet(flags, StandOutFlags.FLAG_DECORATION_SYSTEM)) {
						content.setBackgroundResource(R.drawable.border);
					} else {
						content.setBackgroundResource(0);
					}
				}
			}
			StandOutWindow.StandOutLayoutParams params = getLayoutParams();
			params.setFocusFlag(focus);
			mContext.updateViewLayout(id, params);
			if (focus) {
				mContext.setFocusedWindow(this);
			} else {
				if (mContext.getFocusedWindow() == this) {
					mContext.setFocusedWindow(null);
				}
			}
			return true;
		}
		return false;
	}
	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params) {
		if (params instanceof StandOutWindow.StandOutLayoutParams) {
			super.setLayoutParams(params);
		} else {
			throw new IllegalArgumentException(
					"Window"
							+ id
							+ ": LayoutParams must be an instance of StandOutLayoutParams.");
		}
	}
	public Editor edit() {
		return new Editor();
	}
	@Override
	public StandOutWindow.StandOutLayoutParams getLayoutParams() {
		StandOutWindow.StandOutLayoutParams params = (StandOutWindow.StandOutLayoutParams) super
				.getLayoutParams();
		if (params == null) {
			params = originalParams;
		}
		return params;
	}
	private View getSystemDecorations() {
		final View decorations = mLayoutInflater.inflate(
				R.layout.system_window_decorators, null);
		final ImageView icon = (ImageView) decorations
				.findViewById(R.id.window_icon);
		icon.setImageResource(mContext.getAppIcon());
		icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupWindow dropDown = mContext.getDropDown(id);
				if (dropDown != null) {
					dropDown.showAsDropDown(icon);
				}
			}
		});
		TextView title = (TextView) decorations.findViewById(R.id.title);
		title.setText(mContext.getTitle(id));
		View hide = decorations.findViewById(R.id.hide);
		hide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.hide(id);
			}
		});
		hide.setVisibility(View.GONE);
		View maximize = decorations.findViewById(R.id.maximize);
		maximize.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StandOutWindow.StandOutLayoutParams params = getLayoutParams();
				boolean isMaximized = data
						.getBoolean(WindowDataKeys.IS_MAXIMIZED);
				if (isMaximized && params.width == displayWidth
						&& params.height == displayHeight && params.x == 0
						&& params.y == 0) {
					data.putBoolean(WindowDataKeys.IS_MAXIMIZED, false);
					int oldWidth = data.getInt(
							WindowDataKeys.WIDTH_BEFORE_MAXIMIZE, -1);
					int oldHeight = data.getInt(
							WindowDataKeys.HEIGHT_BEFORE_MAXIMIZE, -1);
					int oldX = data
							.getInt(WindowDataKeys.X_BEFORE_MAXIMIZE, -1);
					int oldY = data
							.getInt(WindowDataKeys.Y_BEFORE_MAXIMIZE, -1);
					edit().setSize(oldWidth, oldHeight).setPosition(oldX, oldY)
							.commit();
				} else {
					data.putBoolean(WindowDataKeys.IS_MAXIMIZED, true);
					data.putInt(WindowDataKeys.WIDTH_BEFORE_MAXIMIZE,
							params.width);
					data.putInt(WindowDataKeys.HEIGHT_BEFORE_MAXIMIZE,
							params.height);
					data.putInt(WindowDataKeys.X_BEFORE_MAXIMIZE, params.x);
					data.putInt(WindowDataKeys.Y_BEFORE_MAXIMIZE, params.y);
					edit().setSize(1f, 1f).setPosition(0, 0).commit();
				}
			}
		});
		View close = decorations.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.close(id);

			}
		});
		View titlebar = decorations.findViewById(R.id.titlebar);
		titlebar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// handle dragging to move
				boolean consumed = mContext.onTouchHandleMove(id, Window.this,
						v, event);
				return consumed;
			}
		});
		View corner = decorations.findViewById(R.id.corner);
		corner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean consumed = mContext.onTouchHandleResize(id,
						Window.this, v, event);
				return consumed;
			}
		});
		if (Utils.isSet(flags, StandOutFlags.FLAG_WINDOW_HIDE_ENABLE)) {
			hide.setVisibility(View.VISIBLE);
		}
		if (Utils.isSet(flags, StandOutFlags.FLAG_DECORATION_MAXIMIZE_DISABLE)) {
			maximize.setVisibility(View.GONE);
		}
		if (Utils.isSet(flags, StandOutFlags.FLAG_DECORATION_CLOSE_DISABLE)) {
			close.setVisibility(View.GONE);
		}
		if (Utils.isSet(flags, StandOutFlags.FLAG_DECORATION_MOVE_DISABLE)) {
			titlebar.setOnTouchListener(null);
		}
		if (Utils.isSet(flags, StandOutFlags.FLAG_DECORATION_RESIZE_DISABLE)) {
			corner.setVisibility(View.GONE);
		}
		return decorations;
	}
	void addFunctionality(View root) {
		// corner for resize
		if (!Utils.isSet(flags,
				StandOutFlags.FLAG_ADD_FUNCTIONALITY_RESIZE_DISABLE)) {
			View corner = root.findViewById(R.id.corner);
			if (corner != null) {
				corner.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						boolean consumed = mContext.onTouchHandleResize(id,
								Window.this, v, event);
						return consumed;
					}
				});
			}
		}
		if (!Utils.isSet(flags,
				StandOutFlags.FLAG_ADD_FUNCTIONALITY_DROP_DOWN_DISABLE)) {
			final View icon = root.findViewById(R.id.window_icon);
			if (icon != null) {
				icon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						PopupWindow dropDown = mContext.getDropDown(id);
						if (dropDown != null) {
							dropDown.showAsDropDown(icon);
						}
					}
				});
			}
		}
	}
	void fixCompatibility(View root) {
		Queue<View> queue = new LinkedList<View>();
		queue.add(root);
		View view = null;
		while ((view = queue.poll()) != null) {
			if (view instanceof ViewGroup) {
				ViewGroup group = (ViewGroup) view;
				for (int i = 0; i < group.getChildCount(); i++) {
					queue.add(group.getChildAt(i));
				}
			}
		}
	}
	public class Editor {
		public static final int UNCHANGED = Integer.MIN_VALUE;
		StandOutWindow.StandOutLayoutParams mParams;
		float anchorX, anchorY;
		public Editor() {
			mParams = getLayoutParams();
			anchorX = anchorY = 0;
		}
		public Editor setAnchorPoint(float x, float y) {
			if (x < 0 || x > 1 || y < 0 || y > 1) {
				throw new IllegalArgumentException(
						"Anchor point must be between 0 and 1, inclusive.");
			}
			anchorX = x;
			anchorY = y;
			return this;
		}
		public Editor setSize(float percentWidth, float percentHeight) {
			DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
			int mDisplayWidth = metrics.widthPixels;
			int mDisplayHeight = (int) (metrics.heightPixels - 25 * metrics.density);
			return setSize((int) (mDisplayWidth * percentWidth), (int) (mDisplayHeight * percentHeight));
		}
		public Editor setSize(int width, int height) {
			return setSize(width, height, false);
		}
		private Editor setSize(int width, int height, boolean skip) {
			if (mParams != null) {
				if (anchorX < 0 || anchorX > 1 || anchorY < 0 || anchorY > 1) {
					throw new IllegalStateException(
							"Anchor point must be between 0 and 1, inclusive.");
				}
				int lastWidth = mParams.width;
				int lastHeight = mParams.height;
				if (width != UNCHANGED) {
					mParams.width = width;
				}
				if (height != UNCHANGED) {
					mParams.height = height;
				}
				int maxWidth = mParams.maxWidth;
				int maxHeight = mParams.maxHeight;
				if (Utils.isSet(flags,
						StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE)) {
					maxWidth = (int) Math.min(maxWidth, displayWidth);
					maxHeight = (int) Math.min(maxHeight, displayHeight);
				}
				mParams.width = Math.min(
						Math.max(mParams.width, mParams.minWidth), maxWidth);
				mParams.height = Math.min(
						Math.max(mParams.height, mParams.minHeight), maxHeight);
				if (Utils.isSet(flags,
						StandOutFlags.FLAG_WINDOW_ASPECT_RATIO_ENABLE)) {
					int ratioWidth = (int) (mParams.height * touchInfo.ratio);
					int ratioHeight = (int) (mParams.width / touchInfo.ratio);
					if (ratioHeight >= mParams.minHeight
							&& ratioHeight <= mParams.maxHeight) {
						mParams.height = ratioHeight;
					} else {
						mParams.width = ratioWidth;
					}
				}
				if (!skip) {
					setPosition((int) (mParams.x + lastWidth * anchorX),
							(int) (mParams.y + lastHeight * anchorY));
				}
			}
			return this;
		}
		public Editor setPosition(float percentWidth, float percentHeight) {
			return setPosition((int) (displayWidth * percentWidth),
					(int) (displayHeight * percentHeight));
		}
		public Editor setPosition(int x, int y) {
			return setPosition(x, y, false);
		}
		private Editor setPosition(int x, int y, boolean skip) {
			if (mParams != null) {
				if (anchorX < 0 || anchorX > 1 || anchorY < 0 || anchorY > 1) {
					throw new IllegalStateException(
							"Anchor point must be between 0 and 1, inclusive.");
				}
				if (x != UNCHANGED) {
					mParams.x = (int) (x - mParams.width * anchorX);
				}
				if (y != UNCHANGED) {
					mParams.y = (int) (y - mParams.height * anchorY);
				}
				if (Utils.isSet(flags,
						StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE)) {
					if (mParams.gravity != (Gravity.TOP | Gravity.LEFT)) {
						throw new IllegalStateException(
								"The window "
										+ id
										+ " gravity must be TOP|LEFT if FLAG_WINDOW_EDGE_LIMITS_ENABLE or FLAG_WINDOW_EDGE_TILE_ENABLE is set.");
					}
					mParams.x = Math.min(Math.max(mParams.x, 0), displayWidth
							- mParams.width);
					mParams.y = Math.min(Math.max(mParams.y, 0), displayHeight
							- mParams.height);
				}
			}
			return this;
		}
		public void commit() {
			if (mParams != null) {
				mContext.updateViewLayout(id, mParams);
				mParams = null;
			}
		}
	}
	public static class WindowDataKeys {
		public static final String IS_MAXIMIZED = "isMaximized";
		public static final String WIDTH_BEFORE_MAXIMIZE = "widthBeforeMaximize";
		public static final String HEIGHT_BEFORE_MAXIMIZE = "heightBeforeMaximize";
		public static final String X_BEFORE_MAXIMIZE = "xBeforeMaximize";
		public static final String Y_BEFORE_MAXIMIZE = "yBeforeMaximize";
	}
}