package com.kd8bny.maintenanceman.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.kd8bny.maintenanceman.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class backupRestoreHelper extends AsyncTask<String, Void, String> {
    private static final String TAG = "bckpHlpr";

    private DropboxAPI<AndroidAuthSession> mDBApi;

    private final String FLEETROSTER_FILENAME = "fleetRoster.json";
    private final String VEHICLELOG_FILENAME = "vehicleLog.db";
    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private Context context;
    private String action;

    public backupRestoreHelper(Context context, String action) {
        this.context = context;
        this.action = action;
    }


    @Override
    protected String doInBackground(String... params){
        if(action.equals("restore")) {
            requestRestore();
        }else {
            requestBackup();
        }

        return null;
    }

    public void requestBackup(){
        File fleetRoster = new File(context.getFilesDir() + "/" + FLEETROSTER_FILENAME);
        File vehicleLog = new File(context.getDatabasePath(VEHICLELOG_FILENAME).toString());

        if(context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                .getString(context.getString(R.string.pref_key_dropbox), null) != null) {

            saveDropbox(fleetRoster, vehicleLog);
        }else{
            Log.i(TAG, "No dropbox");
        }
    }

    public void requestRestore(){
        final String APP_KEY = context.getResources().getString(R.string.dropboxKey);
        final String APP_SECRET = context.getResources().getString(R.string.dropboxSecret);

        File fleetRoster = new File(context.getFilesDir() + "/" + FLEETROSTER_FILENAME);
        File vehicleLog = new File(context.getDatabasePath(VEHICLELOG_FILENAME).toString());

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().setOAuth2AccessToken(
                context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                        .getString(context.getString(R.string.pref_key_dropbox), null)); //Never null

        try{
            FileOutputStream outputStream1 = new FileOutputStream(fleetRoster);
            FileOutputStream outputStream2 = new FileOutputStream(vehicleLog);
            DropboxAPI.DropboxFileInfo info1 = mDBApi.getFile("/" + FLEETROSTER_FILENAME, null, outputStream1, null);
            DropboxAPI.DropboxFileInfo info2 = mDBApi.getFile("/" + VEHICLELOG_FILENAME, null, outputStream2, null);

        }catch (IOException | DropboxException e){
            e.printStackTrace();
        }
    }

    public void saveDropbox(File fleetRoster, File vehicleLog){
        final String APP_KEY = context.getResources().getString(R.string.dropboxKey);
        final String APP_SECRET = context.getResources().getString(R.string.dropboxSecret);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().setOAuth2AccessToken(
                context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                        .getString(context.getString(R.string.pref_key_dropbox), null)); //Never null

        try {
            FileInputStream inputStream1 = new FileInputStream(fleetRoster);
            FileInputStream inputStream2 = new FileInputStream(vehicleLog);

            mDBApi.putFileOverwrite("/" + FLEETROSTER_FILENAME, inputStream1, fleetRoster.length(), null);
            mDBApi.putFileOverwrite("/" + VEHICLELOG_FILENAME, inputStream2, vehicleLog.length(), null);

        }catch (IOException | DropboxException e){
            e.printStackTrace();
        }
    }
}
