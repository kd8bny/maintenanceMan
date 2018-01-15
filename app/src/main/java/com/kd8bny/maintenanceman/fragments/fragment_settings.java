package com.kd8bny.maintenanceman.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.kd8bny.maintenanceman.BuildConfig;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.utils.LocaleChange;


public class fragment_settings extends PreferenceFragment {
    private static final String TAG = "frgmnt_sttngs";

    private DbxClientV2 mClient;

    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private Context mContext;

    public fragment_settings() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mContext = getActivity().getApplicationContext();

        final Preference appVersion = findPreference(getString(R.string.pref_key_about));
        appVersion.setSummary(BuildConfig.VERSION_NAME);

        final Preference dropboxButton = findPreference(getString(R.string.pref_key_dropbox));
        final String APP_KEY = mContext.getResources().getString(R.string.dropboxKey);
        final String APP_SECRET = mContext.getResources().getString(R.string.dropboxSecret);

        dropboxButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Auth.startOAuth2Authentication(mContext, APP_KEY);

                return true;
            }
        });

        Preference listPreference = getPreferenceManager().findPreference("prefUnitDist");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String unit = (String) newValue;
                SharedPreferences.Editor editor = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).edit();
                editor.putString("prefUnitDist", unit);
                editor.apply();
                new LocaleChange(mContext).changeUnits(unit);
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        String authToken = Auth.getOAuth2Token();

        if(authToken != null){
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString(getString(R.string.pref_key_dropbox), authToken);
            editor.apply();
        }
    }
}