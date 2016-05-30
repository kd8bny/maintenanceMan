package com.kd8bny.maintenanceman.Fragments;

public class PowerTrainFragment extends InfoFragment {
    private static final String TAG = "pwrtrn_frg";

    public PowerTrainFragment() {}

    @Override
    public void dataInit(){
        mCardInfo = mVehicle.getPowerTrainSpecs();
    }
}