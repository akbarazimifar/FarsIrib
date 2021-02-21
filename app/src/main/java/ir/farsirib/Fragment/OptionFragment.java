package ir.farsirib.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ir.farsirib.Activity.MainActivity;
import ir.farsirib.Activity.OptionActivity;
import ir.farsirib.Activity.TransitionDetailActivity;
import ir.farsirib.Adapter.OptionRecyclerViewAdapter;
import ir.farsirib.Model.OptionItemObject;
import ir.farsirib.R;
import ir.farsirib.menu.ResideMenu;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class OptionFragment extends Fragment {

    private GridLayoutManager lLayout;
    private ResideMenu resideMenu;

    ListView lst_data;
    Context myContext;
    private String parentName;

    Dialog dialog;
    Button btnCancel;
    boolean isOnline = false;
    boolean wifi = false;

    @SuppressLint("ValidFragment")
    public OptionFragment(String parentName,Context context) {
        myContext = context;
        //Toast.makeText(myContext, "in option fragment = " + myContext.getPackageName(), Toast.LENGTH_SHORT).show();
        this.parentName = parentName;
    }

    public OptionFragment() {
    }

    public OptionFragment(Context context) {
        myContext = context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView= inflater.inflate(R.layout.fragment_option, container, false);

        List<OptionItemObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(myContext, 2);

        RecyclerView rView = RootView.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        OptionRecyclerViewAdapter rcAdapter = new OptionRecyclerViewAdapter(myContext, rowListItem);
        rView.setAdapter(rcAdapter);

        try {
            ConnectivityManager connManager = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeConnection = connManager.getActiveNetworkInfo();
            if ( (activeConnection != null) && activeConnection.isConnected() )
                isOnline = true;

            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if ( mWifi.isConnected() )
                wifi = true;

            if ( wifi == true || isOnline == true ) {

               // UpdateCheckActivity updateCheckActivity = new UpdateCheckActivity();

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
                }
            }
            else {
                showCustomDialog();
            }
        }

    catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return RootView;
    }

    protected void showCustomDialog() {
        dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);

        btnCancel = dialog.findViewById(R.id.btncancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                dialog.cancel();
            }

        });

        final ImageView myImage = dialog.findViewById(R.id.loader);
        myImage.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate) );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));

        dialog.show();
    }

    private List<OptionItemObject> getAllItemList(){

        List<OptionItemObject> allItems = new ArrayList<OptionItemObject>();
        allItems.add(new OptionItemObject("صدا", R.mipmap.ic_radio));
        allItems.add(new OptionItemObject("سیما", R.mipmap.ic_tv));
        allItems.add(new OptionItemObject("موسیقی", R.mipmap.ic_music));
        allItems.add(new OptionItemObject("خبر", R.mipmap.ic_news));
        allItems.add(new OptionItemObject("شهروند خبرنگار", R.mipmap.ic_reporter));
        allItems.add(new OptionItemObject("ارسال نظر", R.mipmap.ic_comment));
        allItems.add(new OptionItemObject("فنی", R.mipmap.ic_technical));
        allItems.add(new OptionItemObject("روابط عمومی", R.mipmap.ic_relation));
        allItems.add(new OptionItemObject("فضای مجازی", R.mipmap.ic_cyber));
       // allItems.add(new OptionItemObject("تابلو اعلانات", R.mipmap.ic_board));
        allItems.add(new OptionItemObject("پخش زنده شبکه های تلویزیونی", R.mipmap.ic_live));

        return allItems;
    }

}
