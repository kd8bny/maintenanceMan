/*package com.kd8bny.maintenanceman.charts;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;

class MyXAxisValueFormatter implements AxisValueFormatter {

    private ArrayList<String> mValues;

    public MyXAxisValueFormatter(ArrayList<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        Log.d("format", value + ":" + mValues);
        if (value > 1) {
            return mValues.get((int) value) + "";
        }

        return mValues.get(0) + "";
    }

    /** this is only needed if numbers are returned, else return 0 */
    /*@Override
    public int getDecimalDigits() { return 0; }
}*/