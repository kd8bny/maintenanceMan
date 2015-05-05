package com.kd8bny.maintenanceman.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kd8bny.maintenanceman.R;


public class activity_main extends AppCompatActivity {
    private static final String TAG = "activity_main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer_main);

        if (fragment == null){
            fragment = new fragment_overview();
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
