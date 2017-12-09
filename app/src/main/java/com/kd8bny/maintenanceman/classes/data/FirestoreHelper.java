package com.kd8bny.maintenanceman.classes.data;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.interfaces.QueryComplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FirestoreHelper {
    private static final String TAG = "fs_helper";

    private static final String USERS = "users";
    private static final String FLEET = "fleet";
    private static final String MAINTENANCE = "maintenance";

    private static FirestoreHelper sInstance;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private static QueryComplete mQueryComplete;

    private Boolean isAuthUser = false;

    public static synchronized FirestoreHelper getInstance(QueryComplete queryComplete) {
        if (sInstance == null) {
            sInstance = new FirestoreHelper(queryComplete);
        } else {
            mQueryComplete = queryComplete;
        }

        return sInstance;
    }

    public FirestoreHelper(QueryComplete queryComplete){
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        isAuthUser = mFirebaseUser != null;
        mQueryComplete = queryComplete;
    }

    public Boolean getIsAuthUser(){
        return isAuthUser;
    }

    public void getFleet() {
        final ArrayList<Object> fleetRoster = new ArrayList<>();
        mFirestore.collection(USERS).document(mFirebaseUser.getUid())
                .collection(FLEET)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Vehicle vehicle = new Vehicle(document.getString("refID"));
                                vehicle.setYear(document.getString("year"));
                                vehicle.setMake(document.getString("make"));
                                vehicle.setModel(document.getString("model"));
                                vehicle.setVehicleType(document.getString("type"));
                                vehicle.setGeneralSpecs((HashMap<String, String>) document.get("general"));
                                vehicle.setEngineSpecs((HashMap<String, String>) document.get("engine"));
                                vehicle.setPowerTrainSpecs((HashMap<String, String>) document.get("power_train"));
                                vehicle.setOtherSpecs((HashMap<String, String>) document.get("other"));

                                fleetRoster.add(vehicle);
                            }

                            mQueryComplete.updateUI(fleetRoster);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addToFleet(Vehicle vehicle) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("refID", vehicle.getRefID());
        entry.put("year", vehicle.getYear());
        entry.put("make", vehicle.getMake());
        entry.put("model", vehicle.getModel());
        entry.put("type", vehicle.getVehicleType());
        entry.put("commercial", vehicle.getIsCommercial());
        entry.put("general", vehicle.getGeneralSpecs());
        entry.put("engine", vehicle.getEngineSpecs());
        entry.put("power_train", vehicle.getPowerTrainSpecs());
        entry.put("other", vehicle.getOtherSpecs());

        mFirestore.collection(USERS).document(mFirebaseUser.getUid())
                .collection(FLEET).document(vehicle.getRefID())
                .set(entry)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void addMaintenanceEvent(Maintenance maintenance){
        Map<String, Object> entry = new HashMap<>();
        entry.put("refID", maintenance.getRefID());
        entry.put("date", maintenance.getDate());
        entry.put("icon", maintenance.getIcon());
        entry.put("event", maintenance.getEvent());
        entry.put("odometer", maintenance.getOdometer());
        entry.put("price", maintenance.getPrice());
        entry.put("comment", maintenance.getComment());

        mFirestore.collection(USERS).document(mFirebaseUser.getUid())
                .collection(MAINTENANCE).document()
                .set(entry)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void getMaintenanceEvents(String refID){
        final ArrayList<Object> vehicleHistory = new ArrayList<>();
        mFirestore.collection(USERS).document(mFirebaseUser.getUid())
                .collection(MAINTENANCE)
                .whereEqualTo("refID", refID)
                .get() //TODO limit number of events
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Maintenance maintenance = new Maintenance(document.getString("refID"));
                                maintenance.setDate(document.getString("date"));
                                maintenance.setIcon(document.getLong("icon").intValue());
                                maintenance.setEvent(document.getString("event"));
                                maintenance.setOdometer(document.getString("odometer"));
                                maintenance.setPrice(document.getString("price"));
                                maintenance.setComment(document.getString("comment"));

                                vehicleHistory.add(maintenance);
                            }

                            mQueryComplete.updateUI(vehicleHistory);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    /* private ArrayList<String> setEvents(){ //TODO get out of sql helper and include
        //VehicleLogDBHelper vehicleDB = VehicleLogDBHelper.getInstance(getActivity().getApplicationContext());
        ArrayList<String> eventList = new ArrayList<>();
       // eventList.addAll(vehicleDB.getEntries());

        return eventList;
    }*/

/*
    public ArrayList<Maintenance> getMaintenanceEntries(String refID, Boolean sortDesc) {
        SQLiteDatabase db = getReadableDatabase();
        String sort = (sortDesc) ? "DESC" : "ASC";
        String QUERY = String.format("SELECT * FROM %s WHERE %s = '%s' ORDER BY %s %s;", TABLE_VEHICLE, COLUMN_VEHICLE_REFID, refID, COLUMN_VEHICLE_DATE, sort);
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

    public ArrayList<Maintenance> getCostByYear(String refID, int year) {
        SQLiteDatabase db = getReadableDatabase();
        String wildcard = year + "%"; //SQL wildcard %
        String QUERY = String.format("SELECT %s, %s FROM %s WHERE %s = '%s' AND %s LIKE '%s';",
                COLUMN_VEHICLE_DATE, COLUMN_VEHICLE_PRICE, TABLE_VEHICLE,
                COLUMN_VEHICLE_REFID, refID, COLUMN_VEHICLE_DATE, wildcard);
        Cursor cursor = db.rawQuery(QUERY, null);

        ArrayList<Maintenance> maintenanceList = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    Maintenance maintenance = new Maintenance(refID);
                    maintenance.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_DATE)));
                    maintenance.setPrice(cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_PRICE)));

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

   */
}
