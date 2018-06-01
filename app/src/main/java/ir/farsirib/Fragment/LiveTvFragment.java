package ir.farsirib.Fragment;


import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveTvFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public LiveTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_live_tv, container, false);

        viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


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

        public ViewPagerAdapter(FragmentManager manager) {
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
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
