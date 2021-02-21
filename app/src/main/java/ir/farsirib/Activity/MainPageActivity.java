package ir.farsirib.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.farsirib.Adapter.DBAdapter;
import ir.farsirib.Adapter.MainPageRecyclerViewAdapter;
import ir.farsirib.Adapter.SpecialProgramRecyclerViewAdapter;
import ir.farsirib.Database.DatabaseAssets;
import ir.farsirib.Model.barname;
import ir.farsirib.Model.program;
import ir.farsirib.R;
import ir.farsirib.Webservice.GetJson;
import ir.farsirib.utils.MainPageXml;
import ir.farsirib.utils.Rss;

public class MainPageActivity extends AppCompatActivity {

    CarouselView customCarouselView;
    public List<Rss> data;
    public String[] sampleTitles;
    public String[] sampleNetworkImageURLs;
    public String link_value = "";
    String title;
    String videoURL;
    String imgURL;
    int barname_id, barname_id2;
    Context myContext;
    private LinearLayoutManager lLayout;
    private String result = "";
    private String result2 = "";
    private String id = "";
    private static String TAG = "PermissionDemo";
    private boolean flag = false;
    ProgressBar progress, progress2;
    barname my_barname;
    DBAdapter dbAdapter;
    DatabaseAssets mydatabase;
    RecyclerView rView1, rView2;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        myContext = this.getApplicationContext();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                //allowAccessAlert();

                //Create DataBase
                mydatabase = new DatabaseAssets(myContext);
                LoadingDatabase();

                final List<program> rowListItem = getAllItemList();

                RecyclerView rView = findViewById(R.id.main_page_recycler_view);
                rView.setHasFixedSize(true);
                lLayout = new LinearLayoutManager(MainPageActivity.this, LinearLayoutManager.HORIZONTAL, false);
                rView.setLayoutManager(lLayout);

                MainPageRecyclerViewAdapter rcAdapter = new MainPageRecyclerViewAdapter(MainPageActivity.this, rowListItem);
                rView.setAdapter(rcAdapter);

                progress = findViewById(R.id.kashaneh_progress);
                progress2 = findViewById(R.id.khosha_progress);

                new ProgramTask(3).execute(); // Kashane

                new ProgramTask2(4).execute(); // Khosha

                Button button_kashane = findViewById(R.id.btn_kashane);
                Button button_khosha = findViewById(R.id.btn_khosha);

