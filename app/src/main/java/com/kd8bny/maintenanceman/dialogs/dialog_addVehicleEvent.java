package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.util.ArrayList;


public class dialog_addVehicleEvent extends DialogFragment {
    private static final String TAG = "dlg_add_evnt";

    private int RESULT_CODE;
    private Maintenance mMaintenance;

    public dialog_addVehicleEvent(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESULT_CODE = getTargetRequestCode();
        Bundle bundle = getArguments();
        mMaintenance = (Maintenance) bundle.getSerializable("event");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_vehicle_event, null);
        final MaterialAutoCompleteTextView editValue = (MaterialAutoCompleteTextView) view.findViewById(R.id.value);
        Resources mRes = view.getResources();
        switch (RESULT_CODE){//TODO get data for edit
            case 2:
                editValue.setHint(mRes.getString(R.string.field_odo));
                editValue.setFloatingLabelText(mRes.getString(R.string.field_odo));
                editValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                editValue.setText(mMaintenance.getOdometer());
                break;

            case 3:
                editValue.setHint(mRes.getString(R.string.field_event));
                editValue.setFloatingLabelText(mRes.getString(R.string.field_event));
                editValue.setText(mMaintenance.getEvent());

                VehicleLogDBHelper vehicleDB = VehicleLogDBHelper.getInstance(getActivity().getApplicationContext());
                ArrayList<String> eventList = new ArrayList<>();
                eventList.addAll(vehicleDB.getEntries());
                //TODO add premade lists items

                editValue.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, eventList));
                break;

            case 4:
                editValue.setHint(mRes.getString(R.string.field_price));
                editValue.setFloatingLabelText(mRes.getString(R.string.field_price));
                editValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editValue.setText(mMaintenance.getPrice());
                break;

            case 5:
                editValue.setHint(mRes.getString(R.string.field_comment));
                editValue.setFloatingLabelText(mRes.getString(R.string.field_comment));
                editValue.setInputType(InputType.TYPE_CLASS_TEXT);
                editValue.setText(mMaintenance.getComment());
                break;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle("Set:")
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            sendResult(editValue.getText().toString());
                        }
                    })
            .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // DO SOMETHING
                        }
                    });

        alertDialog.setView(view);

        return alertDialog.create();
    }

    private void sendResult(String value) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                new Intent().putExtra("value", value));
    }
}