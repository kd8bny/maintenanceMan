package com.kd8bny.maintenanceman;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;


public class addVehicle extends Fragment {

    private static final String TAG = "addFragment";

    //private careLog mlogValue;
    private vehicle mvehicle;
    private EditText mmake;
    private EditText mmodel;
    private EditText mengine;
    private NumberPicker myear;

    private Button mYearButton;
    private static final String dialog_year = "Vehicle Year"; //TODO:String??

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

        mYearButton = (Button)v.findViewById(R.id.button_year);
        mYearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getFragmentManager();
                dialog_year dialog = new dialog_year();
                dialog.show(fm, dialog_year);
            }
        });

        return v;
    }
}

