package com.kd8bny.maintenanceman.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.adapter_add_vehicleEvent;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.backupRestoreHelper;
import com.kd8bny.maintenanceman.classes.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.dialogs.dialog_addVehicleEvent;
import com.kd8bny.maintenanceman.dialogs.dialog_iconPicker;
import com.kd8bny.maintenanceman.dialogs.dialog_datePicker;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class fragment_vehicleEvent_add extends Fragment {
    private static final String TAG = "frg_add_vhclEvnt";

    private Toolbar toolbar;
    private MaterialBetterSpinner vehicleSpinner;
    private ArrayAdapter<String> spinnerAdapter;

    private RecyclerView eventList;
    private RecyclerView.Adapter eventListAdapter;

    private ArrayList<Vehicle> roster;
    private int vehiclePos;
    private Vehicle vehicle;
    private String refID;
    ArrayList<String> singleVehicle = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();
    private ArrayList<String> editData;
    private HashMap<String, String> dataSet = new LinkedHashMap<>();

    private Boolean isNew = true;

    public fragment_vehicleEvent_add() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments() ;
        vehiclePos = bundle.getInt("vehiclePos", -1);
        vehicle = bundle.getParcelable("vehicle");

        editData = (ArrayList) getActivity().getIntent().getSerializableExtra("dataSet"); //TODO ??
        refID = vehicle.getRefID();

        labels.add(0, "icon");
        labels.add(1, "Date");
        labels.add(2, "Odometer");
        labels.add(3, "Event");
        labels.add(4, "Price");
        labels.add(5, "Comment");

        if(editData == null) {
            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            dataSet.put(labels.get(0), "");
            dataSet.put(labels.get(1), month + 1 + "/" + day + "/" + year);
            dataSet.put(labels.get(2), "");
            dataSet.put(labels.get(3), "");
            dataSet.put(labels.get(4), "");
            dataSet.put(labels.get(5), "");

        }else{
            isNew = false;
            dataSet.put(labels.get(0), editData.get(1));
            dataSet.put(labels.get(1), editData.get(2));
            dataSet.put(labels.get(2), editData.get(3));
            dataSet.put(labels.get(3), editData.get(4));
            dataSet.put(labels.get(4), editData.get(5));
            dataSet.put(labels.get(5), editData.get(6));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_vehicle_event, container, false);

        //Spinner
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.vehicleSpinner);
        vehicleSpinner.setText(vehicle.getTitle());
        spinnerAdapter = setVehicles();
        vehicleSpinner.setAdapter(spinnerAdapter);

        //Recycler View
        eventList = (RecyclerView) view.findViewById(R.id.add_vehicle_event);
        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventList.setHasFixedSize(true);
        eventList.setItemAnimator(new DefaultItemAnimator());
        eventList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity().getApplicationContext(), eventList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                FragmentManager fm = getFragmentManager();
                Bundle args = new Bundle();
                args.putString("label", labels.get(pos));
                args.putString("value", dataSet.get(labels.get(pos)));

                switch (pos){
                    case 0:
                        dialog_iconPicker dialogIconPicker = new dialog_iconPicker();
                        dialogIconPicker.setTargetFragment(fragment_vehicleEvent_add.this, 0);
                        dialogIconPicker.show(fm, "dialogIconPicker");

                        break;

                    case 1:
                        dialog_datePicker datePickerFrag = new dialog_datePicker();
                        datePickerFrag.setTargetFragment(fragment_vehicleEvent_add.this, 1);
                        datePickerFrag.setArguments(args);
                        datePickerFrag.show(fm, "datePicker");

                        break;

                    case 3:
                        args.putBoolean("isEvent", true);

                        dialog_addVehicleEvent dialog_addVehicleisEvent = new dialog_addVehicleEvent();
                        dialog_addVehicleisEvent.setTargetFragment(fragment_vehicleEvent_add.this, 1);
                        dialog_addVehicleisEvent.setArguments(args);
                        dialog_addVehicleisEvent.show(fm, "dialog_addisEvent");

                        break;

                    default:
                        args.putBoolean("isEvent", false);
                        dialog_addVehicleEvent dialog_addVehicleEvent = new dialog_addVehicleEvent();
                        dialog_addVehicleEvent.setTargetFragment(fragment_vehicleEvent_add.this, 1);
                        dialog_addVehicleEvent.setArguments(args);
                        dialog_addVehicleEvent.show(fm, "dialog_addEvent");

                        break;
                }}

            @Override
            public void onItemLongClick(View view, int pos) {}
        }));

        eventListAdapter = new adapter_add_vehicleEvent(dataSet, labels);
        eventList.setAdapter(eventListAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                dataSet.put("icon", data.getStringExtra("icon"));
                eventListAdapter = new adapter_add_vehicleEvent(dataSet, labels);
                eventList.setAdapter(eventListAdapter);

                break;

            case 1:
                String labelResult = data.getStringExtra("label");
                String valueResult = data.getStringExtra("value");

                dataSet.put(labelResult, valueResult);

                eventListAdapter = new adapter_add_vehicleEvent(dataSet, labels);
                eventList.setAdapter(eventListAdapter);

                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_fleet_roster, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_save:
                if(!isLegit()){
                    int pos = singleVehicle.indexOf(vehicleSpinner.getText().toString());
                    String refID = roster.get(pos).getRefID();

                    vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(getActivity().getApplicationContext());
                    if(!isNew){
                        vehicleDB.deleteEntry(getActivity().getApplicationContext(), editData);
                    }
                    vehicleDB.saveEntry(getActivity().getApplicationContext(), refID, dataSet);

                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.error_field_event), Snackbar.LENGTH_SHORT)
                            .setActionTextColor(getResources().getColor(R.color.error)).show(); ///TODO snakz w/ right label

                    backupRestoreHelper mbackupRestoreHelper = new backupRestoreHelper();
                    mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "backup", false);

                    getActivity().finish();

                    return true;
                }

                return false;

            case R.id.menu_cancel:
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ArrayAdapter<String> setVehicles(){
        for(Vehicle v : roster) {
            singleVehicle.add(v.getTitle());
        }
        return new ArrayAdapter<> (getActivity(), R.layout.spinner_drop_item, singleVehicle);
    }

    public boolean isLegit(){
        if (singleVehicle.indexOf(vehicleSpinner.getText().toString()) == -1){
            vehicleSpinner.setError(getResources().getString(R.string.error_set_vehicle));
            return true;
        }
        if (dataSet.get("Event").equals("")){
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.error_field_event), Snackbar.LENGTH_SHORT)
                    .setActionTextColor(getResources().getColor(R.color.error)).show();
            return true;
        }

        return false;
    }
}
