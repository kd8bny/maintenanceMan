package com.kd8bny.maintenanceman.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.kd8bny.maintenanceman.R;



public class fragment_settings extends PreferenceFragment {
    private static final String TAG = "fragment_settings";

    public fragment_settings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }

}