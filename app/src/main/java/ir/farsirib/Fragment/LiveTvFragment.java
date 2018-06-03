package ir.farsirib.Fragment;


import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.farsirib.R;
import ir.farsirib.shenavarview.QueryPreferences;

import static android.content.Context.ACTIVITY_SERVICE;
import static ir.farsirib.Activity.DetailActivity.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveTvFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    View rootView;

    public LiveTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_live_tv, container, false);

        viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(QueryPreferences.getPermissionStatus(rootView.getContext())==null){
            QueryPreferences.setPermissionStatus(rootView.getContext(), "notGranted");
            checkPermission();
        }else{
            if(QueryPreferences.getPermissionStatus(rootView.getContext()).equals("notGranted")){
                checkPermission();
            }
        }


        return rootView;
    }
    private void setupViewPager(ViewPager viewPager) {
        //   viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

        LiveTvFragment.ViewPagerAdapter adapter = new LiveTvFragment.ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new NationalTvFragment(), "سراسری");
        adapter.addFragment(new ProvincialTvFragment(), "استانی");
        adapter.addFragment(new OverseasTvFragment(), "برون مرزی");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(rootView.getContext())) {
                // You don't have permission
                checkPermission();
            }
            else
            {
                QueryPreferences.setPermissionStatus(rootView.getContext(), "OK");
            }
        }
    }
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(rootView.getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + rootView.getContext().getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

//    public boolean isSL(){
//        ActivityManager manager = (ActivityManager) rootView.getContext().getSystemService(ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
//        {
//            if ("ir.farsirib.shenavarview.Video"
//                    .equals(service.service.getClassName()))
//            {
//                return true;
//            }
//        }
//        return false;
//    }
}
