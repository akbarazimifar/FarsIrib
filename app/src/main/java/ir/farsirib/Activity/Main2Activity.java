package ir.farsirib.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;



import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.farsirib.R;
import ir.farsirib.utils.DateConverter;


public class Main2Activity extends AppCompatActivity {


    Typeface myfont;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;

    TextView account_txt;
    public TextView account_tv;

    private JSONObject new_like;


    SharedPreferences settings;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        myContext = this.getApplicationContext();

        myfont = Typeface.createFromAsset(getAssets(), "b.ttf");

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            Field st = Typeface.class.getDeclaredField("MONOSPACE");

            st.setAccessible(true);

            try {


                st.set(null, myfont);


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        headerView = findViewById(R.id.account_txt);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.mnu_account) {

                    if (settings.getInt("user_id", 0) != 0) {
                        Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                        startActivity(i);
                        drawer.closeDrawer(Gravity.RIGHT);

                    } else {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        drawer.closeDrawer(Gravity.RIGHT);
                    }
                }else if (id == R.id.mnu_main) {
                    if (settings.getInt("user_id", 0) != 0) {
                        Intent i = new Intent(getApplicationContext(), MainListItemsActivity.class);
                        startActivity(i);
                        drawer.closeDrawer(Gravity.RIGHT);
                    } else {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        drawer.closeDrawer(Gravity.RIGHT);
                    }
                }else if (id == R.id.mnu_old_app) {
                    if (settings.getInt("user_id", 0) != 0) {
                        Intent i = new Intent(getApplicationContext(), MainPageActivity.class);
                        startActivity(i);
                        drawer.closeDrawer(Gravity.RIGHT);
                    } else {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        drawer.closeDrawer(Gravity.RIGHT);
                    }
                }else if (id == R.id.mnu_share) {
                    Intent i=new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT,"شبکه فارس");
                    i.putExtra(Intent.EXTRA_TEXT,"شبکه فارس رو در بازار اندروید ببین:\n" +
                            "https://cafebazaar.ir/app/ir.farsirib/?l=fa");

                    startActivity(Intent.createChooser(i,"یک برنامه را انتخاب کنید"));
                }



                return true;
            }
        });


        // setNavigationItemSelectedListener

        toolbar.setNavigationIcon(R.drawable.menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawer.isDrawerOpen(Gravity.RIGHT))
                    drawer.closeDrawer(Gravity.RIGHT);
                else
                    drawer.openDrawer(Gravity.RIGHT);

            }
        });


        toolbar.setTitle("شبکه استانی فارس");


        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {

                TextView tv = (TextView) view;
                tv.setTypeface(myfont);
                tv.setTextSize(18);

            }
        }
    }

    public void success_message_dialog(String title,String errMsg) {

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title.toString())
                .setContentText(errMsg.toString())
                .show();
    }

    public void error_message_dialog(String title,String errMsg) {

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title.toString())
                .setContentText(errMsg.toString())
                .show();
    }

    public class LikeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            new_like =new JSONObject();

            Calendar calendar = Calendar.getInstance();
            String day          = (String) DateFormat.format("dd",   calendar.getTime());
            String month  = (String) DateFormat.format("MM",   calendar.getTime());
            String year         = (String) DateFormat.format("yyyy", calendar.getTime());

            DateConverter converter = new DateConverter();
            converter.gregorianToPersian(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

            Date currentTime = Calendar.getInstance().getTime();
            final String like_time = currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();


            try {
                new_like.put("mobile_num",settings.getString("mobile_num",""));
                new_like.put("like_date",converter.toString());
                new_like.put("like_time",like_time);
                new_like.put("command","new_like");

            }catch (JSONException e)
            {
                e.printStackTrace();
            }



            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();

            namevaluepairs.add(new BasicNameValuePair("myjson",new_like.toString()));

            try
            {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://shahreraz.com/club/app/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));
                HttpResponse httpresponse=httpclient.execute(httppost);

                String response= EntityUtils.toString(httpresponse.getEntity());

                if (response.startsWith("<farsirib_app>")&&response.endsWith("</farsirib_app>")) {//response is valid

                    response = response.replace("<farsirib_app>", "").replace("</farsirib_app>", "");


                    if(response.trim().equals("ok"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                success_message_dialog( "شبکه فارس" , "نظر شما ثبت شد" );
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                error_message_dialog("اوه..." , "خطا در ارسال اطلاعات");
                            }
                        });
                    }
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            error_message_dialog("اوه..." , "خطا در ارسال اطلاعات");
                        }
                    });
                }

            }catch(Exception e)
            {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        error_message_dialog("اوه..." , "خطا در ارسال اطلاعات");
                    }
                });
            }

            return null;
        }
    }


}
