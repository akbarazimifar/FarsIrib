package ir.farsirib.Activity;

//import co.ronash.pushe.Pushe;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ir.farsirib.Database.DatabaseAssets;
import ir.farsirib.Fragment.AboutFragment;
import ir.farsirib.Fragment.ElementsFragment;
import ir.farsirib.Fragment.OptionFragment;
import ir.farsirib.Fragment.ProgramFragment;
import ir.farsirib.Fragment.TransitionListFragment;
import ir.farsirib.R;
import ir.farsirib.menu.ResideMenu;
import ir.farsirib.menu.ResideMenuItem;

public class OptionActivity extends AppCompatActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemElements;
    private ResideMenuItem itemList1;
    private ResideMenuItem itemList2;
    private ResideMenuItem itemList3;
    private ResideMenuItem itemList4;
    TextView headerTitle;
    DatabaseAssets mydatabase;
    public static boolean flag = false;

    private static String TAG = "PermissionDemo";
    private static final int REQUEST_WRITE_STORAGE = 112;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        allowAccessAlert();

        //Create DataBase
        mydatabase=new DatabaseAssets(getApplicationContext());
        LoadingDatabase();

        //Pushe.initialize(this,true);

        setUpMenu();

        if (savedInstanceState == null)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.optionContainer, new OptionFragment("OptionActivity",OptionActivity.this));
            transaction.commit();

        }


        headerTitle = findViewById(R.id.header_title);

//        BottomNavigationView bottomNavigationView = (BottomNavigationView)
//                findViewById(R.id.navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener
//                (new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        Fragment selectedFragment = null;
//                        switch (item.getItemId()) {
//                            case R.id.action_item1:
//                                selectedFragment = ProgramFragment.newInstance("OptionActivity");
//                                headerTitle.setText("برنامه های سیما");
//                                flag = true;
//                                break;
//                            case R.id.action_item2:
//                                selectedFragment = ItemTwoFragment.newInstance();
//                                headerTitle.setText("رادیو نما فارس");
//                                flag = true;
//                                break;
//                            case R.id.action_item3:
//                                selectedFragment = ItemThreeFragment.newInstance();
//                                headerTitle.setText("سیمای فارس");
//                                flag = true;
//                                break;
//                        }
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.optionContainer, selectedFragment);
//                        transaction.commit();
//                        return true;
//                    }
//                });
    }

    public void finish_all()
    {
        this.finish();

    }
    private void LoadingDatabase() {
        try {
            mydatabase.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mydatabase.openDataBase();
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

        itemHome     = new ResideMenuItem(this, R.drawable.ic_home,"خانه");
      //  itemElements  = new ResideMenuItem(this, R.drawable.ic_elements_alternative,  "Elements");
        itemList1 = new ResideMenuItem(this, R.drawable.ic_list_2, "لیست برنامه ها و کنداکتور سیما");
        itemList2 = new ResideMenuItem(this, R.drawable.ic_list_1, "لیست برنامه های سیما");
        itemList3 = new ResideMenuItem(this, R.drawable.about, "درباره");
        itemList4 = new ResideMenuItem(this, R.drawable.exit, "خروج");

        itemHome.setOnClickListener(this);
       // itemElements.setOnClickListener(this);
        itemList1.setOnClickListener(this);
        itemList2.setOnClickListener(this);
        itemList3.setOnClickListener(this);
        itemList4.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome);
     //   resideMenu.addMenuItem(itemElements);
        resideMenu.addMenuItem(itemList1);
        resideMenu.addMenuItem(itemList2);
        resideMenu.addMenuItem(itemList3);
        resideMenu.addMenuItem(itemList4);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu();
            }
        });

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            changeFragment(new OptionFragment("OptionActivity",getApplicationContext()));
            headerTitle.setText("اپلیکیشن جامع شبکه فارس");
            flag = false;
        }else if (view == itemElements){
            changeFragment(new ElementsFragment());
        }else if (view == itemList1){
            changeFragment(new ProgramFragment("OptionActivity"));
            headerTitle.setText("برنامه های سیما");
            flag = true;
        }else if (view == itemList2){
            changeFragment(new TransitionListFragment("OptionActivity"));
            headerTitle.setText("برنامه های سیما");
            flag = true;
        }
         else if (view == itemList3) {
                    changeFragment(new AboutFragment("OptionActivity"));
                    headerTitle.setText("درباره");
            flag = true;

                }
                else if (view == itemList4) {

                    new AlertDialog.Builder(this)
                            .setMessage("آیا می خواهید از برنامه خارج شوید؟ ")
                            .setCancelable(false)
                            .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                //    System.exit(0);
                                }
                            })
                            .setNegativeButton("خیر", null)
                            .show();
                }


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
                .replace(R.id.optionContainer, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    //return the residemenu to fragments
    public ResideMenu getResideMenu(){
        return resideMenu;
    }


  @Override
  public void onBackPressed() {

      if (flag == true) {
          changeFragment(new OptionFragment("OptionActivity",getApplicationContext()));
          headerTitle.setText("اپلیکیشن جامع شبکه فارس");
          flag = false;
      }
      else
      {
          if (doubleBackToExitPressedOnce) {
              super.onBackPressed();OptionActivity.this.finish();
              return;
          }
          this.doubleBackToExitPressedOnce = true;

          Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

          new Handler().postDelayed(new Runnable() {

              @Override
              public void run() {
                  doubleBackToExitPressedOnce=false;
              }

          }, 2000);
      }
          //finish();
  }

    /*  @Override
    public void onBackPressed() {

        if ( flag == true )
        {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();OptionActivity.this.finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;

            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }

            }, 2000);
        }
        else
        if (resideMenu.isOpened()){
            resideMenu.closeMenu();
        } else {
            resideMenu.openMenu();
            flag = false;
        }
    }*/
}
