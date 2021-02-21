package ir.farsirib.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.farsirib.CustomWidgets.MyTextView;
import ir.farsirib.Interfaces.ResultObject;
import ir.farsirib.Interfaces.VideoInterface;
import ir.farsirib.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KoodakUGCActivity extends Main2Activity implements EasyPermissions.PermissionCallbacks, AdapterView.OnItemSelectedListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Context myContext;
    String itemSelected="";
    private EditText name_text;
    private EditText age_text;
    private EditText mobile_text;
    private EditText city_text;
    private EditText description_text;
    private Button submit_btn;
    private TextInputLayout name_layout;
    private TextInputLayout city_layout;
    private TextInputLayout age_layout;
    private TextInputLayout mobile_layout;

    ArrayList<ImageView> images = new ArrayList<ImageView>();
    final int FROM_GALARY = 1;
    final int FROM_CAPTURE = 2;
    final int FROM_CAMERA = 3;
    private Uri fileUri;
    private final int CAMERA_REQUEST_CODE = 100;
    private static final int READ_REQUEST_CODE = 200;
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final String TAG = MainActivity.class.getSimpleName();
    Uri uri,gallary_uri;
    String image_base64;
    private Button submit_bt;
    TextView account_tv;
    private String address_image;
    private ImageView display_image_view;
    private JSONObject new_koodak_UGC;
    private Spinner spinner;
    MyTextView upload_txt;
    ProgressBar uploaProcess;
    private VideoView displayRecordedVideo;
    private String pathToStoredVideo;
    private static final String SERVER_PATH = "http://www.shahreraz.com/Farsirib/webservice/videos/";
    private String address_video="";
    private int id_category;
    private int category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame= findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.ugc,content_frame);

        myContext = getApplicationContext();

        toolbar.setTitle("تولیدات بچه ها");

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

        age_text = findViewById(R.id.age_text);
        age_layout = findViewById(R.id.age_layout);

        mobile_text = findViewById(R.id.mobile_text);
        mobile_layout = findViewById(R.id.mobile_layout);
        mobile_text.setText(settings.getString("mobile_num",""));

        city_text = findViewById(R.id.city_text);
        city_layout = findViewById(R.id.city_layout);
        city_text.setText(settings.getString("city",""));

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("نقاشی با مداد رنگ");
        spinnerArray.add("نقاشی با آبرنگ");
        spinnerArray.add("نقاشی با گواش");
        spinnerArray.add("نقاشی با مداد شمعی");
        spinnerArray.add("نقاشی با رنگ روغن");
        spinnerArray.add("کلاژ");
        spinnerArray.add("کاردستی");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        description_text = findViewById(R.id.description_text);
        display_image_view = findViewById(R.id.display_image_view);
        displayRecordedVideo = findViewById(R.id.video_display);
        upload_txt = findViewById(R.id.upload_txt);
        uploaProcess = findViewById(R.id.upload_progress);

        //permission Access
        allowAccessAlert();

        images.add((ImageView) findViewById(R.id.image1));
        images.add((ImageView) findViewById(R.id.image2));
        images.add((ImageView) findViewById(R.id.image3));

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

        images.get(2).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                show_dialog(FROM_CAMERA);
            }
        });


        submit_btn = findViewById(R.id.submit_bt);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean is_validate=true;

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
                        case "KoodakActivity":
                            id_category = 72;
                            break;
                    }

                    switch (itemSelected)
                    {
                        case  "نقاشی با مداد رنگ":
                            category = 0;
                            break;
                        case  "نقاشی با آبرنگ":
                            category = 1;
                            break;
                        case  "نقاشی با گواش":
                            category = 2;
                            break;
                        case  "نقاشی با مداد شمعی":
                            category = 3;
                            break;
                        case  "نقاشی با رنگ روغن":
                            category = 4;
                            break;
                        case  "کلاژ":
                            category = 5;
                            break;
                        case  "کاردستی":
                            category = 6;
                            break;
                    }

                    new_koodak_UGC =new JSONObject();

                    try {

                        new_koodak_UGC.put("name",name_text.getText().toString().trim());
                        new_koodak_UGC.put("age",age_text.getText().toString().trim());
                        new_koodak_UGC.put("city",city_text.getText().toString().trim());
                        new_koodak_UGC.put("mobile",mobile_text.getText().toString().trim());
                        new_koodak_UGC.put("description",description_text.getText().toString().trim());
                        new_koodak_UGC.put("category",category);
                        new_koodak_UGC.put("image", address_image);
                        new_koodak_UGC.put("video",address_video);
                        new_koodak_UGC.put("id_category",id_category);

                        new_koodak_UGC.put("command","new_ugc");

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    new send_koodak_UGC_info().execute();

                }
                else
                    Toast.makeText(getApplicationContext(),"خطا در ورود اطلاعات",Toast.LENGTH_SHORT).show();

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

                if (ContextCompat.checkSelfPermission(KoodakUGCActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakenPictureIntent();
                } else {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                    show_dialog(FROM_CAPTURE);
                }
                break;
            case FROM_CAMERA:

                if (ContextCompat.checkSelfPermission(KoodakUGCActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if(videoCaptureIntent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                    }
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


        }else if (requestCode==REQUEST_VIDEO_CAPTURE&&resultCode==RESULT_OK) {//video

            uri = data.getData();
            if(EasyPermissions.hasPermissions(KoodakUGCActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                displayRecordedVideo.setVisibility(View.GONE);
                uploaProcess.setVisibility(View.VISIBLE);
                upload_txt.setVisibility(View.VISIBLE);
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, KoodakUGCActivity.this);
                Log.d(TAG, "Recorded Video Path " + pathToStoredVideo);
                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }else {
                EasyPermissions.requestPermissions(KoodakUGCActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        itemSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class upload_image extends AsyncTask<Void,Void,String> {

        ProgressDialog pd=new ProgressDialog(KoodakUGCActivity.this);

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

    private class send_koodak_UGC_info extends AsyncTask<Void,Void,String>
    {

        ProgressDialog pd=new ProgressDialog(KoodakUGCActivity.this);

        protected  void onPreExecute()
        {
            super.onPreExecute();

            pd.setMessage("در حال ارسال ...");
            pd.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();

            namevaluepairs.add(new BasicNameValuePair("myjson",new_koodak_UGC.toString()));

            try
            {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://www.shahreraz.com/club/app/chelcheragh.php");
                //HttpPost httppost=new HttpPost("http://www.mob.shahreraz.com/mob/Farsirib/webservice/command.php");
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
                .setTitleText("برنامه کودک")
                .setContentText( "اطلاعات با موفقیت ارسال شد" )
                .show();

        address_image = "";
        address_video = "";
        name_text.setText("");
        age_text.setText("");
        description_text.setText("");
        display_image_view.setVisibility(View.GONE);
        displayRecordedVideo.setVisibility(View.GONE);
        name_text.requestFocus();
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void uploadVideoToServer(String pathToVideoFile){
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
        address_video = "videos/" + videoFile.getName();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VideoInterface vInterface = retrofit.create(VideoInterface.class);
        Call<ResultObject> serverCom = vInterface.uploadVideoToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if(!TextUtils.isEmpty(result.getSuccess())){
                    Toast.makeText(KoodakUGCActivity.this, "سپاس، " + result.getSuccess(), Toast.LENGTH_LONG).show();
                    uploaProcess.setVisibility(View.GONE);
                    upload_txt.setVisibility(View.GONE);
                    Log.d(TAG, "Result " + result.getSuccess());
                }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
            }
        });
    }

}
