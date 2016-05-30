package com.kd8bny.maintenanceman.fragments;

public class PowerTrainFragment extends InfoFragment {
    private static final String TAG = "pwrtrn_frg";

    public PowerTrainFragment() {}

    @Override
    public void dataInit(){
        mCase = 2;
        mCardInfo = mVehicle.getPowerTrainSpecs();
    }
}