package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import static com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener;
import static com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener;

public class ViewModel {
    private FragmentStatePagerAdapter viewPagerAdapter;
    private ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener;
    private TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener;

    public ViewModel(FragmentManager fm,
                         ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener,
                         TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener) {
        this.viewPagerOnTabSelectedListener = viewPagerOnTabSelectedListener;
        this.tabLayoutOnPageChangeListener = tabLayoutOnPageChangeListener;

        // ViewPagerAdapter 생성
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChatListFragment());
        fragments.add(new UserProfileFragment());
        viewPagerAdapter = new ViewPager(fm, fragments);
    }

    public FragmentStatePagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    public ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return viewPagerOnTabSelectedListener;
    }

    public TabLayoutOnPageChangeListener getTabLayoutOnPageChangeListener() {
        return tabLayoutOnPageChangeListener;
    }
}
