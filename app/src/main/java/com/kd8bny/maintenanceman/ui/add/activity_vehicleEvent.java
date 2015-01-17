package com.kd8bny.maintenanceman.ui.add;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.kd8bny.maintenanceman.R;

public class activity_vehicleEvent extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_event);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentcontainer_vehicle_event);

        if (fragment == null){
            fragment = new fragment_add_vehicleEvent();
            fm.beginTransaction()
                    .add(R.id.fragmentcontainer_vehicle_event, fragment)
                    .commit();

        }

    }
}
