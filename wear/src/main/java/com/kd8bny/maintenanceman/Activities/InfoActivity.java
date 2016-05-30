package com.kd8bny.maintenanceman.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.kd8bny.maintenanceman.Adapters.InfoGridPagerAdapter;
import com.kd8bny.maintenanceman.Adapters.MainAdapter;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.SaveLoadHelperWear;
import com.kd8bny.maintenanceman.Vehicle.Vehicle;

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
