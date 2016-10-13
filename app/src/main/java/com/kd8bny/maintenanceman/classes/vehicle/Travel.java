package com.kd8bny.maintenanceman.classes.vehicle;

/**
 * Created by kd8bny on 2/13/16.
 */
public class Travel extends Entry {
    private static final String TAG = "Business";

    private Double mStart;
    private Double mStop = -1.0;
    private Double mDelta;
    private String mDest;
    private String mPurpose = "";
    private String mStartClock;
    private String mStopClock = "";
    private String mTimeDelta;

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

    public String getStartClock() {
        return mStartClock;
    }

    public void setStartClock(String s) {
        mStartClock = s;
    }

    public String getStopClock() {
        return mStopClock;
    }

    public void setStopClock(String s) {
        mStopClock = s;
        /*stop - start and save*/
        mTimeDelta = "1.2kinda sorta";
    }

    public String getTimeDelta(){
        return mTimeDelta;
    }
}
