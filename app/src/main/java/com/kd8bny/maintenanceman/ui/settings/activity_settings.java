package com.kd8bny.maintenanceman.ui.settings;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.ui.add.fragment_add_fleetRoster;

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
