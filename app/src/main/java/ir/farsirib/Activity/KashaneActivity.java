package ir.farsirib.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import java.util.ArrayList;

import ir.farsirib.R;

public class KashaneActivity extends Main2Activity {

    private int page_index;
    private Context myContext;
    ArrayList<JSONObject> data_list;
    private String URL;
    public ImageView kashane_header, ertebatbama, moarefi_mehman,like,kashane_archive,kashane_gallary,kashane_ugc;
    String page_url = "";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_kashane, content_frame);

        myContext = getApplicationContext();

        String title = String.valueOf(getIntent().getExtras().getString("title"));
        toolbar.setTitle(title);

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {

                TextView tv = (TextView) view;
                tv.setTypeface(myfont);
                tv.setTextSize(18);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable arrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.closeDrawer(Gravity.RIGHT);

                onBackPressed();

            }
        });

        if (settings.getInt("user_id",0)!=0)
        {

            navigationView = findViewById(R.id.nav_view);
            headerView = navigationView.getHeaderView(0);
            account_tv = headerView.findViewById(R.id.account_txt);
            account_tv.setText(settings.getString("mobile_num",""));

            data_list = new ArrayList<JSONObject>();

            new OptionTask(10).execute(); // Items of Chelcheragh page

            drawer.closeDrawer(Gravity.RIGHT);
        }
        else {
            error_message_dialog("شبکه فارس!" , "شما وارد حساب کاربری نشده اید!");

            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawer.closeDrawer(Gravity.RIGHT);
        }

        kashane_header = findViewById(R.id.kashane_header);
        ertebatbama = findViewById(R.id.ertebatbama);
        like = findViewById(R.id.like);
      //  moarefi_mehman = findViewById(R.id.moarefi_mehman);
        kashane_archive = findViewById(R.id.kashane_archive);
      //  kashane_gallary = findViewById(R.id.kashane_gallary);
        kashane_ugc = findViewById(R.id.kashane_ugc);

        ertebatbama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KashaneActivity.this, PostCommentActivity.class);
                intent.putExtra("title","ارتباط با ما" );
                intent.putExtra("caller_context","KashaneActivity" );
                startActivity(intent);
            }
        });

        kashane_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KashaneActivity.this, WebMainPageActivity.class);
                intent.putExtra("URL", page_url);
                startActivity(intent);
            }
        });

//        moarefi_mehman.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getBaseContext(), "در نسخه های بعدی عملیاتی خواهد شد!!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        kashane_gallary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getBaseContext(), "در نسخه های بعدی عملیاتی خواهد شد!!", Toast.LENGTH_SHORT).show();
//            }
//        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LikeTask().execute();
            }
        });

        kashane_ugc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KashaneActivity.this, UGCActivity.class);
                intent.putExtra("title","در شبکه فارس دیده شوید" );
                intent.putExtra("caller_activity","KashaneActivity" );
                startActivity(intent);
            }
        });
    }

    private class OptionTask extends AsyncTask<Void, Void, String> {
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
                    Glide.with(this).load(URL).apply(requestOptions).into(kashane_header);
                    break;
                case 1:
                    Glide.with(this).load(URL).apply(requestOptions).into(ertebatbama);
                    break;
                case 2:
                    Glide.with(this).load(URL).apply(requestOptions).into(like);
                    break;
//                case 3:
//                    Glide.with(this).load(URL).apply(requestOptions).into(moarefi_mehman);
//                    break;
                case 4:
                    page_url = data_list.get(4).getString("page_url");
                    Glide.with(this).load(URL).apply(requestOptions).into(kashane_archive);
                    break;
//                case 5:
//                    Glide.with(this).load(URL).apply(requestOptions).into(kashane_gallary);
//                    break;
                case 6:
                    Glide.with(this).load(URL).apply(requestOptions).into(kashane_ugc);
                    break;
            }
        }
    }
}
