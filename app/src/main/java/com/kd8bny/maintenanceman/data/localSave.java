package com.kd8bny.maintenanceman.data;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kd8bny on 10/23/14.
 */
public class localSave {
    private Context mContext;
    private String mFilename;

    public void localSave(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    //public void saveData(ArrayList<vehicleLog> make);

}
