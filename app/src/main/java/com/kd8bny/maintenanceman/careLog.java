package com.kd8bny.maintenanceman;

import android.util.Log;

/**
 * Created by kd8bny on 10/19/14.
 */
public class careLog {
    private static final String TAG = "careLog";

    private String mfield;

    public String getField() {
        return mfield;
    }

    public void setField(String mfield) {
        Log.d(TAG, "Made it");
        this.mfield = mfield;
    }
}
