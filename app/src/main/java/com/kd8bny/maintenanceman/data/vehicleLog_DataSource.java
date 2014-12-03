package com.kd8bny.maintenanceman.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class vehicleLog_DataSource {
    private SQLiteDatabase database;
    private vehicleLogDBHelper dbHelper;
    private String[] allColumns = {vehicleLogDBHelper.COLUMN_ID,vehicleLogDBHelper.COLUMN_VEHICLE_NAME};

    public vehicleLog_DataSource(Context context){
        dbHelper = new vehicleLogDBHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public vehicleLog createvehicleLog(String vehicleName){
        ContentValues values = new ContentValues();
        values.put(vehicleLogDBHelper.COLUMN_VEHICLE_NAME, vehicleName);
        long insertID = database.insert(vehicleLogDBHelper.TABLE_VEHICLE, null, values);

        Cursor cursor = database.query(vehicleLogDBHelper.TABLE_VEHICLE, allColumns, vehicleLogDBHelper.COLUMN_ID + "="
                +insertID, null,null,null,null);
        cursor.moveToFirst();
        vehicleLog newvehicleLog = cursorTovehicleName(cursor);
        cursor.close();
        return newvehicleLog;

    }

    private vehicleLog cursorTovehicleName(Cursor cursor){
        vehicleLog vehicleName = new vehicleLog();
        vehicleName.setID(cursor.getLong(0));
        vehicleName.setVehicle(cursor.getString(1));

        return vehicleName;
    }
}
