package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class fleetRosterJSONHelper {
    private static final String TAG = "fltRstrJSONHlpr";

    public static final String JSON_NAME = "fleetRoster.json";

    public fleetRosterJSONHelper(){

    }

    public String openJSON(Context context) {
        File file = new File(context.getFilesDir() + "/" + JSON_NAME);

        if (file.isFile() && file.canRead()) {
            try {
                BufferedReader buffReader = new BufferedReader(new FileReader(file));

                StringBuffer stringBuffer = new StringBuffer();
                String json = null;
                while ((json = buffReader.readLine()) != null) {
                    stringBuffer.append(json);
                }

                json = stringBuffer.toString();

                return json;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void saveEntry(Context context, String refID, ArrayList<ArrayList> vehicleDataGEN, ArrayList<ArrayList> vehicleDataENG,
                          ArrayList<ArrayList> vehicleDataPWR, ArrayList<ArrayList> vehicleDataOTHER) {

        String json = openJSON(context);

        if (refID == null) {
            refID = UUID.randomUUID().toString();
        }

        JSONObject vehicle = new JSONObject();
        JSONObject gen = new JSONObject();
        JSONObject eng = new JSONObject();
        JSONObject pwr = new JSONObject();
        JSONObject other = new JSONObject();

        try{
            for (int i = 0; i < vehicleDataGEN.size(); i++) {
                ArrayList<String> tempData = new ArrayList<String>(vehicleDataGEN.get(i));
                gen.put(tempData.get(0), tempData.get(1));
            }
            for (int i = 0; i < vehicleDataENG.size(); i++) {
                ArrayList<String> tempData = new ArrayList<String>(vehicleDataENG.get(i));
                eng.put(tempData.get(0), tempData.get(1));
            }
            for (int i = 0; i < vehicleDataPWR.size(); i++) {
                ArrayList<String> tempData = new ArrayList<String>(vehicleDataPWR.get(i));
                pwr.put(tempData.get(0), tempData.get(1));
            }
            for (int i = 0; i < vehicleDataOTHER.size(); i++) {
                ArrayList<String> tempData = new ArrayList<String>(vehicleDataOTHER.get(i));
                other.put(tempData.get(0), tempData.get(1));
            }

            vehicle.put("gen", gen);
            vehicle.put("eng", eng);
            vehicle.put("pwr", pwr);
            vehicle.put("other", other);

            if(json != null) {
                JSONObject roster = new JSONObject(json);
                roster.put(refID, vehicle);

                File file = new File(context.getFilesDir() + "/" + JSON_NAME);
                FileWriter fileWriter = new FileWriter(file); //TODO write class

                fileWriter.write(roster.toString());
                fileWriter.flush();
                fileWriter.close();
            }else{
                File file = new File(context.getFilesDir() + "/" + JSON_NAME);

                JSONObject roster = new JSONObject();
                roster.put(refID, vehicle);

                FileWriter fileWriter = new FileWriter(file);

                fileWriter.write(roster.toString());
                fileWriter.flush();
                fileWriter.close();
            }
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }

    public HashMap<String, HashMap> getEntries(Context context){
        String json = openJSON(context);

        HashMap<String, HashMap> vehicleDataAll = new HashMap<>();

        try {
            if (json != null) {
                JSONObject roster = new JSONObject(json);
                JSONArray vehicles = roster.names(); //TODO use object.keys() to iterate

                for (int i = 0; i < vehicles.length(); i++) {
                    JSONObject vehicle = roster.getJSONObject(vehicles.getString(i));
                    JSONArray categories = vehicle.names();
                    for (int j = 0; j < categories.length(); j++) {
                        JSONObject category = vehicle.getJSONObject(categories.getString(j));
                        JSONArray specs = category.names();

                        HashMap<String, String> tempSpecMap = new HashMap<>();
                        HashMap<String, HashMap> vehicleData = new HashMap<>();
                        if(specs != null) {
                            for (int k = 0; k < specs.length(); k++) {
                                tempSpecMap.put(specs.getString(k), category.getString(specs.getString(k)));
                            }
                            switch (categories.getString(j)) {
                                case "gen":
                                    vehicleData.put("gen", tempSpecMap);
                                    break;

                                case "eng":
                                    vehicleData.put("eng", tempSpecMap);
                                    break;

                                case "pwr":
                                    vehicleData.put("pwr", tempSpecMap);
                                    break;

                                case "other":
                                    vehicleData.put("other", tempSpecMap);
                                    break;

                                default:
                                    break;
                            }
                            vehicleDataAll.put(vehicles.getString(i), vehicleData);
                        }
                    }
                }
            }else{
                vehicleDataAll.put(null, null);
            }
            }catch(JSONException e){
                e.printStackTrace();
            }

            return vehicleDataAll;
    }

    public void deleteEntry(Context context, String refID){

        String json = openJSON(context);

        try {
            JSONObject roster = new JSONObject(json);
            roster.remove(refID);

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
