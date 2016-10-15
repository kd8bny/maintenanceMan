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
import com.kd8bny.maintenanceman.classes.utils.Utils;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;

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
        mTravel.setDateEnd(new DateTime().toString());
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
                        mTravel.setStop(Double.parseDouble(vOdo.getText().toString()));

                        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
                        vehicleLogDBHelper.deleteEntry(mTravel);
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
        Bundle bundle = data.getBundleExtra("bundle");
        DateTime dateTime = new DateTime(mTravel.getDate());
        MutableDateTime mutableDateTime = dateTime.toMutableDateTime();
        switch (resultCode) {
            case 0:
                mutableDateTime.setDate(bundle.getInt("year"), bundle.getInt("month"), bundle.getInt("day"));
                vDate.setText(new Utils(mContext).toFriendlyDate(mutableDateTime.toDateTime()));
                break;

            case 1:
                mutableDateTime.setTime(bundle.getInt("hour"), bundle.getInt("min"), 0, 0);
                vStopTime.setText(new Utils(mContext).toFriendlyTime(mutableDateTime.toDateTime()));
                break;
        }
        mTravel.setDateEnd(mutableDateTime.toDateTime().toString());
    }

    public boolean isLegit(){
        if (vStopTime.getText() == null){
            vStopTime.setError(getString(R.string.error_start_val));
            return false;
        }
        if (vOdo.getText() == null){
            vOdo.setError(getString(R.string.error_start_val));
            return false;
        }
        if (Double.parseDouble(vOdo.getText().toString()) < mTravel.getStart()){
            vOdo.setError(getString(R.string.error_odo_val));
            return false;
        }

        return true;
    }
}