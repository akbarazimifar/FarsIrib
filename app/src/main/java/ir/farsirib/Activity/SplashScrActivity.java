package ir.farsirib.Activity;

import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farsitel.bazaar.IUpdateCheckService;

import ir.farsirib.R;

public class SplashScrActivity extends AppCompatActivity {

    boolean isOnline = false;
    boolean wifi = false;
    Context myContext;
    Dialog dialog;
    Button btnCancel,btnsetting,btntry;
    IUpdateCheckService service;
    UpdateServiceConnection connection;
    private static final String TAG = "UpdateCheck";
    private static final int SPLASH_DISPLAY_TIME = 4000;
    ProgressBar progressBar;
    TextView textView;
    private Animation animation;
    AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scr);

        myContext = this.getApplicationContext();
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = findViewById(R.id.preloader);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        textView.startAnimation(animation);

//        set = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.anim.grow);
//        set.setTarget(textView);
//        set.start();
//


        try {
            ConnectivityManager connManager = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeConnection = connManager.getActiveNetworkInfo();
            if ( (activeConnection != null) && activeConnection.isConnected() )
                isOnline = true;

            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if ( mWifi.isConnected() )
                wifi = true;

            if ( wifi == true || isOnline == true ) {

                initService(); // Update Check

                new Handler().postDelayed(new Runnable() {
                    public void run() {



                        Intent intent = new Intent(SplashScrActivity.this,MainPageActivity.class);
                        startActivity(intent);
                        SplashScrActivity.this.finish();

                        // transition from splash to main menu
                        overridePendingTransition(R.anim.activityfadein,
                                R.anim.splashfadeout);

                    }

                }, SPLASH_DISPLAY_TIME);

            }
            else {
            showCustomDialog();
        }
    }
        catch (IllegalArgumentException e) {
        e.printStackTrace();
    } catch (SecurityException e) {
        e.printStackTrace();
    } catch (IllegalStateException e) {
        e.printStackTrace();
    }
}
    protected void showCustomDialog() {
        dialog = new Dialog(this,android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);

        btntry = dialog.findViewById(R.id.btntry);
        btnsetting = dialog.findViewById(R.id.btnsetting);
        btnCancel = dialog.findViewById(R.id.btncancel);

       btnsetting.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
           }
       });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                SplashScrActivity.this.finish();
            }
        });
        btntry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {
                    ConnectivityManager connManager = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeConnection = connManager.getActiveNetworkInfo();
                    if ( (activeConnection != null) && activeConnection.isConnected() )
                        isOnline = true;

                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if ( mWifi.isConnected() )
                        wifi = true;

                    if ( wifi == true || isOnline == true ) {


                        dialog.cancel();
                        initService(); // Update Check

                        new Handler().postDelayed(new Runnable() {
                            public void run() {



                                Intent intent = new Intent(SplashScrActivity.this,MainPageActivity.class);
                                startActivity(intent);
                                SplashScrActivity.this.finish();

                                // transition from splash to main menu
                                overridePendingTransition(R.anim.activityfadein,
                                        R.anim.splashfadeout);

                            }

                        }, SPLASH_DISPLAY_TIME);

                    }
//                    else {
//                        showCustomDialog();
//                    }
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }

        });

        final ImageView myImage = dialog.findViewById(R.id.loader);
        myImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate) );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));

        dialog.show();
    }

    private void initService() {
        Log.i(TAG, "initService()");
        connection = new UpdateServiceConnection();
        Intent i = new Intent(
                "com.farsitel.bazaar.service.UpdateCheckService.BIND");
        i.setPackage("com.farsitel.bazaar");
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "initService() bound value: " + ret);
    }

    /** This is our function to un-binds this activity from our service. */
    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService(): unbound.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }

    @Override
    public void onBackPressed() {

    }

    private class UpdateServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IUpdateCheckService.Stub
                    .asInterface(boundService);
            try {
                long vCode = service.getVersionCode(SplashScrActivity.this.getPackageName());
//                long vCode = service.getVersionCode(SplashScrActivity.this.getPackageName());
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("bazaar://details?id=" + getPackageName()));
//                intent.setPackage("com.farsitel.bazaar");
//                startActivity(intent);

                if(vCode != -1){

                    Toast.makeText(SplashScrActivity.this, "نسخه جدید در دسترس است از بخش اعلانات به روز رسانی کنید",
                            Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        setNotification();
                    }
                }
                else
                {
                    Toast.makeText(SplashScrActivity.this, "شما در حال حاضر از آخرین نسخه برنامه استفاده می کنید",
                            Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onServiceConnected(): Connected");
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d(TAG, "onServiceDisconnected(): Disconnected");
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        private void setNotification() {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("bazaar://details?id=" + getPackageName()));
            intent.setPackage("com.farsitel.bazaar");
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(intent);

            PendingIntent contentIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("بروزرسانی")
                    .setContentText("برنامه رو بروزرسانی کنید");

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
