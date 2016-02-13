package com.kd8bny.maintenanceman.classes.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleLogDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "vehicleLogDB";

    private static VehicleLogDBHelper sInstance;

    private static final int DB_VERSION = 3; // v2 was 50
    private static final String DB_NAME = "vehicleLog.db";

    private static final String TABLE_VEHICLE = "grandVehicleLog";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_VEHICLE_REFID = "refID";
    public static final String COLUMN_VEHICLE_DATE = "date";
    public static final String COLUMN_VEHICLE_ODO = "odo";
    public static final String COLUMN_VEHICLE_EVENT = "event";
    public static final String COLUMN_VEHICLE_PRICE = "price";
    public static final String COLUMN_VEHICLE_COMMENT = "comment";
    public static final String COLUMN_ICON = "icon";

    public static synchronized VehicleLogDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VehicleLogDBHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    public VehicleLogDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "
                + TABLE_VEHICLE
                + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_VEHICLE_REFID + " text not null, "
                + COLUMN_VEHICLE_DATE + " text not null, "
                + COLUMN_VEHICLE_ODO + " text not null, "
                + COLUMN_VEHICLE_EVENT + " text not null, "
                + COLUMN_VEHICLE_PRICE + " text not null, "
                + COLUMN_VEHICLE_COMMENT + " text not null,"
                + COLUMN_ICON + " text not null default '0'"
                + ");");
    }

    public void insertEntry(String refID, HashMap<String, String> dataSet) {
        /* Data incoming format
        *String date, String odo, String task, String price, String comment, String icon
        */
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VEHICLE_REFID, refID);
        contentValues.put(COLUMN_VEHICLE_DATE, dataSet.get("Date"));
        contentValues.put(COLUMN_VEHICLE_ODO, dataSet.get("Odometer"));
        contentValues.put(COLUMN_VEHICLE_EVENT, dataSet.get("Event"));
        contentValues.put(COLUMN_VEHICLE_PRICE, dataSet.get("Price"));
        contentValues.put(COLUMN_VEHICLE_COMMENT, dataSet.get("Comment"));
        contentValues.put(COLUMN_ICON, dataSet.get("icon"));
        db.beginTransaction();
        try {
            db.insertOrThrow(TABLE_VEHICLE, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG, "Error on insert entry");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ArrayList> getFullVehicleEntries(String refID) {
        SQLiteDatabase db = getReadableDatabase();
        String QUERY = String.format("SELECT * FROM %s WHERE %s = '%s';", TABLE_VEHICLE, COLUMN_VEHICLE_REFID, refID);
        Cursor cursor = db.rawQuery(QUERY, null);

        ArrayList<ArrayList> vehicleList = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    ArrayList<String> temp = new ArrayList<>();

                    temp.add(cursor.getString(cursor.getColumnIndex(COLUMN_ICON)));
                    temp.add(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_DATE)));
                    temp.add(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_ODO)));
                    temp.add(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_EVENT)));
                    temp.add(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_PRICE)));
                    temp.add(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_COMMENT)));

                    vehicleList.add(temp);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        }

        return vehicleList;
    }

    public ArrayList<ArrayList> getPriceByDate(String refID) {
        SQLiteDatabase db = getReadableDatabase();
        String QUERY = String.format("SELECT %s, %s FROM %s WHERE %s = '%s' AND %s != '';",
                COLUMN_VEHICLE_DATE, COLUMN_VEHICLE_PRICE,
                TABLE_VEHICLE,
                COLUMN_VEHICLE_REFID, refID,
                COLUMN_VEHICLE_PRICE);
        Cursor cursor = db.rawQuery(QUERY, null);

        ArrayList<ArrayList> vehicleList = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_DATE)));
                    temp.add(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_PRICE)));

                    vehicleList.add(temp);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        }

        return vehicleList;
    }

    public void deleteEntry(ArrayList<String> arrayList){//TODO test
        /* Data incoming format
        *String icon, String date, String odo, String event, String price, String comment
        */
        SQLiteDatabase db = getWritableDatabase();
        String QUERY = String.format("DELETE FROM %s WHERE %s = %s " +
                "AND %s = %s AND %s = %s AND %s = %s AND %s = %s;",
                TABLE_VEHICLE,
                COLUMN_VEHICLE_DATE, arrayList.get(1),
                COLUMN_VEHICLE_EVENT, arrayList.get(3),
                COLUMN_VEHICLE_ODO, arrayList.get(2),
                COLUMN_VEHICLE_PRICE, arrayList.get(4),
                COLUMN_VEHICLE_COMMENT, arrayList.get(5));
        try {
            db.rawQuery(QUERY, null);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    public void purgeVehicle(String refID){ //TODO test
        SQLiteDatabase db = getWritableDatabase();
        String QUERY = String.format("DELETE FROM %s WHERE %s = '%s';",
                TABLE_VEHICLE,
                COLUMN_VEHICLE_REFID, refID);
        try {
            db.rawQuery(QUERY, null);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(VehicleLogDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.beginTransaction();
        String TABLE_VEHICLE_NEW = TABLE_VEHICLE + "_new";
        try {
            Log.i(TAG, "trying");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_VEHICLE_NEW + " AS SELECT * FROM " + TABLE_VEHICLE);
            db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " ADD COLUMN " + COLUMN_ICON + " text not null default '0'");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
            db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " RENAME TO " + TABLE_VEHICLE);
            onCreate(db);
            db.setTransactionSuccessful();
            Log.i(TAG, "success");
        } catch (Exception e){
            Log.e(TAG,e.toString());
            Log.e(TAG, "Error updating db");
        }finally {
            db.setVersion(DB_VERSION);
            db.endTransaction();
        }
    }
}
