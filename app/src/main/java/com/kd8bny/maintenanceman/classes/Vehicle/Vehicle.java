package com.kd8bny.maintenanceman.classes.Vehicle;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Vehicle implements Parcelable, Serializable{
    private static final String TAG = "Vehicle";
    public String vehicleType;
    public String refID;
    public String title;

    public HashMap<String, String> reservedSpecs;
    public HashMap<String, String> generalSpecs;
    public HashMap<String, String> engineSpecs;
    public HashMap<String, String> powerTrainSpecs;
    public HashMap<String, String> otherSpecs;

    public Vehicle(String vehicleType, String year, String make, String model){
        this.refID = UUID.randomUUID().toString();
        this.title = year + " " + make + " " + model;
        this.vehicleType = vehicleType;
        this.reservedSpecs = new HashMap<>();
        this.reservedSpecs.put("year", year);
        this.reservedSpecs.put("make", make);
        this.reservedSpecs.put("model", model);
    }

    public Vehicle(Parcel source){
        Log.v(TAG, "ParcelData(Parcel source): time to put back parcel data");

        Log.wtf(TAG, "ParcaPutFIRST"
                + vehicleType
                + refID
                + title
                + reservedSpecs
                + generalSpecs
                + engineSpecs
                + powerTrainSpecs
                + otherSpecs);


        Bundle bundle = source.readBundle(getClass().getClassLoader());
        vehicleType = bundle.getString("vehicleType");
        refID = bundle.getString("refID");
        title = bundle.getString("title");

        reservedSpecs = (HashMap) bundle.getSerializable("reserved");
        generalSpecs = (HashMap) bundle.getSerializable("general");
        engineSpecs = (HashMap) bundle.getSerializable("engine");
        powerTrainSpecs = (HashMap) bundle.getSerializable("power");
        otherSpecs = (HashMap) bundle.getSerializable("other");

        Log.wtf(TAG, "ParcaPut"
                + vehicleType
                + refID
                + title
                + reservedSpecs
                + generalSpecs
                + engineSpecs
                + powerTrainSpecs
                + otherSpecs);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        Log.v(TAG, "write to parcel: " + flags);
        Bundle bundle = new Bundle();
        bundle.putString("vehicleType", vehicleType);
        bundle.putString("refID", refID);
        bundle.putString("title", title);
        bundle.putSerializable("reserved", reservedSpecs);
        bundle.putSerializable("general", generalSpecs);
        bundle.putSerializable("engine", engineSpecs);
        bundle.putSerializable("power", powerTrainSpecs);
        bundle.putSerializable("other", otherSpecs);

        dest.writeBundle(bundle);

        Log.wtf(TAG, "writeToParcelINST"
                + vehicleType
                + refID
                + title
                + reservedSpecs
                + generalSpecs
                + engineSpecs
                + powerTrainSpecs
                + otherSpecs);

        Log.wtf(TAG, "writeToParcelBUNDLE"
                + bundle.getString("vehicleType")
                + bundle.getString("refID")
                + bundle.getString("title")
                + bundle.getSerializable("reserved" )
                + bundle.getSerializable("general")
                + bundle.getSerializable("engine")
                + bundle.getSerializable("power")
                + bundle.getSerializable("other")
        );
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public String getRefID() {
        return refID;
    }

    public String getTitle() {
        return title;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String type) {
        this.vehicleType = type;
    }

    public HashMap<String, String> getReservedSpecs() {
        return reservedSpecs;
    }

    public void setReservedSpecs(HashMap<String, String> reserveredSpecs) {
        this.reservedSpecs = reserveredSpecs;
    }

    public HashMap<String, String> getGeneralSpecs() {
        return generalSpecs;
    }

    public void setGeneralSpecs(HashMap<String, String> generalSpecs) {
        this.generalSpecs = generalSpecs;
    }

    public HashMap<String, String> getEngineSpecs() {
        return engineSpecs;
    }

    public void setEngineSpecs(HashMap<String, String> engineSpecs) {
        this.engineSpecs = engineSpecs;
    }

    public HashMap<String, String> getPowerTrainSpecs() {
        return powerTrainSpecs;
    }

    public void setPowerTrainSpecs(HashMap<String, String> powerTrainSpecs) {
        this.powerTrainSpecs = powerTrainSpecs;
    }

    public HashMap<String, String> getOtherSpecs() {
        return otherSpecs;
    }

    public void setOtherSpecs(HashMap<String, String> otherSpecs) {
        this.otherSpecs = otherSpecs;
    }

    /**
    * Creator required for class implementing the parcelable interface.
    */
    public static final Parcelable.Creator<Vehicle> CREATOR = new Creator<Vehicle>() {

        @Override
        public Vehicle createFromParcel(Parcel source) {
            Log.d(TAG, "create");
            return new Vehicle(source);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}


