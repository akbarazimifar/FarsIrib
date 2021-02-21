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

public class ChelcheraghActivity extends Main2Activity {


    private int page_index;
    private Context myContext;
    public ImageView chelcheragh_header,dareGhali, register,chelcheragh_ertebat, chelcheragh_archive, chelcheragh_like,chelcheragh_ugc;
    ArrayList<JSONObject> data_list;
    private String URL;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.chelcheragh, content_frame);



        myContext = getApplicationContext();

        String title = String.valueOf(getIntent().getExtras().getString("title"));
        toolbar.setTitle(title);
        //toolbar.setTitle("چلچراغ");

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
            //  deleteCache(myContext);

            new OptionTask(1).execute(); // Items of Chelcheragh page

            drawer.closeDrawer(Gravity.RIGHT);
        }
        else {
            error_message_dialog("چلچراغ!" , "شما وارد حساب کاربری نشده اید!");

            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawer.closeDrawer(Gravity.RIGHT);
        }


        chelcheragh_header = findViewById(R.id.chelcheragh_header);
        dareGhali = findViewById(R.id.dareGhali);
        chelcheragh_ertebat = findViewById(R.id.chelcheragh_ertebat);
        chelcheragh_archive = findViewById(R.id.chelcheragh_archive);
        chelcheragh_like = findViewById(R.id.chelcheragh_like);
        register = findViewById(R.id.register);
        chelcheragh_ugc = findViewById(R.id.chelcheragh_ugc);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),WriteOnFlagActivity.class);
                startActivity(i);
            }
        });

        chelcheragh_ertebat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChelcheraghActivity.this, PostCommentActivity.class);
                intent.putExtra("title","ارتباط با ما" );
                intent.putExtra("caller_context","ChelcheraghActivity" );
                startActivity(intent);
            }
        });

        chelcheragh_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChelcheraghActivity.this, WebMainPageActivity.class);
                intent.putExtra("URL", "http://farsirib.ir/chelcheragh");
                startActivity(intent);
            }
        });

        chelcheragh_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LikeTask().execute();
            }
        });

        chelcheragh_ugc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChelcheraghActivity.this, UGCActivity.class);
                intent.putExtra("title","در شبکه فارس دیده شوید" );
                intent.putExtra("caller_activity","ChelcheraghActivity" );
                startActivity(intent);
            }
        });
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
                HttpPost httppost = new HttpPost("http://www.shahreraz.com/mob/Farsirib/webservice/command.php");
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
            URL = "http://www.shahreraz.com/mob/Farsirib/img/MainPage/new/" + data_list.get(i).getString("image_url");
            switch (i) {
                case 0:
                    Glide.with(this).load(URL).apply(requestOptions).into(chelcheragh_header);
                    break;
                case 1:
                    Glide.with(this).load(URL).apply(requestOptions).into(chelcheragh_ertebat);
                    break;
                case 2:
                    Glide.with(this).load(URL).apply(requestOptions).into(chelcheragh_like);
                    break;
                case 3:
                    Glide.with(this).load(URL).apply(requestOptions).into(chelcheragh_archive);
                    break;
//                case 4:
//                    Glide.with(this).load(URL).apply(requestOptions).into(chelcheragh_write_on_flag);
//                    break;
                case 5:
                    Glide.with(this).load(URL).apply(requestOptions).into(dareGhali);
                    break;
                case 6:
                    Glide.with(this).load(URL).apply(requestOptions).into(chelcheragh_ugc);
                    break;
            }
        }
    }

}
