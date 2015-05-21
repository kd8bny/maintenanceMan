package com.kd8bny.maintenanceman.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;


public class dialog_addField_required extends DialogFragment{
    private static final String TAG = "dlg_add_fld";

    private String fieldName;
    private String fieldVal;
    private Boolean isEdit = false;

    public dialog_addField_required(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            isEdit = true;
            ArrayList<String> fieldData = (ArrayList<String>) args.getSerializable("field");
            fieldName = fieldData.get(1);
            fieldVal = fieldData.get(2);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_field_required, null);

        final EditText editFieldVal = (EditText) view.findViewById(R.id.field_val);

        if (isEdit){
            editFieldVal.setText(fieldVal);
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle("Set: " + fieldName)
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
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
        temp.add("General");
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