package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.kd8bny.maintenanceman.R;


public class dialog_sync extends DialogFragment {
    private static final String TAG = "dlg_whts_nw";

    public dialog_sync(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sync, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false);

        return alertDialog.create();
    }
}