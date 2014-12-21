package com.kd8bny.maintenanceman.data;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class vehicleLogDB{
    private static final String TAG = "vehicleLogDB";

    public static final String TABLE_VEHICLE = "vehicle";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VEHICLE_NAME = "name";
    public static final String COLUMN_VEHICLE_YEAR = "year";
    public static final String COLUMN_VEHICLE_MAKE = "make";
    public static final String COLUMN_VEHICLE_MODEL = "model";

    public static final String DATABASE_CREATE = "create table "
        + TABLE_VEHICLE
        + "("
        + COLUMN_ID + " integer primary key autoincrement, "
        + COLUMN_VEHICLE_NAME + " text not null, "
        + COLUMN_VEHICLE_YEAR + " text not null, "
        + COLUMN_VEHICLE_MAKE + " text not null, "
        + COLUMN_VEHICLE_MODEL + " text not null"
        + ");";

    public static void onCreate(SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(vehicleLogDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
        onCreate(db);
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
