package com.kd8bny.maintenanceman.classes.vehicle;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.UUID;

public class Vehicle implements Parcelable{
    private static final String TAG = "com/kd8bnyapps/kd8bny/maintenanceman/Vehicle";
    public String vehicleType;
    public String refID;
    public String title;
    public Boolean isBusiness;

    public String reservedSpecs;
    public String generalSpecs;
    public String engineSpecs;
    public String powerTrainSpecs;
    public String otherSpecs;

    public Vehicle(String vehicleType, Boolean isBusiness, String year, String make, String model){
        this.refID = UUID.randomUUID().toString();
        this.vehicleType = vehicleType;
        this.isBusiness = isBusiness;
        this.title = year + " " + make + " " + model;
        HashMap<String, String> temp = new HashMap<>();
        temp.put("year", year);
        temp.put("make", make);
        temp.put("model", model);
        setReservedSpecs(temp);
    }

    private Vehicle(Parcel parcel){
        vehicleType = parcel.readString();
        refID = parcel.readString();
        title = parcel.readString();
        isBusiness = parcel.readByte() != 0;
        reservedSpecs = parcel.readString();
        generalSpecs= parcel.readString();
        engineSpecs = parcel.readString();
        powerTrainSpecs = parcel.readString();
        otherSpecs = parcel.readString();
    }

    public Vehicle(String refID, String vehicleType, String year, String make, String model){
        /*
        *TODO Temp to bring everyone up to new spec
         */
        this.refID = refID;
        this.title = year + " " + make + " " + model;
        this.vehicleType = vehicleType;
        HashMap<String, String> temp = new HashMap<>();
        temp.put("year", year);
        temp.put("make", make);
        temp.put("model", model);
        setReservedSpecs(temp);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeString(vehicleType);
        parcel.writeString(refID);
        parcel.writeString(title);
        parcel.writeByte((byte) (isBusiness ? 1 : 0));

        parcel.writeString(reservedSpecs);
        parcel.writeString(generalSpecs);
        parcel.writeString(engineSpecs);
        parcel.writeString(powerTrainSpecs);
        parcel.writeString(otherSpecs);
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
        Gson gson = new Gson();
        return gson.fromJson(reservedSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public Boolean getBusiness() {
        return isBusiness;
    }

    public void setBusiness(Boolean bool) {
        isBusiness = bool;
    }

    public void setReservedSpecs(HashMap<String, String> reserveredSpecs) {
        Gson gson = new Gson();

        this.reservedSpecs = gson.toJson(reserveredSpecs);
    }

    public HashMap<String, String> getGeneralSpecs() {
        Gson gson = new Gson();
        return gson.fromJson(generalSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public void setGeneralSpecs(HashMap<String, String> generalSpecs) {
        Gson gson = new Gson();
        this.generalSpecs = gson.toJson(generalSpecs);
    }

    public HashMap<String, String> getEngineSpecs() {
        Gson gson = new Gson();
        return gson.fromJson(engineSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public void setEngineSpecs(HashMap<String, String> engineSpecs) {
        Gson gson = new Gson();

        this.engineSpecs = gson.toJson(engineSpecs);
    }

    public HashMap<String, String> getPowerTrainSpecs() {
        Gson gson = new Gson();
        return gson.fromJson(powerTrainSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public void setPowerTrainSpecs(HashMap<String, String> powerTrainSpecs) {
        Gson gson = new Gson();

        this.powerTrainSpecs = gson.toJson(powerTrainSpecs);
    }

    public HashMap<String, String> getOtherSpecs() {
        Gson gson = new Gson();
        return gson.fromJson(otherSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public void setOtherSpecs(HashMap<String, String> otherSpecs) {
        Gson gson = new Gson();

        this.otherSpecs = gson.toJson(otherSpecs);
    }

    /**
    * Creator required for class implementing the parcelable interface.
    */
    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {

        @Override
        public Vehicle createFromParcel(Parcel source) {
            return new Vehicle(source);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}


