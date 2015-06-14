package com.kd8bny.maintenanceman.ui.preferences;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kd8bny.maintenanceman.R;


public class activity_settings extends AppCompatActivity {
    private static final String TAG = "actvty_prf";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer_settings);

        if (fragment == null) {
            fragment = new fragment_settings();
            fm.beginTransaction()
                    .replace(R.id.fragmentContainer_settings, fragment)
                    .commit();
        }
    }
}
