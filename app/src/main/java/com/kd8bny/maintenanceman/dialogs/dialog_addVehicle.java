package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.kd8bny.maintenanceman.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class dialog_addVehicle extends DialogFragment {
    private static final String TAG = "dlg_add_fld";

    private static final int REQUEST_CODE = 0;

    private MaterialEditText yearVal;
    private MaterialEditText makeVal;
    private MaterialEditText modelVal;
    private String YEAR;
    private String MAKE;
    private String MODEL;
    private Boolean isBusiness = false;
    private Boolean isEdit;

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