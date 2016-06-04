package com.kd8bny.maintenanceman.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.kd8bny.maintenanceman.BuildConfig;
import com.kd8bny.maintenanceman.R;


public class fragment_settings extends PreferenceFragment{
    private static final String TAG = "frgmnt_sttngs";

    private DropboxAPI<AndroidAuthSession> mDBApi;

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
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);

        mDBApi = new DropboxAPI<>(session);

        dropboxButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDBApi.getSession().startOAuth2Authentication(getActivity());

                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mDBApi.getSession().authenticationSuccessful()){
            try{
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString(getString(R.string.pref_key_dropbox), accessToken);
                editor.apply();

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "DROPBOX: Error authenticating", e);
            }
        }
    }
}