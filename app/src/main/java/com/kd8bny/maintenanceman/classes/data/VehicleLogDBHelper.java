package com.kd8bny.maintenanceman.classes.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kd8bny.maintenanceman.classes.Vehicle.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_VEHICLE_REFID + " TEXT NOT NULL, "
                + COLUMN_VEHICLE_DATE + " TEXT NOT NULL, "
                + COLUMN_VEHICLE_ODO + " TEXT NOT NULL, "
                + COLUMN_VEHICLE_EVENT + " TEXT NOT NULL, "
                + COLUMN_VEHICLE_PRICE + " TEXT NOT NULL, "
                + COLUMN_VEHICLE_COMMENT + " TEXT NOT NULL,"
                + COLUMN_ICON + " TEXT NOT NULL DEFAULT '0');");
    }

    public void insertEntry(Event event) {
        /* Data incoming format
        *String date, String odo, String task, String price, String comment, String icon
        */
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VEHICLE_REFID, event.getRefID());
        contentValues.put(COLUMN_VEHICLE_DATE, event.getDate());
        contentValues.put(COLUMN_VEHICLE_ODO, event.getOdometer());
        contentValues.put(COLUMN_VEHICLE_EVENT, event.getEvent());
        contentValues.put(COLUMN_VEHICLE_PRICE, event.getPrice());
        contentValues.put(COLUMN_VEHICLE_COMMENT, event.getComment());
        contentValues.put(COLUMN_ICON, event.getIcon());

        try {
            db.beginTransaction();
            db.insertOrThrow(TABLE_VEHICLE, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Error on insert entry");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Event> getFullVehicleEntries(String refID) {
        SQLiteDatabase db = getReadableDatabase();
        String QUERY = String.format("SELECT * FROM %s WHERE %s = '%s';", TABLE_VEHICLE, COLUMN_VEHICLE_REFID, refID);
        Cursor cursor = db.rawQuery(QUERY, null);

        ArrayList<Event> eventList = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Event event = new Event(refID);
                    if (cursor.getString(cursor.getColumnIndex(COLUMN_ICON)).isEmpty()){
                        event.setIcon(0);
                    }else{
                        event.setIcon(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ICON))));
                    }
                    event.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_DATE)));
                    event.setOdometer(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_ODO)));
                    event.setEvent(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_EVENT)));
                    event.setPrice(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_PRICE)));
                    event.setComment(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_COMMENT)));

                    eventList.add(event);
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

        return eventList;
    }

    public HashSet<String> getEntries() {
        SQLiteDatabase db = getReadableDatabase();
        String QUERY = String.format("SELECT %s FROM %s;", COLUMN_VEHICLE_EVENT, TABLE_VEHICLE);
        Cursor cursor = db.rawQuery(QUERY, null);

        HashSet<String> entryList = new HashSet<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    entryList.add(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_EVENT)));
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

        return entryList;
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

    public void deleteEntry(Event event){
        SQLiteDatabase db = getWritableDatabase();
        String QUERY = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_VEHICLE,
                COLUMN_VEHICLE_REFID, event.getRefID(),
                COLUMN_VEHICLE_DATE, event.getDate(),
                COLUMN_VEHICLE_EVENT, event.getEvent());
        try {
            db.beginTransaction();
            db.execSQL(QUERY);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
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
        if (oldVersion < 3) {
            try {
                Log.i(TAG, "trying");
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_VEHICLE_NEW + " AS SELECT * FROM " + TABLE_VEHICLE);
                db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " ADD COLUMN " + COLUMN_ICON + " text not null default '0'");
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
                db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " RENAME TO " + TABLE_VEHICLE);
                db.setTransactionSuccessful();
                Log.i(TAG, "success");
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                Log.e(TAG, "Error updating db");
            } finally {
                db.endTransaction();
            }
        }
    }
}
