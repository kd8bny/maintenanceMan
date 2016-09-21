package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.interfaces.SyncFinished;


public class dialog_sync extends DialogFragment implements SyncFinished {
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

        TextView whatsNew = (TextView) view.findViewById(R.id.whats_new);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false);

        return alertDialog.create();
    }

    public void onDownloadComplete(Boolean isComplete){
        Log.d(TAG, "dismess plz");
        dismiss();
        Log.d(TAG, "dismess plz");
    }
}