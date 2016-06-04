package com.kd8bny.maintenanceman.classes.vehicle;

public class Mileage extends Entry {
    private static final String TAG = "Mileage";

    public Double mOdo;
    public Double mVol;
    public Double mMileage;

    public Mileage(String refID) {
        mRefID = refID;
    }

    public Double getMileage() {
        return mMileage;
    }

    public void setMileage(Double mileage) {
        mMileage = mileage;
    }

    public Double getOdo() {
        return mOdo;
    }

    public void setOdo(Double odo) {
        mOdo = odo;
    }

    public Double getVol() {
        return mVol;
    }

    public void setVol(Double vol) {
        mVol = vol;
    }
}
