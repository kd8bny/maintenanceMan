package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


public class vehicleLogDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "vehicleLogDB";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "vehicleLog.db";
    SQLiteDatabase vehicleLogDB = null;

    public static final String TABLE_VEHICLE = "grandVehicleLog";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VEHICLE_REFID = "refID";
    public static final String COLUMN_VEHICLE_DATE = "date";
    public static final String COLUMN_VEHICLE_ODO = "odo";
    public static final String COLUMN_VEHICLE_TASK = "event";


    public static final String DATABASE_CREATE = "create table "
        + TABLE_VEHICLE
        + "("
        + COLUMN_ID + " integer primary key autoincrement, "
        + COLUMN_VEHICLE_REFID + " text not null, "
        + COLUMN_VEHICLE_DATE + " text not null, "
        + COLUMN_VEHICLE_ODO + " text not null, "
        + COLUMN_VEHICLE_TASK + " text not null"
        + ");";

    public vehicleLogDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d(TAG,"This is oncreate!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(vehicleLogDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
        onCreate(db);
    }

    public void createDatabase(Context context){
        try {
            vehicleLogDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
            vehicleLogDB.execSQL(DATABASE_CREATE);
        }

        catch(Exception e){
            Log.e(TAG,e.toString());
        }
    }

    public void saveEntry(Context context, String refID, String date, String odo, String task) {
        File database = context.getDatabasePath("vehicleLog.db");
        if (!database.exists()) {
            this.createDatabase(context);
        }

        else {
            vehicleLogDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

            vehicleLogDB.execSQL("INSERT INTO grandVehicleLog (refid, date, odo, event) VALUES ('"
                            + refID + "','"
                            + date + "','"
                            + odo + "','"
                            + task + "');");
        }
    }

    public ArrayList<ArrayList> getEntries(Context context, String refID){

        this.createDatabase(context);
        Cursor cursor = vehicleLogDB.rawQuery("SELECT * FROM grandvehicleLog WHERE refID = '" + refID + "';", null);

        int refIDCol = cursor.getColumnIndex("refID");
        int dateCol = cursor.getColumnIndex("date");
        int odoCol = cursor.getColumnIndex("odo");
        int eventCol = cursor.getColumnIndex("event");

        ArrayList<ArrayList> vehicleList = new ArrayList<ArrayList>();

        cursor.moveToFirst();

        if(cursor != null && (cursor.getCount() > 0)) {
            do {
                ArrayList<String> singleVehicleList = new ArrayList<String>();

                if(refID.equals(cursor.getString(refIDCol))) {
                    singleVehicleList.add(refID);
                    singleVehicleList.add(cursor.getString(dateCol));
                    singleVehicleList.add(cursor.getString(odoCol));
                    singleVehicleList.add(cursor.getString(eventCol));

                    vehicleList.add(singleVehicleList);
                }

            } while(cursor.moveToNext());

        }
        else{
            ArrayList<String> singleVehicleList = new ArrayList<String>();
            singleVehicleList.add(null);
            singleVehicleList.add("No Vehicle History Given");
            vehicleList.add(singleVehicleList);
        }

        return vehicleList;

    }

}
