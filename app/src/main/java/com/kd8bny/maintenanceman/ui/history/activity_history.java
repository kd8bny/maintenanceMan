package com.kd8bny.maintenanceman.ui.history;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.kd8bny.maintenanceman.R;

public class activity_history extends ActionBarActivity {

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
