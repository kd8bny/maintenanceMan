package com.kd8bny.maintenanceman.data;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kd8bny.maintenanceman.data.vehicleLog;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "vehicleLog";

    private static final String DB_NAME = "vehicleLog";
    private static final int VERSION = 1;

    private static final String TABLE_VEHICLE = "vehicle";
    private static final String COLUMN_VEHICLE_NAME = "name";

    public databaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table vehicle ("+
            "_id integer primary key autoincrement, name varchar(100)");

        db.execSQL("create table history ("+
            "date integer, odometer real, task varchar(100)"+
            "vehicle_id integer references vehicle(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public long newHistory(vehicleLog vehicleLog){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_VEHICLE_NAME, vehicleLog.getVehicle());
        return getWritableDatabase().insert(TABLE_VEHICLE, null, cv);
    }


}
