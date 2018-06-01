package ir.farsirib.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import ir.farsirib.Activity.OptionActivity;
import ir.farsirib.Activity.VideoPlayerActivity;
import ir.farsirib.R;

import java.util.ArrayList;

import ir.farsirib.Activity.MainActivity;
import ir.farsirib.Activity.TransitionDetailActivity;
import ir.farsirib.Adapter.TransitionListAdapter;
import ir.farsirib.Model.ListItem;
import ir.farsirib.menu.ResideMenu;
import ir.farsirib.utils.UISwipableList;

@SuppressLint("ValidFragment")
public class TransitionListFragment extends Fragment {

	//Views & Widgets
    private View parentView;
    private String parentName;
    private UISwipableList listView;
    private TransitionListAdapter mAdapter;
    private ResideMenu resideMenu;
    int barname_id=0;
    
    //Vars
    private String PACKAGE = "IDENTIFY";

    @SuppressLint("ValidFragment")
    public TransitionListFragment(String parentName) {
        this.parentName= parentName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_list_transition, container, false);
        listView   = parentView.findViewById(R.id.listView);
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
        }

        initView();
        return parentView;
    }

    private void initView(){
    	mAdapter = new TransitionListAdapter(getActivity(), getListData());
        listView.setActionLayout(R.id.hidden_view);
        listView.setItemLayout(R.id.front_layout);

        listView.setAdapter(mAdapter);
        listView.setIgnoredViewHandler(resideMenu);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewa, int i, long l) { 
                ListItem item = (ListItem) listView.getAdapter().getItem(i);
        
                Intent intent = new Intent(getActivity(), TransitionDetailActivity.class);
                switch (i)
                {
                    case 0:
                        barname_id = 3 ; //kashaneh
                        break;
                    case 1:
                        barname_id = 4 ;//khosha
                        break;
                    case 2:
                        barname_id = 9 ;//gompegola
                        break;
                    case 3:
                        barname_id = 11 ;//shabeparsi
                        break;
                    case 4:
                        barname_id = 15 ;//sobheDekgosha
                        break;
                    case 5:
                        barname_id = 10 ;//goftegoo
                        break;
                    case 6:
                        barname_id = 12 ;//shahreraz
                        break;
                    case 7:
                        barname_id = 13 ;//moshavereShoma
                        break;
                    case 8:
                        barname_id = 30 ;//hamvelayati
                        break;
                    case 9:
                        barname_id = 67 ;//shamdooni
                        break;
                }

                Bundle bundle = new Bundle();
                bundle.putString("title", item.getTitle());
                bundle.putInt("img", item.getImageId());
                bundle.putString("descr", item.getDesc());
                bundle.putInt("barname_id",barname_id);

                int[] screen_location = new int[2];
                View view = viewa.findViewById(R.id.item_image);
                view.getLocationOnScreen(screen_location);
                
                bundle.putInt(PACKAGE + ".left", screen_location[0]);
                bundle.putInt(PACKAGE + ".top", screen_location[1]);
                bundle.putInt(PACKAGE + ".width", view.getWidth());
                bundle.putInt(PACKAGE + ".height", view.getHeight());
                
                intent.putExtras(bundle);

                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
    }

    private ArrayList<ListItem> getListData(){
        ArrayList<ListItem> listData = new ArrayList<ListItem>();
        listData.add(new ListItem(R.drawable.ph_1, "کاشانه مهر", "برنامه خانواده سیمای فارس شنبه تا چهارشنبه ساعت 10", "3", null));
        listData.add(new ListItem(R.drawable.ph_2, "خوشاشیراز", "جمعه ها ساعت 10 صبح از شبکه فارس و شبکه شما", "4", null));
        listData.add(new ListItem(R.drawable.ph_3, "گمپ گلا", "برنامه کودک سیمای فارس شنبه تا چهارشنبه ساعت 16", "9", null));
        listData.add(new ListItem(R.drawable.ph_4, "شب پارسی", "برنامه زنده شبانه شنبه تا چهارشنبه ساعت 22", "11", null));
        listData.add(new ListItem(R.drawable.ph_5, "صبح دلگشا", "برنامه زنده صبحگاهی سیمای فارس شنبه تا چهارشنبه ساعت 7:45", "15", null));
        listData.add(new ListItem(R.drawable.ph_6, "گفتگو", "برنامه گفتگو شنبه ها ساعت 21 از سیمای فارس", null, null));
        listData.add(new ListItem(R.drawable.ph_7, "شهرراز", "یکشنبه ، سه شنبه ، پنجشنبه ساعت 21", null, null));
        listData.add(new ListItem(R.drawable.ph_8, "مشاور شما", "شنبه تا سه شنبه ساعت 18", null, null));
        listData.add(new ListItem(R.drawable.ph_11, "هم ولایتی", " ", null, null));
        listData.add(new ListItem(R.drawable.ph_12, "شمعدونی", " ", null, null));
        return listData;
    }
}
