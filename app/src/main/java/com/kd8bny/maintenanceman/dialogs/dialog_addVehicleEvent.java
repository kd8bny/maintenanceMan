package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.HashSet;


public class dialog_addVehicleEvent extends DialogFragment {
    private static final String TAG = "dlg_add_evnt";

    private int REQUEST_CODE = 1;
    private String label;
    private String value;
    private Boolean isEvent;

    public dialog_addVehicleEvent(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            label = args.getString("label");
            value = args.getString("value");
            isEvent = args.getBoolean("isEvent");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_vehicle_event, null);
        final MaterialAutoCompleteTextView editValue = (MaterialAutoCompleteTextView) view.findViewById(R.id.value);
        editValue.setHint(label);
        editValue.setFloatingLabelText(label);

        if (isEvent){
            VehicleLogDBHelper vehicleDB = new VehicleLogDBHelper(this.getActivity());
            ArrayList<String> eventList = vehicleDB.getEvents(getActivity().getApplicationContext());
            //TODO add premade lists items

            // Remove dup's
            HashSet tempHS = new HashSet();
            tempHS.addAll(eventList);
            eventList.clear();
            eventList.addAll(tempHS);

            editValue.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, eventList));
        }
        editValue.setText(value);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle("Set:")
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            value = editValue.getText().toString();
                            sendResult();
                        }
                    }
            )
            .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // DO SOMETHING
                        }
                    }
            );

        alertDialog.setView(view);

        return alertDialog.create();
    }

    private void sendResult() {
        Intent intent = new Intent();
        intent.putExtra("label", label);
        intent.putExtra("value", value);

        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, intent);
    }
}