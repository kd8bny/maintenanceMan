package com.kd8bny.maintenanceman.classes.Vehicle;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Vehicle implements Parcelable{
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

    private Vehicle(Parcel parcel){
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

        vehicleType = parcel.readString();
        refID = parcel.readString();
        title = parcel.readString();

        int N = parcel.readInt();
        if (N > 0) {
            reservedSpecs = new HashMap<>(N);
            for (int i = 0; i < N; i++) {
                String key = parcel.readString();
                String value = parcel.readString();
                reservedSpecs.put(key, value);
            }
        } else {
            reservedSpecs = null;
        }

        N = parcel.readInt();
        if (N > 0) {
            generalSpecs = new HashMap<>(N);
            for (int i = 0; i < N; i++) {
                String key = parcel.readString();
                String value = parcel.readString();
                generalSpecs.put(key, value);
            }
        } else {
            generalSpecs = null;
        }

        N = parcel.readInt();
        if (N > 0) {
            engineSpecs = new HashMap<>(N);
            for (int i = 0; i < N; i++) {
                String key = parcel.readString();
                String value = parcel.readString();
                engineSpecs.put(key, value);
            }
        } else {
            engineSpecs = null;
        }

        N = parcel.readInt();
        if (N > 0) {
            powerTrainSpecs = new HashMap<>(N);
            for (int i = 0; i < N; i++) {
                String key = parcel.readString();
                String value = parcel.readString();
                powerTrainSpecs.put(key, value);
            }
        } else {
            powerTrainSpecs = null;
        }

        N = parcel.readInt();
        if (N > 0) {
            otherSpecs = new HashMap<>(N);
            for (int i = 0; i < N; i++) {
                String key = parcel.readString();
                String value = parcel.readString();
                otherSpecs.put(key, value);
            }
        } else {
            otherSpecs = null;
        }

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
    public void writeToParcel(Parcel parcel, int flags){
        Log.v(TAG, "write to parcel: " + flags);
        parcel.writeString(vehicleType);
        parcel.writeString(refID);
        parcel.writeString(title);

        if (reservedSpecs == null){
            parcel.writeInt(0);
        }else{
            Set<Map.Entry<String, String>> entries = reservedSpecs.entrySet();
            parcel.writeInt(entries.size());
            for (Map.Entry<String, String> entry : entries) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        if (generalSpecs == null){
            parcel.writeInt(0);
        }else{
            Set<Map.Entry<String, String>> entries = generalSpecs.entrySet();
            parcel.writeInt(entries.size());
            for (Map.Entry<String, String> entry : entries) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        if (engineSpecs == null){
            parcel.writeInt(0);
        }else{
            Set<Map.Entry<String, String>> entries = engineSpecs.entrySet();
            parcel.writeInt(entries.size());
            for (Map.Entry<String, String> entry : entries) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        if (powerTrainSpecs == null){
            parcel.writeInt(0);
        }else{
            Set<Map.Entry<String, String>> entries = powerTrainSpecs.entrySet();
            parcel.writeInt(entries.size());
            for (Map.Entry<String, String> entry : entries) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        if (otherSpecs == null){
            parcel.writeInt(0);
        }else{
            Set<Map.Entry<String, String>> entries = otherSpecs.entrySet();
            parcel.writeInt(entries.size());
            for (Map.Entry<String, String> entry : entries) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        Log.wtf(TAG, "writeToParcelINST"
                + vehicleType
                + refID
                + title
                + reservedSpecs
                + generalSpecs
                + engineSpecs
                + powerTrainSpecs
                + otherSpecs);

        /*Log.wtf(TAG, "writeToParcelBUNDLE"
                + bundle.getString("vehicleType")
                + bundle.getString("refID")
                + bundle.getString("title")
                + bundle.getSerializable("reserved" )
                + bundle.getSerializable("general")
                + bundle.getSerializable("engine")
                + bundle.getSerializable("power")
                + bundle.getSerializable("other")
        );*/
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
    public static final Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {

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


