package com.kd8bny.maintenanceman.billing;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.kd8bny.maintenanceman.R;

public class activity_billing extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentcontainer_billing);

        if (fragment == null) {
            fragment = new fragment_billing();
            fm.beginTransaction()
                    .add(R.id.fragmentcontainer_billing, fragment)
                    .commit();
        }
    }
}
