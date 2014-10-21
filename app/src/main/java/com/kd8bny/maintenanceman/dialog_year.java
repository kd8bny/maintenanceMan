package com.kd8bny.maintenanceman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;


public class dialog_year extends DialogFragment {


    public dialog_year() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_year, null);
        NumberPicker np = (NumberPicker) v.findViewById(R.id.picker_year);
        np.setMaxValue(2020);
        np.setMinValue(1950);
        np.setValue(2000);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.spec_year)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

}
