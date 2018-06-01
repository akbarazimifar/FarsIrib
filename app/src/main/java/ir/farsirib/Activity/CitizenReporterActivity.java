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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ir.farsirib.CustomWidgets.MyEditText;
import ir.farsirib.CustomWidgets.MyTextView;
import ir.farsirib.Interfaces.ResultObject;
import ir.farsirib.Interfaces.VideoInterface;
import java.util.jar.Manifest;
import ir.farsirib.R;
import ir.farsirib.utils.UICircularImage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by alireza on 02/08/2017.
 */

public class CitizenReporterActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private String pathToStoredVideo;
    private VideoView displayRecordedVideo;
    private static final String SERVER_PATH = "http://www.mob.shahreraz.com/Farsirib/webservice/videos/";
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    int current_image;
    Boolean[] fill_images=new Boolean[3];
    String[] address_images=new String[3];
    String image_base64;
    Uri uri,gallary_uri;
    Button submit_bt;
    final int FROM_GALARY = 1;
    final int FROM_CAPTURE = 2;
    final int FROM_CAMERA = 3;

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



    public CitizenReporterActivity() {
        // Required empty public constructor
    }

    public static ir.farsirib.Fragment.CitizenReporterFragment newInstance() {
        ir.farsirib.Fragment.CitizenReporterFragment fragment = new ir.farsirib.Fragment.CitizenReporterFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_citizen_reporter);

        allowAccessAlert();

        displayRecordedVideo = findViewById(R.id.video_display);
        upload_txt = findViewById(R.id.upload_txt);
        uploaProcess = findViewById(R.id.upload_progress);

        images.add((ImageView) findViewById(R.id.image1));
        images.add((ImageView) findViewById(R.id.image2));
        images.add((ImageView) findViewById(R.id.image3));

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

              //  Toast.makeText(getApplicationContext(),"این بخش در نسخه های بعدی عملیاتی خواهد شد.",Toast.LENGTH_SHORT).show();
                                                 if (!fill_images[current_image]) {
                                                     show_dialog(FROM_CAMERA);
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
                        }else
                        {
                            new_report.put("image3", "");
                        }

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
                        //displayRecordedVideo.setBackground();
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
        images.get(0).setImageResource(R.mipmap.ic_folder);
        fill_images[0] = false;

        images.get(1).setImageResource(R.mipmap.ic_camera);
        fill_images[1] = false;

        images.get(2).setImageResource(R.mipmap.ic_video);
        fill_images[2] = false;

        title_news_text.setText("");
        news_text.setText("");
        name_text.setText("");
        email_text.setText("");
        tel_text.setText("");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void show_dialog(int type) {
        switch (type)
        {
            case FROM_GALARY:
              //  if(ContextCompat.checkSelfPermission(CitizenReporterActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    Intent gallery_intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) ;
                    startActivityForResult(Intent.createChooser(gallery_intent,"لطفا یک عکس را انتخاب کنید"),2);
//                }
//                else{
//                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
//                    }
//                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
//                    show_dialog(FROM_GALARY);
//
//                }
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

//                if ( getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
//                    dispatchTakenPictureIntent();
//                }

//                if(ContextCompat.checkSelfPermission(CitizenReporterActivity.this, ir.farsirib.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//                    dispatchTakenPictureIntent();
//                }
//                else{
//                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
//                        Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
//                    }
//                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
//                }

//                if ( getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
//                {
//                    Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(camera_intent,1);
//
//                }


//                File file = new File(Environment.getExternalStorageDirectory(),"file/"+String.valueOf(System.currentTimeMillis()+".png"));
//
//                uri= Uri.fromFile(file);
//
//                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
//
//                camera_intent.putExtra("return-data",true);

                //startActivityForResult(camera_intent,1);
                break;
            case FROM_CAMERA:

                Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(videoCaptureIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
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

        } else if (requestCode==REQUEST_VIDEO_CAPTURE&&resultCode==RESULT_OK) {//video

            fill_images[2]=true;

            uri = data.getData();
            if(EasyPermissions.hasPermissions(CitizenReporterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                uploaProcess.setVisibility(View.VISIBLE);
                upload_txt.setVisibility(View.VISIBLE);
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

        }
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
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, CitizenReporterActivity.this);
                Log.d(TAG, "Recorded Video Path " + pathToStoredVideo);


                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "User has denied requested permission");
    }

    private void uploadVideoToServer(String pathToVideoFile){
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
        address_images[2] = "videos/" + videoFile.getName();
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
                HttpPost httppost=new HttpPost("http://www.mob.shahreraz.com/Farsirib/webservice/command.php");
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
                HttpPost httppost=new HttpPost("http://www.mob.shahreraz.com/Farsirib/webservice/command.php");
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

                                Toast.makeText(getApplicationContext(),"با موفقیت ارسال شد",Toast.LENGTH_SHORT).show();
                                empty_field();
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getBaseContext(),"خطا در ارسال",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getBaseContext(),"خطا در ارسال",Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }catch(Exception e)
            {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(),"خطا در ارسال",Toast.LENGTH_SHORT).show();
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
//        Intent intent = new Intent(CitizenReporterActivity.this,OptionActivity.class);
//        CitizenReporterActivity.this.finish();
//        OptionActivity.flag = true;
//        startActivity(intent);
//    }
}
