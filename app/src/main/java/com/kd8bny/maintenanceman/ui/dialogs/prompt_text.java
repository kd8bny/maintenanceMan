package com.kd8bny.maintenanceman.ui.dialogs;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.kd8bny.maintenanceman.R;


public class prompt_text extends DialogFragment {


    public prompt_text() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_prompt_text, null);



        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.spec_year)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel,null)
                .create();
    }
}
