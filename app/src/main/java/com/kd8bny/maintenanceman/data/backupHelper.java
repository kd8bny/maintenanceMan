/*package com.kd8bny.maintenanceman.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.kd8bny.maintenanceman.R;

/**
 * Created by kd8bny on 8/16/15.

public class backupHelper {
    private static final String TAG = "bckphlpr";

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    public void backupHelper(Context context, String action){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String cloudDefault = sharedPreferences.getString(context.getString(R.string.pref_cloud_default), null);

        switch (cloudDefault){
            case "dropbox":
                dropboxHelper dropboxHelper = new dropboxHelper(context, action);

                break;
            case "gdrive":

                break;

            default:

                break;

        }
    }
}*/
