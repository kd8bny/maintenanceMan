package com.kd8bny.maintenanceman.classes.data;

import android.content.Context;
import android.util.Log;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.interfaces.SyncFinished;
import com.kd8bny.maintenanceman.interfaces.UpdateUI;

public class BackupRestoreHelper {
    private static final String TAG = "bckp_rstr_hlpr";

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private SyncFinished mSyncFinished;
    private DropboxHelper mdropboxHelper;

    public BackupRestoreHelper(){}

    public void startAction(Context context, SyncFinished syncFinished){
        String cloudDefault = context.getString(R.string.pref_cloud_default);
        mSyncFinished = syncFinished;
        String cloudExists = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREF, 0).getString(cloudDefault, "");

        if (!cloudExists.isEmpty()) {
            switch (cloudDefault) {
                case "dropbox":
                    mdropboxHelper = new DropboxHelper(context);
                    mdropboxHelper.listener = mSyncFinished;
                    mdropboxHelper.execute();

                    break;

                case "gdrive":

                    break;

                default:

                    break;
            }
        }else{
            Log.i(TAG, "Cloud source not set up");
        }
    }
}
