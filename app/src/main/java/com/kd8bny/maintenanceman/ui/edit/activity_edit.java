package com.kd8bny.maintenanceman.ui.edit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.kd8bny.maintenanceman.R;

public class activity_edit extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentcontainer_edit);

        if (fragment == null){
            fragment = new fragment_edit();
            fm.beginTransaction()
                    .add(R.id.fragmentcontainer_edit, fragment)
                    .commit();

        }

    }
}
