package com.kd8bny.maintenanceman.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.fragments.fragment_main;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "activity_main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_main);

        if (fragment == null){
            fragment = new fragment_main();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
