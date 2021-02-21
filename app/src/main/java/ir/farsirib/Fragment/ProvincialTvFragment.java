package ir.farsirib.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.farsirib.Adapter.ProvincialTvRecyclerViewAdapter;
import ir.farsirib.Model.LiveItemObject;
import ir.farsirib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProvincialTvFragment extends Fragment {

    private GridLayoutManager lLayout;
    Context myContext;

    public ProvincialTvFragment() {
        myContext = this.getContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rooView = inflater.inflate(R.layout.fragment_provincial_tv, container, false);

        myContext = this.getContext();

        List<LiveItemObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(myContext, 4);

        RecyclerView rView = rooView.findViewById(R.id.provincial_recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        ProvincialTvRecyclerViewAdapter rcAdapter = new ProvincialTvRecyclerViewAdapter(rowListItem,myContext);
        rView.setAdapter(rcAdapter);



        return rooView;
    }

    private List<LiveItemObject> getAllItemList() {

        List<LiveItemObject> allItems = new ArrayList<LiveItemObject>();
        allItems.add(new LiveItemObject("شبکه سهند", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/azSharqi.png"));
        allItems.add(new LiveItemObject("شبکه آذربایجان غربی", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/azQarbi.png"));
        allItems.add(new LiveItemObject("شبکه سبلان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/ardabil.png"));
        allItems.add(new LiveItemObject("شبکه اصفهان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/esfahan.png"));
        allItems.add(new LiveItemObject("شبکه البرز", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/alborz.png"));
        allItems.add(new LiveItemObject(" شبکه ایلام", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/ilam.png"));
        allItems.add(new LiveItemObject("شبکه بوشهر", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/bushehr.png"));
        allItems.add(new LiveItemObject("شبکه جهان بین", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/charmahal.png"));
        allItems.add(new LiveItemObject("شبکه خراسان جنوبی", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/khorasan_jonubi.png"));
        allItems.add(new LiveItemObject("شبکه خراسان رضوی", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/razavi.png"));
        allItems.add(new LiveItemObject("شبکه خراسان شمالی", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/khorasan_shomali.png"));
        allItems.add(new LiveItemObject("شبکه خوزستان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/khuzestan.png"));
        allItems.add(new LiveItemObject("شبکه اشراق(زنجان)", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/zanjan.png"));
        allItems.add(new LiveItemObject("شبکه سمنان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/semnan.png"));
        allItems.add(new LiveItemObject("شبکه هامون", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/sistan.png"));
        allItems.add(new LiveItemObject("شبکه فارس", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/fars.png"));
        allItems.add(new LiveItemObject("شبکه قزوین", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/qazvin.png"));
        allItems.add(new LiveItemObject("شبکه نور(قم)", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/qom.png"));
        allItems.add(new LiveItemObject("شبکه کردستان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/kordestan.png"));
        allItems.add(new LiveItemObject("شبکه کرمان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/kerman.png"));
        allItems.add(new LiveItemObject("شبکه کرمانشاه", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/kermanshah.png"));
        allItems.add(new LiveItemObject("شبکه دنا", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/yasooj.png"));
        allItems.add(new LiveItemObject("شبکه گلستان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/golestan.png"));
        allItems.add(new LiveItemObject("شبکه باران(گیلان)", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/gilan.png"));
        allItems.add(new LiveItemObject("شبکه افلاک(لرستان)", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/lorestan.png"));
        allItems.add(new LiveItemObject("شبکه مازندران", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/mazandaran.png"));
        allItems.add(new LiveItemObject("شبکه آفتاب(اراک)", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/markazi.png"));
        allItems.add(new LiveItemObject("شبکه خلیج فارس", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/khalij.png"));
        allItems.add(new LiveItemObject("شبکه کیش", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/kish.png"));
        allItems.add(new LiveItemObject("شبکه همدان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/hamedan.png"));
        allItems.add(new LiveItemObject("شبکه تابان(یزد)", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/yazd.png"));
        allItems.add(new LiveItemObject("شبکه مهاباد", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/mahabad.png"));
        allItems.add(new LiveItemObject("شبکه آبادان", "http://www.mob.shahreraz.com/mob/Naranj/img/marakez/abadan.png"));

        return allItems;

    }

}
