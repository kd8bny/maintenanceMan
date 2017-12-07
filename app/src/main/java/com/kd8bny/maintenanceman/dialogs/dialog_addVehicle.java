package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.kd8bny.maintenanceman.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public class dialog_addVehicle extends DialogFragment {
    private static final String TAG = "dlg_add_vehicle";

    private static final int REQUEST_CODE = 0;

    private Context mContext;
    private MaterialBetterSpinner vehicleSpinner;
    private MaterialEditText yearVal;
    private MaterialEditText makeVal;
    private MaterialEditText modelVal;
    private String YEAR;
    private String MAKE;
    private String MODEL;
    private Boolean isBusiness = false;
    private Boolean isEdit;

    private String [] mvehicleTypes;

    public dialog_addVehicle(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            YEAR = bundle.getString("year");
            MAKE = bundle.getString("make");
            MODEL = bundle.getString("model");
            isEdit = bundle.getBoolean("isEdit");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_vehicle, null);

        //Spinner
        //vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_vehicle_type);
        /*mvehicleTypes = mContext.getResources().getStringArray(R.array.vehicle_type);
        vehicleSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // vehicle.setVehicleType(vehicleSpinner.getText().toString());
            }});
        vehicleSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, mvehicleTypes));*/
      //  businessVal = (CheckBox) view.findViewById(R.id.checkbox_business);*/

        yearVal = (MaterialEditText) view.findViewById(R.id.year_val);
        yearVal.setHint(view.getResources().getString(R.string.hint_year));
        yearVal.setFloatingLabelText(view.getResources().getString(R.string.hint_year));

        makeVal = (MaterialEditText) view.findViewById(R.id.make_val);
        makeVal.setHint(view.getResources().getString(R.string.hint_make));
        makeVal.setFloatingLabelText(view.getResources().getString(R.string.hint_make));

        modelVal = (MaterialEditText) view.findViewById(R.id.model_val);
        modelVal.setHint(view.getResources().getString(R.string.hint_model));
        modelVal.setFloatingLabelText(view.getResources().getString(R.string.hint_model));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle(view.getResources().getString(R.string.title_dialog_add))
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //Leave blank as we override onStart()
                }})
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (isEdit){
                        getDialog().dismiss();
                    }
                    getActivity().finish();
                }
            });

        alertDialog.setView(view);

        return alertDialog.create();
    }

    @Override
    public void onStart() {
        /*Used to keep alert dialog open for error check*/
        super.onStart();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.setCanceledOnTouchOutside(false);
        Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                YEAR = yearVal.getText().toString();
                MAKE = makeVal.getText().toString();
                MODEL = modelVal.getText().toString();
                if (isLegit()) {
                    sendResult();
                    alertDialog.dismiss();
                }
            }
        });
    }

    private boolean isLegit(){
        String error = getResources().getString(R.string.error_required);
        if (YEAR.isEmpty()){
            yearVal.setError(error);
            return false;
        }
        if (makeVal.getText() == null){
            makeVal.setError(error);
            return false;
        }
        if (modelVal.getText() == null){
            modelVal.setError(error);
            return false;
        }
        return true;
    }

    private void sendResult() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(YEAR);
        temp.add(MAKE);
        temp.add(MODEL);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fieldData", temp);

        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE,
                new Intent().putExtra("bundle", bundle));
    }
}