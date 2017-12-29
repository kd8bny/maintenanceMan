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
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
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
    private static final String MILEAGE = "mileage";

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

    public void addMileageEvent(Mileage mileage){
        Map<String, Object> entry = new HashMap<>();
        entry.put("refID", mileage.getRefID());
        entry.put("date", mileage.getDate());
        entry.put("mileage", mileage.getMileage());
        entry.put("price", mileage.getPrice());
        entry.put("fill_vol", mileage.getFillVol());
        entry.put("tripometer", mileage.getTripometer());

        mFirestore.collection(USERS).document(mFirebaseUser.getUid())
                .collection(MILEAGE).document()
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

    public void getMileageEvents(String refID){
        final ArrayList<Object> mileageHistory = new ArrayList<>();
        mFirestore.collection(USERS).document(mFirebaseUser.getUid())
                .collection(MILEAGE)
                .whereEqualTo("refID", refID)
                .get() //TODO limit number of events
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Mileage mileage = new Mileage(document.getString("refID"));
                                mileage.setDate(document.getString("date"));
                                mileage.setMileage(
                                        document.getDouble("tripomter"),
                                        document.getDouble("fill_vol"),
                                        document.getDouble("price"));

                                mileageHistory.add(mileage);
                            }

                            mQueryComplete.updateUI(mileageHistory);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addTravelEvent(Travel travel){
        Map<String, Object> entry = new HashMap<>();
        entry.put("refID", travel.getRefID());
        entry.put("date", travel.getDate());
        entry.put("start", travel.getStart());
        entry.put("stop", travel.getStop());
        entry.put("delta", travel.getDelta());
        entry.put("dest", travel.getDest());
        entry.put("purpose", travel.getPurpose());
        entry.put("date_end", travel.getDateEnd());

        mFirestore.collection(USERS).document(mFirebaseUser.getUid())
                .collection(MILEAGE).document()
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

    public void getTravelEvents(String refID){
        final ArrayList<Object> travelHistory = new ArrayList<>();
        mFirestore.collection(USERS).document(mFirebaseUser.getUid())
                .collection(MILEAGE)
                .whereEqualTo("refID", refID)
                .get() //TODO limit number of events
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Travel travel = new Travel(document.getString("refID"));
                                travel.setDate(document.getString("date"));
                                travel.setStop(document.getDouble("stop"));
                                travel.setDest(document.getString("dest"));
                                travel.setPurpose("purpose");
                                travel.setDateEnd("date_end");

                                travelHistory.add(travel);
                            }

                            mQueryComplete.updateUI(travelHistory);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
