package ir.farsirib.Activity;

import android.annotation.SuppressLint;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * Created by alireza on 02/08/2017.
 */

public class CitizenReporterActivity extends Main2Activity implements EasyPermissions.PermissionCallbacks{
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private static final int SELECT_VIDEO = 3 ;
    private String pathToStoredVideo;
    private VideoView displayRecordedVideo;
//    private static final String SERVER_PATH = "https://farsirib.cloud/Farsirib/webservice/videos/";
    private static final String SERVER_PATH = "http://shahreraz.com/Farsirib/webservice/videos/";
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    int current_image;
    Boolean[] fill_images=new Boolean[4];
    String[] address_images=new String[4];
    String image_base64;
    Uri uri,gallary_uri;
    Button submit_bt;
    final int FROM_GALARY = 1;
    final int FROM_CAPTURE = 2;
    final int FROM_CAMERA = 3;
    final int VIDEO_FROM_GALARY = 4;

    TextInputLayout title_news_layout;
    TextInputLayout news_layout;
    EditText title_news_text;
    EditText news_text;
    EditText name_text;
    EditText email_text;
    EditText tel_text;
    MyTextView upload_txt;
    ProgressBar uploaProcess;
    private JSONObject new_report;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    // Request code for camera
    private final int CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_WRITE_STORAGE = 112;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private String selectedPath;

    public CitizenReporterActivity() {
        // Required empty public constructor
    }

    public static ir.farsirib.Fragment.CitizenReporterFragment newInstance() {
        ir.farsirib.Fragment.CitizenReporterFragment fragment = new ir.farsirib.Fragment.CitizenReporterFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.fragment_citizen_reporter);

