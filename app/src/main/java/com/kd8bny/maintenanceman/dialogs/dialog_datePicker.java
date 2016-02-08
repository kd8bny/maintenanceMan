package com.kd8bny.maintenanceman.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;


public class dialog_datePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "dlg_dtPckr";

    private String label;
    private String value;

    public dialog_datePicker (){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Bundle args = getArguments();
        if (args != null){
            label = args.getString("label");
            value = args.getString("value");
        }

        return new DatePickerDialog(getActivity(), this,  year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Intent intent = new Intent();
        intent.putExtra("label", label);
        intent.putExtra("value", month + 1 + "/" + day + "/" + year);

        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
    }
}
