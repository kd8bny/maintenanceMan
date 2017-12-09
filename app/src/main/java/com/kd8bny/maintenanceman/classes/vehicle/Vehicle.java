package com.kd8bny.maintenanceman.classes.vehicle;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Since;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.UUID;

public class Vehicle implements Parcelable{
    private static final String TAG = "Vehicle";

    @Since(1.0)
    private String mRefID;
    private String mYear;
    private String mMake;
    private String mModel;
    private String mVehicleType;
    private String mGeneralSpecs;
    private String mEngineSpecs;
    private String mPowerTrainSpecs;
    private String mOtherSpecs;

    private Boolean mBusinessVehicle;

    public Vehicle(){}

    public Vehicle(String refID){
        mRefID = (refID == null) ? UUID.randomUUID().toString() : refID;
    }

    private Vehicle(Parcel parcel){
        mRefID = parcel.readString();
        mVehicleType = parcel.readString();
        mBusinessVehicle = parcel.readByte() != 0;
        mGeneralSpecs= parcel.readString();
        mEngineSpecs = parcel.readString();
        mPowerTrainSpecs = parcel.readString();
        mOtherSpecs = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeString(mRefID);
        parcel.writeString(mVehicleType);
        parcel.writeByte((byte) (mBusinessVehicle ? 1 : 0));
        parcel.writeString(mGeneralSpecs);
        parcel.writeString(mEngineSpecs);
        parcel.writeString(mPowerTrainSpecs);
        parcel.writeString(mOtherSpecs);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public String getRefID() {
        return mRefID;
    }

    public void setYear(String year){
        mYear = year;
    }

    public String getYear(){
        return mYear;
    }

    public void setMake(String make){
        mMake = make;
    }

    public String getMake(){
        return mMake;
    }

    public void setModel(String model){
        mModel = model;
    }

    public String getModel(){
        return mModel;
    }

    public String getVehicleType() {
        return mVehicleType;
    }

    public void setVehicleType(String type) {
        mVehicleType = type;
    }

    public Boolean getBusiness() {
        return mBusinessVehicle;
    }

    public void setBusiness(Boolean isBusiness) {
        mBusinessVehicle = isBusiness;
    }

    public HashMap<String, String> getGeneralSpecs() {
        Gson gson = new Gson();
        return gson.fromJson(mGeneralSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public void setGeneralSpecs(HashMap<String, String> generalSpecs) {
        Gson gson = new Gson();
        mGeneralSpecs = gson.toJson(generalSpecs);
    }

    public HashMap<String, String> getEngineSpecs() {
        Gson gson = new Gson();
        return gson.fromJson(mEngineSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public void setEngineSpecs(HashMap<String, String> engineSpecs) {
        Gson gson = new Gson();

        mEngineSpecs = gson.toJson(engineSpecs);
    }

    public HashMap<String, String> getPowerTrainSpecs() {
        Gson gson = new Gson();
        return gson.fromJson(mPowerTrainSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public void setPowerTrainSpecs(HashMap<String, String> powerTrainSpecs) {
        Gson gson = new Gson();

        mPowerTrainSpecs = gson.toJson(powerTrainSpecs);
    }

    public HashMap<String, String> getOtherSpecs() {
        Gson gson = new Gson();
        return gson.fromJson(mOtherSpecs, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public void setOtherSpecs(HashMap<String, String> otherSpecs) {
        Gson gson = new Gson();

        mOtherSpecs = gson.toJson(otherSpecs);
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


