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
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;

public class dialog_addTravelEntry extends DialogFragment {
    private static final String TAG = "dlg_add_trvl_evnt";

    private int RESULT_CODE;
    private Context mContext;
    private MaterialBetterSpinner vehicleSpinner;
    private MaterialEditText vDate, vOdo, vDest, vPurpose;

    private ArrayList<Vehicle> mRoster;
    private Vehicle mVehicle;
    private Travel mTravel, mOldTravel;
    private int mPos;
    private Boolean isNew = true;
    private ArrayList<String> mVehicleTitles;


    public dialog_addTravelEntry(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESULT_CODE = getTargetRequestCode();
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getArguments();
        mRoster = bundle.getParcelableArrayList("roster");
        mPos = bundle.getInt("pos", -1);

        mTravel = (Travel) bundle.getSerializable("event");
        if (mTravel == null) {
            mTravel = new Travel("");
            final Calendar cal = Calendar.getInstance();
            mTravel.setDate(cal.get(Calendar.MONTH) + 1
                    + "/" + cal.get(Calendar.DAY_OF_MONTH)
                    + "/" + cal.get(Calendar.YEAR));
        }else { //TODO make object implement clone
            isNew = false;
            mOldTravel = new Travel(mTravel.getRefID());
            mOldTravel.setDate(mTravel.getDate());
            mOldTravel.setStart(mTravel.getStart());
            mOldTravel.setDest(mTravel.getDest());
            mOldTravel.setPurpose(mTravel.getPurpose());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_travel_event, null);

        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_roster);
        vDate = (MaterialEditText) view.findViewById(R.id.val_date);
        vDate.setText(mTravel.getDate());
        vOdo = (MaterialEditText) view.findViewById(R.id.val_odo);
        vDest = (MaterialEditText) view.findViewById(R.id.val_dest);
        vPurpose = (MaterialEditText) view.findViewById(R.id.val_purpose);

        vehicleSpinner.setAdapter(setVehicles());
        if (mPos > -1) {
            mVehicle = mRoster.get(mPos);
            vehicleSpinner.setText(mVehicle.getTitle());
        }
        vOdo.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if (!isNew){
            vDate.setText(mTravel.getDate());
            vOdo.setText(mTravel.getStart().toString());
            vDest.setText(mTravel.getDest());
            vPurpose.setText(mTravel.getPurpose());
        }

        view.findViewById(R.id.val_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_datePicker datePicker = new dialog_datePicker();
                datePicker.setTargetFragment(dialog_addTravelEntry.this, 0);
                datePicker.show(getFragmentManager(), "datePicker");
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
                        int pos = mVehicleTitles.indexOf(vehicleSpinner.getText().toString());
                        mTravel.setRefID(mRoster.get(pos).getRefID());

                        mTravel.setDate(vDate.getText().toString());
                        mTravel.setStart(Double.parseDouble(vOdo.getText().toString()));
                        mTravel.setDest(vDest.getText().toString());
                        mTravel.setPurpose(vPurpose.getText().toString());

                        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
                        if (mOldTravel != null){
                            vehicleLogDBHelper.deleteEntry(mOldTravel);
                        }
                        vehicleLogDBHelper.insertEntry(mTravel);
                        dismiss();
                    }}});
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0:
                String val = data.getBundleExtra("bundle").getString("value");
                mTravel.setDate(val);
                vDate.setText(val);
                break;
        }
    }

    private ArrayAdapter<String> setVehicles(){
        mVehicleTitles = new ArrayList<>();
        for(Vehicle v : mRoster) {
            mVehicleTitles.add(v.getTitle());
        }
        return new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, mVehicleTitles);
    }

    public boolean isLegit(){
        if (mVehicleTitles.indexOf(vehicleSpinner.getText().toString()) == -1){
            vehicleSpinner.setError(getResources().getString(R.string.error_set_vehicle));
            return true;
        }
        if (vOdo.getText() == null){
            vOdo.setError(getString(R.string.error_start_val));
            return false;
        }
        if (vDest.getText() == null){
            vDest.setError(getString(R.string.error_dest_val));
            return false;
        }

        return true;
    }
}