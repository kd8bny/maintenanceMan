package com.kd8bny.maintenanceman.fragments;

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
import com.kd8bny.maintenanceman.classes.data.BackupRestoreHelper;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;

import java.io.File;


public class fragment_settings_dbx extends PreferenceFragment{
    private static final String TAG = "frgmnt_sttngs_dbx";

    private DropboxAPI<AndroidAuthSession> mDBApi;

    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private static final String FILE_NAME = "fleetRoster.json";
    private Context context;

    public fragment_settings_dbx() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_dbx);

        context = getActivity().getApplicationContext();
        final Preference setupButton = findPreference(getString(R.string.pref_title_cloud_init));
        final Preference backupButton = findPreference(getString(R.string.pref_title_cloud_backup));
        final Preference restoreButton = findPreference(getString(R.string.pref_title_cloud_restore));
        final String APP_KEY = context.getResources().getString(R.string.dropboxKey);
        final String APP_SECRET = context.getResources().getString(R.string.dropboxSecret);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);

        mDBApi = new DropboxAPI<>(session);

        if (getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getString(getString(R.string.pref_key_dropbox), null) != null) {
        }

        setupButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mDBApi.getSession().startOAuth2Authentication(getActivity());

                return true;
            }
        });
        backupButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                BackupRestoreHelper mbackupRestoreHelper = new BackupRestoreHelper();
                mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "backup", true);

                Snackbar.make(getActivity().findViewById(R.id.fragmentContainer_settings), getString(R.string.pref_toast_dropbox_backup), Snackbar.LENGTH_SHORT).show();

                return true;
            }
        });
        restoreButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                BackupRestoreHelper mbackupRestoreHelper = new BackupRestoreHelper();
                mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "restore", true);

                Snackbar.make(getActivity().findViewById(R.id.fragmentContainer_settings), getString(R.string.pref_toast_dropbox_restore), Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mDBApi.getSession().authenticationSuccessful()){
            Log.d(TAG, "done auth");
            try{
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString(getString(R.string.pref_key_dropbox), accessToken);
                editor.apply();


                /*File file = new File(context.getFilesDir() + "/" + FILE_NAME);
                if ((new SaveLoadHelper(getActivity().getApplicationContext()).load()).isEmpty()){
                    BackupRestoreHelper mbackupRestoreHelper = new BackupRestoreHelper();
                    mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "restore", true);
                }else{
                    BackupRestoreHelper mbackupRestoreHelper = new BackupRestoreHelper();
                    mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "backup", true);
                }*/

                /*PackageManager packageManager = context.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(componentName);
                context.startActivity(mainIntent);
                System.exit(0);*/

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "DROPBOX: Error authenticating", e);
            }
        }
    }
}