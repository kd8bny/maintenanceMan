package com.kd8bny.maintenanceman.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class vehicleLogDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "vehicleLogDBHelper";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "vehicleLog.db";

    public vehicleLogDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        vehicleLogDB.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        vehicleLogDB.onUpgrade(db, oldVersion, newVersion);
    }
}
