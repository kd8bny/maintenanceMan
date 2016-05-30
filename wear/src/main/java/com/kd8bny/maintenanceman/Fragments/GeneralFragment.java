package com.kd8bny.maintenanceman.Fragments;

public class GeneralFragment extends InfoFragment {
    private static final String TAG = "gen_frg";

    public GeneralFragment() {}

    @Override
    public void dataInit(){
        mCardInfo = mVehicle.getGeneralSpecs();
    }
}
