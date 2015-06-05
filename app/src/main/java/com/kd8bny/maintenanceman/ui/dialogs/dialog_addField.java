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
import android.widget.EditText;

import com.kd8bny.maintenanceman.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;


public class dialog_addField extends DialogFragment{
    private static final String TAG = "dlg_add_fld";

    private MaterialBetterSpinner spinnerFieldType;
    private String fieldType;
    private String fieldName;
    private String fieldVal;
    private Boolean isEdit = false;
    private Boolean isRequired = false;

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
        final String [] mfieldTypes = getActivity().getResources().getStringArray(R.array.field_type);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, mfieldTypes);
        spinnerFieldType.setAdapter(spinnerAdapter);
        //TODO set position on edit

        final EditText editFieldName = (EditText) view.findViewById(R.id.field_name);
        final EditText editFieldVal = (EditText) view.findViewById(R.id.field_val);
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
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // DO SOMETHING
                            fieldType = spinnerFieldType.getText().toString();
                            fieldName = editFieldName.getText().toString();
                            fieldVal = editFieldVal.getText().toString();
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
}