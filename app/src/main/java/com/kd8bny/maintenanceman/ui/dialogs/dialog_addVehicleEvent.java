package com.kd8bny.maintenanceman.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.HashSet;


public class dialog_addVehicleEvent extends DialogFragment{
    private static final String TAG = "dlg_add_evnt";

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
            vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
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
                            sendResult(0);
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

    private void sendResult(int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.putExtra("label", label);
        intent.putExtra("value", value);

        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, intent);
    }
}