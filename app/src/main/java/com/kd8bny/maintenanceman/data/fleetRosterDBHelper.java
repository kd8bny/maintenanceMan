package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;


public class fleetRosterDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "vehicleLogDB";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "fleetRoster.db";
    SQLiteDatabase fleetRosterDB = null;

    public static final String TABLE_ROSTER = "grandFleetRoster";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ROSTER_refID = "refid";
    public static final String COLUMN_ROSTER_YEAR = "year";
    public static final String COLUMN_ROSTER_MAKE = "make";
    public static final String COLUMN_ROSTER_MODEL = "model";
    public static final String COLUMN_ROSTER_ENGINE = "engine";


    public static final String DATABASE_CREATE = "create table "
        + TABLE_ROSTER
        + "("
        + COLUMN_ID + " integer primary key autoincrement, "
        + COLUMN_ROSTER_refID + " text not null, "
        + COLUMN_ROSTER_YEAR + " text not null, "
        + COLUMN_ROSTER_MAKE + " text not null, "
        + COLUMN_ROSTER_MODEL + " text not null,"
        + COLUMN_ROSTER_ENGINE + " text not null"
        + ");";

    public fleetRosterDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(fleetRosterDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROSTER);
        onCreate(db);
    }

    public void createDatabase(Context context){
        Log.d(TAG,"Database Creation");
        try {
            fleetRosterDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
            fleetRosterDB.execSQL(DATABASE_CREATE);

            Toast.makeText(context, "Database created", Toast.LENGTH_SHORT).show();
        }

        catch(Exception e){
            Log.e(TAG,"Error creating db");
        }
    }

    public void saveEntry(Context context, String make, String model, String year, String engine) {
        Log.d(TAG, "Saving entry");


        File database = context.getDatabasePath("fleetRoster.db");
        if (!database.exists()) {
            this.createDatabase(context);
        }

        else {
            String refid = year+make+model;
            Toast.makeText(context, refid, Toast.LENGTH_SHORT).show();
            fleetRosterDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

            fleetRosterDB.execSQL("INSERT INTO grandFleetRoster (refid, make, model, year, engine) VALUES ('"
                    + refid + "','"
                    + make + "','"
                    + model + "','"
                    + year + "','"
                    + engine + "');");
        }
    }
    public void getEntries(Context context){
        Log.d(TAG,"GET ENTRIES");

        String id = "00FordMust";

        Cursor cursor = fleetRosterDB.rawQuery("SELECT id FROM grandVehicleLog", null);
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();

    }

}
