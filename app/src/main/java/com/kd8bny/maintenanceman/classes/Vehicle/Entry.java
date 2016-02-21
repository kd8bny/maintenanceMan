package com.kd8bny.maintenanceman.classes.Vehicle;

import java.io.Serializable;

/**
 * Created by kd8bny on 2/13/16.
 */
public class Entry implements Serializable{
    private static final String TAG = "Entry";

    public String mRefID;
    public String mDate;
    public String mUnit;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getRefID() {
        return mRefID;
    }

    public void setRefID(String refID) {
        mRefID = refID;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }
}