                button_kashane.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainPageActivity.this,WebMainPageActivity.class);
                        intent.putExtra("URL","http://farsirib.ir/kashanehmehr");
                        //intent.putExtra("URL","http://farsirib.ir/timearchive");
                        startActivity(intent);
                    }
                });

                button_khosha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainPageActivity.this,WebMainPageActivity.class);
                        intent.putExtra("URL","http://farsirib.ir/khoshashiraz");
                        startActivity(intent);
                    }
                });

                GetRSSDataTask3 task3 = new GetRSSDataTask3();
                task3.execute();



    }

    private void allowAccessAlert() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the SD-CARD is required for this app to Download PDF.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");

                } else {

                    Log.i(TAG, "Permission has been granted by user");

                }
                return;
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

    private void LoadingDatabase() {
        try {
            mydatabase.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mydatabase.openDataBase();
    }

    private List<program> getAllItemList() {

        List<program> allItems = new ArrayList<program>();
        allItems.add(new program("صدا", "http://www.shahreraz.com/Farsirib/img/MainPage/seda.png", R.mipmap.ic_radio, "برنامه خانواده سیمای فارس شنبه تا چهارشنبه ساعت 10"));
        allItems.add(new program("سیما", "http://www.shahreraz.com/Farsirib/img/MainPage/sima.png", R.drawable.ph_2, "جمعه ها ساعت 10 صبح از شبکه فارس و شبکه شما"));
        allItems.add(new program("موسیقی", "http://www.shahreraz.com/Farsirib/img/MainPage/moseghi.png", R.drawable.ph_3, "برنامه کودک سیمای فارس شنبه تا چهارشنبه ساعت 16"));
        allItems.add(new program("خبر", "http://www.shahreraz.com/Farsirib/img/MainPage/khabar.png", R.drawable.ph_4, "برنامه زنده شبانه شنبه تا چهارشنبه ساعت 22"));
        allItems.add(new program("شهروند خبرنگار", "http://www.shahreraz.com/Farsirib/img/MainPage/shahrvand.png", R.drawable.ph_5, "برنامه زنده صبحگاهی سیمای فارس شنبه تا چهارشنبه ساعت 7:45"));
        allItems.add(new program("فرکانس پخش", "http://www.shahreraz.com/Farsirib/img/MainPage/fanni.png", R.drawable.ph_6, "برنامه گفتگو شنبه ها ساعت 21 از سیمای فارس"));
        allItems.add(new program("پل های ارتباطی", "http://www.shahreraz.com/Farsirib/img/MainPage/ravabet.png", R.drawable.ph_7, "یکشنبه ، سه شنبه ، پنجشنبه ساعت 21"));
       // allItems.add(new program("پخش زنده شبکه ها", "http://www.mob.shahreraz.com/mob/Farsirib/img/MainPage/live.png", R.drawable.ph_8, "شنبه تا سه شنبه ساعت 18"));

        return allItems;
    }

    public class GetRSSDataTask3 extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {

            MainPageXml rssReader = new MainPageXml();
            data = rssReader.parser();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (data.size() != 0) {
                Log.e("Xml", data.toString());

                sampleTitles = new String[]{data.get(0).getTitle(), data.get(1).getTitle(), data.get(2).getTitle(), data.get(3).getTitle(), data.get(4).getTitle()};
                sampleNetworkImageURLs = new String[]{
                        data.get(0).getImg(),
                        data.get(1).getImg(),
                        data.get(2).getImg(),
                        data.get(3).getImg(),
                        data.get(4).getImg()
                };

                customCarouselView = findViewById(R.id.customCarouselView);

                customCarouselView.setViewListener(viewListener);

                customCarouselView.setImageClickListener(new ImageClickListener() {
                    @Override
                    public void onClick(int position) {

                        String link = data.get(position).getLink();
                        videoURL = "http://dppmedia.irib.ir/fars/media/" + link.substring(25, 29) + ".mp4";
                        imgURL = data.get(position).getImg();
                        barname_id = 5; // ویژه های سایت
                        link_value = data.get(position).getLink();
                        title = data.get(position).getTitle();

                        new BackgroundWorker().execute();

                    }
                });

                customCarouselView.setPageCount(5);
                customCarouselView.setSlideInterval(4000);
            } else

            {
                new SweetAlertDialog(MainPageActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("اسلايدر ويژه هاي سايت")
                        .setContentText("دسترسي به ويژه هاي سايت در این لحظه امكان پذير نمي باشد")
                        .show();
            }

               // Toast.makeText(getApplicationContext(),"دسترسي به ويژه هاي سايت در این لحظه امكان پذير نمي باشد" + data , Toast.LENGTH_SHORT).show();

        }


        // To set custom views
        ViewListener viewListener = new ViewListener() {
            @Override
            public View setViewForPosition(int position) {

                View customView = getLayoutInflater().inflate(R.layout.view_custom_carousel_main_page, null);

                ImageView fruitImageView = customView.findViewById(R.id.fruitImageView);
                Picasso.with(MainPageActivity.this).load(sampleNetworkImageURLs[position]).into(fruitImageView);

                TextView labelTextView = customView.findViewById(R.id.labelTextView);
                Typeface tf = Typeface.createFromAsset(getAssets(), "IRANSansMobile.ttf");
                labelTextView.setTypeface(tf);
                labelTextView.setText(sampleTitles[position]);

                return customView;
            }
        };

        View.OnClickListener pauseOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //customCarouselView.reSetSlideInterval(0);
            }
        };
    }

    private class BackgroundWorker extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            getDarewod();
            return null;
        }

        public void getDarewod() {

            try {
                Document htmlDocument = Jsoup.connect(link_value).get();
                Elements element = htmlDocument.select("div.row > div.col-sm-8 > article.blog-post.format-video.WhiteSkin > div.post-content.clearfix > p");
                Log.e("Description is:", element.toString());

                String clearDesc = String.valueOf(element);//html2text(element.toString());

                //view.loadData(youtContentStr, "text/html", "utf-8");

                Intent intent = new Intent(MainPageActivity.this, DetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("img", imgURL);
                bundle.putString("descr", clearDesc);
                bundle.putString("video_url", videoURL);
                bundle.putInt("barname_id", barname_id);

                intent.putExtras(bundle);
                //  finish();
                startActivity(intent);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ProgramTask extends AsyncTask<Void, Void, Void> {

        public ProgramTask(int id) {
            barname_id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {

            GetJson getJson = new GetJson();

            //result = getJson.JsonRequest("http://www.mob.shahreraz.com/mob/Farsirib/webservice/showBarnameSection.php?q="+barname_id);
            result = getJson.JsonRequest("http://www.mob.farsirib.ir/index.php?q=" + barname_id);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.setVisibility(View.INVISIBLE);

            JSONArray jsonArray;

            my_barname = new barname();
            dbAdapter = new DBAdapter(MainPageActivity.this);
            dbAdapter.update();

            try {
                jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    id = jsonObject.getString("id");
                    String image_url = jsonObject.getString("image_url");
                    String titel = jsonObject.getString("title");
                    String video_url = jsonObject.getString("video_url");
                    String description = jsonObject.getString("description");

                    String clearDesc = String.valueOf(description);

                    image_url = "http://farsirib.ir/images/product/thumb/" + image_url;
                    video_url = "http://dppmedia.irib.ir/fars/media/" + video_url + ".mp4";

                    my_barname.setCategory_id(Integer.parseInt(id));
                    my_barname.setImage_url(image_url);
                    my_barname.setTitle(titel);
                    my_barname.setVideo_url(video_url);
                    my_barname.setDescription(clearDesc);

// DataBase Section
                    flag = dbAdapter.isDataExists(id, titel);
                    if (flag == false) {
                        dbAdapter.insertBarname(my_barname);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FillData();
            //}
        }
    }

    private class ProgramTask2 extends AsyncTask<Void, Void, Void> {

        public ProgramTask2(int id) {
            barname_id2 = id;
        }

        @Override
        protected Void doInBackground(Void... params) {

            GetJson getJson = new GetJson();

            //result = getJson.JsonRequest("http://www.mob.shahreraz.com/mob/Farsirib/webservice/showBarnameSection.php?q="+barname_id);
            result2 = getJson.JsonRequest("http://www.mob.farsirib.ir/index.php?q=" + barname_id2);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress2.setVisibility(View.INVISIBLE);

            JSONArray jsonArray;

            my_barname = new barname();
            dbAdapter = new DBAdapter(MainPageActivity.this);
            dbAdapter.update();


            try {
                jsonArray = new JSONArray(result2);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    id = jsonObject.getString("id");
                    String image_url = jsonObject.getString("image_url");
                    String titel = jsonObject.getString("title");
                    String video_url = jsonObject.getString("video_url");
                    String description = jsonObject.getString("description");

                    //String clearDesc = html2text(description);
                    String clearDesc = String.valueOf(description);

                    image_url = "http://farsirib.ir/images/product/thumb/" + image_url;
                    video_url = "http://dppmedia.irib.ir/fars/media/" + video_url + ".mp4";

                    my_barname.setCategory_id(Integer.parseInt(id));
                    my_barname.setImage_url(image_url);
                    my_barname.setTitle(titel);
                    my_barname.setVideo_url(video_url);
                    my_barname.setDescription(clearDesc);

// DataBase Section
                    flag = dbAdapter.isDataExists(id, titel);
                    if (flag == false) {
                        dbAdapter.insertBarname(my_barname);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FillData2();
            //}
        }
    }

    private void FillData() {
        myContext = this;

        boolean tableIsEmpty = dbAdapter.isBarnameEmpty();

        if (tableIsEmpty == true) {
            new AlertDialog.Builder(myContext)
                    .setMessage("اگر برای بار اول وارد اپلیکیشن شدید یا برای بروزرسانی به اینترنت متصل شوید و مجددا سعی کنید")
                    .setCancelable(false)
                    .setPositiveButton("می پذیرم", null)
                    .show();
        } else {

            ArrayList<barname> barnameArrayList = dbAdapter.getBarname();

//            switch (barname_id)
//            {
//                case 3:
            rView1 = findViewById(R.id.kashane_recycler_view);
//                    break;
//                case 4:
//                    rView = (RecyclerView) findViewById(R.id.khosha_recycler_view);
//                    break;
//            }

            rView1.setHasFixedSize(true);
            lLayout = new LinearLayoutManager(MainPageActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rView1.setLayoutManager(lLayout);

            SpecialProgramRecyclerViewAdapter rcAdapter = new SpecialProgramRecyclerViewAdapter(MainPageActivity.this, barnameArrayList);
            rView1.setAdapter(rcAdapter);


        }
    }

    private void FillData2() {
        myContext = this;

        boolean tableIsEmpty = dbAdapter.isBarnameEmpty();

        if (tableIsEmpty == true) {
            new AlertDialog.Builder(myContext)
                    .setMessage("اگر برای بار اول وارد اپلیکیشن شدید یا برای بروزرسانی به اینترنت متصل شوید و مجددا سعی کنید")
                    .setCancelable(false)
                    .setPositiveButton("می پذیرم", null)
                    .show();
        } else {

            ArrayList<barname> barnameArrayList = dbAdapter.getBarname();

//            switch (barname_id)
//            {
//                case 3:
//                    rView = (RecyclerView) findViewById(R.id.kashane_recycler_view);
//                    break;
//                case 4:
            rView2 = findViewById(R.id.khosha_recycler_view);
//                    break;
//            }

            rView2.setHasFixedSize(true);
            lLayout = new LinearLayoutManager(MainPageActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rView2.setLayoutManager(lLayout);

            SpecialProgramRecyclerViewAdapter rcAdapter = new SpecialProgramRecyclerViewAdapter(MainPageActivity.this, barnameArrayList);
            rView2.setAdapter(rcAdapter);

        }
    }
}

