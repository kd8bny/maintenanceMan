package com.kd8bny.maintenanceman.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kd8bny.maintenanceman.R;

public class backupRestoreHelper {
    private static final String TAG = "bckp_rstr_hlpr";
    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    public void startAction(Context context, String action, Boolean force){
        String cloudDefault = context.getString(R.string.pref_cloud_default);
        String cloudExists = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREF, 0).getString(cloudDefault, "");

        if (!cloudExists.isEmpty()) {
            switch (cloudDefault) {
                case "dropbox":
                    dropboxHelper mdropboxHelper = new dropboxHelper(context, action, force);
                    mdropboxHelper.execute();

                    break;

                case "gdrive":

                    break;

                default:

                    break;
            }
            Log.i(TAG, "Cloud source not set up");
        }
    }
}
