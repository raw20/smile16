package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.databinding.ActivitySubBinding;
import com.google.android.material.tabs.TabLayout;
public class SubActivity extends AppCompatActivity {
    private  TabLayout tabLayout;
    private  ViewPager viewPager;
    private  ViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySubBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sub);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        viewModel = new ViewModel(
                getSupportFragmentManager(),
                new TabLayout.ViewPagerOnTabSelectedListener((androidx.viewpager.widget.ViewPager) findViewById(R.id.viewPager)),
                new TabLayout.TabLayoutOnPageChangeListener((TabLayout) findViewById(R.id.tabLayout)));
        binding.setViewModel(viewModel);

    }
}
