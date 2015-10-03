package com.kd8bny.maintenanceman.ui.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.kd8bny.maintenanceman.BuildConfig;
import com.kd8bny.maintenanceman.R;


public class fragment_settings extends PreferenceFragment{
    private static final String TAG = "frgmnt_sttngs";

    public fragment_settings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        final Preference appVersion = findPreference(getString(R.string.pref_key_about));
        final Preference dropboxButton = findPreference(getString(R.string.pref_key_dropbox));
        appVersion.setSummary(BuildConfig.VERSION_NAME);

        dropboxButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent dbxIntent = new Intent(getActivity().getApplicationContext(), activity_settings_dbx.class);
                dbxIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().getApplicationContext().startActivity(dbxIntent);

                return false;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

    }
}