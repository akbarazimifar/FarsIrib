package ir.farsirib.Activity;

import ir.farsirib.Setting.Default_Station;
import ir.farsirib.shenavarlib.StandOutWindow;
import ir.farsirib.shenavarview.QueryPreferences;
import ir.farsirib.shenavarview.Video;
import ir.farsirib.utils.UICircularImage;
import ir.farsirib.utils.UIParallaxScroll;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ir.farsirib.R;

public class DetailActivity extends Activity {

	public  static final String key_top ="";
	public  static final String key_left = "";
	public  static final String key_height = "";
	public  static final String key_width ="";
	public  static final String key_title = "title";
	public  static final String key_description = "description";
	public  static final String key_imageId = "";
	public  static final String key_barnameId = "";

	// Progress Dialog
	private ProgressDialog pDialog;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0;
	public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;

	//Views
	LinearLayout topview;
	TextView titleview;
	TextView titleBar;
	ImageView mImg,shareImage,downloadImage;
	UICircularImage play_btn,fullScr_btn,multiWinBtn;
	VideoView videoView;
	MediaController mediaController;
	Context context;

	//Layouts
	String title;
	String videoURL;
	int barname_id;
	Default_Station default_station;
	private String PACKAGE = "IDENTIFY";
	public static final String MYPrefrences="Prefrence_Station";
	public SharedPreferences sharedPreferences;

	Bundle bundle;
	int imageId;
	String sum;
	String imgURL;
    private int currentPosition=0;

    @SuppressLint("NewApi")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		context = getApplicationContext();

		((UIParallaxScroll) findViewById(R.id.scroller)).setOnScrollChangedListener(mOnScrollChangedListener);

	    topview = findViewById(R.id.layout_top);
	    titleBar = findViewById(R.id.titleBar);
		shareImage = findViewById(R.id.imageView2);
		downloadImage = findViewById(R.id.downloadImg);
		multiWinBtn = findViewById(R.id.multiwin_btn);



	    //Setting the titlebar background blank (initial state)
	    topview.getBackground().setAlpha(0);
	    titleBar.setVisibility(View.INVISIBLE);
	    
	    bundle = getIntent().getExtras();
	    title = bundle.getString("title");
	    sum = bundle.getString("descr");
	    //int imgId = bundle.getInt("img");
		imgURL = bundle.getString("img");
		videoURL = bundle.getString("video_url");
		this.barname_id = bundle.getInt("barname_id");

	    titleview = findViewById(R.id.title);
	    TextView mSum = findViewById(R.id.sumary);
	    TextView mDescr = findViewById(R.id.description);
	    mImg = findViewById(R.id.imageView);
	    
	    titleview.setText(title);

		mDescr.setText(Html.fromHtml("<html lang=\"ir\"><body><p dir=\"rtl\">" + sum + "</p></body></html>"));

	     //mSum.setText(sum);
	    //mImg.setImageResource(imgId);

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgURL, opts);
		opts.inJustDecodeBounds = false;

		Picasso.with(getApplicationContext())
				.load(imgURL)
				.error(R.mipmap.ic_launcher)
				.placeholder(R.mipmap.ic_launcher)
				.resize(300,192)
				.centerCrop()
				.into((mImg));


	    
	    titleBar.setText(title);
	    
	    Button btnback = findViewById(R.id.title_bar_left_menu);
	    btnback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(getApplicationContext(), TransitionDetailActivity.class);

				//default_station = new Default_Station(getApplicationContext());

//				Bundle bundle = getIntent().getExtras();
//				title = bundle.getString("title");
//				final String sum = bundle.getString("descr");
//				//int imgId = bundle.getInt("img");
//				final String imgURL = bundle.getString("img");
//				videoURL = bundle.getString("video_url");
			//	barname_id = bundle.getInt("barname_id");


				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//				title = sharedPreferences.getString(key_title,"");
