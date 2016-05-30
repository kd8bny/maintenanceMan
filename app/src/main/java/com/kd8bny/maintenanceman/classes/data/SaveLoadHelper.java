package com.kd8bny.maintenanceman.classes.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.legacy.FleetRosterJSONHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kd8bny on 12/29/15.
 */
public class SaveLoadHelper {
    private static final String TAG = "svLdHlpr";

    public static final int DB_VERSION = 2; //v56 2(v57)

    private BackupRestoreHelper mbackupRestoreHelper;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private static final String FILE_NAME = "fleetRoster.json";
    private static String FILE_LOCATION;
    private Context mContext;

    public SaveLoadHelper(Context context){
        mContext = context;
        FILE_LOCATION = context.getFilesDir() + "/" + FILE_NAME;

        mbackupRestoreHelper = new BackupRestoreHelper();

        sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, 0);
        int oldVersion = sharedPreferences.getInt("fleetRosterDBVersion", -1);
        if (DB_VERSION > oldVersion){
            onUpgrade(oldVersion);
        }
    }

    public Boolean save(ArrayList<Vehicle> l){
        /**
         * Saves objects returns true is successful
         * Auto saves to cloud source
         */
        Gson gson = new Gson();
        String json = gson.toJson(l);
        try {
            FileWriter fileWriter = new FileWriter(new File(FILE_LOCATION));

            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        mbackupRestoreHelper.startAction(mContext, "backup", false);

        return true;
    }

    public ArrayList<Vehicle> load(){
        mbackupRestoreHelper.startAction(mContext, "restore", false);

        File file = new File(FILE_LOCATION);
        Gson gson = new Gson();

        ArrayList<Vehicle> roster = new ArrayList<>();
        String json;

        if (file.isFile() && file.canRead()) {
            try {
                BufferedReader buffReader = new BufferedReader(new FileReader(file));
                StringBuffer stringBuffer = new StringBuffer();

                while ((json = buffReader.readLine()) != null) {
                    stringBuffer.append(json);
                }

                json = stringBuffer.toString();
                roster = gson.fromJson(json, new TypeToken<ArrayList<Vehicle>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return roster;
    }

    private void onUpgrade(int oldVersion){
        switch (oldVersion) {
            case -1:
                File file = new File(FILE_LOCATION);
                if (file.exists()) {
                    //Make new objects //TODO remove by Production version and 95% users
                    FleetRosterJSONHelper fleetRosterJSONHelper = new FleetRosterJSONHelper();
                    HashMap<String, HashMap> oldRoster = fleetRosterJSONHelper.getEntries(mContext);
                    save(fleetRosterJSONHelper.saveToNew(oldRoster));
                }

            case 1:
                ArrayList<Vehicle> temp = load();
                for (int i=0; i<temp.size(); i++) {
                    Vehicle v =temp.get(i);
                    v.setBusiness(false);
                    temp.set(i, v);
                }
                save(temp);
            default:
                Log.e(TAG, "No case for DB upgrade");
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("fleetRosterDBVersion", DB_VERSION);
        editor.apply();
    }
}
