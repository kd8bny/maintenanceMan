package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

public class dialog_addField extends DialogFragment {
    private static final String TAG = "dlg_add_fld";

    private int RESULT_CODE = 1;

    public MaterialBetterSpinner spinnerFieldType;
    public MaterialAutoCompleteTextView editFieldName;
    public MaterialAutoCompleteTextView editFieldVal;
    public String fieldType;
    public String fieldName;
    public String fieldVal;
    public String [] mfieldTypes;
    public Integer recyclerPosition;

    public dialog_addField(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            Log.d(TAG, "editing");
            RESULT_CODE = 2;
            recyclerPosition = bundle.getInt("pos");
            ArrayList<String> fieldData = bundle.getStringArrayList("field");
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
        if (RESULT_CODE == 2){
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
                        sendResult();
                        alertDialog.dismiss();
                    }
                }
            });
        }
    }

    private void sendResult() {
        Bundle bundle = new Bundle();
        ArrayList<String> temp = new ArrayList<>();
        temp.add(fieldType);
        temp.add(fieldName);
        temp.add(fieldVal);

        if (RESULT_CODE == 2){
            bundle.putInt("pos", recyclerPosition);
        }
        bundle.putStringArrayList("fieldData", temp);

        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                new Intent().putExtra("bundle", bundle));
    }

    public boolean isLegit(){
        if (Arrays.asList(mfieldTypes).indexOf(spinnerFieldType.getText().toString()) == -1){
            spinnerFieldType.setError(getResources().getString(R.string.error_set_vehicle_type));
            return true;
        }
        if (fieldName.isEmpty()){
            editFieldName.setError(getResources().getString(R.string.error_field_label));
            return true;
        }
        if (fieldVal.isEmpty()){
            editFieldVal.setError(getResources().getString(R.string.error_field_val));
            return true;
        }

        return false;
    }
}