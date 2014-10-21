package com.kd8bny.maintenanceman;


import java.util.Date;

/**
 * Created by kd8bny on 10/20/14.
 */
public class vehicle {
    private static final String TAG = "careLog";

    private String mfield;
    private String mmake;
    private String mmodel;
    private String mengine;
    private Date mDate;

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getMfield() {
        return mfield;
    }

    public void setMfield(String mfield) {
        this.mfield = mfield;
    }

    public String getMmake() {
        return mmake;
    }

    public void setMmake(String mmake) {
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
}
