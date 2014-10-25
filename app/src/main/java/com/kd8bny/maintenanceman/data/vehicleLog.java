package com.kd8bny.maintenanceman.data;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kd8bny on 10/20/14.
 */
public class vehicleLog {
    private static final String TAG = "careLog";

    private long mID;
    private String mvehicle;
    private ArrayList<String> mData;

    private ArrayList mmake;
    private String mfield;
    private String mmodel;
    private String mengine;
    private Date mDate;

    public vehicleLog(){
        mID = -1;
    }

    public long getID() {
        return mID;
    }

    public void setID(long mID) {
        this.mID = mID;
    }

    public String getVehicle() {
        return mvehicle;
    }

    public void setVehicle(String mvehicle) {
        this.mvehicle = mvehicle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getMfield() {
        return mfield;
    }

    public void setMfield(String mfield) {
        this.mfield = mfield;
    }

    public ArrayList getMake() {
        return mmake;
    }

    public void setMake(ArrayList mmake) {

        this.mmake = mmake;
    }

    public String getMmodel() {
        return mmodel;
    }

    public void setMmodel(String mmodel) {
        this.mmodel = mmodel;
    }

    public String getMengine() {
        return mengine;
    }

    public void setMengine(String mengine) {
        this.mengine = mengine;
    }

    public ArrayList getData(Resources resources) {
        ArrayList mData = new ArrayList();
        //TODO Get vehicles and add to array Collection
        mData.add(resources.getString(R.string.spec_make));
        mData.add(resources.getString(R.string.spec_model));
        mData.add(resources.getString(R.string.spec_year));
        mData.add(resources.getString(R.string.spec_engine));
        mData.add(resources.getString(R.string.spec_filter_oil));
        mData.add(resources.getString(R.string.spec_filter_fuel));
        return mData;
    }

    public void setData(ArrayList mData) {
        this.mData = mData;
    }
}
