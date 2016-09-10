package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public class dialog_addMileageEntry extends DialogFragment {
    private static final String TAG = "dlg_add_mileage";

    private MaterialBetterSpinner vehicleSpinner;
    private MaterialEditText vTripValue, vFillValue, vPriceValue;

    private ArrayList<Vehicle> roster;
    private ArrayList<String> singleVehicle = new ArrayList<>();
    private int RESULT_CODE;
    private Mileage mMileage;
    private int mPos;

    public dialog_addMileageEntry(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESULT_CODE = getTargetRequestCode();
        Bundle bundle = getArguments();
        roster = bundle.getParcelableArrayList("roster");
        mMileage = (Mileage) bundle.getSerializable("event");
        mPos = bundle.getInt("pos");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_mileage_event, null);
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_roster);
        vehicleSpinner.setAdapter(setVehicles());
        vTripValue = (MaterialEditText) view.findViewById(R.id.tripometer);
        vFillValue = (MaterialEditText) view.findViewById(R.id.fill_vol);
        vPriceValue = (MaterialEditText) view.findViewById(R.id.price);
        vTripValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        vFillValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        vPriceValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if (mMileage != null){
            vTripValue.setText(mMileage.getTripometer().toString());
            vFillValue.setText(mMileage.getFillVol().toString());
            vPriceValue.setText(mMileage.getPrice().toString());
        }

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
                        ArrayList<Double> arrayList = new ArrayList<>();
                        arrayList.add(Double.parseDouble(vTripValue.getText().toString()));
                        arrayList.add(Double.parseDouble(vFillValue.getText().toString()));
                        arrayList.add(Double.parseDouble(vPriceValue.getText().toString()));

                        sendResult(arrayList);
                        alertDialog.dismiss();
                    }
                }
            });
        }
    }

    private void sendResult(ArrayList<Double> values) {
        Bundle bundle = new Bundle();
        int pos = singleVehicle.indexOf(vehicleSpinner.getText().toString());
        bundle.putInt("pos", pos);
        bundle.putDouble("trip", values.get(0));
        bundle.putDouble("fill", values.get(1));
        bundle.putDouble("price", values.get(2));
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                new Intent().putExtra("bundle", bundle));
    }

    private ArrayAdapter<String> setVehicles(){
        for(Vehicle v : roster) {
            singleVehicle.add(v.getTitle());
        }
        return new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, singleVehicle);
    }

    private Boolean isLegit(){
        if (singleVehicle.indexOf(vehicleSpinner.getText().toString()) == -1) {
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