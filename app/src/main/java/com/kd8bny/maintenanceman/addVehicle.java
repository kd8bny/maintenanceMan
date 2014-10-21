package com.kd8bny.maintenanceman;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class addVehicle extends Fragment {

    private static final String TAG = "addFragment";

    //private careLog mlogValue;
    private vehicle mvehicle;
    private EditText mmake;
    private EditText mmodel;
    private EditText mengine;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvehicle = new vehicle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_vehicle, parent, false);
        mmake = (EditText)v.findViewById(R.id.val_spec_make);
        mmake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence value, int start, int before, int count) {
                mvehicle.setMfield(value.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

}