//				int imageId = sharedPreferences.getInt(key_imageId,0);
//				String desc = sharedPreferences.getString(key_description,"");
				//int barname_id2 = sharedPreferences.getInt(key_barnameId,barname_id);
				int left = sharedPreferences.getInt(key_left,0);
				int top = sharedPreferences.getInt(key_top,0);
				int width = sharedPreferences.getInt(key_width,0);
				int height = sharedPreferences.getInt(key_height,0);


				switch (barname_id)
				{
					case 2:
						imageId = R.drawable.ph_13;
						title="ویژه های سایت";
						sum="";
						break;
					case 3:
						imageId = R.drawable.ph_1;
						title = "کاشانه مهر";
						sum = "برنامه خانواده سیمای فارس شنبه تا چهارشنبه ساعت 10";
						break;
					case 4:
						imageId = R.drawable.ph_2;
						title="خوشاشیراز";
						sum="جمعه ها ساعت 10 صبح از شبکه فارس و شبکه شما";
						break;
					case 72:
						imageId = R.drawable.ph_3;
						title="کودک و نوجوان";
						sum="برنامه کودک و نوجوان سیمای فارس شنبه تا چهارشنبه ساعت 16";
						break;
					case 11:
						imageId = R.drawable.ph_4;
						title="شب پارسی";
						sum="برنامه زنده شبانه شنبه تا چهارشنبه ساعت 22";
						break;
					case 15:
						imageId = R.drawable.ph_5;
						title="صبح دلگشا";
						sum="برنامه زنده صبحگاهی سیمای فارس شنبه تا چهارشنبه ساعت 7:45";
						break;
					case 10:
						imageId = R.drawable.ph_6;
						title="گفتگو";
						sum="برنامه گفتگو شنبه ها ساعت 21 از سیمای فارس";
						break;
					case 12:
						imageId = R.drawable.ph_7;
						title="شهرراز";
						sum="یکشنبه ، سه شنبه ، پنجشنبه ساعت 21";
						break;
					case 13:
						imageId = R.drawable.ph_8;
						title="مشاور شما";
						sum="شنبه تا سه شنبه ساعت 18";
						break;
					case 30:
						imageId = R.drawable.ph_11;
						title="هم ولایتی";
						sum="";
						break;
					case 67:
						imageId = R.drawable.ph_12;
						title="شمعدونی";
						sum="";
						break;
				}

			//	title = default_station.getTitle();
			//	int imageId = default_station.getImageId();
			//	Toast.makeText(getApplicationContext(), "Image ID:" + imageId , Toast.LENGTH_SHORT).show();
			//	String desc = default_station.getDescription();
//				int barname_id = default_station.getBarnameId();
//				int left = default_station.getLeft();
//				int top = default_station.getTop();
//				int width = default_station.getWidth();Toast.makeText(getApplicationContext(), "width:" + width , Toast.LENGTH_SHORT).show();
//				int height = default_station.getHeight();

				Bundle bundle = new Bundle();

				bundle.putString("title", title);
				bundle.putInt("img",imageId);
				bundle.putString("descr", sum);
				bundle.putInt("barname_id", barname_id);

				bundle.putInt(PACKAGE + ".left", left);
				bundle.putInt(PACKAGE + ".top", top);
				bundle.putInt(PACKAGE + ".width", width);
				bundle.putInt(PACKAGE + ".height", height);

				intent.putExtras(bundle);
				DetailActivity.this.finish();
				startActivity(intent);
			}
	    });

		mediaController = new MediaController(this);
		play_btn = findViewById(R.id.play_btn);
		videoView = findViewById(R.id.video_view);
		fullScr_btn = findViewById(R.id.fullScr_btn);

		play_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				play_btn.setVisibility(View.INVISIBLE);
				videoView.setVisibility(View.VISIBLE);
				fullScr_btn.setVisibility(View.VISIBLE);
				multiWinBtn.setVisibility(View.VISIBLE);

				Uri uri = Uri.parse(videoURL);
				videoView.setVideoURI(uri);
				videoView.setMediaController(mediaController);
				videoView.start();


			}
		});

		fullScr_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


