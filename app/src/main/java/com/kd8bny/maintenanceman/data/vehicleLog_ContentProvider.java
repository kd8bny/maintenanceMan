package com.kd8bny.maintenanceman.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by kd8bny on 12/21/14.
 */
public class vehicleLog_ContentProvider extends ContentProvider{

    //db
    private vehicleLogDBHelper db;

    //URI
    private static final int VEHICLES = 10; //TODO ??
    private static final int VEHICLES_ID = 20; //???

    private static final String AUTHORITY = "com.kd8bny.maintenanceman.data.vehicleLog_ContentProvider";
    private static final String BASE_PATH = "vehicles";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vehicles";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vehicle";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        static {
            sURIMatcher.addURI(AUTHORITY,BASE_PATH,VEHICLES);
            sURIMatcher.addURI(AUTHORITY,BASE_PATH + "/#", VEHICLES_ID);
        }

    @Override
    public boolean onCreate(){
        db = new vehicleLogDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        //Check to see if column exists
        checkColumns(projection);

        queryBuilder.setTables(vehicleLogDB.TABLE_VEHICLE);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case VEHICLES:
                break;
            case VEHICLES_ID:
                //Add ID to query
                queryBuilder.appendWhere(vehicleLogDB.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db_writable = db.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db_writable, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri){
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqldb = db.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case VEHICLES:
                id = sqldb.insert(vehicleLogDB.TABLE_VEHICLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return Uri.parse(BASE_PATH + "/" + id);
        }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case VEHICLES:
                rowsDeleted = sqlDB.delete(vehicleLogDB.TABLE_VEHICLE, selection,
                        selectionArgs);
                break;
            case VEHICLES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(vehicleLogDB.TABLE_VEHICLE,
                            vehicleLogDB.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(vehicleLogDB.TABLE_VEHICLE,
                            vehicleLogDB.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case VEHICLES:
                rowsUpdated = sqlDB.update(vehicleLogDB.TABLE_VEHICLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case VEHICLES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(vehicleLogDB.TABLE_VEHICLE,
                            values,
                            vehicleLogDB.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(vehicleLogDB.TABLE_VEHICLE,
                            values,
                            vehicleLogDB.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { vehicleLogDB.COLUMN_VEHICLE_NAME,
                vehicleLogDB.COLUMN_VEHICLE_YEAR, vehicleLogDB.COLUMN_VEHICLE_MAKE,vehicleLogDB.COLUMN_VEHICLE_MODEL,
                vehicleLogDB.COLUMN_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
    
}

