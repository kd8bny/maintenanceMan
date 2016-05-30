package com.kd8bny.maintenanceman.classes.data;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DropboxHelper extends AsyncTask<String, Void, String> {
    private static final String TAG = "dbxHlpr";

    public AsyncResponse listener = null;
    private DropboxAPI<AndroidAuthSession> mDBApi;

    private final String FLEETROSTER_FILENAME = "fleetRoster.json";
    private final String VEHICLELOG_FILENAME = "vehicleLog.db";
    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private Context mContext;
    private String mAction;
    private Boolean mForce;
    private Boolean filesUpdated = false;

    public DropboxHelper(Context context, String action, Boolean force) {
        mContext = context;
        mAction = action;
        mForce = force;
    }

    @Override
    protected String doInBackground(String... params){
        if(mAction.equals("restore")) {
            requestRestore();
        }else {
            requestBackup();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String results){
        listener.onDownloadComplete(filesUpdated);
    }

    public boolean actionRequired(Date localFileDate, String remoteFile){
        try {
            SimpleDateFormat dbxDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date remoteFileDate = dbxDateFormat.parse(remoteFile);
            long diff = Math.abs(remoteFileDate.getTime() - localFileDate.getTime());

            if (diff > 5000) {
                if (localFileDate.after(remoteFileDate) & mAction.equals("backup")) {
                    Log.i(TAG, "Replacing remote " + localFileDate + " >> " + remoteFileDate);

                    return true;
                }
                if (localFileDate.before(remoteFileDate) & mAction.equals("restore")) {
                    Log.i(TAG, "Replacing local " + remoteFileDate + " >> " + localFileDate);

                    return true;
                }
            }

        }catch (ParseException e){
            e.printStackTrace();
        }

        return false;
    }

    public void requestBackup(){
        if(mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                .getString(mContext.getString(R.string.pref_key_dropbox), null) != null) {

            final String APP_KEY = mContext.getResources().getString(R.string.dropboxKey);
            final String APP_SECRET = mContext.getResources().getString(R.string.dropboxSecret);
            final File fleetRoster = new File(mContext.getFilesDir() + "/" + FLEETROSTER_FILENAME);
            final File vehicleLog = new File(mContext.getDatabasePath(VEHICLELOG_FILENAME).toString());
            final FileInputStream inputStream1;
            final FileInputStream inputStream2;
            AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
            AndroidAuthSession session = new AndroidAuthSession(appKeys);
            mDBApi = new DropboxAPI<AndroidAuthSession>(session);
            mDBApi.getSession().setOAuth2AccessToken(
                    mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                            .getString(mContext.getString(R.string.pref_key_dropbox), ""));

            if (mForce) {
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
        final String APP_KEY = mContext.getResources().getString(R.string.dropboxKey);
        final String APP_SECRET = mContext.getResources().getString(R.string.dropboxSecret);
        final File fleetRoster = new File(mContext.getFilesDir() + "/" + FLEETROSTER_FILENAME);
        final File vehicleLog = new File(mContext.getDatabasePath(VEHICLELOG_FILENAME).toString());
        Log.d(TAG, mContext.getDatabasePath(VEHICLELOG_FILENAME).toString());
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().setOAuth2AccessToken(
                mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                        .getString(mContext.getString(R.string.pref_key_dropbox), ""));

        if (mForce) {
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
                    filesUpdated = true;
                }
                if (actionRequired(localVehicleLog, remoteVehicleLog)) {
                    try {
                        FileOutputStream outputStream2 = new FileOutputStream(vehicleLog);
                        DropboxAPI.DropboxFileInfo info2 = mDBApi.getFile("/" + VEHICLELOG_FILENAME, null, outputStream2, null);
                    }catch (FileNotFoundException e) {
                        VehicleLogDBHelper.getInstance(mContext).getReadableDatabase();
                        FileOutputStream outputStream2 = new FileOutputStream(vehicleLog);
                        DropboxAPI.DropboxFileInfo info2 = mDBApi.getFile("/" + VEHICLELOG_FILENAME, null, outputStream2, null);
                    }
                }
            } catch (IOException | DropboxException e){
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
        try {
            FileOutputStream outputStream1 = new FileOutputStream(fleetRoster.getPath());
            FileOutputStream outputStream2 = new FileOutputStream(vehicleLog.getAbsolutePath());
            mDBApi.getFile("/" + FLEETROSTER_FILENAME, null, outputStream1, null);
            mDBApi.getFile("/" + VEHICLELOG_FILENAME, null, outputStream2, null);
            filesUpdated = true;
            Log.i(TAG, "Forced restore");
        } catch (IOException | DropboxException e) {
            e.printStackTrace();
        }
    }
}
