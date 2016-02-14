package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.adapter_iconPicker;
import com.kd8bny.maintenanceman.classes.Vehicle.Event;


public class dialog_iconPicker extends DialogFragment {
    private static final String TAG = "dlg_icnPckr";

    private int RESULT_CODE;

    public dialog_iconPicker(){
        RESULT_CODE = getTargetRequestCode();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_icon_picker, null);
        final GridView iconGrid = (GridView) view.findViewById(R.id.iconGrid);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(view);

        iconGrid.setAdapter(new adapter_iconPicker(view.getContext()));
        iconGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendResult(position);
                dismiss();
            }
        });

        return alertDialog.create();
    }

    private void sendResult(int position) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                new Intent().putExtra("value", position));
    }
}