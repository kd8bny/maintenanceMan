package com.kd8bny.maintenanceman.data;

import android.content.res.Resources;
import android.util.Log;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

/**
 * Created by kd8bny on 1/1/15.
 * Purpose is to save all make and model data
 */
public class vehicleArray {
    private static final String TAG = "vehicleArray";

    private ArrayList mMakeArray = new ArrayList();
    private ArrayList mModelArray = new ArrayList();

    public ArrayList getMakeArray(Resources resources) {

        mMakeArray.add(resources.getString(R.string.make_chevy));
        mMakeArray.add(resources.getString(R.string.make_dodge));
        mMakeArray.add(resources.getString(R.string.make_ford));
        mMakeArray.add(resources.getString(R.string.make_saturn));
        return mMakeArray;
    }

    public ArrayList getModelArray(Resources resources, String make) {
        if (make=="chevy") {//Rid???
            mModelArray.add(resources.getString(R.string.model_camero));
        }
        else if(make=="dodge") {
            mModelArray.add(resources.getString(R.string.model_charger));
        }
        else if(make=="ford") {
            mModelArray.add(resources.getString(R.string.model_must));
        }
        else if(make=="saturn") {
            mModelArray.add(resources.getString(R.string.model_ion));
        }
        else{
            Log.d(TAG,"No model found" );
        }
        return mModelArray;
    }
}
