package com.kd8bny.maintenanceman.ui.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;


public class dialog_whatsNew extends DialogFragment{
    private static final String TAG = "dlg_whts_nw";

    private String WHATS_NEW_FILNAME = "/whatsNew.txt";

    public dialog_whatsNew(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_whats_new, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView whatsNew = (TextView) view.findViewById(R.id.whats_new);
        whatsNew.setText(getResources().getString(R.string.whatsNew));

        return view;
    }
}