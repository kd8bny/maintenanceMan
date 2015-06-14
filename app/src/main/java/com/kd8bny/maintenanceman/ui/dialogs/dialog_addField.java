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
import android.widget.Button;

import com.kd8bny.maintenanceman.R;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Arrays;


public class dialog_addField extends DialogFragment{
    private static final String TAG = "dlg_add_fld";

    public MaterialBetterSpinner spinnerFieldType;
    public MaterialAutoCompleteTextView editFieldName;
    public MaterialAutoCompleteTextView editFieldVal;
    public String fieldType;
    public String fieldName;
    public String fieldVal;
    public Boolean isEdit = false;
    public Boolean isRequired = false;
    public String [] mfieldTypes;

    public dialog_addField(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            isEdit = true;
            ArrayList<String> fieldData = (ArrayList<String>) args.getSerializable("field");
            isRequired = args.getBoolean("isRequired");
            fieldType = fieldData.get(0);
            fieldName = fieldData.get(1);
            fieldVal = fieldData.get(2);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_field, null);

        //Spinner
        spinnerFieldType = (MaterialBetterSpinner) view.findViewById(R.id.spinner_field_type);
        spinnerFieldType.setText(fieldType);
        mfieldTypes = getActivity().getResources().getStringArray(R.array.field_type);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, mfieldTypes);
        spinnerFieldType.setAdapter(spinnerAdapter);

        editFieldName = (MaterialAutoCompleteTextView) view.findViewById(R.id.field_name);
        editFieldVal = (MaterialAutoCompleteTextView) view.findViewById(R.id.field_val);
        if(isRequired){
            editFieldName.setEnabled(false);
            editFieldVal.requestFocus();
        }
        if (isEdit){
            editFieldName.setText(fieldName);
            editFieldVal.setText(fieldVal);
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle(R.string.title_add_field)
            .setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Leave blank as we override onStart()
                    }
                })
            .setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dismiss();
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
                    fieldType = spinnerFieldType.getText().toString();
                    fieldName = editFieldName.getText().toString();
                    fieldVal = editFieldVal.getText().toString();

                    if(!isLegit()) {
                        sendResult(0);
                        alertDialog.dismiss();
                    }
                }
            });
        }
    }

    private void sendResult(int REQUEST_CODE) {
        Intent intent = new Intent();
        ArrayList<String> temp = new ArrayList<>();
        temp.add(fieldType);
        temp.add(fieldName);
        temp.add(fieldVal);

        if (isEdit){
            intent.putExtra("action", "edit");
        }else{
            intent.putExtra("action", "new");
        }

        intent.putExtra("fieldData", temp);

        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, intent);
    }

    public boolean isLegit(){
        Boolean error = false;
        if (Arrays.asList(mfieldTypes).indexOf(spinnerFieldType.getText().toString()) == -1){
            spinnerFieldType.setError(getResources().getString(R.string.error_set_vehicle_type));

            error = true;
        }
        if (fieldName.equals("")){
            editFieldName.setError(getResources().getString(R.string.error_field_label));

            error = true;
        }
        if (fieldVal.equals("")){
            editFieldVal.setError(getResources().getString(R.string.error_field_val));

            error = true;
        }

        return error;
    }
}