package ir.farsirib.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.farsirib.R;
import pub.devrel.easypermissions.EasyPermissions;

public class WriteOnFlagActivity extends Main2Activity implements EasyPermissions.PermissionCallbacks{

    private Context myContext;

    TextInputLayout flagname_layout;
    TextInputLayout flagcity_layout;
    TextInputLayout flagdelneveshte_layout;

    EditText flagname_text;
    EditText flagcity_text;
    EditText flagdelneveshte_text;

    ArrayList<ImageView> images = new ArrayList<ImageView>();
    final int FROM_GALARY = 1;
    final int FROM_CAPTURE = 2;
    private Uri fileUri;
    private final int CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final String TAG = MainActivity.class.getSimpleName();
    Uri uri,gallary_uri;
    String image_base64;
    private Button submit_bt;
    private JSONObject new_flag_write;
    TextView account_tv;
    private String address_image;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private int timeNow;
    private ImageView display_image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.write_on_flag, content_frame);

        myContext = getApplicationContext();

        toolbar.setTitle("ثبت گره عشق");

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
            error_message_dialog("چلچراغ!" , "شما وارد حساب کاربری نشده اید!");

            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawer.closeDrawer(Gravity.RIGHT);
        }

        flagname_layout = findViewById(R.id.flagname_layout);
        flagname_text = findViewById(R.id.flagname_text);
        flagname_text.requestFocus();

        flagcity_layout = findViewById(R.id.flagcity_layout);
        flagcity_text = findViewById(R.id.flagcity_text);

        flagdelneveshte_layout = findViewById(R.id.flagdelneveshte_layout);
        flagdelneveshte_text = findViewById(R.id.flagdelneveshte_text);

        flagcity_text.setText(settings.getString("city",""));
        display_image_view = findViewById(R.id.display_image_view);

        //permission Access
        allowAccessAlert();

        images.add((ImageView) findViewById(R.id.image1));
        images.add((ImageView) findViewById(R.id.image2));

        images.get(0).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                show_dialog(FROM_GALARY);
            }
        });

        images.get(1).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                show_dialog(FROM_CAPTURE);
            }
        });


        submit_bt= findViewById(R.id.submit_bt);

        Date currentTime = Calendar.getInstance().getTime();
        timeNow = currentTime.getHours() ;


        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean is_validate=true;

                if(flagname_text.getText().toString().trim().length()>=1)
                {
                    flagname_layout.setErrorEnabled(false);
                }else
                {
                    flagname_layout.setError("یک اسم وارد کنید");
                    flagname_layout.setErrorEnabled(true);

                    is_validate=false;
                    flagname_text.requestFocus();
                }

                if(flagcity_text.getText().toString().trim().length()>=1)
                {
                    flagcity_layout.setErrorEnabled(false);
                }
                else
                {
                    flagcity_layout.setError("نام شهر را وارد کنید");
                    flagcity_layout.setErrorEnabled(true);

                    is_validate=false;
                    flagcity_text.requestFocus();
                }

                if (is_validate)
                {

                    new_flag_write =new JSONObject();


                    try {

                        new_flag_write.put("image1", address_image);
                        new_flag_write.put("flag_name",flagname_text.getText().toString().trim());
                        new_flag_write.put("flag_mobile",settings.getString("mobile_num",""));
                        new_flag_write.put("flag_city",flagcity_text.getText().toString().trim());
                        new_flag_write.put("flag_delneveshte",flagdelneveshte_text.getText().toString().trim());

                        new_flag_write.put("command","new_flag_write");


                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    if (timeNow >= 0 && timeNow <= 24 )
                    {
                        new send_flag_info().execute();
                    }
                    else
                        error_message_dialog(  "گره عشق", "ثبت گره عشق همزمان با شروع برنامه امکان پذیر است");
                }else
                {
                    Toast.makeText(getApplicationContext(),"خطا در ورود اطلاعات",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void allowAccessAlert() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage("Permission to access the SD-CARD is required for this app to Download PDF.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequest();
                    }
                });

                android.app.AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }

    }

    private boolean hasPermissions(Context context, String[] permissions) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void show_dialog(int type) {
        switch (type) {
            case FROM_GALARY:

                Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(gallery_intent, "لطفا یک عکس را انتخاب کنید"), 2);

                break;
            case FROM_CAPTURE:

                if (ContextCompat.checkSelfPermission(WriteOnFlagActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakenPictureIntent();
                } else {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                    show_dialog(FROM_CAPTURE);

                }
                break;
        }
    }

    private void dispatchTakenPictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){

            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK ){//camera

            CropImage.activity(fileUri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);

        }else  if (requestCode==2&&resultCode==RESULT_OK) {//gallery

            gallary_uri=data.getData();
            CropImage.activity(gallary_uri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);


        }else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resutlUri=result.getUri();

            display_image_view.setVisibility(View.VISIBLE);
            display_image_view.setImageURI(resutlUri);

            // fill_images[current_image]=true;


            //BitmapDrawable bd=((BitmapDrawable) images.get(current_image).getDrawable());
            BitmapDrawable bd=((BitmapDrawable) display_image_view.getDrawable());
            Bitmap bm=bd.getBitmap();

            ByteArrayOutputStream bao=new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,90,bao);

            image_base64= Base64.encodeToString(bao.toByteArray(),Base64.DEFAULT);

            new upload_image().execute();

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "User has denied requested permission");
    }

    public class upload_image extends AsyncTask<Void,Void,String> {

        ProgressDialog pd=new ProgressDialog(WriteOnFlagActivity.this);

        protected void onPreExecute(){
            super.onPreExecute();
            pd.setMessage("در حال آپلود تصویر...");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();
            namevaluepairs.add(new BasicNameValuePair("image",image_base64));

            try{

                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://www.shahreraz.com/club/app/chelcheragh.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));

                HttpResponse httpresponse=httpclient.execute(httppost);

                String response= EntityUtils.toString(httpresponse.getEntity());

                if (response.startsWith("<farsirib_app>")&&response.endsWith("</farsirib_app>")) {//response is valid

                    response = response.replace("<farsirib_app>", "").replace("</farsirib_app>", "");

                    if (!response.trim().equals("0")) {//upload ok

                        //address_images[current_image]=response.trim();
                        address_image =response.trim();
                    } else
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getBaseContext(),"خطا در آپلود تصویر",Toast.LENGTH_SHORT).show();

                                //     images.get(current_image).setImageResource(R.drawable.select_image);

                                //   fill_images[current_image]=false;

                            }
                        });

                    }
                }
                else
                {//error

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getBaseContext(),"خطا در آپلود تصویر",Toast.LENGTH_SHORT).show();

//                            images.get(current_image).setImageResource(R.drawable.select_image);
//
//                            fill_images[current_image]=false;

                        }
                    });
                }
            }catch (Exception e)
            {

                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(),"خطا در آپلود تصویر",Toast.LENGTH_SHORT).show();

//                        images.get(current_image).setImageResource(R.drawable.select_image);
//
//                        fill_images[current_image]=false;

                    }
                });
            }
            return null;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }

    private class send_flag_info extends AsyncTask<Void,Void,String>
    {

        ProgressDialog pd=new ProgressDialog(WriteOnFlagActivity.this);

        protected  void onPreExecute()
        {
            super.onPreExecute();

            pd.setMessage("در حال ارسال ...");
            pd.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();

            namevaluepairs.add(new BasicNameValuePair("myjson",new_flag_write.toString()));

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
                .setTitleText("یا حسین")
                .setContentText( "گره شماره " + node_id + " بنام شما ثبت گردید.\n" +
                        "التماس دعا")
                .show();

        address_image = "";
        flagname_text.setText("");
        flagdelneveshte_text.setText("");
        display_image_view.setVisibility(View.GONE);

        flagname_text.requestFocus();
    }
}
