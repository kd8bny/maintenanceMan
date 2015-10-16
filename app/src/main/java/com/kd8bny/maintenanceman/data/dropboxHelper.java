package com.kd8bny.maintenanceman.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.interfaces.AsyncResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class dropboxHelper extends AsyncTask<String, Void, String> {
    private static final String TAG = "dbxHlpr";

    public AsyncResponse listener = null;
    private DropboxAPI<AndroidAuthSession> mDBApi;

    private final String FLEETROSTER_FILENAME = "fleetRoster.json";
    private final String VEHICLELOG_FILENAME = "vehicleLog.db";
    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private Context context;
    private String action;
    private Boolean force;

    public dropboxHelper(Context context, String action, Boolean force) {
        this.context = context;
        this.action = action;
        this.force = force;
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

    @Override
    protected void onPostExecute(String results){
        listener.onDownloadComplete(results);
    }

    public boolean actionRequired(Date localFileDate, String remoteFile){
        try {
            SimpleDateFormat dbxDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date remoteFileDate = dbxDateFormat.parse(remoteFile);

            if (localFileDate.after(remoteFileDate) & action.equals("backup")){
                Log.i(TAG, "Replacing remote " + localFileDate + " >> " + remoteFileDate);

                return true;
            }
            if (localFileDate.before(remoteFileDate) & action.equals("restore")){
                Log.i(TAG, "Replacing local " + remoteFileDate + " >> " + localFileDate);

                return true;
            }

        }catch (ParseException e){
            e.printStackTrace();
        }

        return false;
    }

    public void requestBackup(){
        if(context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                .getString(context.getString(R.string.pref_key_dropbox), null) != null) {

            final String APP_KEY = context.getResources().getString(R.string.dropboxKey);
            final String APP_SECRET = context.getResources().getString(R.string.dropboxSecret);
            final File fleetRoster = new File(context.getFilesDir() + "/" + FLEETROSTER_FILENAME);
            final File vehicleLog = new File(context.getDatabasePath(VEHICLELOG_FILENAME).toString());
            final FileInputStream inputStream1;
            final FileInputStream inputStream2;
            AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
            AndroidAuthSession session = new AndroidAuthSession(appKeys);
            mDBApi = new DropboxAPI<AndroidAuthSession>(session);
            mDBApi.getSession().setOAuth2AccessToken(
                    context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                            .getString(context.getString(R.string.pref_key_dropbox), null)); //Never null

            if (force) {
                forceBackup(mDBApi, fleetRoster, vehicleLog);
            } else {
                try {
                    Date localFleetRoster = new Date(fleetRoster.lastModified());
                    Date localVehicleLog = new Date(vehicleLog.lastModified());
                    String remoteFleetRoster = mDBApi.metadata("/" + FLEETROSTER_FILENAME, 1, null, false, null).modified;
                    String remoteVehicleLog = mDBApi.metadata("/" + VEHICLELOG_FILENAME, 1, null, false, null).modified;

                    if (actionRequired(localFleetRoster, remoteFleetRoster)) {
                        inputStream1 = new FileInputStream(fleetRoster);
                        mDBApi.putFileOverwrite("/" + FLEETROSTER_FILENAME, inputStream1, fleetRoster.length(), null);
                    }
                    if (actionRequired(localVehicleLog, remoteVehicleLog)) {
                        inputStream2 = new FileInputStream(vehicleLog);
                        mDBApi.putFileOverwrite("/" + VEHICLELOG_FILENAME, inputStream2, vehicleLog.length(), null);
                    }
                } catch (IOException | DropboxException e) {
                    e.printStackTrace();
                    forceBackup(mDBApi, fleetRoster, vehicleLog);
                }
            }
        }else{
            Log.i(TAG, "No dropbox");
        }
    }

    public void requestRestore(){
        final String APP_KEY = context.getResources().getString(R.string.dropboxKey);
        final String APP_SECRET = context.getResources().getString(R.string.dropboxSecret);
        final File fleetRoster = new File(context.getFilesDir() + "/" + FLEETROSTER_FILENAME);
        final File vehicleLog = new File(context.getDatabasePath(VEHICLELOG_FILENAME).toString());
        final String dropboxTokenNew = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                .getString(context.getString(R.string.pref_key_dropbox), null);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().setOAuth2AccessToken(
                context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                        .getString(context.getString(R.string.pref_key_dropbox), null)); //Never null

        if (force) {
            forceRestore(mDBApi, fleetRoster, vehicleLog);
        } else {
            try {
                Date localFleetRoster = new Date(fleetRoster.lastModified());
                Date localVehicleLog = new Date(vehicleLog.lastModified());
                String remoteFleetRoster = mDBApi.metadata("/" + FLEETROSTER_FILENAME, 1, null, false, null).modified;
                String remoteVehicleLog = mDBApi.metadata("/" + VEHICLELOG_FILENAME, 1, null, false, null).modified;

                if (actionRequired(localFleetRoster, remoteFleetRoster)) {
                    FileOutputStream outputStream1 = new FileOutputStream(fleetRoster);
                    DropboxAPI.DropboxFileInfo info1 = mDBApi.getFile("/" + FLEETROSTER_FILENAME, null, outputStream1, null);
                }
                if (actionRequired(localVehicleLog, remoteVehicleLog)) {
                    FileOutputStream outputStream2 = new FileOutputStream(vehicleLog);
                    DropboxAPI.DropboxFileInfo info2 = mDBApi.getFile("/" + VEHICLELOG_FILENAME, null, outputStream2, null);
                }
            } catch (IOException | DropboxException e) {
                e.printStackTrace();
            }
        }
    }

    public void forceBackup(DropboxAPI<AndroidAuthSession> mDBApi, File fleetRoster, File vehicleLog) {
        try {
            FileInputStream inputStream1 = new FileInputStream(fleetRoster);
            FileInputStream inputStream2 = new FileInputStream(vehicleLog);
            mDBApi.putFileOverwrite("/" + FLEETROSTER_FILENAME, inputStream1, fleetRoster.length(), null);
            mDBApi.putFileOverwrite("/" + VEHICLELOG_FILENAME, inputStream2, vehicleLog.length(), null);
            Log.i(TAG, "Forced backup");
        } catch (IOException | DropboxException e) {
            e.printStackTrace();
        }
    }

    public void forceRestore(DropboxAPI<AndroidAuthSession> mDBApi, File fleetRoster, File vehicleLog) {
        vehicleLogDBHelper mvehicleLogDBHelper = new vehicleLogDBHelper(context);
        mvehicleLogDBHelper.createDatabase(context);
        try {
            FileOutputStream outputStream1 = new FileOutputStream(fleetRoster.getPath());
            FileOutputStream outputStream2 = new FileOutputStream(vehicleLog.getAbsolutePath());
            mDBApi.getFile("/" + FLEETROSTER_FILENAME, null, outputStream1, null);
            mDBApi.getFile("/" + VEHICLELOG_FILENAME, null, outputStream2, null);
            Log.i(TAG, "Forced restore");
        } catch (IOException | DropboxException e) {
            e.printStackTrace();
        }
    }
}
