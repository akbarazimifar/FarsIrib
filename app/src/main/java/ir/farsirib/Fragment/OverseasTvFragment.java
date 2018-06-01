package ir.farsirib.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.farsirib.Adapter.NationalTvRecyclerViewAdapter;
import ir.farsirib.Adapter.OverseasTvRecyclerViewAdapter;
import ir.farsirib.Model.LiveItemObject;
import ir.farsirib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverseasTvFragment extends Fragment {

    private GridLayoutManager lLayout;
    Context myContext;

    public OverseasTvFragment() {
        this.myContext = myContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  rooView = inflater.inflate(R.layout.fragment_overseas_tv, container, false);

        myContext = this.getContext();

        List<LiveItemObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(myContext, 4);

        RecyclerView rView = rooView.findViewById(R.id.overseas_recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        OverseasTvRecyclerViewAdapter rcAdapter = new OverseasTvRecyclerViewAdapter(rowListItem,myContext);
        rView.setAdapter(rcAdapter);

        return rooView;
    }

    private List<LiveItemObject> getAllItemList() {
        List<LiveItemObject> allItems = new ArrayList<LiveItemObject>();
        allItems.add(new LiveItemObject("شبکه پرس تی وی", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/presstv.png"));
        allItems.add(new LiveItemObject("شبکه جام جم", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/jamejam.png"));
        allItems.add(new LiveItemObject("شبکه العالم", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/alalam.png"));
        allItems.add(new LiveItemObject("شبکه سحر 1", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/sahar.png"));
        allItems.add(new LiveItemObject("شبکه سحر 2", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/sahar.png"));
        allItems.add(new LiveItemObject("شبکه سحر 3", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/sahar.png"));
       // allItems.add(new LiveItemObject("شبکه هیسپان تی وی", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/hispantv.png"));
        allItems.add(new LiveItemObject("شبکه الکوثر", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/alkosar.png"));
        allItems.add(new LiveItemObject("شبکه آی فیلم عربی", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/ifilmarabi.png"));
       // allItems.add(new LiveItemObject("شبکه آی فیلم انگلیسی", "http://www.mob.shahreraz.com/Farsirib/img/overseasTv/ifilmenglish.png"));




        return allItems;
    }

}
