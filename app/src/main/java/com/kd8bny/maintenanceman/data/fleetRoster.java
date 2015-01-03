package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.EditText;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;
import java.util.Date;

public class fleetRoster {
    private static final String TAG = "Fleet Roster getter/setter";

    private String mmake;
    private String myear;
    private String mmodel;
    private String mengine;

    public String getMake() {
        return mmake;
    }

    public void setMake(String mmake) {

        this.mmake = mmake;
    }

    public String getModel() {
        return mmodel;
    }

    public void setModel(String mmodel) {
        this.mmodel = mmodel;
    }

    public String getEngine() {
        return mengine;
    }

    public void setEngine(String mengine) {
        this.mengine = mengine;
    }

    public String getYear() {
        return myear;
    }

    public void setYear(String myear) {
        this.myear = myear;
    }

    /*@Override
    public String toString() {
        return mvehicle;
    }*/
}
