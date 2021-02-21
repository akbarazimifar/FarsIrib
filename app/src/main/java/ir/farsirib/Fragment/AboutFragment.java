package ir.farsirib.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ir.farsirib.Activity.MainActivity;
import ir.farsirib.Activity.OptionActivity;
import ir.farsirib.Activity.TransitionDetailActivity;
import ir.farsirib.Activity.VideoPlayerActivity;
import ir.farsirib.R;
import ir.farsirib.menu.ResideMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    private String parentName;
    private View parentView;
    private ResideMenu resideMenu;

    public AboutFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public AboutFragment(String parentName) {
        this.parentName= parentName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_about, container, false);

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

        return parentView;
    }
}
