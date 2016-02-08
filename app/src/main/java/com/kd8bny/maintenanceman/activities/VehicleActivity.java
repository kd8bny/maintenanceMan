package com.kd8bny.maintenanceman.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.fragments.fragment_fleetRoster_add;
import com.kd8bny.maintenanceman.fragments.fragment_info;
import com.kd8bny.maintenanceman.fragments.fragment_vehicleEvent_add;

public class VehicleActivity extends AppCompatActivity {
    private static final String TAG = "activity_vehicle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        switch (bundle.getInt("caseID", -1)){
            case 0:
                fragment = new fragment_fleetRoster_add();
                break;
            case 1:
                fragment = new fragment_vehicleEvent_add();
                break;
            default:
                fragment = new fragment_info();
        }

        fragment.setArguments(bundle);
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
