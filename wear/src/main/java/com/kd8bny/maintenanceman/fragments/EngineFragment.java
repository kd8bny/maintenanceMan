package com.kd8bny.maintenanceman.fragments;

public class EngineFragment extends InfoFragment {
    private static final String TAG = "eng_frg";

    public EngineFragment() {}

    @Override
    public void dataInit(){
        mCase = 1;
        mCardInfo = mVehicle.getEngineSpecs();
    }
}
