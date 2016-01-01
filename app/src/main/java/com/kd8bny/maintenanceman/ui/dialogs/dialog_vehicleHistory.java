package com.kd8bny.maintenanceman.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;


public class dialog_vehicleHistory extends DialogFragment {
    private static final String TAG = "dlg_vhcl_hstry";

    public ArrayList<String> event;

    public dialog_vehicleHistory(){

    }

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

        SpannableStringBuilder dateString = new SpannableStringBuilder("Completed on " + event.get(2));
        SpannableStringBuilder odoString = new SpannableStringBuilder(" at " + event.get(3) + " mi");
        StyleSpan styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        StyleSpan styleSpan2 = new StyleSpan(android.graphics.Typeface.BOLD);

        dateString.setSpan(styleSpan, 10, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        odoString.setSpan(styleSpan2, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        ((TextView) view.findViewById(R.id.val_spec_date_and_odo)).setText(TextUtils.concat(dateString, odoString));
        ((TextView)view.findViewById(R.id.val_spec_event)).setText(event.get(4));
        ((TextView) view.findViewById(R.id.val_spec_price)).setText(event.get(5));
        ((TextView)view.findViewById(R.id.val_spec_comment)).setText(event.get(6));

        return view;
    }
}