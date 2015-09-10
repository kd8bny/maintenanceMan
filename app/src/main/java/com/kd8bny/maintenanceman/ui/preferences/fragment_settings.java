package com.kd8bny.maintenanceman.ui.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.kd8bny.maintenanceman.BuildConfig;
import com.kd8bny.maintenanceman.R;


public class fragment_settings extends PreferenceFragment{
    private static final String TAG = "frgmnt_sttngs";

    private DropboxAPI<AndroidAuthSession> mDBApi;

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private String action = "backup";
    private Context context;

    public fragment_settings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        context = getActivity().getApplicationContext();
        final String APP_KEY = context.getResources().getString(R.string.dropboxKey);
        final String APP_SECRET = context.getResources().getString(R.string.dropboxSecret);

        final Preference appVersion = findPreference(getString(R.string.pref_key_about));
        //final Preference dropboxButton = findPreference(getString(R.string.pref_key_dropbox));
        //final Preference gdriveButton = findPreference(getString(R.string.pref_key_gdrive));
        appVersion.setSummary(BuildConfig.VERSION_NAME);

        //Dropbox
        /*AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        if (getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getString(getString(R.string.pref_key_dropbox), null) != null) {
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
                dropboxHelper backupRestoreHelper = new dropboxHelper(getActivity(), action);
                backupRestoreHelper.execute();

                Snackbar.make(getActivity().findViewById(R.id.fragmentContainer_settings), getString(R.string.pref_toast_cloud_restore), Snackbar.LENGTH_SHORT).show();

                return false;
            }
            }
        });

        //gdrive
        if (getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getString(getString(R.string.pref_key_gdrive), null) != null) {
            gdriveButton.setSummary(R.string.pref_summary_cloud_restore);
            action = "restore";
        }
        gdriveButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!action.equals("restore")) {
                    gdriveHelper gdriveHelper = new gdriveHelper(context, "init");
                    gdriveHelper.gdriveInit();


                    return false;

                } else {
                    //gdriveHelper backupRestoreHelper = new gdriveHelper(getActivity(), action);
                    //backupRestoreHelper.execute();

                    //Snackbar.make(getActivity().findViewById(R.id.fragmentContainer_settings), getString(R.string.pref_toast_cloud_restore), Snackbar.LENGTH_SHORT).show();
                    //TODO
                    return false;
                }
            }
        });*/
    }

    @Override
    public void onStart(){
        super.onStart();



    }

    @Override
    public void onPause() {
        super.onPause();
        //if (mgdriveAPI != null) {
         //   mgdriveAPI.disconnect();
    //    }
    }


    @Override
    public void onResume(){
        super.onResume();
        //Dropbox
        /*if(mDBApi.getSession().authenticationSuccessful()){
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
        }*/
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
           /* case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }

                break;
        */}
    }
}