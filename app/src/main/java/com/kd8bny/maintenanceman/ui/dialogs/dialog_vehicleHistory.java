package com.kd8bny.maintenanceman.ui.dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;


public class dialog_vehicleHistory extends DialogFragment{
    private static final String TAG = "dlg_vhcl_hstry";

    public ArrayList<String> event;

    public dialog_vehicleHistory(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            event = (ArrayList) args.getSerializable("event");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_vehicle_history, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ((TextView)view.findViewById(R.id.val_spec_date)).setText(event.get(1));
        ((TextView)view.findViewById(R.id.val_spec_odo)).setText(event.get(2));
        ((TextView)view.findViewById(R.id.val_spec_event)).setText(event.get(3));
        ((TextView)view.findViewById(R.id.val_spec_price)).setText(event.get(4));
        ((TextView)view.findViewById(R.id.val_spec_comment)).setText(event.get(5));

        return view;
    }
}