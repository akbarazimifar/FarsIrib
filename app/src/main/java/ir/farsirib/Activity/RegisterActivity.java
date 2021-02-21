package ir.farsirib.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import com.google.android.material.textfield.TextInputLayout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.farsirib.R;

public class RegisterActivity extends Main2Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Context myContext;
    private EditText name_text;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private EditText age_text;
    private EditText mobile_text;
    private EditText city_text;
    private Button submit_btn;
    private TextInputLayout name_layout;
    private TextInputLayout city_layout;
    private TextInputLayout age_layout;
    private TextInputLayout mobile_layout;
    private JSONObject new_register;
    private RadioButton radioMale;
    private int id_category = 0;
    private int sex_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame= findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.register,content_frame);

        myContext = getApplicationContext();

        toolbar.setTitle("ثبت نام حضور در برنامه");

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

            drawer.closeDrawer(Gravity.RIGHT);
        }
        else {
            error_message_dialog("شبکه فارس!" , "شما وارد حساب کاربری نشده اید!");

            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawer.closeDrawer(Gravity.RIGHT);
        }

        name_text = findViewById(R.id.name_text);
        name_layout = findViewById(R.id.name_layout);
        name_text.requestFocus();

        radioSexGroup = findViewById(R.id.radioSex);
        radioMale = findViewById(R.id.radioMale);

        age_text = findViewById(R.id.age_text);
        age_layout = findViewById(R.id.age_layout);

        mobile_text = findViewById(R.id.mobile_text);
        mobile_layout = findViewById(R.id.mobile_layout);
        mobile_text.setText(settings.getString("mobile_num",""));

        city_text = findViewById(R.id.city_text);
        city_layout = findViewById(R.id.mobile_layout);
        city_text.setText(settings.getString("city",""));

        submit_btn = findViewById(R.id.submit_bt);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean is_validate=true;

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = findViewById(selectedId);

                if(name_text.getText().toString().trim().length()>=1)
                {
                    name_layout.setErrorEnabled(false);
                }else
                {
                    name_layout.setError("یک اسم وارد کنید");
                    name_layout.setErrorEnabled(true);

                    is_validate=false;
                    name_text.requestFocus();
                }

                if(age_text.getText().toString().trim().length()>=1)
                {
                    age_layout.setErrorEnabled(false);
                }
                else
                {
                    age_layout.setError("سن را وارد کنید");
                    age_layout.setErrorEnabled(true);

                    is_validate=false;
                    age_text.requestFocus();
                }

                if(city_text.getText().toString().trim().length()>=1)
                {
                    city_layout.setErrorEnabled(false);
                }
                else
                {
                    city_layout.setError("نام شهر را وارد کنید");
                    city_layout.setErrorEnabled(true);

                    is_validate=false;
                    city_text.requestFocus();
                }

                if(mobile_text.getText().toString().trim().length()>=1)
                {
                    mobile_layout.setErrorEnabled(false);
                }
                else
                {
                    mobile_layout.setError("شماره تلفن را وارد کنید");
                    mobile_layout.setErrorEnabled(true);

                    is_validate=false;
                    mobile_text.requestFocus();
                }

                if (is_validate) {

                    final String caller_activity = String.valueOf(getIntent().getExtras().getString("caller_activity"));

                    switch (caller_activity)
                    {
                        case "FarsiShoActivity":
                            id_category = 91;
                            break;
                        case "KoodakActivity":
                            id_category = 72;
                            break;
                        case "KhoshaActivity":
                            id_category = 4;
                            break;
                        case "KashaneActivity":
                            id_category = 3;
                            break;
                    }

                    switch (radioSexButton.getText().toString().trim())
                    {
                        case "مذکر":
                            sex_id = 0;
                            break;
                        case  "مونث":
                            sex_id = 1;
                            break;
                    }

                    new_register =new JSONObject();

                    try {

                        new_register.put("name",name_text.getText().toString().trim());
                        new_register.put("sex",sex_id);
                        new_register.put("age",age_text.getText().toString().trim());
                        new_register.put("city",city_text.getText().toString().trim());
                        new_register.put("mobile",mobile_text.getText().toString().trim());
                        new_register.put("id_category",id_category);

                        new_register.put("command","new_register");

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    new send_register_info().execute();

                }
                else
                    Toast.makeText(getApplicationContext(),"خطا در ورود اطلاعات",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class send_register_info extends AsyncTask<Void,Void,String> {

        ProgressDialog pd=new ProgressDialog(RegisterActivity.this);

        protected  void onPreExecute()
        {
            super.onPreExecute();

            pd.setMessage("در حال ارسال ...");
            pd.show();

        }

        @Override
        protected String doInBackground(Void... voids) {

            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();

            namevaluepairs.add(new BasicNameValuePair("myjson",new_register.toString()));

            try
            {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://www.shahreraz.com/club/app/chelcheragh.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));
                HttpResponse httpresponse=httpclient.execute(httppost);

                String response= EntityUtils.toString(httpresponse.getEntity());

                if (response.startsWith("<farsirib_app>")&&response.endsWith("</farsirib_app>")) {//response is valid

                    response = response.replace("<farsirib_app>", "").replace("</farsirib_app>", "");


                    if(!response.trim().equals(""))
                    {
                        final String finalResponse = response;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int node_id = Integer.parseInt(finalResponse);

                                empty_field(node_id);
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                error_message_dialog("اوه..." , "خطا در ارسال اطلاعات");
                                //Toast.makeText(getBaseContext(),"خطا در ارسال",Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(getBaseContext(),"خطا در ارسال",Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(getBaseContext(),"خطا در ارسال",Toast.LENGTH_SHORT).show();
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

    private void empty_field(int node_id) {

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("شبکه فارس")
                .setContentText( "ثبت نام با موفقیت انجام شد" )
                .show();

        name_text.setText("");
        radioMale.setChecked(true);
        age_text.setText("");
        name_text.requestFocus();

    }
}
