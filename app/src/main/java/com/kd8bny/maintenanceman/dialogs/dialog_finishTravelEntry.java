package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Locale;

public class dialog_finishTravelEntry extends DialogFragment {
    private static final String TAG = "dlg_add_trvl_evnt";

    private int RESULT_CODE;
    private Context mContext;
    private MaterialEditText vDate, vOdo, vStopTime;

    private ArrayList<Vehicle> mRoster;
    private Vehicle mVehicle;
    private Travel mTravel;
    private int mPos;


    public dialog_finishTravelEntry(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESULT_CODE = getTargetRequestCode();
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getArguments();
        mRoster = bundle.getParcelableArrayList("roster");
        mPos = bundle.getInt("pos", -1);

        mTravel = (Travel) bundle.getSerializable("event");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_finish_travel_event, null);

        vDate = (MaterialEditText) view.findViewById(R.id.val_date);
        vDate.setText(mTravel.getDate());
        vStopTime = (MaterialEditText) view.findViewById(R.id.val_time);
        vOdo = (MaterialEditText) view.findViewById(R.id.val_odo);
        vOdo.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        view.findViewById(R.id.val_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_datePicker datePicker = new dialog_datePicker();
                datePicker.setTargetFragment(dialog_finishTravelEntry.this, 0);
                datePicker.show(getFragmentManager(), "datePicker");
            }
        });

        view.findViewById(R.id.val_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_timePicker timePicker = new dialog_timePicker();
                timePicker.setTargetFragment(dialog_finishTravelEntry.this, 1);
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(view.getResources().getString(R.string.title_add_vehicle_event))
                .setPositiveButton(view.getResources().getString(R.string.button_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Leave blank as we override onStart()
                            }
                        })
                .setNegativeButton(view.getResources().getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // DO SOMETHING
                            }
                        });

        alertDialog.setView(view);

        return alertDialog.create();
    }

    @Override
    public void onStart(){
        /*Used to keep alert dialog open for error check*/
        super.onStart();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if(alertDialog != null){
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (isLegit()) {
                        mTravel.setDate(vDate.getText().toString());
                        mTravel.setStop(Double.parseDouble(vOdo.getText().toString()));

                        //TODO remove old entry

                        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
                        vehicleLogDBHelper.insertEntry(mTravel);
                        dismiss();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                                new Intent());
                    }}});
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        switch (resultCode) {
            case 0:
                bundle = data.getBundleExtra("bundle");
                String val = bundle.getString("value");
                mTravel.setDate(val);
                vDate.setText(val);
                break;

            case 1:
                bundle = data.getBundleExtra("bundle");
                int hour = bundle.getInt("hour");
                int min = bundle.getInt("min");
                //mTravel.setStartClock(String.format(Locale.ENGLISH, "%s:%s", hour, min));
                String xM = "AM";
                if (hour > 12){
                    xM = "PM";
                }
                hour = hour % 12;
                if (hour == 0){
                    hour = 12;
                }

                vStopTime.setText(String.format(Locale.ENGLISH, "%s:%s %s", hour, min, xM));
                break;
        }
    }

    public boolean isLegit(){
        if (vOdo.getText() == null){
            vOdo.setError(getString(R.string.error_start_val));
            return false;
        }

        //TODO cehck if delta is neg
        //check time

        return true;
    }
}