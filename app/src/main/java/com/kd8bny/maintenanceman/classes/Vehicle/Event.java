package com.kd8bny.maintenanceman.classes.Vehicle;

import java.io.Serializable;

/**
 * Created by kd8bny on 2/13/16.
 */
public class Event implements Serializable{
    private static final String TAG = "Event";

    public int mIcon = 0;
    public String mRefID;
    public String mDate;
    public String mEvent;
    public String mOdometer;
    public String mPrice = "";
    public String mComment = "";
    public String mUnit;

    public Event(String refID){
        mRefID = refID;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        mEvent = event;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public String getOdometer() {
        return mOdometer;
    }

    public void setOdometer(String odometer) {
        mOdometer = odometer;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
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
