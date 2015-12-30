package com.kd8bny.maintenanceman.classes.Vehicle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Vehicle implements Serializable{
    public String vehicleType;
    public String refID;

    public String title;

    public HashMap<String, String> reservedSpecs;
    public HashMap<String, String> generalSpecs;
    public HashMap<String, String> engineSpecs;
    public HashMap<String, String> powerTrainSpecs;
    public HashMap<String, String> otherSpecs;

    public Vehicle(String year, String make, String model){
        refID = UUID.randomUUID().toString();
        title = year + " " + make + " " + model;
        reservedSpecs.put("year", year);
        reservedSpecs.put("make", make);
        reservedSpecs.put("model", model);
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String type) {
        this.vehicleType = type;
    }

    public String getRefID() {
        return refID;
    }

    public String getTitle() {
        return title;
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
}
