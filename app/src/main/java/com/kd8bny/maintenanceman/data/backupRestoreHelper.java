package com.kd8bny.maintenanceman.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kd8bny.maintenanceman.R;

public class backupRestoreHelper {
    private static final String TAG = "bckphlpr";

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    public void startAction(Context context, String action){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String cloudDefault = context.getString(R.string.pref_cloud_default);//sharedPreferences.getString(context.getString(R.string.pref_cloud_default), "");

        if (!cloudDefault.isEmpty()){
        switch (cloudDefault) {
            case "dropbox":
                dropboxHelper mdropboxHelper = new dropboxHelper(context, action);
                mdropboxHelper.execute();

                break;

            case "gdrive":

                break;

            default:

                break;
            }
        }
    }
}
