package com.kd8bny.maintenanceman.ui.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.backupRestoreHelper;


public class fragment_settings extends PreferenceFragment {
    private static final String TAG = "frgmnt_sttngs";

    private DropboxAPI<AndroidAuthSession> mDBApi;
    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private String action = "backup";

    public fragment_settings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final String APP_KEY = getActivity().getApplicationContext().getResources().getString(R.string.dropboxKey);
        final String APP_SECRET = getActivity().getApplicationContext().getResources().getString(R.string.dropboxSecret);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        final Preference dropboxButton = findPreference(getString(R.string.pref_key_dropbox));
        if (getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getString("Dropbox", null) != null) {
            dropboxButton.setSummary(R.string.pref_summary_cloud_restore);
            action = "restore";
        }

        dropboxButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
            if (!action.equals("restore")) {
                mDBApi.getSession().startOAuth2Authentication(getActivity());

                return false;

            } else {
                backupRestoreHelper backupRestoreHelper = new backupRestoreHelper(getActivity(), action);
                backupRestoreHelper.execute();

                Snackbar.make(getActivity().findViewById(R.id.fragmentContainer_settings), getString(R.string.pref_toast_cloud_restore), Snackbar.LENGTH_SHORT).show();

                return false;
            }
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
                editor.commit();

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "DROPBOX: Error authenticating", e);
            }
        }
    }
}