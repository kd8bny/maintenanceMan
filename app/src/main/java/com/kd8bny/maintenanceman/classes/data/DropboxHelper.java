package com.kd8bny.maintenanceman.classes.data;

import android.content.Context;
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
import com.kd8bny.maintenanceman.interfaces.SyncFinished;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * v3
 **/

public class DropboxHelper extends AsyncTask<String, Void, String> {
    private static final String TAG = "dbxHlpr";

    public SyncFinished listener = null;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private Context mContext;

    private static final int sTimeDifference = 5000; //ms
    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private static final String FLEETROSTER = "/fleetRoster.json";
    private static final String VEHICLELOG = "/vehicleLog.db";
    private static final String FLEETROSTER_MD5 = "/fleetRoster.md5";
    private static final String VEHICLELOG_MD5 = "/vehicleLog.md5";
    private static final String FLEETROSTER_EMPTY_MD5 = "d751713988987e9331980363e24189ce";
    private static final String VEHICLELOG_EMPTY_MD5 = "d751713988987e9331980363e24189ce";
    private Boolean filesUpdated = false;

    public DropboxHelper(Context context) {
        mContext = context;
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
        listener.onDownloadComplete(filesUpdated);
    }

    @Override
    protected String doInBackground(String... params){
        try {
            File fleetRoster = new File(mContext.getFilesDir() + FLEETROSTER);
            HashCode hc = Files.hash(fleetRoster, Hashing.md5());

            if (hc.toString().equals(FLEETROSTER_EMPTY_MD5)){
                download(FLEETROSTER, fleetRoster);
            }else{
                download(FLEETROSTER_MD5, new File(mContext.getFilesDir() + FLEETROSTER_MD5));
                sync(fleetRoster, hc.toString(), FLEETROSTER, FLEETROSTER_MD5);
            }
        }catch (IOException e){}

        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        File vehicleLog = new File(vehicleLogDBHelper.getReadableDatabase().getPath());

        String hash = vehicleLogDBHelper.getTablesHash();

        if (FLEETROSTER_EMPTY_MD5.equals(hash)){
            download(VEHICLELOG, vehicleLog);
        }else{
            download(VEHICLELOG_MD5, new File(mContext.getFilesDir() + VEHICLELOG_MD5));
            sync(vehicleLog , hash, VEHICLELOG, VEHICLELOG_MD5);
        }

        return null;
    }

    private void sync(File local, String localHash, String REMOTE, String remoteHash){
        try {
            SimpleDateFormat dbxDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date remoteDate = dbxDateFormat.parse(mDBApi.metadata(REMOTE, 1, null, false, null).modified);
            Date localDate = new Date(local.lastModified());
            long timeDiff = Math.abs(remoteDate.getTime() - localDate.getTime());

            if (!localHash.equals(readMD5(remoteHash))) {
                if (timeDiff > sTimeDifference) {
                    if (localDate.before(remoteDate)) {
                        download(REMOTE, local);
                        filesUpdated = true;
                        Log.v(TAG, "Replacing local " + local.getName() + remoteDate + " >> " + localDate);
                    } else {
                        upload(local, REMOTE);
                        upload(writeMD5(mContext.getFilesDir() + remoteHash, localHash), remoteHash);
                        Log.v(TAG, "Replacing remote " + local.getName() + localDate + " >> " + remoteDate);
                    }
                }
            }
        }catch (ParseException | DropboxException e){
            e.printStackTrace();
        }
    }

    private Boolean upload(File local, String REMOTE){
        /**
         * Returns true if successful
         **/
        try {
            FileInputStream fis = new FileInputStream(local);
            mDBApi.putFileOverwrite(REMOTE, fis, local.length(), null);
        } catch (IOException | DropboxException e) {
            e.printStackTrace(); //TODO log dbx
            return false;
        }

        return true;
    }

    private Boolean download(String REMOTE, File local){
        try {
            FileOutputStream fos = new FileOutputStream(local);
            DropboxAPI.DropboxFileInfo ifInfo = mDBApi.getFile(REMOTE, null, fos, null);
        } catch (IOException | DropboxException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private File writeMD5(String name, String md5){ //TODO move to utils
        File file = new File(name);
        try {
            FileWriter fileWriter = new FileWriter(file, false); //no append

            fileWriter.write(md5);
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return file;
    }

    private String readMD5(String name){
        File file = new File(mContext.getFilesDir() + name);
        String md5 = "";
        if (file.isFile() && file.canRead()) {
            try {
                BufferedReader buffReader = new BufferedReader(new FileReader(file));
                StringBuffer stringBuffer = new StringBuffer();

                while ((md5 = buffReader.readLine()) != null) {
                    stringBuffer.append(md5);
                }
                md5 = stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return md5;
    }
}