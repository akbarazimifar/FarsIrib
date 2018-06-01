package ir.farsirib.Activity;

import ir.farsirib.Database.DatabaseAssets;
import ir.farsirib.Fragment.CyberspaceFragment;
import ir.farsirib.Fragment.ElementsFragment;
import ir.farsirib.Fragment.ItemOneFragment;
import ir.farsirib.Fragment.ItemThreeFragment;
import ir.farsirib.Fragment.ItemTwoFragment;
import ir.farsirib.Fragment.LiveTvFragment;
import ir.farsirib.Fragment.MusicFragment;
import ir.farsirib.Fragment.NewsFragment;
import ir.farsirib.Fragment.OptionFragment;
import ir.farsirib.Fragment.PostCommentFragment;
import ir.farsirib.Fragment.ProgramFragment;
import ir.farsirib.Fragment.RavabetFragment;
import ir.farsirib.Fragment.TechnicalFragment;
import ir.farsirib.Fragment.TransitionListFragment;
import ir.farsirib.menu.ResideMenu;
import ir.farsirib.menu.ResideMenuItem;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import ir.farsirib.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemElements;
    private ResideMenuItem itemList1;
    private ResideMenuItem itemList2;
    private ResideMenuItem itemList3;
    private ResideMenuItem itemList4;
    private BottomNavigationView bottomNavigationView;

    private static String TAG = "PermissionDemo";
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int SPLASH_DISPLAY_TIME = 4000;
    DatabaseAssets mydatabase;
    TextView headerTitle;

    Context myContext;
    private boolean flag = false;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    //    allowAccessAlert();
        myContext = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        headerTitle = findViewById(R.id.header_title);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ProgramFragment.newInstance("MainActivity");
                                headerTitle.setText("برنامه های سیما");
                                break;
                            case R.id.action_item2:
                                selectedFragment = ItemTwoFragment.newInstance();
                                headerTitle.setText("رادیو نما فارس");
                                break;
                            case R.id.action_item3:
                                selectedFragment = ItemThreeFragment.newInstance();
                                headerTitle.setText("سیمای فارس");
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_fragment, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });


        //Create DataBase
      //  mydatabase=new DatabaseAssets(getApplicationContext());
     //   LoadingDatabase();

        setUpMenu();

        if (savedInstanceState == null) {
            String page_title = String.valueOf(getIntent().getExtras().getString("Page_Title"));
            this.setTitle(page_title);
            int Option_Id = getIntent().getExtras().getInt("Option_Id");

            switch ( Option_Id )
            {
                case 0:
                    changeFragment(ItemOneFragment.newInstance());
                    headerTitle.setText("رادیو فارس");
                    break;
                case 1:
                    changeFragment(new ProgramFragment("MainActivity"));
                    headerTitle.setText("برنامه های سیما");
                    break;
                case 2:
                    changeFragment(new MusicFragment());
                    headerTitle.setText("واحد موسیقی مرکز فارس");
                    break;
                case 3:
                    changeFragment(new NewsFragment().newInstance());
                    headerTitle.setText("خبر مرکز فارس");
                    break;
                case 4:
                    Intent intent = new Intent(MainActivity.this,CitizenReporterActivity.class);
                    MainActivity.this.finish();
                    startActivity(intent);
                    headerTitle.setText("شهروند خبرنگار");
                    break;
//                case 5:
//                    changeFragment(PostCommentFragment.newInstance());
//                    headerTitle.setText("ارسال نظر");
//                    break;
                case 5:
                    changeFragment(TechnicalFragment.newInstance());
                    headerTitle.setText("فنی - فرکانس های پخش");
                    break;
                case 6:
                    changeFragment(RavabetFragment.newInstance());
                    headerTitle.setText("پل های ارتباطی");
                    break;
//                case 8:
//                    changeFragment(new CyberspaceFragment());
//                    headerTitle.setText("فضای مجازی مرکز فارس");
//                    break;
//                case 9:
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//                    changeFragment(TabloFragment.newInstance());
//                    headerTitle.setText("تابلو اعلانات مرکز فارس");
//                    break;
                case 7:
                    changeFragment(new LiveTvFragment());
                    headerTitle.setText("پخش زنده شبکه های تلویزیونی");
                    break;
            }
        }
    }

    private void LoadingDatabase() {
        try {
            mydatabase.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mydatabase.openDataBase();
    }

    private void allowAccessAlert() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };

        if(!hasPermissions(this, PERMISSIONS)){
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

    private void setUpMenu() {
    	
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setShadowVisible(true);
        resideMenu.setHeaderView(findViewById(R.id.actionbar));
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        itemHome     = new ResideMenuItem(this, R.drawable.ic_home,     "خانه");
       // itemElements  = new ResideMenuItem(this, R.drawable.ic_elements_alternative,  "Elements");
        itemList1 = new ResideMenuItem(this, R.drawable.ic_list_2, "لیست برنامه ها و کنداکتور سیما");
        itemList2 = new ResideMenuItem(this, R.drawable.ic_list_1, "لیست برنامه های سیما");
      //  itemList3 = new ResideMenuItem(this, R.drawable.about, "درباره");
      //  itemList4 = new ResideMenuItem(this, R.drawable.exit, "بازگشت");

        itemHome.setOnClickListener(this);
     //   itemElements.setOnClickListener(this);
        itemList1.setOnClickListener(this);
        itemList2.setOnClickListener(this);
//        itemList3.setOnClickListener(this);
 //       itemList4.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome);
      //  resideMenu.addMenuItem(itemElements);
        resideMenu.addMenuItem(itemList1);
        resideMenu.addMenuItem(itemList2);
     //   resideMenu.addMenuItem(itemList3);
    //    resideMenu.addMenuItem(itemList4);
        
        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu();
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            //changeFragment(new OptionFragment("MainAvtivity",getApplicationContext()));
            MainActivity.this.finish();
            //flag = true;
            //headerTitle.setText("اپلیکیشن جامع شبکه فارس");
//        }else if (view == itemElements){
//            changeFragment(new ElementsFragment());
        }else if (view == itemList1){
            changeFragment(new ProgramFragment("MainActivity"));
            headerTitle.setText("برنامه های سیما");
        }else if (view == itemList2){
            changeFragment(new TransitionListFragment("MainActivity"));
            headerTitle.setText("برنامه های سیما");
        }
//        else if (view == itemList3) {
//            changeFragment(new AboutFragment("MainActivity"));
//            headerTitle.setText("درباره");
//        }
//        else if (view == itemList4) {

//            new AlertDialog.Builder(this)
//                    .setMessage("آیا می خواهید از برنامه خارج شوید؟")
//                    .setCancelable(false)
//                    .setPositiveButton("بلی", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
                    //       finish();
           // finish();
            //Application.System.exit(0);
//                        }
//                    })
//                    .setNegativeButton("خیر", null)
//                    .show();
      //  }

        resideMenu.closeMenu();
    }

    //Example of menuListener
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() { }

        @Override
        public void closeMenu() { }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    //return the residemenu to fragments
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
/*
    @Override
    public void onBackPressed() {

        if (flag == false)
        {
            if (resideMenu.isOpened()){
                resideMenu.closeMenu();
            } else {
                resideMenu.openMenu();
            }
        }
        else
        {
            //finish();
            new AlertDialog.Builder(this)
                    .setMessage("برای خروج می بایست کلید بازگشت را دوبار بزنید ")
                    .setCancelable(false)
                    .setPositiveButton("می پذیرم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            //    System.exit(0);
                        }
                    })
                    .setNegativeButton("خیر", null)
                    .show();
        }

//        OptionActivity.flag = false;
//        Intent intent = new Intent(MainActivity.this,OptionActivity.class);
//        MainActivity.this.finish();
//        startActivity(intent);
    }
*/
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

}
