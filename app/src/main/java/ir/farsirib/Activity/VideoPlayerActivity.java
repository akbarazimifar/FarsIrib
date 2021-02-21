package ir.farsirib.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import ir.farsirib.Fragment.AboutFragment;
import ir.farsirib.Fragment.ElementsFragment;
import ir.farsirib.Fragment.ItemThreeFragment;
import ir.farsirib.Fragment.ItemTwoFragment;
import ir.farsirib.Fragment.LiveTvFragment;
import ir.farsirib.Fragment.ProgramFragment;
import ir.farsirib.Fragment.TransitionListFragment;
import ir.farsirib.R;
import ir.farsirib.menu.ResideMenu;
import ir.farsirib.menu.ResideMenuItem;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemElements;
    private ResideMenuItem itemList1;
    private ResideMenuItem itemList2;
    BottomNavigationView bottomNavigationView;

    public static final int RADIO_NAMA_ID = 7;
    public static final int TV_ID = 8;
    public static final int PROVICIALTV_ID = 9;


    TextView headerTitle;
    private ResideMenuItem itemList3,itemList4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        setUpMenu();

        headerTitle = findViewById(R.id.header_title);
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ProgramFragment.newInstance("VideoPlayerActivity");
                                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                                bottomNavigationView.getMenu().getItem(1).setChecked(false);
                                bottomNavigationView.getMenu().getItem(2).setChecked(false);
                                headerTitle.setText("برنامه های سیما");
                                break;
                            case R.id.action_item2:
                                selectedFragment = ItemTwoFragment.newInstance();
                                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                                bottomNavigationView.getMenu().getItem(2).setChecked(false);
                                headerTitle.setText("رادیو نما فارس");
                                break;
                            case R.id.action_item3:
                                selectedFragment = ItemThreeFragment.newInstance();
                                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                bottomNavigationView.getMenu().getItem(1).setChecked(false);
                                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                                headerTitle.setText("سیمای فارس");
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.videoPlayerContainer, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        if (savedInstanceState == null) {
            String page_title = String.valueOf(getIntent().getExtras().getString("Page_Title"));

            headerTitle.setText(page_title);

            int Option_Id = getIntent().getExtras().getInt("Option_Id");

            switch ( Option_Id )
            {
                case RADIO_NAMA_ID:
                    changeFragment(ItemTwoFragment.newInstance());
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    bottomNavigationView.getMenu().getItem(2).setChecked(false);
                    break;
                case TV_ID:
                    changeFragment(ItemThreeFragment.newInstance());
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                    bottomNavigationView.getMenu().getItem(1).setChecked(false);
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    break;
                case PROVICIALTV_ID:
                    changeFragment(new LiveTvFragment());
                    break;
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

        itemHome     = new ResideMenuItem(this, R.mipmap.ic_return,     "بازگشت");
        // itemElements  = new ResideMenuItem(this, R.drawable.ic_elements_alternative,  "Elements");
        itemList1 = new ResideMenuItem(this, R.drawable.ic_list_2, "لیست برنامه ها و کنداکتور سیما");
        itemList2 = new ResideMenuItem(this, R.drawable.ic_list_1, "لیست برنامه های سیما");
        itemList3 = new ResideMenuItem(this, R.drawable.about, "درباره");
        itemList4 = new ResideMenuItem(this, R.drawable.exit, "خروج");

        itemHome.setOnClickListener(this);
        // itemElements.setOnClickListener(this);
        itemList1.setOnClickListener(this);
        itemList2.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome);
        //resideMenu.addMenuItem(itemElements);
        resideMenu.addMenuItem(itemList1);
        resideMenu.addMenuItem(itemList2);
        itemList3.setOnClickListener(this);
        itemList4.setOnClickListener(this);

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
            finish();//changeFragment(new OptionFragment("VideoPlayerActivity",getApplicationContext()));
            headerTitle.setText("اپلیکیشن جامع شبکه فارس");
        }else if (view == itemElements){
            changeFragment(new ElementsFragment());
        }else if (view == itemList1){
            changeFragment(new ProgramFragment("VideoPlayerActivity"));
            headerTitle.setText("برنامه های سیما");
        }else if (view == itemList2){
            changeFragment(new TransitionListFragment("VideoPlayerActivity"));
            headerTitle.setText("برنامه های سیما");
        }else if (view == itemList3) {
            changeFragment(new AboutFragment("VideoPlayerActivity"));
            headerTitle.setText("درباره");
        }
        else if (view == itemList4) {

            new AlertDialog.Builder(getApplicationContext())
                    .setMessage("آیا می خواهید از برنامه خارج شوید؟")
                    .setCancelable(false)
                    .setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            System.exit(0);
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
                .replace(R.id.videoPlayerContainer, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    //return the residemenu to fragments
    public ResideMenu getResideMenu(){
        return resideMenu;
    }

  /*  @Override
    public void onBackPressed() {
//        if (resideMenu.isOpened()){
//            resideMenu.closeMenu();
//        } else {
//            resideMenu.openMenu();
//        }
        Intent intent = new Intent(VideoPlayerActivity.this,OptionActivity.class);
        VideoPlayerActivity.this.finish();
        startActivity(intent);
    }*/
}
