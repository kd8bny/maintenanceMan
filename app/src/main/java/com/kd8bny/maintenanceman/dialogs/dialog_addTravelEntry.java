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

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Travel;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

public class dialog_addTravelEntry extends DialogFragment {
    private static final String TAG = "dlg_add_evnt";

    private int RESULT_CODE;
    private Travel mTravel;
    private int mPos;

    public dialog_addTravelEntry(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESULT_CODE = getTargetRequestCode();
        Bundle bundle = getArguments();
        mTravel = (Travel) bundle.getSerializable("event");
        mPos = bundle.getInt("pos");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_vehicle_event, null);
        final MaterialAutoCompleteTextView editValue = (MaterialAutoCompleteTextView) view.findViewById(R.id.value);
        Resources mRes = view.getResources();
        switch (RESULT_CODE){//TODO get data for edit
            case 1:
                editValue.setHint(mRes.getString(R.string.field_start));
                editValue.setFloatingLabelText(mRes.getString(R.string.field_start));
                editValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case 2:
                editValue.setHint(mRes.getString(R.string.field_end));
                editValue.setFloatingLabelText(mRes.getString(R.string.field_end));
                editValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case 3:
                editValue.setHint(mRes.getString(R.string.field_dest));
                editValue.setFloatingLabelText(mRes.getString(R.string.field_dest));
                break;

            case 4:
                editValue.setHint(mRes.getString(R.string.field_purpose));
                editValue.setFloatingLabelText(mRes.getString(R.string.field_purpose));
                editValue.setInputType(InputType.TYPE_CLASS_TEXT);
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
        Bundle bundle = new Bundle();
        bundle.putInt("pos", mPos);
        bundle.putString("value", value);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                new Intent().putExtra("bundle", bundle));
    }
}