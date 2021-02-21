package ir.farsirib.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;


import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.farsirib.BuildConfig;
import ir.farsirib.Database.DatabaseAssets;
import ir.farsirib.R;
import ir.farsirib.shenavarlib.StandOutWindow;
import ir.farsirib.shenavarview.QueryPreferences;
import ir.farsirib.shenavarview.Video;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import static ir.farsirib.Activity.DetailActivity.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE;

public class MainListItemsActivity extends Main2Activity {

    private int page_index;
    private Context myContext;
    public ImageView barname_shabane, enteghad, shahrvand, timearchive, live, koodak, khosha, kashane, programarchive,radionama, like,ugc, tamin;
    ArrayList<JSONObject> data_list;
    private String URL;
    TextView account_tv;
    private DatabaseAssets mydatabase;
    boolean isOnline = false;
    boolean wifi = false;
    private Dialog dialog;
    Button btnCancel,btnsetting,btntry;
    private static AlertDialog alertDialog;
    private boolean doubleBackToExitPressedOnce = false;
    String time_archive_page_url = "";
    //String live_url = "http://livecdn1.irib.ir/channel-live/smil:fars/playlist.m3u8";
    String live_url ="";
    String radio_nama_page_url = "";
    String radio_nama_live_url = "";
    private String shabanehPageTitle ="";
    private String shahrvandPageTitle="";
    private String koodakPageTitle="";
    private String khoshaPageTitle="";
    private String kashanePageTitle="";
    private String ugcPageTitle="";
    private String taminPageTitle="";

