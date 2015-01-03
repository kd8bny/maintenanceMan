package com.kd8bny.maintenanceman.ui.add;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.kd8bny.maintenanceman.R;


public class activity_add_fleetRoster extends Activity {

    private static final String TAG = "activity_add_fleetroster";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fleetroster);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentcontainer_add_fleetroster);

        if (fragment == null){
            fragment = new fragment_add_fleetRoster();
            fm.beginTransaction()
                    .add(R.id.fragmentcontainer_add_fleetroster, fragment)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //Used to save instance on rotation or if destroyed
    }
}
