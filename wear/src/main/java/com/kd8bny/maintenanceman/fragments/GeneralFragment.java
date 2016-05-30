package com.kd8bny.maintenanceman.fragments;

public class GeneralFragment extends InfoFragment {
    private static final String TAG = "gen_frg";

    public GeneralFragment() {}

    @Override
    public void dataInit(){
        mCase = 0;
        mCardInfo = mVehicle.getGeneralSpecs();
    }
}
