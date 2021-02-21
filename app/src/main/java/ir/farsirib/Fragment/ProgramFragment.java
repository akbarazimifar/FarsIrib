package ir.farsirib.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

import ir.farsirib.Activity.MainActivity;
import ir.farsirib.Activity.OptionActivity;
import ir.farsirib.Activity.TransitionDetailActivity;
import ir.farsirib.Activity.VideoPlayerActivity;
import ir.farsirib.Adapter.ProgramRecyclerViewAdapter;
import ir.farsirib.Adapter.TransitionListAdapter;
import ir.farsirib.Model.program;
import ir.farsirib.R;
import ir.farsirib.menu.ResideMenu;
import ir.farsirib.utils.UISwipableList;


public class ProgramFragment extends Fragment {


    private String parentName="";
    Context myContext;
    private View parentView;
    private ResideMenu resideMenu;
    private LinearLayoutManager lLayout;
    private UISwipableList listView;
    //private ProgramRecyclerViewAdapter mAdapter;
    private TransitionListAdapter mAdapter;
    private BottomNavigationView bottomNavigationView;
    int barname_id=0;

    //Vars
    private String PACKAGE = "IDENTIFY";

    public static ProgramFragment newInstance(String parentName) {
        ProgramFragment fragment = new ProgramFragment(parentName);
        return fragment;
    }

    public ProgramFragment() {}

    @SuppressLint("ValidFragment")
    public ProgramFragment(String parentName) {
        myContext = this.getContext();
        this.parentName= parentName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView   = inflater.inflate(R.layout.fragment_program, container, false);
        listView   = parentView.findViewById(R.id.listView);

        myContext = this.getContext();

        final List<program> rowListItem = getAllItemList();

        RecyclerView rView = parentView.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        lLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rView.setLayoutManager(lLayout);

        ProgramRecyclerViewAdapter rcAdapter = new ProgramRecyclerViewAdapter(myContext,rowListItem);
        rView.setAdapter(rcAdapter);



        bottomNavigationView = parentView.findViewById(R.id.navigation);

        WebView webView = parentView.findViewById(R.id.webView1);

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.clearCache(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl("http://77.36.166.137/mob/FarsApp/index.php/kondaktor/fetch_data/1");


        setUpViews();
        return parentView;
    }

    private void setUpViews() {

        switch (parentName)
        {
            case "MainActivity":
                MainActivity parentActivity1 = (MainActivity) getActivity();
                resideMenu = parentActivity1.getResideMenu();
                break;
            case "TransitionDetailActivity":
                TransitionDetailActivity parentActivity2 = (TransitionDetailActivity) getActivity();
                resideMenu = parentActivity2.getResideMenu();
                break;
            case "OptionActivity":
                OptionActivity parentActivity3 = (OptionActivity) getActivity();
                resideMenu = parentActivity3.getResideMenu();
                break;
            case "VideoPlayerActivity":
                VideoPlayerActivity parentActivity4 = (VideoPlayerActivity) getActivity();
                resideMenu = parentActivity4.getResideMenu();
                break;
            case "MainListItemsActivity":
                MainActivity parentActivity5 = (MainActivity) getActivity();
                resideMenu = parentActivity5.getResideMenu();
                break;
        }

    }

    private List<program> getAllItemList() {

        List<program> allItems = new ArrayList<program>();
        allItems.add(new program("کاشانه مهر", "http://www.shahreraz.com/Farsirib/img/kashane.png",R.drawable.ph_1,"برنامه خانواده سیمای فارس شنبه تا چهارشنبه ساعت 10"));
        allItems.add(new program("خودمونی", "http://www.shahreraz.com/Farsirib/img/khodemooni.png",R.drawable.khodemooni,"برنامه طنز خودمونی"));
        allItems.add(new program("خوشاشیراز", "http://www.shahreraz.com/Farsirib/img/khosha.png",R.drawable.ph_2,"جمعه ها ساعت 10 صبح از شبکه فارس و شبکه شما"));
        allItems.add(new program("کودک و نوجوان", "http://www.shahreraz.com/Farsirib/img/gompegola.png",R.drawable.ph_3,"برنامه کودک و نوجوان سیمای فارس شنبه تا چهارشنبه ساعت 16"));
        allItems.add(new program("شب پارسی", "http://www.shahreraz.com/Farsirib/img/shabe_parsi.png",R.drawable.ph_4, "برنامه زنده شبانه شنبه تا چهارشنبه ساعت 22"));
        allItems.add(new program("صبح دلگشا", "http://www.shahreraz.com/Farsirib/img/sobhe_delgosha.png",R.drawable.ph_5,"برنامه زنده صبحگاهی سیمای فارس شنبه تا چهارشنبه ساعت 7:45"));
        allItems.add(new program("گفتگو", "http://www.shahreraz.com/Farsirib/img/goftegoo.png",R.drawable.ph_6,"برنامه گفتگو شنبه ها ساعت 21 از سیمای فارس"));
        allItems.add(new program("شهرراز", "http://www.shahreraz.com/Farsirib/img/shahreraz.png",R.drawable.ph_7,"یکشنبه ، سه شنبه ، پنجشنبه ساعت 21"));
        allItems.add(new program("مشاورشما","http://www.shahreraz.com/Farsirib/img/moshavere_shoma.png",R.drawable.ph_8, "شنبه تا سه شنبه ساعت 18"));
        allItems.add(new program("هم ولایتی","http://www.shahreraz.com/Farsirib/img/hamvelayati.png",R.drawable.ph_11, ""));
        allItems.add(new program("شمعدونی","http://www.shahreraz.com/Farsirib/img/shamdooni.png",R.drawable.ph_12, ""));

        return allItems;
    }
}
