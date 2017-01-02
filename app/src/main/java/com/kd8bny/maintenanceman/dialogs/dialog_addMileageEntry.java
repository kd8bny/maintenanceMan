package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.utils.Utils;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class dialog_addMileageEntry extends DialogFragment {
    private static final String TAG = "dlg_add_mileage";

    private int RESULT_CODE;
    private Context mContext;
    private MaterialBetterSpinner vehicleSpinner;
    private MaterialEditText vDate, vTripValue, vFillValue, vPriceValue;

    private ArrayList<Vehicle> mRoster;
    private Vehicle mVehicle;
    private Mileage mMileage, mOldMileage;
    private int mPos;
    private ArrayList<String> mVehicleTitles;
    private Boolean isNew = true;


    public dialog_addMileageEntry(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESULT_CODE = getTargetRequestCode();
        mContext = getContext();

        Bundle bundle = getArguments();
        mMileage = (Mileage) bundle.getSerializable("event");

        mRoster = bundle.getParcelableArrayList("roster");
        mPos = bundle.getInt("pos", -1);

        mMileage = (Mileage) bundle.getSerializable("event");
        if (mMileage == null) {
            mMileage = new Mileage("");
            mMileage.setDate(new DateTime().toString());
        }else { //TODO make object implement clone
            isNew = false;
            mOldMileage = new Mileage(mMileage.getRefID());
            mOldMileage.setDate(mMileage.getDate());
            mOldMileage.setMileage(
                    mMileage.getTripometer(),
                    mMileage.getFillVol(),
                    mMileage.getPrice());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_mileage_event, null);
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_roster);
        vDate = (MaterialEditText) view.findViewById(R.id.date);
        vTripValue = (MaterialEditText) view.findViewById(R.id.tripometer);
        vFillValue = (MaterialEditText) view.findViewById(R.id.fill_vol);
        vPriceValue = (MaterialEditText) view.findViewById(R.id.price);

        vehicleSpinner.setAdapter(setVehicles());
        if (mPos > -1) {
            mVehicle = mRoster.get(mPos);
            vehicleSpinner.setText(mVehicle.getTitle());
        }
        vDate.setText(new Utils(mContext).toFriendlyDate(new DateTime(mMileage.getDate())));
        vTripValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        vFillValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        vPriceValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if (!isNew){
            vDate.setText(new Utils(mContext).toFriendlyDate(new DateTime(mOldMileage.getDate())));
            vTripValue.setText(mOldMileage.getTripometer().toString());
            vFillValue.setText(mOldMileage.getFillVol().toString());
            vPriceValue.setText(mOldMileage.getPrice().toString());
        }

        view.findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_datePicker datePicker = new dialog_datePicker();
                datePicker.setTargetFragment(dialog_addMileageEntry.this, 0);
                datePicker.show(getFragmentManager(), "datePicker");
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle(view.getResources().getString(R.string.title_mileage_add))
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
                        //save
                        int pos = mVehicleTitles.indexOf(vehicleSpinner.getText().toString());
                        mMileage.setRefID(mRoster.get(pos).getRefID());
                        mMileage.setMileage(
                                Double.parseDouble(vTripValue.getText().toString()),
                                Double.parseDouble(vFillValue.getText().toString()),
                                Double.parseDouble(vPriceValue.getText().toString()));
                        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
                        if (mOldMileage != null){
                            vehicleLogDBHelper.deleteEntry(mOldMileage);
                        }
                        vehicleLogDBHelper.insertEntry(mMileage);

                        dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("event", mMileage);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                                new Intent().putExtra("bundle", bundle));
                    }}});
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        switch (resultCode) {
            case 0:
                DateTime dateTime = new DateTime().withDate(bundle.getInt("year"),
                        bundle.getInt("month"), bundle.getInt("day"));
                mMileage.setDate(dateTime.toString());
                vDate.setText(new Utils(mContext).toFriendlyDate(dateTime));
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

    private Boolean isLegit(){
        if (mVehicleTitles.indexOf(vehicleSpinner.getText().toString()) == -1) {
            vehicleSpinner.setError(getResources().getString(R.string.error_set_vehicle_type));

            return false;
        }
        if (vTripValue.getText().toString().matches("")){
            vTripValue.setError(getResources().getString(R.string.error_field_val));

            return false;
        }
        if (vFillValue.getText().toString().matches("")){
            vFillValue.setError(getResources().getString(R.string.error_field_val));

            return false;
        }
        if (vPriceValue.getText().toString().matches("")){
            vPriceValue.setError(getResources().getString(R.string.error_field_val));

            return false;
        }

        return true;
    }
}