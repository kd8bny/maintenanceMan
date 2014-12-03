package com.kd8bny.maintenanceman.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import com.kd8bny.maintenanceman.data.vehicleLog;

public class vehicleLog_DataSource {
    private SQLiteDatabase database;
    private vehicleLogDBHelper dbHelper;
    private String[] allColumns = {vehicleLogDBHelper.COLUMN_ID,vehicleLogDBHelper.COLUMN_VEHICLE_NAME};

    public vehicleLog_DataSource(Context context){
        dbHelper = new vehicleLogDBHelper(context);
    }

    public void open() throws SQLiteException {
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

    /*public void deletevehicleLog(vehicleLog vehicleLog) {
        long id = vehicleLog.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(vehicleLogDBHelper.TABLE_VEHICLE, vehicleLogDBHelper.COLUMN_ID
                + " = " + id, null);
    }*/

    public List<vehicleLog> getAllvehicleLog() {
        List<vehicleLog> comments = new ArrayList<vehicleLog>();

        Cursor cursor = database.query(vehicleLogDBHelper.TABLE_VEHICLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            vehicleLog comment = cursorTovehicleName(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private vehicleLog cursorTovehicleName(Cursor cursor){
        vehicleLog vehicleName = new vehicleLog();
        vehicleName.setID(cursor.getLong(0));
        vehicleName.setVehicle(cursor.getString(1));

        return vehicleName;
    }
}
