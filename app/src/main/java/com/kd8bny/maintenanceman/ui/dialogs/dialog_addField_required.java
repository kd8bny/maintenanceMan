package com.kd8bny.maintenanceman.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.kd8bny.maintenanceman.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;


public class dialog_addField_required extends DialogFragment{
    private static final String TAG = "dlg_add_fld";

    private static final int REQUEST_CODE = 0;

    private String YEAR;
    private String MAKE;
    private String MODEL;

    public dialog_addField_required(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            YEAR = args.getString("year");
            MAKE = args.getString("make");
            MODEL = args.getString("model");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_field_required, null);

        final MaterialEditText yearVal = (MaterialEditText) view.findViewById(R.id.year_val);
        yearVal.setHint(view.getResources().getString(R.string.hint_year));
        yearVal.setFloatingLabelText(view.getResources().getString(R.string.hint_year));

        final MaterialEditText makeVal = (MaterialEditText) view.findViewById(R.id.make_val);
        makeVal.setHint(view.getResources().getString(R.string.hint_make));
        makeVal.setFloatingLabelText(view.getResources().getString(R.string.hint_make));

        final MaterialEditText modelVal = (MaterialEditText) view.findViewById(R.id.model_val);
        modelVal.setHint(view.getResources().getString(R.string.hint_model));
        modelVal.setFloatingLabelText(view.getResources().getString(R.string.hint_model));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle("Set:")
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            YEAR = yearVal.getText().toString();
                            MAKE = makeVal.getText().toString();
                            MODEL = modelVal.getText().toString();
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
        ArrayList<String> temp = new ArrayList<>();
        temp.add(YEAR);
        temp.add(MAKE);
        temp.add(MODEL);
        intent.putExtra("fieldData", temp);

        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, intent);
    }
}