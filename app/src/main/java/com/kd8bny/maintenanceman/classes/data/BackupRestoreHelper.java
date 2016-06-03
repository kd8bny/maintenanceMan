package com.kd8bny.maintenanceman.classes.data;

import android.content.Context;
import android.util.Log;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.interfaces.AsyncResponse;
import com.kd8bny.maintenanceman.interfaces.UpdateUI;

public class BackupRestoreHelper implements AsyncResponse {
    private static final String TAG = "bckp_rstr_hlpr";

    public UpdateUI updateUI = null;
    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private DropboxHelper mdropboxHelper;

    public BackupRestoreHelper(){}

    public void startAction(Context context, String action, Boolean force){
        String cloudDefault = context.getString(R.string.pref_cloud_default);
        String cloudExists = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREF, 0).getString(cloudDefault, "");

        if (!cloudExists.isEmpty()) {
            switch (cloudDefault) {
                case "dropbox":
                    mdropboxHelper = new DropboxHelper(context);
                    mdropboxHelper.listener = this;
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

    public void onDownloadComplete(Boolean isComplete){
        if (updateUI != null) {
            updateUI.onUpdate(isComplete);
        }
    }
}
