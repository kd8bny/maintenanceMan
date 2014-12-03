package com.kd8bny.maintenanceman.data;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kd8bny.maintenanceman.data.vehicleLog;

public class vehicleLogDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "vehicleLog";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "vehicleLog.db";

    public static final String TABLE_VEHICLE = "vehicle";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VEHICLE_NAME = "name";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_VEHICLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_VEHICLE_NAME
            + " text not null);";

    public vehicleLogDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(vehicleLogDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
        onCreate(db);
    }


}
