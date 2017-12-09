package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.data.FirestoreHelper;
import com.kd8bny.maintenanceman.classes.utils.Utils;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class dialog_addMaintenanceEntry extends DialogFragment {
    private static final String TAG = "dlg_add_maint";

    private int RESULT_CODE;
    private Context mContext;
    private MaterialBetterSpinner vehicleSpinner;
    private MaterialAutoCompleteTextView vEvent;
    private ImageView vIcon;
    private MaterialEditText vDate, vOdo, vPrice, vComment;
    private TypedArray icons;

    private ArrayList<Vehicle> mRoster;
    private Maintenance mMaintenance, mOldMaintenance;
    private Vehicle mVehicle;
    private ArrayList<String> mVehicleTitles;
    private Boolean isNew = true;
    private int mPos;

    public dialog_addMaintenanceEntry(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        RESULT_CODE = getTargetRequestCode();

        Bundle bundle = getArguments();
        mRoster = bundle.getParcelableArrayList("roster");
        mMaintenance = (Maintenance) bundle.getSerializable("event");
        mPos = bundle.getInt("pos", -1);

        mMaintenance = (Maintenance) bundle.getSerializable("event");
        if (mMaintenance == null){
            mMaintenance = new Maintenance("");
            mMaintenance.setDate(new DateTime().toString());
        }else { //TODO make object implement clone
            isNew = false;
            mOldMaintenance = new Maintenance(mMaintenance.getRefID());
            mOldMaintenance.setIcon(mMaintenance.getIcon());
            mOldMaintenance.setDate(mMaintenance.getDate());
            mOldMaintenance.setOdometer(mMaintenance.getOdometer());
            mOldMaintenance.setEvent(mMaintenance.getEvent());
            mOldMaintenance.setPrice(mMaintenance.getPrice());
            mOldMaintenance.setComment(mMaintenance.getComment());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_maintenance_event, null);
        icons = view.getResources().obtainTypedArray(R.array.icon_event);

        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_roster);
        vIcon = (ImageView) view.findViewById(R.id.eventIcon);
        vEvent = (MaterialAutoCompleteTextView) view.findViewById(R.id.val_event);
        vDate = (MaterialEditText) view.findViewById(R.id.val_date);
        vOdo = (MaterialEditText) view.findViewById(R.id.val_odo);
        vPrice = (MaterialEditText) view.findViewById(R.id.val_price);
        vComment = (MaterialEditText) view.findViewById(R.id.val_comment);

        vehicleSpinner.setAdapter(setVehicles());
        if (mPos > -1) {
            mVehicle = mRoster.get(mPos);
            vehicleSpinner.setText(mVehicle.getYear());
        }
        vDate.setText(new Utils(mContext).toFriendlyDate(new DateTime(mMaintenance.getDate())));
        vOdo.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        vPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //TODO vEvent.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, setEvents()));

        if (!isNew){
            vIcon.setImageResource(icons.getResourceId(mOldMaintenance.getIcon(), 0));
            icons.recycle();
            vEvent.setText(mOldMaintenance.getEvent());
            vDate.setText(mOldMaintenance.getDate());
            vOdo.setText(mOldMaintenance.getOdometer());
            vPrice.setText(mOldMaintenance.getPrice());
            vComment.setText(mOldMaintenance.getComment());
        }

        view.findViewById(R.id.rel_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_iconPicker iconPicker = new dialog_iconPicker();
                iconPicker.setTargetFragment(dialog_addMaintenanceEntry.this, 0);
                iconPicker.show(getFragmentManager(), "dialogIconPicker");
            }
        });

        view.findViewById(R.id.val_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_datePicker datePicker = new dialog_datePicker();
                datePicker.setTargetFragment(dialog_addMaintenanceEntry.this, 0);
                datePicker.show(getFragmentManager(), "datePicker");
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle(view.getResources().getString(R.string.title_add_vehicle_event))
            .setPositiveButton(view.getResources().getString(R.string.button_ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Leave blank as we override onStart()
                        }
                    })
            .setNegativeButton(view.getResources().getString(R.string.button_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // DO SOMETHING
                        }
                    });

        alertDialog.setView(view);

        return alertDialog.create();
    }

    @Override
    public void onStart(){
        /*Used to keep alert dialog open for error check*/
        super.onStart();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if(alertDialog != null){
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (isLegit()) {
                        mMaintenance.setEvent(vEvent.getText().toString());
                        mMaintenance.setOdometer(vOdo.getText().toString());
                        mMaintenance.setPrice(vPrice.getText().toString());
                        mMaintenance.setComment(vComment.getText().toString());

                        //save
                        int pos = mVehicleTitles.indexOf(vehicleSpinner.getText().toString());
                        mMaintenance.setRefID(mRoster.get(pos).getRefID());

                        FirestoreHelper firestoreHelper = FirestoreHelper.getInstance(null);
                        firestoreHelper.addMaintenanceEvent(mMaintenance);
                        //TODO when edit lets just update the event
                        //if (mOldMaintenance != null){
                         //   vehicleLogDBHelper.deleteEntry(mOldMaintenance);
                        //}
                       // vehicleLogDBHelper.insertEntry(mMaintenance);

                        /*Bundle bundle = new Bundle();
                        bundle.putBoolean("save", true);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CODE,
                                new Intent().putExtra("bundle", bundle));*/
                        dismiss();
                    }}});
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        switch (resultCode) {
            case 0:
                DateTime dateTime = new DateTime().withDate(bundle.getInt("year"),
                        bundle.getInt("month"), bundle.getInt("day"));
                mMaintenance.setDate(dateTime.toString());
                vDate.setText(new Utils(mContext).toFriendlyDate(dateTime));
                break;

            case 1:
                int icon = bundle.getInt("value", 0);
                mMaintenance.setIcon(icon);
                vIcon.setImageResource(icons.getResourceId(icon, 0));
                break;
        }
    }

    private ArrayAdapter<String> setVehicles(){
        mVehicleTitles = new ArrayList<>();
        for(Vehicle v : mRoster) {
            mVehicleTitles.add(String.format("%s %s %s", v.getYear(), v.getMake(), v.getModel()));
        }

        return new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, mVehicleTitles);
    }

    private Boolean isLegit(){
        if (mVehicleTitles.indexOf(vehicleSpinner.getText().toString()) == -1){
            vehicleSpinner.setError(getResources().getString(R.string.error_set_vehicle));

            return false;
        }
        if (vEvent.getText() == null){
            vEvent.setError(getResources().getString(R.string.error_field_val));

            return false;
        }
        if (vOdo.getText() == null){
            vOdo.setError(getResources().getString(R.string.error_field_val));

            return false;
        }

        return true;
    }
}