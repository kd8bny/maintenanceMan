package com.kd8bny.maintenanceman.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kd8bny.maintenanceman.R;

import java.util.Calendar;


public class dialog_datePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "dialog_datePicker";

    public dialog_datePicker (){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this,  year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        ((EditText) getActivity().findViewById(R.id.val_spec_date)).setText(month+1 + "/" + day + "/" + year);
    }

}
