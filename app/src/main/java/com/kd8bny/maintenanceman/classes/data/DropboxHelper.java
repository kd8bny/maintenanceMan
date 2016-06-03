package com.kd8bny.maintenanceman.classes.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
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

/**
 * rv3
 **/

public class DropboxHelper extends AsyncTask<String, Void, String> {
    private static final String TAG = "dbxHlpr";

    public AsyncResponse listener = null;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    private final String FLEETROSTER = "fleetRoster.json";
    private final String VEHICLELOG = "vehicleLog.db";
    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private final String FLEETROSTER_MD5 = "d751713988987e9331980363e24189ce";
    private final String VEHICLELOG_MD5 = "db9b2415f74b936d64c4a5c82aef9e13";

    private Boolean filesUpdated = false;

    public DropboxHelper(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String APP_KEY =  mContext.getResources().getString(R.string.dropboxKey);
        String APP_SECRET = mContext.getResources().getString(R.string.dropboxSecret);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<>(session);
        mDBApi.getSession().setOAuth2AccessToken(
                mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                        .getString(mContext.getString(R.string.pref_key_dropbox), ""));
    }

    @Override
    protected void onPostExecute(String results){
        listener.onDownloadComplete(filesUpdated); //Fix this at fragment level
    }

    @Override
    protected String doInBackground(String... params){
        String frHash = sharedPreferences.getString(mContext.getString(R.string.fleet_roster_hash), FLEETROSTER_MD5);

        try {
            File fleetRoster = new File(mContext.getFilesDir() + "/" + FLEETROSTER);
            HashCode hc = Files.hash(fleetRoster, Hashing.md5());

            if (hc.toString().equals(FLEETROSTER_MD5)){
                download(FLEETROSTER, fleetRoster);
            }else{
                sync(fleetRoster, hc.toString(), frHash, FLEETROSTER);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(mContext.getString(R.string.fleet_roster_hash), hc.toString());
                editor.apply();
            }
        }catch (IOException e){}

        String vlHash = sharedPreferences.getString(mContext.getString(R.string.vehicle_log_hash), VEHICLELOG_MD5);

        try {
            File vehicleLog = new File(mContext.getDatabasePath(VEHICLELOG).toString());
            HashCode hc = Files.hash(vehicleLog, Hashing.md5());

            if (hc.toString().equals(VEHICLELOG_MD5)){
                VehicleLogDBHelper.getInstance(mContext).getReadableDatabase();
                vehicleLog = new File(mContext.getDatabasePath(VEHICLELOG).toString());
                download(VEHICLELOG, vehicleLog);
            }else{
                sync(vehicleLog , hc.toString(), vlHash, VEHICLELOG);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(mContext.getString(R.string.vehicle_log_hash), hc.toString());
                editor.apply();
            }
        }catch (IOException e){}

        return null;
    }

    private boolean sync(File local, String hash, String oldHash, String REMOTE){
        try {
            SimpleDateFormat dbxDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date remoteDate = dbxDateFormat.parse(mDBApi.metadata("/" + REMOTE, 1, null, false, null).modified);
            Date localDate = new Date(local.lastModified());
            long timeDiff = Math.abs(remoteDate.getTime() - localDate.getTime());

            if (!hash.equals(oldHash)) {
                if (timeDiff > 5000) {
                    if (localDate.before(remoteDate)) {
                        download(REMOTE, local);
                        Log.v(TAG, "Replacing local " + local.getName() + remoteDate + " >> " + localDate);
                    } else {
                        upload(local, REMOTE);
                        Log.v(TAG, "Replacing remote " + local.getName() + localDate + " >> " + remoteDate);
                    }

                    return true;
                }
            }
        }catch (ParseException | DropboxException e){
            e.printStackTrace();
        }

        return false;
    }

    private Boolean upload(File local, String REMOTE){
        /**
         * Returns true if successful
         **/
        try {
            FileInputStream fis = new FileInputStream(local);
            mDBApi.putFileOverwrite("/" + REMOTE, fis, local.length(), null);
        } catch (IOException | DropboxException e) {
            e.printStackTrace(); //TODO log dbx
            return false;
        }

        return true;
    }

    private Boolean download(String REMOTE, File local){
        try {
            FileOutputStream fos = new FileOutputStream(local);
            DropboxAPI.DropboxFileInfo ifInfo = mDBApi.getFile("/" + REMOTE, null, fos, null);
        } catch (IOException | DropboxException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}