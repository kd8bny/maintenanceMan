package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;


public class dialog_whatsNew extends DialogFragment {
    private static final String TAG = "dlg_whts_nw";

    public dialog_whatsNew(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_whats_new, null);

        TextView whatsNew = (TextView) view.findViewById(R.id.whats_new);
        whatsNew.setText(getResources().getString(R.string.whatsNew));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        });

        alertDialog.setView(view);

        return alertDialog.create();
    }
}