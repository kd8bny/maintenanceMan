package com.kd8bny.maintenanceman.classes.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kd8bny.maintenanceman.classes.Vehicle.Travel;
import com.kd8bny.maintenanceman.classes.Vehicle.Maintenance;

import java.util.ArrayList;
import java.util.HashSet;

public class VehicleLogDBHelper extends SQLiteOpenHelper{
    private static final String TAG = "vehicleLogDB";

    private static VehicleLogDBHelper sInstance;

    private static final int DB_VERSION = 4; // v3(58) v2(50)
    private static final String DB_NAME = "vehicleLog.db";

    private static final String TABLE_VEHICLE = "grandVehicleLog";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_VEHICLE_REFID = "refID";
    private static final String COLUMN_VEHICLE_DATE = "date";
    private static final String COLUMN_VEHICLE_ODO = "odo";
    private static final String COLUMN_VEHICLE_EVENT = "event";
    private static final String COLUMN_VEHICLE_PRICE = "price";
    private static final String COLUMN_VEHICLE_COMMENT = "comment";
    private static final String COLUMN_ICON = "icon";

    private static final String TABLE_TRAVEL = "travelLog";
    private static final String COLUMN_START = "start";
    private static final String COLUMN_STOP = "end";
    private static final String COLUMN_DEST = "dest";
    private static final String COLUMN_PURPOSE = "purpose";

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
        String CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL,"
                        + " %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL DEFAULT '0');",
                TABLE_VEHICLE, COLUMN_ID, COLUMN_VEHICLE_REFID,
                COLUMN_VEHICLE_DATE, COLUMN_VEHICLE_ODO, COLUMN_VEHICLE_EVENT, COLUMN_VEHICLE_PRICE, COLUMN_VEHICLE_COMMENT, COLUMN_ICON);

        String CREATE_BUSINESS = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL,"
                        + " %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL);",
                TABLE_TRAVEL, COLUMN_ID, COLUMN_VEHICLE_REFID,
                COLUMN_VEHICLE_DATE, COLUMN_START, COLUMN_STOP, COLUMN_DEST, COLUMN_PURPOSE);

        try {
            db.beginTransaction();
            db.execSQL(CREATE);
            db.execSQL(CREATE_BUSINESS);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG, "Error onCreate");
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertEntry(Maintenance maintenance) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VEHICLE_REFID, maintenance.getRefID());
        contentValues.put(COLUMN_VEHICLE_DATE, maintenance.getDate());
        contentValues.put(COLUMN_VEHICLE_ODO, maintenance.getOdometer());
        contentValues.put(COLUMN_VEHICLE_EVENT, maintenance.getEvent());
        contentValues.put(COLUMN_VEHICLE_PRICE, maintenance.getPrice());
        contentValues.put(COLUMN_VEHICLE_COMMENT, maintenance.getComment());
        contentValues.put(COLUMN_ICON, maintenance.getIcon());

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

    public ArrayList<Maintenance> getFullVehicleEntries(String refID) {
        SQLiteDatabase db = getReadableDatabase();
        String QUERY = String.format("SELECT * FROM %s WHERE %s = '%s';", TABLE_VEHICLE, COLUMN_VEHICLE_REFID, refID);
        Cursor cursor = db.rawQuery(QUERY, null);

        ArrayList<Maintenance> maintenanceList = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Maintenance maintenance = new Maintenance(refID);
                    if (cursor.getString(cursor.getColumnIndex(COLUMN_ICON)).isEmpty()){
                        maintenance.setIcon(0);
                    }else{
                        maintenance.setIcon(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ICON))));
                    }
                    maintenance.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_DATE)));
                    maintenance.setOdometer(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_ODO)));
                    maintenance.setEvent(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_EVENT)));
                    maintenance.setPrice(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_PRICE)));
                    maintenance.setComment(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_COMMENT)));

                    maintenanceList.add(maintenance);
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

        return maintenanceList;
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

    public void deleteEntry(Maintenance maintenance){
        SQLiteDatabase db = getWritableDatabase();
        String QUERY = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_VEHICLE,
                COLUMN_VEHICLE_REFID, maintenance.getRefID(),
                COLUMN_VEHICLE_DATE, maintenance.getDate(),
                COLUMN_VEHICLE_EVENT, maintenance.getEvent());
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

    public void insertEntry(Travel travel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VEHICLE_REFID, travel.getRefID());
        contentValues.put(COLUMN_VEHICLE_DATE, travel.getDate());
        contentValues.put(COLUMN_START, travel.getStart());
        contentValues.put(COLUMN_STOP, travel.getStop());
        contentValues.put(COLUMN_DEST, travel.getDest());
        contentValues.put(COLUMN_PURPOSE, travel.getPurpose());

        try {
            db.beginTransaction();
            db.insertOrThrow(TABLE_TRAVEL, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Error on insert entry");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Travel> getFullBusinessEntries(String refID) {
        SQLiteDatabase db = getReadableDatabase();
        String QUERY = String.format("SELECT * FROM %s WHERE %s = '%s';", TABLE_TRAVEL, COLUMN_VEHICLE_REFID, refID);
        Cursor cursor = db.rawQuery(QUERY, null);

        ArrayList<Travel> travelList = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Travel travel = new Travel(refID);
                    travel.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_DATE)));
                    travel.setStart(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_START))));
                    travel.setStop(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_STOP))));
                    travel.setDest(cursor.getString(cursor.getColumnIndex(COLUMN_DEST)));
                    travel.setPurpose(cursor.getString(cursor.getColumnIndex(COLUMN_PURPOSE)));

                    travelList.add(travel);
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

        return travelList;
    }

    public HashSet<String> getPurpose() {
        SQLiteDatabase db = getReadableDatabase();
        String QUERY = String.format("SELECT %s FROM %s;", COLUMN_PURPOSE, TABLE_TRAVEL);
        Cursor cursor = db.rawQuery(QUERY, null);

        HashSet<String> entryList = new HashSet<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    entryList.add(cursor.getString(cursor.getColumnIndex(COLUMN_PURPOSE)));
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

    public void deleteEntry(Travel travel){
        SQLiteDatabase db = getWritableDatabase();
        String QUERY = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_TRAVEL,
                COLUMN_VEHICLE_REFID, travel.getRefID(),
                COLUMN_VEHICLE_DATE, travel.getDate(),
                COLUMN_START, travel.getStart());
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

    public void purgeVehicle(String refID){
        SQLiteDatabase db = getWritableDatabase();
        String QUERY = String.format("DELETE FROM %s WHERE %s = '%s';",
                TABLE_VEHICLE,
                COLUMN_VEHICLE_REFID, refID);
        String QUERY_TRAVEL = String.format("DELETE FROM %s WHERE %s = '%s';",
                TABLE_TRAVEL,
                COLUMN_VEHICLE_REFID, refID);
        try {
            db.beginTransaction();
            db.execSQL(QUERY);
            db.execSQL(QUERY_TRAVEL);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(VehicleLogDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.beginTransaction();
        try {
            if (oldVersion < 3) {
                Log.d(TAG, "icon");
                String TABLE_VEHICLE_NEW = TABLE_VEHICLE + "_new";
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_VEHICLE_NEW + " AS SELECT * FROM " + TABLE_VEHICLE);
                db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " ADD COLUMN " + COLUMN_ICON + " text not null default '0'");
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
                db.execSQL("ALTER TABLE " + TABLE_VEHICLE_NEW + " RENAME TO " + TABLE_VEHICLE);
                Log.d(TAG, "icondone");
            }
            if (oldVersion < 4){
                Log.d(TAG, "bus");
                String CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL,"
                        + " %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL);",
                        TABLE_TRAVEL, COLUMN_ID, COLUMN_VEHICLE_REFID,
                        COLUMN_VEHICLE_DATE, COLUMN_START, COLUMN_STOP, COLUMN_DEST, COLUMN_PURPOSE);
                db.execSQL(CREATE);
                Log.d(TAG, "bus done");
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Error updating db");
        } finally {
            db.endTransaction();
        }
    }
}
