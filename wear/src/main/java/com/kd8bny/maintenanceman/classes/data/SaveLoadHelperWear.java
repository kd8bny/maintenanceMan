package com.kd8bny.maintenanceman.classes.data;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
* Slightly modified version of the helper for wear specifically
 **/
public class SaveLoadHelperWear {
    private static final String TAG = "svLdHlprWr";

    private static  String ROOT_PATH;
    private static final String WEAR_FILE_PATH = "/files";
    private static final String FILE_NAME = "/fleetRoster.json";
    private static String FILE_LOCATION;

    public SaveLoadHelperWear(){
        ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        FILE_LOCATION = ROOT_PATH + WEAR_FILE_PATH + FILE_NAME;
        File rootDir = new File(ROOT_PATH + WEAR_FILE_PATH);

        if (!rootDir.exists()){
            try {
                rootDir.mkdirs();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Boolean save(String json){
        /**
         * Saves objects returns true is successful
         * Auto saves to cloud source
         */

        try {
            FileWriter fileWriter = new FileWriter(new File(FILE_LOCATION));

            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return true;
    }

    public ArrayList<Vehicle> load(){
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
}
