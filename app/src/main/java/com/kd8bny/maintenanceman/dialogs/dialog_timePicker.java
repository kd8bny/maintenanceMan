package com.kd8bny.maintenanceman.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import org.joda.time.Instant;

import java.util.Calendar;

public class dialog_timePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private static final String TAG = "dlg_tmPckr";

    private int RESULT_CODE;

    public dialog_timePicker(){
        RESULT_CODE = 1;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this,  hour, min, false);
    }

    public void onTimeSet(TimePicker view, int hour, int min) {
        //Instant instant
        Bundle bundle = new Bundle();
        bundle.putInt("hour", hour);
        bundle.putInt("min", min);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                new Intent().putExtra("bundle", bundle));
    }
}
