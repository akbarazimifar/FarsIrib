package ir.farsirib.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

import ir.farsirib.Activity.DetailActivity;
import ir.farsirib.Activity.MainActivity;
import ir.farsirib.Activity.TransitionDetailActivity;
import ir.farsirib.Adapter.BarnameListAdapter;
import ir.farsirib.Adapter.DBAdapter;
import ir.farsirib.Adapter.DetailListAdapter;
import ir.farsirib.Model.ListItem;
import ir.farsirib.Model.barname;
import ir.farsirib.R;
import ir.farsirib.Webservice.GetJson;
import ir.farsirib.menu.ResideMenu;
import ir.farsirib.utils.UICircularImage;
import ir.farsirib.utils.UIParallaxScroll;
import ir.farsirib.utils.UISwipableList;
import ir.farsirib.utils.UITabs;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransitionDetailFragment extends Fragment {

    //Configuration
    public static final int DURATION = 500; // in ms
    public static final String PACKAGE = "IDENTIFY";
    private String result = "";
    private String id = "";

    //UI Elements
    private UICircularImage mImageView;
    private TextView mTextView;
    private FrameLayout mLayoutContainer;
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
    int imgId;
    int barname_id;
    barname my_barname;
    DBAdapter dbAdapter;
    private boolean flag = false;
    Context myContext;
    private LinearLayout listView;
    String sum;
    Bundle bundle;

    int top;
    int left;
    int width;
    int height;

    String lor1 = "Pellentesque in luctus dui, non egestas nisl. Donec sapien ante, faucibus a sem at, tincidunt dictum quam. Sed vel blandit neque. Maecenas tincidunt at sem vel sodales. Nullam dignissim eros id tellus commodo, eu vulputate massa accumsan.<br><br>Ut eget volutpat turpis. Praesent ac auctor nisi, sed imperdiet augue. Aenean consequat est vel odio molestie pellentesque. Suspendisse rhoncus velit dolor, at ultrices nulla ullamcorper a. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Vivamus nec felis elit. Mauris at erat euismod leo sagittis gravida in id magna.";
    String lor2 = "Donec ornare eleifend turpis. Cras consectetur at neque sit amet bibendum. Nulla metus dui, porta vel mollis vitae, ornare sit amet lectus. Integer imperdiet quam eleifend nisl dictum vehicula. Suspendisse pharetra aliquet porttitor. Maecenas nec pharetra purus. Sed scelerisque suscipit faucibus. Etiam hendrerit tellus risus, et interdum tortor facilisis quis.";
    String lor3 = "Etiam tristique, sapien non rhoncus vestibulum, erat augue suscipit velit, vestibulum viverra justo nibh ut nibh. Vivamus pulvinar pharetra scelerisque. Curabitur ullamcorper tristique lacus.";
    String lor4 = "Maecenas id tortor sed purus ultricies tempor. In vulputate feugiat iaculis. Phasellus sem turpis, adipiscing sit amet lacus in, aliquet aliquet eros. Integer euismod, orci et tincidunt iaculis, odio odio vulputate lectus, sed rutrum sapien odio eget sem.";

    private ResideMenu resideMenu;


    public TransitionDetailFragment() {

    }

    @SuppressLint("ValidFragment")
    public TransitionDetailFragment(final int top, final int left, final int width, final int height, String title, String descr, int imgId, int barname_id) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;

        this.title = title;
        this.sum = descr;
        this.imgId = imgId;
        this.barname_id = barname_id;

    }

    public static TransitionDetailFragment newInstance(final int top, final int left, final int width, final int height, String title, String descr, int imgId, int barname_id) {
        TransitionDetailFragment fragment = new TransitionDetailFragment(top, left, width, height, title, descr, imgId, barname_id);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transition_detail, container, false);

        myContext = getContext();

        ((UIParallaxScroll) rootView.findViewById(R.id.scroller)).setOnScrollChangedListener(mOnScrollChangedListener);

        mImageView = rootView.findViewById(R.id.image_view);
        //   mTextView = (TextView) findViewById(R.id.contact);
        mNavigationTop = rootView.findViewById(R.id.layout_top);
        mNavigationTitle = rootView.findViewById(R.id.titleBar);
        mLayoutContainer = rootView.findViewById(R.id.bg_layout);
        mTitleView = rootView.findViewById(R.id.title);
        // mNavigationBackBtn = (Button) rootView.findViewById(R.id.title_bar_left_menu1);
        TextView mSum = rootView.findViewById(R.id.sumary);
        mShare = rootView.findViewById(R.id.action1);
        UITabs tab = rootView.findViewById(R.id.toggle);
        lst_data1 = rootView.findViewById(R.id.lst_data);
        progress = rootView.findViewById(R.id.progress);

        // listView   = (UISwipableList) rootView.findViewById(R.id.listView);

        mNavigationTop.getBackground().setAlpha(0);
        mNavigationTitle.setVisibility(View.INVISIBLE);

        mImageView.bringToFront();


        //Our Animation initialization
        ViewTreeObserver observer = mImageView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);

                int[] screen_location = new int[2];
                mImageView.getLocationOnScreen(screen_location);

                delta_left = left - screen_location[0];
                delta_top = top - screen_location[1];

                scale_width = (float) width / mImageView.getWidth();
                scale_height = (float) height / mImageView.getHeight();

                runEnterAnimation();

                return true;
            }
        });


        listView = rootView.findViewById(R.id.listView);

        mTitleView.setText(title);
        mSum.setText(sum);
        mImageView.setImageResource(imgId);
        mNavigationTitle.setText(title);

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FragmentManager fm = getFragmentManager();
                PostComentDialogFragment dialogFragment = new PostComentDialogFragment();
                dialogFragment.show(fm, "Sample Fragment");
            }
        });

        new StationsTask1().execute();

        setUpViews();
        return rootView;
    }

    private void setUpViews() {
        TransitionDetailActivity parentActivity = (TransitionDetailActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
    }

    private void runEnterAnimation() {

        ViewHelper.setPivotX(mImageView, 0.f);
        ViewHelper.setPivotY(mImageView, 0.f);
        ViewHelper.setScaleX(mImageView, scale_width);
        ViewHelper.setScaleY(mImageView, scale_height);
        ViewHelper.setTranslationX(mImageView, delta_left);
        ViewHelper.setTranslationY(mImageView, delta_top);

        animate(mImageView).
                setDuration(DURATION).
                scaleX(1.f).
                scaleY(1.f).
                translationX(0.f).
                translationY(0.f).
                setInterpolator(new DecelerateInterpolator()).
                setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }
                });

        ObjectAnimator bg_anim = ObjectAnimator.ofFloat(mLayoutContainer, "alpha", 0f, 1f);
        bg_anim.setDuration(DURATION);
        bg_anim.start();

    }

    private void runExitAnimation(final Runnable end_action) {

        ViewHelper.setPivotX(mImageView, 0.f);
        ViewHelper.setPivotY(mImageView, 0.f);
        ViewHelper.setScaleX(mImageView, 1.f);
        ViewHelper.setScaleY(mImageView, 1.f);
        ViewHelper.setTranslationX(mImageView, 0.f);
        ViewHelper.setTranslationY(mImageView, 0.f);

        animate(mImageView).
                setDuration(DURATION).
                scaleX(scale_width).
                scaleY(scale_height).
                translationX(delta_left).
                translationY(delta_top).
                setInterpolator(new DecelerateInterpolator()).
                setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        end_action.run();
                    }
                });

        ObjectAnimator bg_anim = ObjectAnimator.ofFloat(mLayoutContainer, "alpha", 1f, 0f);
        bg_anim.setDuration(DURATION);
        bg_anim.start();

    }

    private UIParallaxScroll.OnScrollChangedListener mOnScrollChangedListener = new UIParallaxScroll.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            //Difference between the heights, important to not add margin or remove mNavigationTitle.
            final float headerHeight = ViewHelper.getY(mTitleView) - (mNavigationTop.getHeight() - mTitleView.getHeight());
            final float ratio = Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mNavigationTop.getBackground().setAlpha(newAlpha);

            Animation animationFadeIn = AnimationUtils.loadAnimation(myContext, R.anim.fadein);
            Animation animationFadeOut = AnimationUtils.loadAnimation(myContext, R.anim.fadeout);

            if (newAlpha == 255 && mNavigationTitle.getVisibility() != View.VISIBLE && !animationFadeIn.hasStarted()) {
                mNavigationTitle.setVisibility(View.VISIBLE);
                mNavigationTitle.startAnimation(animationFadeIn);
            } else if (newAlpha < 255 && !animationFadeOut.hasStarted() && mNavigationTitle.getVisibility() != View.INVISIBLE) {
                mNavigationTitle.startAnimation(animationFadeOut);
                mNavigationTitle.setVisibility(View.INVISIBLE);

            }

        }
    };

    private class StationsTask1 extends AsyncTask<Void, Void, Void> {
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
            dbAdapter = new DBAdapter(getContext());
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

                    String clearDesc = String.valueOf(description);//html2text(description);

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

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    private void FillData() {
        myContext = this.getContext();

        boolean tableIsEmpty = dbAdapter.isBarnameEmpty();

        if (tableIsEmpty == true) {
            new AlertDialog.Builder(myContext)
                    .setMessage("اگر برای بار اول وارد اپلیکیشن شدید یا برای بروزرسانی به اینترنت متصل شوید و مجددا سعی کنید")
                    .setCancelable(false)
                    .setPositiveButton("می پذیرم", null)
                    .show();
        } else {
            ArrayList<barname> barnameArrayList = dbAdapter.getBarname();
            BarnameListAdapter barnameListAdapter = new BarnameListAdapter(getContext(), R.layout.fragment_list_item, barnameArrayList);
            //  lst_data1.setAdapter(barnameListAdapter);

            for (int i = 0; i < barnameArrayList.size(); i++) {
                View v = BarnameListAdapter.getView(barnameArrayList.get(i), getContext());
                listView.addView(v);
            }
        }
    }
}
