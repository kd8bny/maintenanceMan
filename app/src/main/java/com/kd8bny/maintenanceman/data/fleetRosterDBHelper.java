package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class fleetRosterDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "fleetRosterDBHelper";

    public static final int DB_VERSION = 2; //v25
    public static final String DB_NAME = "fleetRoster.db";
    SQLiteDatabase fleetRosterDB = null;

    public static final String TABLE_ROSTER = "grandFleetRoster";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ROSTER_refID = "refID";
    public static final String COLUMN_ROSTER_TYPE = "type";
    public static final String COLUMN_ROSTER_YEAR = "year";
    public static final String COLUMN_ROSTER_MAKE = "make";
    public static final String COLUMN_ROSTER_MODEL = "model";
    public static final String COLUMN_ROSTER_ENGINE = "engine";

    public static final String COLUMN_ROSTER_PLATE = "plate";
    public static final String COLUMN_ROSTER_OIL_FILTER = "oilFilter";
    public static final String COLUMN_ROSTER_OIL_WEIGHT = "oilWeight";
    public static final String COLUMN_ROSTER_TIRE_SUMMER = "tireSummer";
    public static final String COLUMN_ROSTER_TIRE_WINTER = "tireWinter";

    public static final String DATABASE_CREATE = "create table "
        + TABLE_ROSTER
        + "("
        + COLUMN_ID + " integer primary key autoincrement, "
        + COLUMN_ROSTER_refID + " text not null, "
        + COLUMN_ROSTER_TYPE + " text not null"
        + COLUMN_ROSTER_YEAR + " text not null, "
        + COLUMN_ROSTER_MAKE + " text not null, "
        + COLUMN_ROSTER_MODEL + " text not null, "
        + COLUMN_ROSTER_ENGINE + " text not null, "
        + COLUMN_ROSTER_PLATE + " text not null, "
        + COLUMN_ROSTER_OIL_FILTER + " text not null, "
        + COLUMN_ROSTER_OIL_WEIGHT + " text not null, "
        + COLUMN_ROSTER_TIRE_SUMMER + " text not null, "
        + COLUMN_ROSTER_TIRE_WINTER + " text not null"
        + ");";

    public fleetRosterDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        fleetRosterDB = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(fleetRosterDBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " +
                newVersion + ", which will destroy all old data");
        db.beginTransaction();
        String TABLE_ROSTER_NEW = TABLE_ROSTER + "_new";
        try {
            Log.d(TAG,"trying");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ROSTER_NEW + " AS SELECT * FROM " + TABLE_ROSTER);
            db.execSQL("ALTER TABLE " + TABLE_ROSTER_NEW + " ADD COLUMN " + COLUMN_ROSTER_TYPE + " text not null");

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROSTER);
            db.execSQL("ALTER TABLE " + TABLE_ROSTER_NEW + " RENAME TO " + TABLE_ROSTER);

            onCreate(db);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG,e.toString());
            Log.e(TAG, "Error updating db");

        }finally {
            db.setVersion(DB_VERSION);
            db.endTransaction();
        }
    }

    public void createDatabase(Context context){
        try {
            fleetRosterDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

            fleetRosterDB.execSQL(DATABASE_CREATE);
            fleetRosterDB.setVersion(DB_VERSION);

            Log.i(TAG,"fleetRoster created");
        }catch(Exception e){
            Log.e(TAG,"Error creating db");
        }finally {
            if(fleetRosterDB.getVersion() < DB_VERSION){
                onUpgrade(fleetRosterDB, fleetRosterDB.getVersion(), DB_VERSION);
                Log.i(TAG,(fleetRosterDB.getVersion())+"");
            }
        }
    }

    public void saveEntry(Context context, HashMap vehicleInfo) {
        Log.d(TAG, "Saving entry");

        File database = context.getDatabasePath("fleetRoster.db");
        if (!database.exists()) {
            this.createDatabase(context);
        }

        else {
            if (vehicleInfo.get("refID") == null) {
                vehicleInfo.put("refID", (UUID.randomUUID()).toString());
            }
            fleetRosterDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

            fleetRosterDB.execSQL("INSERT INTO grandFleetRoster (refID, make, model, year, engine, plate, oilFilter, oilWeight, tireSummer, tireWinter) " +
                    "VALUES ('"
                    + vehicleInfo.get("refID") + "','"
                    + vehicleInfo.get("make") + "','"
                    + vehicleInfo.get("model") + "','"
                    + vehicleInfo.get("year") + "','"
                    + vehicleInfo.get("engine") + "','"
                    + vehicleInfo.get("plate") + "','"
                    + vehicleInfo.get("oilFilter") + "','"
                    + vehicleInfo.get("oilWeight") + "','"
                    + vehicleInfo.get("tireSummer") + "','"
                    + vehicleInfo.get("tireWinter") + "');");
        }
    }

    public ArrayList<HashMap> getEntries(Context context){
        createDatabase(context);
        Cursor cursor = fleetRosterDB.rawQuery("SELECT * FROM grandFleetRoster", null);

        int refIDCol = cursor.getColumnIndex("refID");
        int yearCol = cursor.getColumnIndex("year");
        int makeCol = cursor.getColumnIndex("make");
        int modelCol = cursor.getColumnIndex("model");
        int engineCol = cursor.getColumnIndex("engine");
        int plateCol = cursor.getColumnIndex("plate");
        int oilFilterCol = cursor.getColumnIndex("oilFilter");
        int oilWeightCol = cursor.getColumnIndex("oilWeight");
        int tireSummerCol = cursor.getColumnIndex("tireSummer");
        int tireWinterCol = cursor.getColumnIndex("tireWinter");

        ArrayList<HashMap> vehicleList = new ArrayList<>();

        cursor.moveToFirst();

        if(cursor != null && (cursor.getCount() > 0)) {
            do {
                HashMap<String, String> singleVehicleList = new HashMap<>();

                singleVehicleList.put("refID", cursor.getString(refIDCol));
                singleVehicleList.put("year", cursor.getString(yearCol));
                singleVehicleList.put("make", cursor.getString(makeCol));
                singleVehicleList.put("model", cursor.getString(modelCol));
                singleVehicleList.put("engine", cursor.getString(engineCol));
                singleVehicleList.put("plate", cursor.getString(plateCol));
                singleVehicleList.put("oilFilter", cursor.getString(oilFilterCol));
                singleVehicleList.put("oilWeight", cursor.getString(oilWeightCol));
                singleVehicleList.put("tireSummer", cursor.getString(tireSummerCol));
                singleVehicleList.put("tireWinter", cursor.getString(tireWinterCol));

                vehicleList.add(singleVehicleList);

            } while(cursor.moveToNext());
            }else{
                HashMap<String, String> singleVehicleList = new HashMap<>();
                singleVehicleList.put(null, "Please Add Vehicle To Database");
                vehicleList.add(singleVehicleList);
            }

        return vehicleList;
    }

    public void deleteEntry(Context context, String refID){
        createDatabase(context);
        fleetRosterDB.execSQL("DELETE FROM grandFleetRoster WHERE refID = '" + refID + "';");
    }
}
