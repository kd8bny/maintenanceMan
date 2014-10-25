package com.kd8bny.maintenanceman.ui.add;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.kd8bny.maintenanceman.R;


public class activity_add extends Activity {

    private static final String TAG = "activity_add_vehicle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);//TODO use same frag contain

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer_add_vehicle);

        if (fragment == null){
            fragment = new fragment_add_vehicle();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer_add_vehicle, fragment)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //Used to save instance on rotation or if destroyed
    }
}
