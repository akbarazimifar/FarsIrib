package ir.farsirib.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import ir.farsirib.R;

public class PostCommentActivity extends Main2Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Context myContext;
    TextInputLayout comment_layout;
    EditText name_text;
    EditText email_text;
    EditText tel_text;
    EditText web_text;
    EditText comment_text;
    Button submit_btn;

    JSONObject new_comment;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.fragment_post_comment,content_frame);

        myContext = this.getApplicationContext();

        if (settings.getInt("user_id",0)!=0)
        {
            navigationView = findViewById(R.id.nav_view);
            headerView = navigationView.getHeaderView(0);
            account_tv = headerView.findViewById(R.id.account_txt);
            account_tv.setText(settings.getString("mobile_num",""));

            drawer.closeDrawer(Gravity.RIGHT);
        }
        else
        {
            error_message_dialog("شبکه فارس!" , "شما وارد حساب کاربری خود نشده اید!");

            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawer.closeDrawer(Gravity.RIGHT);
        }

        String title = String.valueOf(getIntent().getExtras().getString("title"));
        final String caller_context = String.valueOf(getIntent().getExtras().getString("caller_context"));

        toolbar.setTitle(title);
        name_text = findViewById(R.id.name_text);
        name_text.requestFocus();

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

        name_text = findViewById(R.id.name_text);
        name_text.requestFocus();

        comment_layout = findViewById(R.id.comment_layout);
        comment_text = findViewById(R.id.comment_text);

        email_text = findViewById(R.id.email_text);
        tel_text = findViewById(R.id.tel_text);
        web_text = findViewById(R.id.web_text);

        submit_btn = findViewById(R.id.submit_bt);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean is_validate=true;

                if(comment_text.getText().toString().trim().length()>=5)
                {
                    comment_layout.setErrorEnabled(false);

                }else
                {

                    comment_layout.setError("متن ارسالی نمی تواند خالی باشد");
                    comment_layout.setErrorEnabled(true);

                    is_validate=false;

                }

                if (is_validate) {


                    new_comment = new JSONObject();

                    try {
                        new_comment.put("name",name_text.getText().toString().trim());
                        new_comment.put("email",email_text.getText().toString().trim());
                        new_comment.put("tel_num",tel_text.getText().toString().trim());
                        new_comment.put("web_address",web_text.getText().toString().trim());
                        new_comment.put("comment_txt",comment_text.getText().toString().trim());

                        new_comment.put("command","new_comment");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new send_comment().execute();
                }
                else
                {
                    Toast.makeText(myContext,"خطا در ورود اطلاعات",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public class send_comment extends AsyncTask<Void,Void,String> {

        ProgressDialog pd = new ProgressDialog(PostCommentActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال ارسال آگهی");
            pd.show();


        }

        @Override
        protected String doInBackground(Void... voids) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();

            namevaluepairs.add(new BasicNameValuePair("myjson", new_comment.toString()));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://77.36.166.137/Farsirib/webservice/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));
                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());

                if (response.startsWith("<farsirib_app>") && response.endsWith("</farsirib_app>")) {//response is valid

                    response = response.replace("<farsirib_app>", "").replace("</farsirib_app>", "");


                    if (response.trim().equals("ok")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(), "نظر با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(), "خطا در ارسال نظر", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "خطا در ارسال نظر", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), "خطا در ارسال نظر", Toast.LENGTH_SHORT).show();


                    }
                });
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.hide();
            pd.dismiss();

        }
    }
}
