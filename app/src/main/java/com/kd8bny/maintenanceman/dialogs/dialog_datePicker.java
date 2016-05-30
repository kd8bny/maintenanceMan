package com.kd8bny.maintenanceman.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;


public class dialog_datePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "dlg_dtPckr";

    private int RESULT_CODE;

    public dialog_datePicker (){
        RESULT_CODE = getTargetRequestCode();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this,  year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = String.format("%s/%s/%s", month + 1, day, year);
        Bundle bundle = new Bundle();
        bundle.putString("value", date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                new Intent().putExtra("bundle", bundle));
    }
}
