package com.kd8bny.maintenanceman.classes.Vehicle;

/**
 * Created by kd8bny on 2/13/16.
 */
public class Travel extends Entry {
    private static final String TAG = "Business";

    public Double mStart;
    public Double mStop = -1.0;
    public Double mDelta;
    public String mDest;
    public String mPurpose = "";

    public Travel(String refID) {
        mRefID = refID;
    }

    public Double getStart() {
        return mStart;
    }

    public void setStart(Double start) {
        mStart = start;
    }

    public Double getStop() {
        return mStop;
    }

    public void setStop(Double stop) {
        mStop = stop;
        mDelta = mStop - mStart;
    }

    public Double getDelta() {
        return mDelta;
    }

    public String getDest() {
        return mDest;
    }

    public void setDest(String price) {
        mDest = price;
    }

    public String getPurpose() {
        return mPurpose;
    }

    public void setPurpose(String comment) {
        mPurpose = comment;
    }
}
