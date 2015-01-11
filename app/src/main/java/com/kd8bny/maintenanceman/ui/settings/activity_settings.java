package com.kd8bny.maintenanceman.ui.settings;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.kd8bny.maintenanceman.R;


public class activity_settings extends Activity {

    private static final String TAG = "activity_settings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer_settings);

        if (fragment == null) {
            fragment = new fragment_settings();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer_settings, fragment)
                    .commit();
        }

    }
}
