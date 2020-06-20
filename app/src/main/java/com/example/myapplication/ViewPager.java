package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;


public class ViewPager extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    public ViewPager(FragmentManager fragmentManager, List<Fragment> fragments){
        super(fragmentManager);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position < fragments.size())
            return fragments.get(position);
        return null;
    }


    @Override
    public int getCount() {
        return fragments.size();
    }
}
