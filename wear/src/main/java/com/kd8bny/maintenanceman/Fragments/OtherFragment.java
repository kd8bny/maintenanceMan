package com.kd8bny.maintenanceman.Fragments;

public class OtherFragment extends InfoFragment {
    private static final String TAG = "othr_frg";

    public OtherFragment() {}

    @Override
    public void dataInit(){
        mCardInfo = mVehicle.getOtherSpecs();
    }
}