package com.kd8bny.maintenanceman.Fragments;

public class EngineFragment extends InfoFragment {
    private static final String TAG = "eng_frg";

    public EngineFragment() {}

    @Override
    public void dataInit(){
        mCardInfo = mVehicle.getEngineSpecs();
    }
}
