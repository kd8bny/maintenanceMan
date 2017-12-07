package com.kd8bny.maintenanceman.classes.data;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.fragments.fragment_main;
import com.kd8bny.maintenanceman.interfaces.AuthenticatedUser;

import java.util.HashMap;
import java.util.Map;


public class FirestoreHelper {
    private static final String TAG = "fs_helper";

    private static final String USERS = "users";
    private static final String FLEET = "fleet";

    private static FirestoreHelper sInstance;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mfirebaseUser;
    private AuthenticatedUser authenticatedUser;

    public Boolean isAuthuser = false;

    public static synchronized FirestoreHelper getInstance() {
        if (sInstance == null) {
            sInstance = new FirestoreHelper();
        }

        return sInstance;
    }

    public FirestoreHelper(){
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mfirebaseUser = mAuth.getCurrentUser();

        isAuthuser = mfirebaseUser != null;
        //isUserAuthenticated(mfirebaseUser != null);
        Log.e(TAG, mfirebaseUser+"");
        Log.e(TAG, mfirebaseUser+"");

        //DatabaseReference myRef = database.getReference("message");
        //myRef.setValue("Hello, World!");
    }

    /*public void newUser(){
        db.collection("users").document("fleet");
    }*/



    public void addToFLeet(Vehicle vehicle) {
        CollectionReference fleetCollectionReference =
                mFirestore.collection(USERS).document(mfirebaseUser.getUid()).collection(FLEET);
        //DocumentReference userReference = usersReference.document(mfirebaseUser.getUid());
        //CollectionReference fleetReference = db.collection(FLEET);
        Map<String, Object> entry = new HashMap<>();
        entry.put("refID", vehicle.getRefID());
        entry.put("title", vehicle.getTitle());
        entry.put("general", vehicle.getGeneralSpecs());

        mFirestore.collection(USERS).document(mfirebaseUser.getUid())
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
        //WriteBatch batch = mFirestore.batch();

        /*batch.set(userReference, vehicle.getRefID());
        batch.set(userReference, vehicle.getTitle());
        batch.set(userReference, vehicle.getVehicleType());
        batch.set(userReference, vehicle.getReservedSpecs());
        batch.set(userReference, vehicle.getGeneralSpecs());
        batch.set(userReference, vehicle.getEngineSpecs());
        batch.set(userReference, vehicle.getPowerTrainSpecs());
        batch.set(userReference, vehicle.getOtherSpecs());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });*/
    }

    public Boolean getIsAuthuser(){
        return isAuthuser;
    }

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