    private static final String TAG = "State changed";
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.main_list_items, content_frame);

        myContext = this.getApplicationContext();
        checkInternetConnection();

        navigationView.getMenu().findItem(R.id.mnu_main).setChecked(true);

        barname_shabane = findViewById(R.id.barname_shabane);
        enteghad = findViewById(R.id.enteghad);
        shahrvand = findViewById(R.id.shahrvand);
        timearchive = findViewById(R.id.timearchive);
        live = findViewById(R.id.live);
        koodak = findViewById(R.id.koodak);
        khosha = findViewById(R.id.khosha);
        kashane = findViewById(R.id.kashane);
        programarchive = findViewById(R.id.programarchive);
        like = findViewById(R.id.like);
        radionama = findViewById(R.id.radionama);
        ugc = findViewById(R.id.ugc);
        tamin = findViewById(R.id.tamin);


        barname_shabane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainListItemsActivity.this, ChelcheraghActivity.class);
                Intent intent = new Intent(MainListItemsActivity.this, FarsiShoActivity.class);
                intent.putExtra("title",shabanehPageTitle );
                startActivity(intent);
            }
        });

        enteghad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainListItemsActivity.this, PostCommentActivity.class);
                intent.putExtra("title","انتقادات و پیشنهادات" );
                intent.putExtra("caller_context","MainListItemsActivity" );
                startActivity(intent);
            }
        });

        shahrvand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainListItemsActivity.this, CitizenReporterActivity.class);
                intent.putExtra("title",shahrvandPageTitle );
                startActivity(intent);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LikeTask().execute();
            }
        });

        programarchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create DataBase
                mydatabase = new DatabaseAssets(myContext);
                LoadingDatabase();

                Intent intent = new Intent(MainListItemsActivity.this, MainActivity.class);
                intent.putExtra("Option_Id", 7);
                intent.putExtra("Page_Title", "آرشیو برنامه ها");
                startActivity(intent);
            }
        });

        timearchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainListItemsActivity.this, WebMainPageActivity.class);
                intent.putExtra("URL", time_archive_page_url);
                startActivity(intent);
            }
        });

        live.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {


                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainListItemsActivity.this, live);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getTitle().toString()) {
                            case "پخش زنده تمام صفحه":
                                Intent intent = new Intent(myContext, FullScrVideoActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("title", "شبکه استانی فارس");
                                bundle.putString("img", "");
                                bundle.putString("descr", "");
                                bundle.putString("video_url", live_url);
                                bundle.putString("caller_context", "NationalTv");

                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case "پخش زنده شناور":

                                if (QueryPreferences.getPermissionStatus(myContext) == null) {
                                    QueryPreferences.setPermissionStatus(myContext, "notGranted");
                                    checkPermission();
                                } else {
                                    if (QueryPreferences.getPermissionStatus(myContext).equals("notGranted")) {
                                        checkPermission();
                                    }
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && QueryPreferences.getPermissionStatus(myContext) != null && QueryPreferences.getPermissionStatus(myContext).equals("OK")) {
                                    StandOutWindow.videoUrl = live_url;
                                    StandOutWindow.title = "شبکه فارس";
                                    StandOutWindow.show(myContext, Video.class, StandOutWindow.DEFAULT_ID);
                                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && QueryPreferences.getPermissionStatus(myContext) != null && QueryPreferences.getPermissionStatus(myContext).equals("OK")) {
                                    StandOutWindow.videoUrl = live_url;
                                    StandOutWindow.title = "شبکه فارس";
                                    StandOutWindow.show(myContext, Video.class, StandOutWindow.DEFAULT_ID);
                                }


                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu


            }
        });

        radionama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainListItemsActivity.this, radionama);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.radionama_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getTitle().toString()) {
                            case "پخش زنده تمام صفحه":
                                Intent intent = new Intent(myContext, FullScrVideoActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("title", "رادیونما فارس");
                                bundle.putString("img", "");
                                bundle.putString("descr", "");
                                bundle.putString("video_url", radio_nama_live_url);
                                bundle.putString("caller_context", "NationalTv");

                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case "پخش زنده شناور":

                                if (QueryPreferences.getPermissionStatus(myContext) == null) {
                                    QueryPreferences.setPermissionStatus(myContext, "notGranted");
                                    checkPermission();
                                } else {
                                    if (QueryPreferences.getPermissionStatus(myContext).equals("notGranted")) {
                                        checkPermission();
                                    }
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && QueryPreferences.getPermissionStatus(myContext) != null && QueryPreferences.getPermissionStatus(myContext).equals("OK")) {
                                    StandOutWindow.videoUrl = radio_nama_live_url;
                                    StandOutWindow.title = "رادیونما فارس";
                                    StandOutWindow.show(myContext, Video.class, StandOutWindow.DEFAULT_ID);
                                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && QueryPreferences.getPermissionStatus(myContext) != null && QueryPreferences.getPermissionStatus(myContext).equals("OK")) {
                                    StandOutWindow.videoUrl = radio_nama_live_url;
                                    StandOutWindow.title = "رادیونما فارس";
                                    StandOutWindow.show(myContext, Video.class, StandOutWindow.DEFAULT_ID);
                                }
                                break;
                            case "آرشیو زمانی":

                                Intent intent2 = new Intent(MainListItemsActivity.this, WebMainPageActivity.class);
                                intent2.putExtra("URL", radio_nama_page_url);
                                startActivity(intent2);

                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        koodak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainListItemsActivity.this, KoodakActivity.class);
                intent.putExtra("title",koodakPageTitle );
                startActivity(intent);
            }
        });

        khosha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainListItemsActivity.this, KhoshaActivity.class);
                intent.putExtra("title",khoshaPageTitle );
                startActivity(intent);
            }
        });

        kashane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainListItemsActivity.this, KashaneActivity.class);
                intent.putExtra("title",kashanePageTitle );
                startActivity(intent);
            }
        });

        ugc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainListItemsActivity.this, UGCActivity.class);
                intent.putExtra("title",ugcPageTitle );
                intent.putExtra("caller_activity","MainListItemsActivity" );
                startActivity(intent);
            }
        });

        tamin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainListItemsActivity.this, UGCActivity.class);
                intent.putExtra("title",taminPageTitle );
                intent.putExtra("caller_activity","MainListItemsActivity" );
                startActivity(intent);
            }
        });
    }

    private void checkInternetConnection() {

        try {
            ConnectivityManager connManager = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeConnection = connManager.getActiveNetworkInfo();
            if ( (activeConnection != null) && activeConnection.isConnected() )
                isOnline = true;

            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if ( mWifi.isConnected() )
                wifi = true;

            if ( wifi == true || isOnline == true ) {
                request(BuildConfig.VERSION_CODE,"fars@2018"); // Update Check
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

    public void request(int version ,String licence) {
        RequestBody formBody = new FormBody.Builder()
                .add("licence", licence)
                .add("version", version+"")
                .build();
        final Request request = new Request.Builder()
                .url("http://www.shahreraz.com/Farsirib/webservice/checkVersion.php")
                .post(formBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().toString();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                try {
                    Log.d("log",responseBody);
                    final JSONObject data = new JSONObject(responseBody);

                    if(data.has("status")){
                        if(data.getInt("status") == 1){
                            checkUserAccount();
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainListItemsActivity.this, "بروزرسانی جدید آماده است", Toast.LENGTH_SHORT).show();
                                    showUpdateDialog();
                                }
                            });
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(responseBody);
            }
        });
    }

    private void checkUserAccount() {

        if (settings.getInt("user_id", 0) != 0) {
            navigationView = findViewById(R.id.nav_view);
            headerView = navigationView.getHeaderView(0);
            account_tv = headerView.findViewById(R.id.account_txt);
            account_tv.setText(settings.getString("mobile_num", ""));

            data_list = new ArrayList<JSONObject>();

            deleteCache(myContext);

            new OptionTask(0).execute(); // Items of Main Page

//            drawer.closeDrawer(Gravity.RIGHT);

        } else {

            Intent i = new Intent(MainListItemsActivity.this, LoginActivity.class);
            startActivity(i);
        }

    }

    private void showUpdateDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView tv_play = (TextView) dialogView.findViewById(R.id.tv_play);
        TextView tv_bazzar = (TextView) dialogView.findViewById(R.id.tv_bazzar);

        tv_bazzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = BuildConfig.APPLICATION_ID;
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("bazaar://details?id=" + appPackageName));
                    intent.setPackage("com.farsitel.bazaar");
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/" + appPackageName + "/?l=fa")));
                }

                alertDialog.dismiss();
            }
        });

        tv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = BuildConfig.APPLICATION_ID;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                alertDialog.dismiss();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
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
                //  SplashScrActivity.this.finish();
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
                        request(BuildConfig.VERSION_CODE,"fars@2018"); // Update Check


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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(myContext)) {
                // You don't have permission
                checkPermission();
            } else {
                QueryPreferences.setPermissionStatus(myContext, "OK");
            }
        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(myContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + myContext.getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void LoadingDatabase() {
        try {
            mydatabase.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mydatabase.openDataBase();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private class OptionTask extends AsyncTask<Void, Void, String> {
        private String page_id;
        private boolean flag = false;

        public OptionTask(int index) {
            page_index = index;
        }

        @Override
        protected String doInBackground(Void... voids) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();

            JSONObject get_ad_list = new JSONObject();

            try {
                get_ad_list.put("command", "get_option_list");
                get_ad_list.put("page_index", settings.getInt("page_index", page_index));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.shahreraz.com/Farsirib/webservice/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());

                if (response.startsWith("<farsirib_app>") && response.endsWith("</farsirib_app>")) {//response is valid

                    response = response.replace("<farsirib_app>", "").replace("</farsirib_app>", "");

                    final String finalResponse = response;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONArray ad_list = new JSONArray(finalResponse);
                                fetch_array(ad_list);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // progress.setVisibility(View.INVISIBLE);
            try {
                FillData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetch_array(JSONArray ad_list) {

        try {

            for (int i = 0; i < ad_list.length(); i++) {


                data_list.add(ad_list.getJSONObject(i));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FillData() throws JSONException {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.gray_btn_bg_color);
        requestOptions.error(R.color.gray_btn_bg_color);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        requestOptions.skipMemoryCache(false);

        for (int i = 0; i < data_list.size(); i++) {
            URL = "http://www.shahreraz.com/Farsirib/img/MainPage/new/" + data_list.get(i).getString("image_url");

            switch (i) {
                case 0:
                    shabanehPageTitle = data_list.get(0).getString("title");
                    Glide.with(this).load(URL).apply(requestOptions).into(barname_shabane);
                    break;
                case 1:
                    Glide.with(this).load(URL).apply(requestOptions).into(enteghad);
                    break;
                case 2:
                    Glide.with(this).load(URL).apply(requestOptions).into(like);
                    break;
                case 3:
                    shahrvandPageTitle = data_list.get(3).getString("title");
                    Glide.with(this).load(URL).apply(requestOptions).into(shahrvand);
                    break;
                case 4:
                    Glide.with(this).load(URL).apply(requestOptions).into(programarchive);
                    break;
                case 5:
                    time_archive_page_url = data_list.get(5).getString("page_url");
                    Glide.with(this).load(URL).apply(requestOptions).into(timearchive);

                    break;
                case 6:
                    live_url = data_list.get(6).getString("live_url");
                    Glide.with(this).load(URL).apply(requestOptions).into(live);
                    break;
                case 7:
                    koodakPageTitle = data_list.get(7).getString("title");
                    Glide.with(this).load(URL).apply(requestOptions).into(koodak);
                    break;
                case 8:
                    khoshaPageTitle = data_list.get(8).getString("title");
                    Glide.with(this).load(URL).apply(requestOptions).into(khosha);
                    break;
                case 9:
                    kashanePageTitle = data_list.get(9).getString("title");
                    Glide.with(this).load(URL).apply(requestOptions).into(kashane);
                    break;
                case 10:
                    radio_nama_page_url = data_list.get(10).getString("page_url");
                    radio_nama_live_url = data_list.get(10).getString("live_url");
                    Glide.with(this).load(URL).apply(requestOptions).into(radionama);
                    break;
                case 11:
                    ugcPageTitle = data_list.get(11).getString("title");
                    Glide.with(this).load(URL).apply(requestOptions).into(ugc);
                    break;
                case 12:
                    taminPageTitle = data_list.get(12).getString("title");
                    Glide.with(this).load(URL).apply(requestOptions).into(tamin);
                    break;
            }
        }
    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @Override
    public void onBackPressed() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("خروج از برنامه")
                .setContentText("قصد خروج از برنامه را دارید؟")
                .setCancelText("خیر")
                .setConfirmText("بله")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        MainListItemsActivity.this.finish();
                        System.exit(0);
                    }
                })
                .show();



//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            MainListItemsActivity.this.finish();
//            return;
//        }
//        this.doubleBackToExitPressedOnce = true;
//
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//
//        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: " );
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: ");

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}

