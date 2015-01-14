package com.kd8bny.maintenanceman.ui.history;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.ui.add.fragment_add_vehicleEvent;

public class activity_history extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentcontainer_history);

        if (fragment == null){
            fragment = new fragment_history();
            fm.beginTransaction()
                    .add(R.id.fragmentcontainer_history, fragment)
                    .commit();

        }

    }
}
