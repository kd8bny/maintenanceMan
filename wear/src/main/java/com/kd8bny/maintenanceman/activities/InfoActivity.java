package com.kd8bny.maintenanceman.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import com.kd8bny.maintenanceman.adapters.InfoGridPagerAdapter;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

public class InfoActivity extends Activity {
    private static final String TAG = "wear_info";

    private Vehicle mVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        mVehicle = bundle.getParcelable("vehicle");

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new InfoGridPagerAdapter(this, getFragmentManager(), mVehicle));

        /* //---Assigns an adapter to provide the content for this pager---
        pager.setAdapter(new ImageAdapter(this));
        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);*/

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
