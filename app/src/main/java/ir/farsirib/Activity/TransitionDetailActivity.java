package ir.farsirib.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.farsirib.Adapter.DBAdapter;
import ir.farsirib.Fragment.ElementsFragment;
import ir.farsirib.Fragment.ItemThreeFragment;
import ir.farsirib.Fragment.ItemTwoFragment;
import ir.farsirib.Fragment.OptionFragment;
import ir.farsirib.Fragment.ProgramFragment;
import ir.farsirib.Fragment.TransitionDetailFragment;
import ir.farsirib.Fragment.TransitionListFragment;
import ir.farsirib.Model.barname;
import ir.farsirib.R;

import ir.farsirib.menu.ResideMenu;
import ir.farsirib.menu.ResideMenuItem;
import ir.farsirib.utils.UICircularImage;
import ir.farsirib.utils.UISwipableList;

public class TransitionDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ResideMenuItem itemHome;
    private ResideMenuItem itemElements;
    private ResideMenuItem itemList1;
    private ResideMenuItem itemList2;
    private ResideMenuItem itemList3;
    private ResideMenuItem itemList4;

	//Configuration
    public static final int DURATION = 500; // in ms
    public static final String PACKAGE = "IDENTIFY";
    private String result="";
    private String id = "";
    
    //UI Elements
    private UICircularImage mImageView;
    private TextView mTextView;
    private RelativeLayout mLayoutContainer;
    private FrameLayout mNavigationTop;
	private TextView mNavigationTitle;
	private Button mNavigationBackBtn;
	private TextView mTitleView;
	private UICircularImage mShare;
    ListView lst_data1;
    ProgressBar progress;
    //Vars
    private int delta_top;
    private int delta_left;
    private float scale_width;
    private float scale_height;
	String title;
    String sum;
	int imgId;
    int barname_id;
    barname my_barname;
    DBAdapter dbAdapter;
    private boolean flag = false;
    Context myContext;
    private UISwipableList listView;
    private ResideMenu resideMenu;

    Bundle bundle;
    TextView headerTitle;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transition);

        setUpMenu();
        headerTitle = findViewById(R.id.header_title);

        if (savedInstanceState == null)
        {
            bundle = getIntent().getExtras();

            final int top = bundle.getInt(PACKAGE + ".top");
            final int left = bundle.getInt(PACKAGE + ".left");
            final int width = bundle.getInt(PACKAGE + ".width");
            final int height = bundle.getInt(PACKAGE + ".height");
            title = bundle.getString("title");
            sum = bundle.getString("descr");
            imgId = bundle.getInt("img");
            barname_id = bundle.getInt("barname_id");

//            headerTitle = (TextView) findViewById(R.id.header_title);
//            headerTitle.setText(title);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.transition_container, TransitionDetailFragment.newInstance(top,left,width,height,title,sum,imgId,barname_id));
            transaction.commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ProgramFragment.newInstance("TransitionDetailActivity");
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
                        transaction.replace(R.id.transition_container, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

//        myContext = getBaseContext();
//
//        ((UIParallaxScroll) findViewById(R.id.scroller)).setOnScrollChangedListener(mOnScrollChangedListener);
//
//        mImageView = (UICircularImage) findViewById(R.id.image_view);
//     //   mTextView = (TextView) findViewById(R.id.contact);
//	    mNavigationTop = (FrameLayout) findViewById(R.id.layout_top);
//	   // mNavigationTitle = (TextView) findViewById(R.id.titleBar);
//        mLayoutContainer = (RelativeLayout) findViewById(R.id.bg_layout);
//	    mTitleView = (TextView) findViewById(R.id.title);
//	   // mNavigationBackBtn = (Button) findViewById(R.id.title_bar_left_menu1);
//	    TextView mSum = (TextView) findViewById(R.id.sumary);
//	    mShare = (UICircularImage) findViewById(R.id.action1);
//	    UITabs tab = (UITabs) findViewById(R.id.toggle);
//        lst_data1 = (ListView) findViewById(R.id.lst_data);
//        progress = (ProgressBar) findViewById(R.id.progress);
//
//        listView   = (UISwipableList) findViewById(R.id.listView);
//
//	    mNavigationTop.getBackground().setAlpha(0);
//	    mNavigationTitle.setVisibility(View.INVISIBLE);

//        mImageView.bringToFront();
//
//        bundle = getIntent().getExtras();
//
//        final int top = bundle.getInt(PACKAGE + ".top");
//        final int left = bundle.getInt(PACKAGE + ".left");
//        final int width = bundle.getInt(PACKAGE + ".width");
//        final int height = bundle.getInt(PACKAGE + ".height");
//	    title = bundle.getString("title");
//	    sum = bundle.getString("descr");
//	    imgId = bundle.getInt("img");
//        barname_id = bundle.getInt("barname_id");

	    //Our Animation initialization
//        ViewTreeObserver observer = mImageView.getViewTreeObserver();
//        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//        	@Override
//        	public boolean onPreDraw() {
//
//                    mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                    int[] screen_location = new int[2];
//                    mImageView.getLocationOnScreen(screen_location);
//
//                    delta_left = left - screen_location[0];
//                    delta_top = top - screen_location[1];
//
//                    scale_width = (float) width / mImageView.getWidth();
//                    scale_height = (float) height / mImageView.getHeight();
//
//                    runEnterAnimation();
//
//                    return true;
//                }
//        });



//	    mTitleView.setText(title);
//	    mSum.setText(sum);
//	    mImageView.setImageResource(imgId);
//	    mNavigationTitle.setText(title);
//
//	    mNavigationBackBtn.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//
//               // changeFragment(new ProgramFragment());
//              //  killActivity();
//             //   ((Activity)myContext).finish();
//              onBackPressed();
//               // finishActivity(0x7f04001b);
//			}
//
//	    });
//
//	    mShare.setOnClickListener(new OnClickListener(){
//	    	@Override
//			public void onClick(View arg0) {
//	    		Toast.makeText(TransitionDetailActivity.this, "Clicked Share", Toast.LENGTH_SHORT).show();
//			}
//	    });
//
//        lst_data1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//               // lst_data1.get
//                barname item = (barname) lst_data1.getAdapter().getItem(position);
//
//                Toast.makeText(TransitionDetailActivity.this, "Clicked item:"+ item.getTitle() , Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(getApplication(), DetailActivity.class);
//
//                Bundle bundle = new Bundle();
//                bundle.putString("title", item.getTitle());
//                bundle.putString("img",item.getImage_url() );
//                bundle.putString("descr", item.getDescription());
//                bundle.putString("video_url",item.getVideo_url());
                //bundle.putInt("barname_id",barname_id);

//                int[] screen_location = new int[2];
//                View view = viewa.findViewById(R.id.item_image);
//                view.getLocationOnScreen(screen_location);
//
//                bundle.putInt(PACKAGE + ".left", screen_location[0]);
//                bundle.putInt(PACKAGE + ".top", screen_location[1]);
//                bundle.putInt(PACKAGE + ".width", view.getWidth());
//                bundle.putInt(PACKAGE + ".height", view.getHeight());

//                intent.putExtras(bundle);
//
//
//                startActivity(intent);
//                finish();
//              //  finishActivity(0x7f04001b);
//            }
//        });
//	    tab.setOnCheckedChangeListener(new OnCheckedChangeListener()
//	    {
//	        public void onCheckedChanged(RadioGroup group, int checkedId) {
//	        	switch (checkedId) {
//
//	        	case R.id.toggle1:
//	        		mTextView.setVisibility(View.GONE);
//	        		listView.setVisibility(LinearLayout.VISIBLE);
//	        		return;
//	        	case R.id.toggle2:
//	        		mTextView.setVisibility(View.VISIBLE);
//	        		listView.setVisibility(LinearLayout.GONE);
//	        		return;
//	        	}
//	        }
//	    });
      //  new StationsTask1().execute();
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
//        itemList3 = new ResideMenuItem(this, R.drawable.about, "بازگشت");
//        itemList4 = new ResideMenuItem(this, R.drawable.exit, "خروج");

        itemHome.setOnClickListener(this);
        // itemElements.setOnClickListener(this);
        itemList1.setOnClickListener(this);
        itemList2.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome);
        //resideMenu.addMenuItem(itemElements);
        resideMenu.addMenuItem(itemList1);
        resideMenu.addMenuItem(itemList2);
    //    resideMenu.addMenuItem(itemList4);
//        itemList3.setOnClickListener(this);
      //  itemList4.setOnClickListener(this);

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
            //changeFragment(new OptionFragment("TransitionDetailActivity",getApplicationContext()));
            TransitionDetailActivity.this.finish();
            //flag = true;
           // headerTitle.setText("اپلیکیشن جامع شبکه فارس");
//        }else if (view == itemElements){
//            changeFragment(new ElementsFragment());
        }else if (view == itemList1){
            changeFragment(new ProgramFragment("TransitionDetailActivity"));
            headerTitle.setText("برنامه های سیما");
        }else if (view == itemList2){
            changeFragment(new TransitionListFragment("TransitionDetailActivity"));
            headerTitle.setText("برنامه های سیما");
        }
//        else if (view == itemList3) {
//            finish();
//            changeFragment(new AboutFragment("TransitionDetailActivity"));
//            headerTitle.setText("درباره");
//        }
//        else if (view == itemList4) {
//
//            new AlertDialog.Builder(this)
//                    .setMessage("برای خروج می بایست کلید بازگشت را دوبار بزنید ")
//                    .setCancelable(false)
//                    .setPositiveButton("می پذیرم", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            finish();
//                            //    System.exit(0);
//                        }
//                    })
//                    .setNegativeButton("خیر", null)
//                    .show();
//        }

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
                .replace(R.id.transition_container, targetFragment, "fragment")
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
   //     if (flag == false)
        {
            if (resideMenu.isOpened()){
                resideMenu.closeMenu();
            } else {
                resideMenu.openMenu();
            }
        }
    //    else {
            //changeFragment(new OptionFragment("TransitionDetailActivity",getApplicationContext()));
       //     TransitionDetailActivity.this.finish();
            //headerTitle.setText("اپلیکیشن جامع شبکه فارس");
    //    }

//        Intent intent = new Intent(TransitionDetailActivity.this,OptionActivity.class);
//        this.finish();
//        startActivity(intent);
    }*/
}