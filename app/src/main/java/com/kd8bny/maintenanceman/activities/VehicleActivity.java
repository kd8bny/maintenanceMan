package com.kd8bny.maintenanceman.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.fragments.fragment_fleetRoster;
import com.kd8bny.maintenanceman.fragments.fragment_travel_add;
import com.kd8bny.maintenanceman.fragments.fragment_vehicleEvent;

public class VehicleActivity extends AppCompatActivity {
    private static final String TAG = "activity_vehicle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;
        switch (bundle.getInt("caseID", -1)){
            case 0:
                toolbar.setTitle(getString(R.string.title_add_fleet_roster));//TODO title
                fragment = new fragment_fleetRoster();
                fragment.setArguments(bundle);
                break;
            case 1:
                toolbar.setTitle(getString(R.string.title_add_vehicle_event));
                fragment = new fragment_vehicleEvent();
                fragment.setArguments(bundle);
                break;
            case 4:
                toolbar.setTitle(getString(R.string.title_edit));
                fragment = new fragment_travel_add();
                fragment.setArguments(bundle);
                break;
        }

        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
