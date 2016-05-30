package com.kd8bny.maintenanceman.Fragments;

public class OtherFragment extends InfoFragment {
    private static final String TAG = "othr_frg";

    public OtherFragment() {}

    @Override
    public void dataInit(){
        mCase = 3;
        mCardInfo = mVehicle.getOtherSpecs();
    }
}