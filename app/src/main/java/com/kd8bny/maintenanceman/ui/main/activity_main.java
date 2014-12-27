package com.kd8bny.maintenanceman.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.kd8bny.maintenanceman.R;


public class activity_main extends Activity {

    private static final String TAG = "activity_main";

    private static final String version = "V0.1a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer_main);

        if (fragment == null){
            fragment = new fragment_overview();
            //fragment = new fragment_dbtest();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer_main, fragment)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //Used to save instance on rotation or if destroyed
    }
}
