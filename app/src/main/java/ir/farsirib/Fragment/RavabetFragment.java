package ir.farsirib.Fragment;


import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ir.farsirib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RavabetFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public RavabetFragment() {
        // Required empty public constructor
    }

    public static RavabetFragment newInstance() {
        RavabetFragment fragment = new RavabetFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ravabet, container, false);

        viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        //   viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

        RavabetFragment.ViewPagerAdapter adapter = new RavabetFragment.ViewPagerAdapter(getFragmentManager());
       // adapter.addFragment(new FarsIribHistoryFragment(), "تاریخچه مرکز");
        adapter.addFragment(new FarsIribRelationshipFragment(), "ارتباط با مرکز ");
        adapter.addFragment(new CyberspaceFragment(), "فضای مجازی");
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
