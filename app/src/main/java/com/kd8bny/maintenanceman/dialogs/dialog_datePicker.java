package com.kd8bny.maintenanceman.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;


import org.joda.time.DateTime;


public class dialog_datePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "dlg_dtPckr";

    private int RESULT_CODE;

    public dialog_datePicker (){
        RESULT_CODE = getTargetRequestCode();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();

        return new DatePickerDialog(getActivity(), this,  year, month-1, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month+1);
        bundle.putInt("day", day);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                new Intent().putExtra("bundle", bundle));
    }
}
