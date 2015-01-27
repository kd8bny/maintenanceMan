package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;


public class fleetRosterDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "fleetRosterDBHelper";

    public static final int DB_VERSION = 2; //v16
    public static final String DB_NAME = "fleetRoster.db";
    SQLiteDatabase fleetRosterDB = null;

    public static final String TABLE_ROSTER = "grandFleetRoster";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ROSTER_refID = "refID";
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
            db.execSQL("UPDATE " + TABLE_ROSTER_NEW + " SET " + COLUMN_ROSTER_PLATE + " = '' WHERE " + COLUMN_ROSTER_PLATE + " IS NULL");
            db.execSQL("UPDATE " + TABLE_ROSTER_NEW + " SET " + COLUMN_ROSTER_OIL_FILTER + " = '' WHERE " + COLUMN_ROSTER_OIL_FILTER + " IS NULL");
            db.execSQL("UPDATE " + TABLE_ROSTER_NEW + " SET " + COLUMN_ROSTER_OIL_WEIGHT + " = '' WHERE " + COLUMN_ROSTER_OIL_WEIGHT + " IS NULL");
            db.execSQL("UPDATE " + TABLE_ROSTER_NEW + " SET " + COLUMN_ROSTER_TIRE_SUMMER + " = '' WHERE " + COLUMN_ROSTER_TIRE_SUMMER + " IS NULL");
            db.execSQL("UPDATE " + TABLE_ROSTER_NEW + " SET " + COLUMN_ROSTER_TIRE_WINTER + " = '' WHERE " + COLUMN_ROSTER_TIRE_WINTER + " IS NULL");

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

    public void saveEntry(Context context, String refID, String make, String model, String year, String engine,
                          String plate, String oilFilter, String oilWeight, String tireSummer, String tireWinter) {
        Log.d(TAG, "Saving entry");


        File database = context.getDatabasePath("fleetRoster.db");
        if (!database.exists()) {
            this.createDatabase(context);
        }

        else {
            if (refID == null) {
                refID = (UUID.randomUUID()).toString();
            }
            fleetRosterDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

            fleetRosterDB.execSQL("INSERT INTO grandFleetRoster (refID, make, model, year, engine, plate, oilFilter, oilWeight, tireSummer, tireWinter) " +
                    "VALUES ('"
                    + refID + "','"
                    + make + "','"
                    + model + "','"
                    + year + "','"
                    + engine + "','"
                    + plate + "','"
                    + oilFilter + "','"
                    + oilWeight + "','"
                    + tireSummer + "','"
                    + tireWinter + "');");
        }
    }
    public ArrayList<ArrayList> getEntries(Context context){
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

        ArrayList<ArrayList> vehicleList = new ArrayList<>();

        cursor.moveToFirst();

        if(cursor != null && (cursor.getCount() > 0)) {
            do {
                ArrayList<String> singleVehicleList = new ArrayList<>();

                singleVehicleList.add(cursor.getString(refIDCol));
                singleVehicleList.add(cursor.getString(yearCol));
                singleVehicleList.add(cursor.getString(makeCol));
                singleVehicleList.add(cursor.getString(modelCol));
                singleVehicleList.add(cursor.getString(engineCol));
                singleVehicleList.add(cursor.getString(plateCol));
                singleVehicleList.add(cursor.getString(oilFilterCol));
                singleVehicleList.add(cursor.getString(oilWeightCol));
                singleVehicleList.add(cursor.getString(tireSummerCol));
                singleVehicleList.add(cursor.getString(tireWinterCol));

                vehicleList.add(singleVehicleList);

            } while(cursor.moveToNext());
            }else{
                ArrayList<String> singleVehicleList = new ArrayList<>();
                singleVehicleList.add(null);
                singleVehicleList.add("Please Add Vehicle To Database");
                vehicleList.add(singleVehicleList);
            }

        return vehicleList;
    }

    public void deleteEntry(Context context, String refID){
        createDatabase(context);
        fleetRosterDB.execSQL("DELETE FROM grandFleetRoster WHERE refID = '" + refID + "';");
    }

}
