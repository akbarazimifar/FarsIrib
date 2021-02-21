package ir.farsirib.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class LoginActivity extends Main2Activity {

    EditText mobile_text;

    EditText city_text;
    Button submit_bt;

    TextView account_tv;


    String mobile;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login);

        FrameLayout content_frame= findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.login,content_frame);

        navigationView.getMenu().findItem(R.id.mnu_account).setChecked(true);

        toolbar.setTitle("ورود به شبکه فارس");


        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {

                TextView tv = (TextView) view;
                tv.setTypeface(myfont);
                tv.setTextSize(18);

            }

            mobile_text=findViewById(R.id.mobile_text) ;
            city_text=findViewById(R.id.city_text) ;
            submit_bt= findViewById(R.id.submit_bt) ;

            mobile_text.requestFocus();


            submit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Boolean is_valid = true;
                    if (!mobile_text.getText().toString().startsWith("09") || mobile_text.getText().toString().length() != 11) {
                        is_valid = false;
                    }

                    if (city_text.getText().toString().length() == 0) {

                        is_valid = false;
                    }

                    if (is_valid) {

                        mobile=mobile_text.getText().toString();
                        city=city_text.getText().toString();

                        new apply_activation_key().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"اطلاعات وارد شده صحیح نیست",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private class apply_activation_key extends AsyncTask<Void,Void,String> {

        ProgressDialog pd=new ProgressDialog(LoginActivity.this);


        protected  void onPreExecute()
        {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();


        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Void... voids) {
            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();
            JSONObject get_ad_list =new JSONObject();

            try {

                get_ad_list.put("command","apply_activation_key");
                get_ad_list.put("mobile",mobile);
                get_ad_list.put("city",city);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            namevaluepairs.add(new BasicNameValuePair("myjson",get_ad_list.toString()));

            try
            {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://www.shahreraz.com/Farsirib/webservice/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse=httpclient.execute(httppost);

                String response= EntityUtils.toString(httpresponse.getEntity());

                if (response.startsWith("<farsirib_app>")&&response.endsWith("</farsirib_app>")) {//response is valid

                    response = response.replace("<farsirib_app>", "").replace("</farsirib_app>", "");


                    if (!response.trim().equals("error"))
                    {

                        SharedPreferences.Editor editor=settings.edit();
                        editor.putInt("user_id",Integer.parseInt(response));
                        editor.putString("mobile_num",mobile);
                        editor.putString("city",city);
                        editor.commit();

                        navigationView = findViewById(R.id.nav_view);
                        headerView = navigationView.getHeaderView(0);
                        account_tv = headerView.findViewById(R.id.account_txt);
                        account_tv.setText(settings.getString("mobile_num",""));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getBaseContext(),"شما به شبکه فارس وارد شدید",Toast.LENGTH_SHORT).show();

                                //Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                                Intent i = new Intent(getApplicationContext(), MainListItemsActivity.class);
                                LoginActivity.this.finish();
                                startActivity(i);
                            }
                        });
                    }else
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getBaseContext(),"خطا در دریافت اطلاعات",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(),"خطا در دریافت اطلاعات",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }catch(Exception e)
            {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(),"خطا در دریافت اطلاعات",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            pd.hide();
            pd.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
