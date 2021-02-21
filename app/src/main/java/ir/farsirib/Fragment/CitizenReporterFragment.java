package ir.farsirib.Fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Arrays;

import ir.farsirib.R;
import ir.farsirib.utils.UICircularImage;

import static android.app.Activity.RESULT_OK;


public class CitizenReporterFragment extends FragmentActivity {

    UICircularImage imgFromGallery;
    UICircularImage imgFromCapture;
    //ArrayList<UICircularImage> images = new ArrayList<UICircularImage>();
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    int current_image;
    Boolean[] fill_images=new Boolean[3];
    String[] address_images=new String[3];
    String image_base64;
    Uri uri;
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
    private JSONObject new_report;


    public CitizenReporterFragment() {
        // Required empty public constructor
    }

    public static CitizenReporterFragment newInstance() {
        CitizenReporterFragment fragment = new CitizenReporterFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_citizen_reporter);

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_citizen_reporter, container, false);

//        imgFromCapture = (UICircularImage) rootView.findViewById(R.id.image2);

//        images.add((UICircularImage) rootView.findViewById(R.id.image1));
//        images.add((UICircularImage) rootView.findViewById(R.id.image2));
//        images.add((UICircularImage) rootView.findViewById(R.id.image3));

        images.add((ImageView) findViewById(R.id.image1));
        images.add((ImageView) findViewById(R.id.image2));
        images.add((ImageView) findViewById(R.id.image3));

        images.get(0).setOnClickListener(new View.OnClickListener() {
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
                                             @Override
                                             public void onClick(View view) {
                                                 current_image = 2;

                                                 Toast.makeText(getApplicationContext(),"این بخش در نسخه های بعدی عملیاتی خواهد شد.",Toast.LENGTH_SHORT).show();
//                                                 if (!fill_images[current_image]) {
//                                                     show_dialog(FROM_CAMERA);
//                                                 } else {
//                                                     show_delete_dialog();
//                                                 }

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

//                if(title_news_text.getText().toString().trim().length()>=5)
//                {
//                    title_news_layout.setErrorEnabled(false);
//                }else
//                {
//                    title_news_layout.setError("عنوان خبر باید حداقل 5 حرف باشد");
//                    title_news_layout.setErrorEnabled(true);
//
//                    is_validate=false;
//                    title_news_text.requestFocus();
//                }
//
//                if(news_text.getText().toString().trim().length()>=10)
//                {
//                    news_layout.setErrorEnabled(false);
//                }
//                else
//                {
//                    news_layout.setError("متن خبر باید حداقل 10 حرف باشد");
//                    news_layout.setErrorEnabled(true);
//
//                    is_validate=false;
//                    news_text.requestFocus();
//                }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("آیا مطمئن به حذف عکس هستید ؟");

        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                images.get(current_image).setImageResource(R.drawable.select_image);
                fill_images[current_image]=false;

            }
        });

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog=builder.create();

        dialog.show();

    }

    private void show_dialog(int type) {
        switch (type)
        {
            case FROM_GALARY:
                Intent gallery_intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) ;
                startActivityForResult(Intent.createChooser(gallery_intent,"لطفا یک عکس را انتخاب کنید"),2);
                break;
            case FROM_CAPTURE:
                Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File file=new File(Environment.getExternalStorageDirectory(),"file/"+String.valueOf(System.currentTimeMillis()+".png"));

                uri= Uri.fromFile(file);

                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                camera_intent.putExtra("return-data",true);

                startActivityForResult(camera_intent,1);
                break;
            case FROM_CAMERA:
                Intent camera_intent2=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                File file2=new File(Environment.getExternalStorageDirectory(),"file/"+String.valueOf(System.currentTimeMillis()+".mp4"));

                uri= Uri.fromFile(file2);

                camera_intent2.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                camera_intent2.putExtra("return-data",true);

                startActivityForResult(camera_intent2,3);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if (requestCode==1&&resultCode==RESULT_OK)
        {//camera

            CropImage.activity(uri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);
      //      Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_SHORT).show();

//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(String.valueOf(uri), opts);
//            opts.inJustDecodeBounds = false;
//            Picasso.with(getContext())
//                    .load(uri)
//                    .error(R.mipmap.ic_launcher)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .resize(200,200)
//                    .centerCrop()
//                    .into(images.get(current_image));

  //          images.get(current_image).setImageURI(uri);
  //          fill_images[current_image]=true;

   //         upload_images();


        }else  if (requestCode==2&&resultCode==RESULT_OK) {//gallery

            uri=data.getData();
  //          Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_SHORT).show();

            CropImage.activity(uri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);


      //      requestCode = CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(String.valueOf(uri), opts);
//            opts.inJustDecodeBounds = false;
//            Picasso.with(getContext())
//                    .load(uri)
//                    .error(R.mipmap.ic_launcher)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .resize(200,200)
//                    .centerCrop()
//                    .into(images.get(current_image));
//
//            fill_images[0]=true;
//
//            upload_images();

   //         images.get(current_image).setImageURI(uri);
  //          fill_images[current_image]=true;

  //          upload_images();


        } else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK)
        {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resutlUri=result.getUri();

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(String.valueOf(resutlUri), opts);
            opts.inJustDecodeBounds = false;
            Picasso.with(getApplicationContext())
                    .load(resutlUri)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(200,200)
                    .centerCrop()
                    .into(images.get(current_image));


            fill_images[current_image]=true;


            BitmapDrawable bd=((BitmapDrawable) images.get(current_image).getDrawable());
            Bitmap bm=bd.getBitmap();

            ByteArrayOutputStream bao=new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG,90,bao);

            image_base64= Base64.encodeToString(bao.toByteArray(),Base64.DEFAULT);

            new upload_image().execute();

        }
    }

    private void upload_images() {

        BitmapDrawable bd=((BitmapDrawable) images.get(current_image).getDrawable());
        Bitmap bm=bd.getBitmap();

        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,95,bao);

        image_base64= Base64.encodeToString(bao.toByteArray(),Base64.DEFAULT);

        new upload_image().execute();

    }

    public class upload_image extends AsyncTask<Void,Void,String> {

        ProgressDialog pd=new ProgressDialog(getApplicationContext());

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
                HttpPost httppost=new HttpPost("http://www.shahreraz.com/mob/Farsirib/webservice/command.php");
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

        ProgressDialog pd=new ProgressDialog(getApplicationContext());

        protected  void onPreExecute()
        {
            super.onPreExecute();

            pd.setMessage("در حال ارسال آگهی");
            pd.show();


        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();

            namevaluepairs.add(new BasicNameValuePair("myjson",new_report.toString()));

            try
            {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://www.shahreraz.com/mob/Farsirib/webservice/command.php");
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

                                Toast.makeText(getApplicationContext(),"آگهی با موفقیت ارسال شد",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getBaseContext(),"خطا در ارسال آگهی",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                {
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getBaseContext(),"خطا در ارسال آگهی",Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }catch(Exception e)
            {
                e.printStackTrace();

               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(),"خطا در ارسال آگهی",Toast.LENGTH_SHORT).show();


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
}