        FrameLayout content_frame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.fragment_citizen_reporter,content_frame);

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


        allowAccessAlert();

        displayRecordedVideo = findViewById(R.id.video_display);
        upload_txt = findViewById(R.id.upload_txt);
        uploaProcess = findViewById(R.id.upload_progress);

        images.add((ImageView) findViewById(R.id.image1));
        images.add((ImageView) findViewById(R.id.image2));
        images.add((ImageView) findViewById(R.id.image3));
        images.add((ImageView) findViewById(R.id.image4));

        images.get(0).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                current_image = 0;

                if (!fill_images[current_image]) {
                    show_dialog(FROM_GALARY);
                }else
                {
                    show_delete_dialog();
                }
            }
        });

        images.get(1).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                current_image = 1;

                if (!fill_images[current_image]) {
                    show_dialog(FROM_CAPTURE);
                }else
                {
                    show_delete_dialog();
                }
            }
        });


        images.get(2).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                current_image = 2;

                if (!fill_images[current_image]) {
                    show_dialog(FROM_CAMERA);
                } else {
                    show_delete_dialog();
                }

            }
        });

        images.get(3).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                current_image = 3;

                if (!fill_images[current_image]) {
                    show_dialog(VIDEO_FROM_GALARY);
                } else {
                    show_delete_dialog();
                }

            }
        });

        Arrays.fill(fill_images,false);
        Arrays.fill(address_images,"");


        title_news_layout = findViewById(R.id.title_news_layout);
        title_news_text = findViewById(R.id.title_news_text);
        news_layout = findViewById(R.id.news_layout);
        news_text = findViewById(R.id.news_text);
        name_text = findViewById(R.id.name_text);
        email_text = findViewById(R.id.email_text);
        tel_text = findViewById(R.id.tel_text);
        tel_text.setText(settings.getString("mobile_num",""));

        submit_bt= findViewById(R.id.submit_bt);

        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean is_validate=true;

                if(title_news_text.getText().toString().trim().length()>=5)
                {
                    title_news_layout.setErrorEnabled(false);
                }else
                {
                    title_news_layout.setError("عنوان خبر باید حداقل 5 حرف باشد");
                    title_news_layout.setErrorEnabled(true);

                    is_validate=false;
                    title_news_text.requestFocus();
                }

                if(news_text.getText().toString().trim().length()>=10)
                {
                    news_layout.setErrorEnabled(false);
                }
                else
                {
                    news_layout.setError("متن خبر باید حداقل 10 حرف باشد");
                    news_layout.setErrorEnabled(true);

                    is_validate=false;
                    news_text.requestFocus();
                }

                if (is_validate)
                {

                    new_report =new JSONObject();


                    try {

                        if (fill_images[0] == true) {
                            new_report.put("image1", address_images[0]);
                        }else
                        {
                            new_report.put("image1", "");
                        }



                        if (fill_images[1] == true) {
                            new_report.put("image2", address_images[1]);
                        }else
                        {
                            new_report.put("image2", "");
                        }


                        if (fill_images[2] == true) {
                            new_report.put("image3", address_images[2]);
                        }

                        if (fill_images[3] == true) {
                            new_report.put("image3", address_images[3]);
                        }

                        if(!fill_images[2] && !fill_images[3])
                            new_report.put("image3", "");


                        new_report.put("news_title",title_news_text.getText().toString().trim());
                        new_report.put("news_text",news_text.getText().toString().trim());
                        new_report.put("citizen_name",name_text.getText().toString().trim());
                        new_report.put("citizen_email",email_text.getText().toString().trim());
                        new_report.put("citizen_tel",tel_text.getText().toString().trim());

                        new_report.put("command","new_report");


                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    new send_report().execute();

                }else
                {
                    Toast.makeText(getApplicationContext(),"خطا در ورود اطلاعات",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //return rootView;
    }

    private void show_delete_dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("آیا از حذف مطمئن هستید ؟");

        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (current_image) {
                    case 0:
                        images.get(current_image).setImageResource(R.mipmap.ic_folder);
                        fill_images[current_image] = false;
                        break;
                    case 1:
                        images.get(current_image).setImageResource(R.mipmap.ic_camera);
                        fill_images[current_image] = false;
                        break;
                    case 2:
                        images.get(current_image).setImageResource(R.mipmap.ic_video);
                        fill_images[current_image] = false;
                        displayRecordedVideo.setVisibility(View.GONE);
                        break;
                    case 3:
                        images.get(current_image).setImageResource(R.mipmap.ic_video_library);
                        fill_images[current_image] = false;
                        displayRecordedVideo.setVisibility(View.GONE);
                        break;
                }


            }
        }).setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog=builder.create();

        dialog.show();

    }

    private  void empty_field()
    {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("شبکه فارس")
                .setContentText( "اطلاعات با موفقیت ارسال شد" )
                .show();

        images.get(0).setImageResource(R.mipmap.ic_folder);
        fill_images[0] = false;
        address_images[0] = "";

        images.get(1).setImageResource(R.mipmap.ic_camera);
        fill_images[1] = false;
        address_images[1] = "";

        images.get(2).setImageResource(R.mipmap.ic_video);
        fill_images[2] = false;
        address_images[2] = "";

        images.get(3).setImageResource(R.mipmap.ic_video_library);
        fill_images[3] = false;
        address_images[3] = "";

        title_news_text.setText("");
        news_text.setText("");
        name_text.setText("");
        email_text.setText("");
        tel_text.setText("");
        displayRecordedVideo.setVisibility(View.GONE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void show_dialog(int type) {
        switch (type)
        {
            case FROM_GALARY:
                    Intent gallery_intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) ;
                    startActivityForResult(Intent.createChooser(gallery_intent,"لطفا یک عکس را انتخاب کنید"),2);
                break;
            case FROM_CAPTURE:
                if(ContextCompat.checkSelfPermission(CitizenReporterActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    dispatchTakenPictureIntent();
                }
                else{
                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                    show_dialog(FROM_CAPTURE);
                }

                break;
            case FROM_CAMERA:

                if (ContextCompat.checkSelfPermission(CitizenReporterActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if(videoCaptureIntent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                    show_dialog(FROM_CAMERA);
                }
                break;
            case VIDEO_FROM_GALARY:

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "لطفا یک ویدیو انتخاب کنید "), SELECT_VIDEO);

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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK ){//camera

            CropImage.activity(fileUri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);

        }else  if (requestCode==2&&resultCode==RESULT_OK) {//gallery

            gallary_uri=data.getData();
            CropImage.activity(gallary_uri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);

        } else if (requestCode==REQUEST_VIDEO_CAPTURE&&resultCode==RESULT_OK) {//video

            fill_images[2]=true;

            uri = data.getData();
            if(EasyPermissions.hasPermissions(CitizenReporterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                uploaProcess.setVisibility(View.VISIBLE);
                upload_txt.setVisibility(View.VISIBLE);
                displayRecordedVideo.setVisibility(View.VISIBLE);
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, CitizenReporterActivity.this);
                Log.d(TAG, "Recorded Video Path " + pathToStoredVideo);
                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }else{
                EasyPermissions.requestPermissions(CitizenReporterActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resutlUri=result.getUri();

            images.get(current_image).setImageURI(resutlUri);


            fill_images[current_image]=true;


            BitmapDrawable bd=((BitmapDrawable) images.get(current_image).getDrawable());
            Bitmap bm=bd.getBitmap();

            ByteArrayOutputStream bao=new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG,90,bao);

            image_base64= Base64.encodeToString(bao.toByteArray(),Base64.DEFAULT);

            new upload_image().execute();

        }else if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {

                fill_images[3]=true;

                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);

                uploaProcess.setVisibility(View.VISIBLE);
                upload_txt.setVisibility(View.VISIBLE);
                displayRecordedVideo.setVisibility(View.VISIBLE);
                displayRecordedVideo.setVideoURI(selectedImageUri);
                displayRecordedVideo.start();

                uploadVideoToServer(selectedPath);
               // textView.setText(selectedPath);
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private String getFileDestinationPath(){
        String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment + "/video/");
        if(!directoryFolder.exists()){
            directoryFolder.mkdir();
        }
        Log.d(TAG, "Full path " + filePathEnvironment + "/video/" + generatedFilename + ".mp4");

        address_images[2] = "videos/" + generatedFilename + ".mp4";
        return filePathEnvironment + "/video/" + generatedFilename + ".mp4";
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, CitizenReporterActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(uri != null){
            if(EasyPermissions.hasPermissions(CitizenReporterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                displayRecordedVideo.setVisibility(View.VISIBLE);
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, CitizenReporterActivity.this);
                Log.d(TAG, "Recorded Video Path " + pathToStoredVideo);


                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }
        }
    }

    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "User has denied requested permission");
    }

    private void uploadVideoToServer(String pathToVideoFile){
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", arabicToDecimal(videoFile.getName()), videoBody);

        if (current_image == 2) {
            address_images[2] = "videos/" + videoFile.getName();
            }
            else if(current_image == 3) {

                address_images[3] = "videos/" + videoFile.getName();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VideoInterface vInterface = retrofit.create(VideoInterface.class);
        Call<ResultObject>  serverCom = vInterface.uploadVideoToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if(!TextUtils.isEmpty(result.getSuccess())){
                    Toast.makeText(CitizenReporterActivity.this, "سپاس، " + result.getSuccess(), Toast.LENGTH_LONG).show();
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

    public class upload_image extends AsyncTask<Void,Void,String> {

        ProgressDialog pd=new ProgressDialog(CitizenReporterActivity.this);

        protected void onPreExecute(){
            super.onPreExecute();
            pd.setMessage("لطفا منتظر بمانید");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();
            namevaluepairs.add(new BasicNameValuePair("image",image_base64));

            try{

                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://shahreraz.com/Farsirib/webservice/command.php");
//                HttpPost httppost=new HttpPost("https://farsirib.cloud/Farsirib/webservice/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));

                HttpResponse httpresponse=httpclient.execute(httppost);

                String response= EntityUtils.toString(httpresponse.getEntity());

                if (response.startsWith("<farsirib_app>")&&response.endsWith("</farsirib_app>")) {//response is valid

                    response = response.replace("<farsirib_app>", "").replace("</farsirib_app>", "");

                    if (!response.trim().equals("0")) {//upload ok

                        address_images[current_image]=response.trim();
                    } else
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getBaseContext(),"خطا در آپلود تصویر",Toast.LENGTH_SHORT).show();

                                images.get(current_image).setImageResource(R.drawable.select_image);

                                fill_images[current_image]=false;

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

                            images.get(current_image).setImageResource(R.drawable.select_image);

                            fill_images[current_image]=false;

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

                        images.get(current_image).setImageResource(R.drawable.select_image);

                        fill_images[current_image]=false;

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

    private class send_report extends AsyncTask<Void,Void,String>
    {

        ProgressDialog pd=new ProgressDialog(CitizenReporterActivity.this);

        protected  void onPreExecute()
        {
            super.onPreExecute();

            pd.setMessage("در حال ارسال ...");
            pd.show();


        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();

            namevaluepairs.add(new BasicNameValuePair("myjson",new_report.toString()));

            try
            {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://77.36.166.137/Farsirib/webservice/command.php");
//                HttpPost httppost=new HttpPost("https://farsirib.cloud/Farsirib/webservice/command.php");
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

                                //Toast.makeText(getApplicationContext(),"با موفقیت ارسال شد",Toast.LENGTH_SHORT).show();
                                empty_field();
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                error_message_dialog("اوه..." , "خطا در ارسال اطلاعات");
                               // Toast.makeText(getBaseContext(),"خطا در ارسال",Toast.LENGTH_SHORT).show();
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

    private void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_WRITE_STORAGE: {
//
//                if (grantResults.length == 0
//                        || grantResults[0] !=
//                        PackageManager.PERMISSION_GRANTED) {
//
//                    Log.i(TAG, "Permission has been denied by user");
//
//                } else {
//
//                    Log.i(TAG, "Permission has been granted by user");
//
//                }
//                return;
//            }
//        }
//    }

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

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(CitizenReporterActivity.this,MainListItemsActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
