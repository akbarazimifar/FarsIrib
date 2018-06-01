package ir.farsirib.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import ir.farsirib.R;

import java.util.ArrayList;

import ir.farsirib.Activity.DetailActivity;
import ir.farsirib.Model.ListItem;

public class ListFragment extends Fragment {

    private View parentView;
    private ListView listView;
    private ir.farsirib.Adapter.ListAdapter mAdapter;
    int barname_id ;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_list, container, false);
        listView   = parentView.findViewById(R.id.listView);
        initView();
        return parentView;
    }

    @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void initView(){
    	//Getting width of display, could be usefull for scaling bitmaps
    	Display display = getActivity().getWindowManager().getDefaultDisplay();
    	int width;
    	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2){
    		Point size = new Point();
    		display.getSize(size);
    		width = size.x;
    	} else{
        	width = display.getWidth();
    	}
    	
    	mAdapter = new ir.farsirib.Adapter.ListAdapter(getActivity(), getListData(), width);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                
                ListItem item = (ListItem) listView.getAdapter().getItem(i);
                switch (i)
                {
                    case 0:
                        barname_id = 3; //kashaneh
                        break;
                    case 1:
                        barname_id = 4; //khosha
                        break;
                    case 2:
                        barname_id = 9; //gompegola
                        break;
                    case 3:
                        barname_id = 11; //shabeparsi
                        break;
                    case 4:
                        barname_id = 15; //sobhedelgosha
                        break;
                    case 5:
                        barname_id = 10; //goftegoo
                        break;
                    case 6:
                        barname_id = 12; //shahreraz
                        break;
                    case 7:
                        barname_id = 13; //moshavereshoma
                        break;
                    case 8:
                        barname_id = 30; //hamvelayati
                        break;
                }
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("img", item.getImageId());
                intent.putExtra("descr", item.getDesc());
                intent.putExtra("barname_id", barname_id);
                startActivity(intent);
            }
        });
    }

    private ArrayList<ListItem> getListData(){
        ArrayList<ListItem> listData = new ArrayList<ListItem>();
        listData.add(new ListItem(R.drawable.ph_1, "Airport Hotel", "Large hotel located next to the Airport Terminal", "5", "Rooms Available"));
        listData.add(new ListItem(R.drawable.ph_1, "Select Hotel", "Small hotel near the City", "3", "Rooms Available" ));
        listData.add(new ListItem(R.drawable.ph_1, "Beach Hotel", "Located next to a white sand beach", "3", "Stars"));
        listData.add(new ListItem(R.drawable.ph_1, "Dance and Party Club", "Ideal for teens", "10+", "Rooms Available"));
        listData.add(new ListItem(R.drawable.ph_1, "Royal City Resort", "Enjoy luxery in the City", "5", "Stars"));
        listData.add(new ListItem(R.drawable.ph_1, "Safari Lodge", "Relax in the Wild", "4.5", "Guest Rating"));
        listData.add(new ListItem(R.drawable.ph_1, "Central Park", "The famous Park Hotel","10+", "Rooms Available"));
        listData.add(new ListItem(R.drawable.ph_1, "Tropical by WorldClub", "Located in South Africa", "4.8", "Guest Rating"));
        listData.add(new ListItem(R.drawable.ph_1, "Ski Hotel", "Located next to the Lifts", "All", "Inclusive"));
        listData.add(new ListItem(R.drawable.ph_1, "Relax by WorldClub", "Affordable Luxery", "3", "Rooms Available"));
        listData.add(new ListItem(R.drawable.ph_1, "Road Motel", "Make a stop worth waiting", "No", "Reservation Needed"));
        listData.add(new ListItem(R.drawable.ph_1, "Alpine Lodge", "Located in the Alps", "Full", "Pension"));
        return listData;
    }
}
