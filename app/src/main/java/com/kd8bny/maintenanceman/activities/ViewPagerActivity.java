package com.kd8bny.maintenanceman.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.ViewPagerAdapter;

public class ViewPagerActivity extends AppCompatActivity {
    private static final String TAG = "activity_viewpager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
        ViewPager mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);
    }
}
