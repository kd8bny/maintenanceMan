package com.kd8bny.maintenanceman.dialogs;

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
import com.kd8bny.maintenanceman.classes.utils.Utils;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import org.joda.time.DateTime;

import java.util.Locale;


public class dialog_maintenanceHistory extends DialogFragment {
    private static final String TAG = "dlg_vhcl_hstry";

    private Vehicle mVehicle;
    private Maintenance mMaintenance;

    public dialog_maintenanceHistory(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mVehicle = bundle.getParcelable("vehicle");
        mMaintenance = (Maintenance) bundle.getSerializable("event");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_vehicle_history, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        String odo;
        try {
            odo = String.format(Locale.ENGLISH, "%1$,.1f %2$s", Double.parseDouble(mMaintenance.getOdometer()),
                    mVehicle.getUnitDist());
        }catch (Exception e){
            Log.wtf(TAG, "parse error:" + mMaintenance.getOdometer());
            odo = mMaintenance.getOdometer();
        }
        SpannableStringBuilder dateString = new SpannableStringBuilder("Completed on " +
                new Utils(getContext()).toFriendlyDate(new DateTime(mMaintenance.getDate())));
        SpannableStringBuilder odoString = new SpannableStringBuilder(" at " + odo);
        StyleSpan styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        StyleSpan styleSpan2 = new StyleSpan(android.graphics.Typeface.BOLD);

        dateString.setSpan(styleSpan, 10, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        odoString.setSpan(styleSpan2, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        ((TextView) view.findViewById(R.id.val_spec_date_and_odo)).setText(TextUtils.concat(dateString, odoString));
        ((TextView) view.findViewById(R.id.val_spec_event)).setText(mMaintenance.getEvent());
        ((TextView) view.findViewById(R.id.val_spec_comment)).setText(mMaintenance.getComment());
        ((TextView) view.findViewById(R.id.val_spec_price)).setText(mMaintenance.getPrice());

        return view;
    }
}