//
//
//				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && QueryPreferences.getPermissionStatus(getApplicationContext())!=null && QueryPreferences.getPermissionStatus(getApplicationContext()).equals("OK")){
//					StandOutWindow.show(getApplicationContext(), Video.class, StandOutWindow.DEFAULT_ID);
//				}else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
//					StandOutWindow.show(getApplicationContext(), Video.class, StandOutWindow.DEFAULT_ID);
//				}

				Intent intent = new Intent(getApplicationContext(), FullScrVideoActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("title", title);
				bundle.putString("img",imgURL );
				bundle.putString("descr", sum);
				bundle.putString("video_url",videoURL);
				bundle.putString("caller_context","DetailActivity");
				bundle.putInt("barname_id",barname_id);


				intent.putExtras(bundle);
                DetailActivity.this.finish();
				startActivity(intent);
			}
		});

        multiWinBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				if(QueryPreferences.getPermissionStatus(context)==null){
					QueryPreferences.setPermissionStatus(context, "notGranted");
					checkPermission();
				}else{
					if(QueryPreferences.getPermissionStatus(context).equals("notGranted")){
						checkPermission();
					}
				}
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && QueryPreferences.getPermissionStatus(context)!=null && QueryPreferences.getPermissionStatus(context).equals("OK")){
                   StandOutWindow.videoUrl = videoURL;
                   StandOutWindow.title = title;
                   StandOutWindow.show(context, Video.class, StandOutWindow.DEFAULT_ID);
                }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                       StandOutWindow.videoUrl = videoURL;
                       StandOutWindow.title = title;
                }
            }
        });

		shareImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, videoURL);
				startActivity(Intent.createChooser(intent, "اشتراک به وسیله :"));
			}
		});

		downloadImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
				String DIR_SAVE = DIR_SDCARD + "/videofiles/";

				new DownloadFileFromURL().execute(videoURL);
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.M)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
			if (!Settings.canDrawOverlays(this)) {
				// You don't have permission
				checkPermission();
			}
			else
			{
				QueryPreferences.setPermissionStatus(this, "OK");
			}
		}
	}
	public void checkPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!Settings.canDrawOverlays(this)) {
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + getPackageName()));
				startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
			}
		}
	}

//	public boolean isSL(){
//		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
//		{
//			if ("ir.farsirib.shenavarview.Video"
//					.equals(service.service.getClassName()))
//			{
//				return true;
//			}
//		}
//		return false;
//	}
	/**
	 * Showing Dialog
	 * */
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
			case progress_bar_type:
				pDialog = new ProgressDialog(this);
				pDialog.setMessage("Downloading file. Please wait...");
				pDialog.setIndeterminate(false);
	//			pDialog.setMax(100);
				pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pDialog.setCancelable(true);
				pDialog.show();
				return pDialog;
			default:
				return null;
		}
	}
	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}

		/**
		 * Downloading file in background thread
		 */
		@Override
		protected String doInBackground(String... file_url) {
			int count;
			try {

				URL url = new URL(file_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream());

				String DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
				String DIR_SAVE = DIR_SDCARD + "/videofiles/";

				File rootFile = new File(DIR_SAVE);
				rootFile.mkdir();

				// Output stream to write file
				OutputStream output = new FileOutputStream(rootFile+"/"+"1087.mp4");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		/**
		 * Updating progress bar
		 */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			dismissDialog(progress_bar_type);
		}
	}

	@Override
	public void onStop(){
		super.onStop();
		mImg.setImageResource(0);
	}
	
	//performing changes to the titlebars visibility
	private UIParallaxScroll.OnScrollChangedListener mOnScrollChangedListener = new UIParallaxScroll.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        	//At this height, the title has to be fully visible
        	final int headerHeight = (findViewById(R.id.imageView).getHeight() + titleview.getHeight()) - topview.getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            topview.getBackground().setAlpha(newAlpha);
            topview.getBackground().setAlpha(newAlpha);
            
            Animation animationFadeIn = AnimationUtils.loadAnimation(DetailActivity.this,R.anim.fadein);
            Animation animationFadeOut = AnimationUtils.loadAnimation(DetailActivity.this,R.anim.fadeout);
            
            if (newAlpha == 255 && titleBar.getVisibility() != View.VISIBLE && !animationFadeIn.hasStarted()){
            	titleBar.setVisibility(View.VISIBLE);
            	titleBar.startAnimation(animationFadeIn);
            } else if (newAlpha < 255 && !animationFadeOut.hasStarted() && titleBar.getVisibility() != View.INVISIBLE)  { 	
            	titleBar.startAnimation(animationFadeOut);
            	titleBar.setVisibility(View.INVISIBLE);
            	
            }
        }
    };


//    @Override
//    public void onPause() {
//     //   Log.d(TAG, "onPause called");
//        super.onPause();
//        currentPosition = videoView.getCurrentPosition(); //stopPosition is an int
//        videoView.pause();
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//       // Log.d(TAG, "onResume called");
//        videoView.seekTo(currentPosition);
//        videoView.start(); //Or use resume() if it doesn't work. I'm not sure
//    }


    @Override
    protected void onPause()
    {
        super.onPause();
        videoView.pause();
        currentPosition = videoView.getCurrentPosition();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        videoView.seekTo(currentPosition);
        videoView.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt("pos", videoView.getCurrentPosition());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

}
