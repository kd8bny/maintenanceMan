package com.kd8bny.maintenanceman.data;

import android.content.Context;
import android.util.Log;

import com.kd8bny.maintenanceman.R;

public class backupRestoreHelper {
    private static final String TAG = "bckp_rstr_hlpr";

    public void startAction(Context context, String action, Boolean force){
        String cloudDefault = context.getString(R.string.pref_cloud_default);

        if (!cloudDefault.isEmpty()){
        switch (cloudDefault) {
            case "dropbox":
                dropboxHelper mdropboxHelper = new dropboxHelper(context, action, force);
                mdropboxHelper.execute();

                break;

            case "gdrive":

                break;

            default:
                Log.i(TAG, "Cloud source not set up");

                break;
            }
        }
    }
}
