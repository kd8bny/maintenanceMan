package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kd8bny.maintenanceman.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class vehicleLogDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "vehicleLogDB";

    public static final int DB_VERSION = 2; // v1 was 29
    public static final String DB_NAME = "vehicleLog.db";
    SQLiteDatabase vehicleLogDB = null;

    public static final String TABLE_VEHICLE = "grandVehicleLog";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VEHICLE_REFID = "refID";
    public static final String COLUMN_VEHICLE_DATE = "date";
    public static final String COLUMN_VEHICLE_ODO = "odo";
    public static final String COLUMN_VEHICLE_TASK = "event";
    public static final String COLUMN_VEHICLE_PRICE = "price";
    public static final String COLUMN_VEHICLE_COMMENT = "comment";


    public static final String DATABASE_CREATE = "create table "
        + TABLE_VEHICLE
        + "("
        + COLUMN_ID + " integer primary key autoincrement, "
        + COLUMN_VEHICLE_REFID + " text not null, "
        + COLUMN_VEHICLE_DATE + " text not null, "
        + COLUMN_VEHICLE_ODO + " text not null, "
        + COLUMN_VEHICLE_TASK + " text not null, "
        + COLUMN_VEHICLE_PRICE + " text not null, "
        + COLUMN_VEHICLE_COMMENT + " text not null"
        + ");";

    public vehicleLogDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(vehicleLogDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.beginTransaction();
        String TABLE_VEHICLE_NEW = TABLE_VEHICLE + "_new";
        try {
            Log.d(TAG, "trying");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_VEHICLE_NEW + " AS SELECT * FROM " + TABLE_VEHICLE);
            db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " ADD COLUMN " + COLUMN_VEHICLE_PRICE + " text not null default ''");
            db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " ADD COLUMN " + COLUMN_VEHICLE_COMMENT + " text not null default ''");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
            db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " RENAME TO " + TABLE_VEHICLE);
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
            vehicleLogDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

            vehicleLogDB.execSQL(DATABASE_CREATE);
            vehicleLogDB.setVersion(DB_VERSION);
        }catch(Exception e){
            //e.printStackTrace();
            Log.e(TAG, "Log probably already exists");

        }finally {
            if(vehicleLogDB.getVersion() < DB_VERSION){
                onUpgrade(vehicleLogDB, vehicleLogDB.getVersion(), DB_VERSION);
                Log.i(TAG,(vehicleLogDB.getVersion())+"");
            }
        }
    }

    public void saveEntry(Context context, String refID, HashMap<String, String> dataSet) {
        //String date, String odo, String task, String price, String comment
        File database = context.getDatabasePath("vehicleLog.db");
        if (!database.exists()) {
            this.createDatabase(context);
        }

        else {
            vehicleLogDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
            try {
                vehicleLogDB.execSQL("INSERT INTO grandVehicleLog (refid, date, odo, event, price, comment) VALUES ('"
                        + refID + "','"
                        + dataSet.get("Date") + "','"
                        + dataSet.get("Odometer") + "','"
                        + dataSet.get("Event") + "','"
                        + dataSet.get("Price") + "','"
                        + dataSet.get("Comment") + "');");

            }catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                Log.e(TAG, "Error updating db");
            }
        }
    }

    public ArrayList<ArrayList> getEntries(Context context, String refID){

        createDatabase(context);
        Cursor cursor = vehicleLogDB.rawQuery("SELECT * FROM grandvehicleLog WHERE refID = '" + refID + "';", null);

        int refIDCol = cursor.getColumnIndex("refID");
        int dateCol = cursor.getColumnIndex("date");
        int odoCol = cursor.getColumnIndex("odo");
        int eventCol = cursor.getColumnIndex("event");
        int priceCol = cursor.getColumnIndex("price");
        int commentCol = cursor.getColumnIndex("comment");

        ArrayList<ArrayList> vehicleList = new ArrayList<>();

        cursor.moveToFirst();

        if(cursor != null && (cursor.getCount() > 0)) {
            do {
                ArrayList<String> singleVehicleList = new ArrayList<>();

                if(refID.equals(cursor.getString(refIDCol))) {
                    singleVehicleList.add(refID);
                    singleVehicleList.add(cursor.getString(dateCol));
                    singleVehicleList.add(cursor.getString(odoCol));
                    singleVehicleList.add(cursor.getString(eventCol));
                    singleVehicleList.add(cursor.getString(priceCol));
                    singleVehicleList.add(cursor.getString(commentCol));

                    vehicleList.add(singleVehicleList);
                }

            } while(cursor.moveToNext());

        }
        else{
            ArrayList<String> singleVehicleList = new ArrayList<>();
            singleVehicleList.add(null);
            singleVehicleList.add(context.getResources().getString(R.string.no_history));
            vehicleList.add(singleVehicleList);
        }

        return vehicleList;
    }

    public ArrayList<String> getEvents(Context context){

        createDatabase(context);
        Cursor cursor = vehicleLogDB.rawQuery("SELECT event FROM grandvehicleLog;", null);

        int eventCol = cursor.getColumnIndex("event");
        ArrayList<String> eventList = new ArrayList<>();
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            do {
                eventList.add(cursor.getString(eventCol));
            } while(cursor.moveToNext());
        }

        return eventList;
    }

    public void deleteEntry(Context context, ArrayList<String> dataSet){
        createDatabase(context);
        try {
            vehicleLogDB.execSQL("DELETE FROM grandVehicleLog WHERE " +
                    "date = '" + dataSet.get(1) + "'" +
                    " AND event = '" + dataSet.get(3) +
                    "' AND odo = '" + dataSet.get(2) +
                    "' AND price = '" + dataSet.get(4) +
                    "' AND comment = '" + dataSet.get(5) + "';");
        }catch (Exception e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Error updating db");
        }
    }

    public void purgeHistory(Context context, String refID){
        createDatabase(context);
        try{
            vehicleLogDB.execSQL("DELETE FROM grandVehicleLog WHERE " +
                "refID = '" + refID + "';");
        }catch (Exception e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Error updating db");
        }
    }
}
