package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;


public class vehicleLogDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "vehicleLogDB";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "vehicleLog.db";
    SQLiteDatabase vehicleLogDB = null;

    public static final String TABLE_VEHICLE = "vehicle";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VEHICLE_ID = "id";
    public static final String COLUMN_VEHICLE_DATE = "date";
    public static final String COLUMN_VEHICLE_ODO = "odo";
    public static final String COLUMN_VEHICLE_EVENT = "event";


    public static final String DATABASE_CREATE = "create table "
        + TABLE_VEHICLE
        + "("
        + COLUMN_ID + " integer primary key autoincrement, "
        + COLUMN_VEHICLE_ID + " text not null, "
        + COLUMN_VEHICLE_DATE + " text not null, "
        + COLUMN_VEHICLE_ODO + " integer, "
        + COLUMN_VEHICLE_EVENT + " text not null"
        + ");";

    public vehicleLogDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //db.execSQL(DATABASE_CREATE);
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
        Log.d(TAG,"Database Creation");
        try {
            vehicleLogDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
            vehicleLogDB.execSQL(DATABASE_CREATE);

            Toast.makeText(context, "Database created", Toast.LENGTH_SHORT).show();
        }

        catch(Exception e){
            Log.e(TAG,"Error creating db");
        }
    }

    public void saveEntry(Context context){
        Log.d(TAG,"Saving entry");

        File database = context.getDatabasePath("vehicleLog.db");
        if (!database.exists()) {
            this.createDatabase(context);
        }

        else {
            String id = "00FordMust";
            String date = "1/1/15";
            int odo = 175000;
            String event = "Wheel Bearing";

            vehicleLogDB = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

            vehicleLogDB.execSQL("INSERT INTO vehicle (id, date, odo, event) VALUES ('"
                    + id +"','"
                    + date + "','"
                    + odo + "','"
                    + event + "');"
            );
        }





    }



    /*public static void addShit(View v){
        @SuppressWarnings("unchecked")

        dataSource.open();

        ArrayAdapter<vehicleLog> adapter = (ArrayAdapter<vehicleLog>) getListAdapter();
        vehicleLog vehicleName = null;
        switch (v.getId()) {
            case R.id.add:
                String[] names = new String[] { "Mustang", "Saturn", "G8" };
                int nextInt = new Random().nextInt(3);
                // save the new comment to the database
                vehicleName = dataSource.createvehicleLog(names[nextInt]);
                adapter.add(vehicleName);
                break;
            
            case R.id.delete:
                if (context.getResources().getListAdapter().getCount() > 0) {
                    vehicleName = (vehicleLog) getListAdapter().getItem(0);
                    //dataSource.deleteComment(vehicleName);
                    adapter.remove(vehicleName);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    };*/


}
