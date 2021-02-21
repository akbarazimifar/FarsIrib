package ir.farsirib.shenavarview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.VideoView;

import ir.farsirib.R;
import ir.farsirib.shenavarlib.StandOutWindow;
import ir.farsirib.shenavarlib.constants.StandOutFlags;
import ir.farsirib.shenavarlib.ui.Window;

import java.io.IOException;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_PHONE;

public class Video extends StandOutWindow{

	private MediaPlayer mMediaPlayer;
	private TextureView mPreview;

	@Override
	public String getAppName() {
		return getTitle();
	}

	@Override
	public int getAppIcon() {
		return R.mipmap.ic_launcher;
	}

	@Override
	public String getTitle(int id) {
		return getAppName();
	}

	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop, frame, true);
		VideoView videoview = (VideoView) view.findViewById(R.id.videoView2);
		mPreview = (TextureView) view.findViewById(R.id.surface);


		if (Build.VERSION.SDK_INT <Build.VERSION_CODES.HONEYCOMB){
			mPreview.setVisibility(View.GONE);
			videoview.setVisibility(View.VISIBLE);
			//Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.asl);
			Uri uri = Uri.parse(getVidoUrl());
			videoview.setVideoURI(uri);
			videoview.seekTo(getMiliSecond());
			videoview.start();
		}else{
			videoview.setVisibility(View.GONE);
			mPreview.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

				@Override
				public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
				}

				@Override
				public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
					mMediaPlayer.stop();
//					mMediaPlayer.pause();
//					videoview.pause();
					return true;
				}

				@Override
				public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
				}

				@Override
				public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
					Surface s = new Surface(surface);

					try {
						mMediaPlayer= new MediaPlayer();
						//mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.asl));
						mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(getVidoUrl()));
						mMediaPlayer.setSurface(s);
						mMediaPlayer.setLooping(true);
						mMediaPlayer.prepare();
						mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
						mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
							@Override
							public void onBufferingUpdate(MediaPlayer mp, int percent) {

							}
						});
						mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {

							}
						});
						mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
							@Override
							public void onPrepared(MediaPlayer mp) {
								mp.start();
							}

						});
						mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
							@Override
							public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

							}
						});

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	@Override
	public StandOutLayoutParams getParams(int id,int type, Window window) {
		double vid_width, vid_height, hpos, vpos;
		double ff = 85;
		vid_width = ((ff/100) * getScreenSize()[0]);
		vid_height = ((ff/100) * getScreenSize()[0]);
		hpos = ((1-(ff/100))/2)* getScreenSize()[0];
		vpos = ((1-(ff/100))/2)* getScreenSize()[0];
		return new StandOutLayoutParams(id,type ,(int) vid_width, (int) vid_height,
				(int) hpos,
				(int) vpos, 100, 100);
	}

	@Override
	public int getFlags(int id) {
		return StandOutFlags.FLAG_DECORATION_SYSTEM
				| StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE;
	}

	@Override
	public String getPersistentNotificationTitle(int id) {
		return getAppName() + " Running";
	}

	@Override
	public String getPersistentNotificationMessage(int id) {
		return "Click to add a new " + getAppName();
	}

	@Override
	public Intent getPersistentNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), getUniqueId());
	}

	@Override
	public int getHiddenIcon() {
		return android.R.drawable.ic_menu_info_details;
	}

	@Override
	public String getHiddenNotificationTitle(int id) {
		return getAppName() + " Hidden";
	}

	@Override
	public String getHiddenNotificationMessage(int id) {
		return "Click to restore #" + id;
	}

	@Override
	public Intent getHiddenNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), id);
	}

	@Override
	public Animation getShowAnimation(int id) {
		if (isExistingId(id)) {
			return AnimationUtils.loadAnimation(this,
					android.R.anim.slide_in_left);
		} else {
			return super.getShowAnimation(id);
		}
	}

	@Override
	public Animation getHideAnimation(int id) {
		return AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
	}

	@Override
	public void onReceiveData(int id, int requestCode, Bundle data,
							  Class<? extends StandOutWindow> fromCls, int fromId) {
	}

	public int[] getScreenSize(){
		Point size = new Point();
		WindowManager w = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
			w.getDefaultDisplay().getSize(size);
			return new int[]{size.x, size.y};
		}else{
			Display d = w.getDefaultDisplay();
			//noinspection deprecation
			if (d.getWidth() <= d.getHeight()){
				return new int[]{d.getWidth(), d.getHeight()};
			}else{
				return new int[]{d.getHeight(), d.getWidth()};
			}
		}
	}
}
