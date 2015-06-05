package com.kd8bny.maintenanceman.ui.add;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_addVehicleEvent;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_datePicker;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class fragment_add_vehicleEvent extends Fragment {
    private static final String TAG = "frg_add_vhclEvnt";

    private Toolbar toolbar;
    private MaterialBetterSpinner vehicleSpinner;
    private ArrayAdapter<String> spinnerAdapter;

    private RecyclerView eventList;
    private RecyclerView.LayoutManager eventMan;
    private RecyclerView.Adapter eventListAdapter;

    private ArrayList<String> labels = new ArrayList<>();
    private ArrayList<String> editData;
    private ArrayList<String> singleVehicle;
    private HashMap<String, String> dataSet = new LinkedHashMap<>();
    private HashMap<String, HashMap> roster;
    private Boolean isNew = true;

    public fragment_add_vehicleEvent() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        editData = (ArrayList) getActivity().getIntent().getSerializableExtra("dataSet");

        labels.add(0, "Date");
        labels.add(1, "Odometer");
        labels.add(2, "Event");
        labels.add(3, "Price");
        labels.add(4, "Comment");
        if(editData == null) {
            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            dataSet.put(labels.get(0), month + 1 + "/" + day + "/" + year);
            dataSet.put(labels.get(1), null);
            dataSet.put(labels.get(2), null);
            dataSet.put(labels.get(3), null);
            dataSet.put(labels.get(4), null);
        }else{
            isNew = false;
            dataSet.put(labels.get(0), editData.get(1)); //TODO set spinner
            dataSet.put(labels.get(1), editData.get(2));
            dataSet.put(labels.get(2), editData.get(3));
            dataSet.put(labels.get(3), editData.get(4));
            dataSet.put(labels.get(4), editData.get(5));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vehicle_event, container, false);

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Spinner
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.vehicleSpinner);
        spinnerAdapter = setVehicles();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(spinnerAdapter);

        //Recycler View
        eventList = (RecyclerView) view.findViewById(R.id.add_vehicle_event);
        eventMan = new LinearLayoutManager(getActivity());
        eventList.setHasFixedSize(true);
        eventList.setItemAnimator(new DefaultItemAnimator());
        eventList.setLayoutManager(eventMan);
        eventList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity().getApplicationContext(), eventList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                FragmentManager fm = getFragmentManager();
                Bundle args = new Bundle();
                args.putString("label", labels.get(pos));
                args.putString("value", dataSet.get(labels.get(pos)));
                if (pos == 0) {
                    dialog_datePicker datePickerFrag = new dialog_datePicker();
                    datePickerFrag.setTargetFragment(fragment_add_vehicleEvent.this, 0);
                    datePickerFrag.setArguments(args);
                    datePickerFrag.show(getFragmentManager(), "datePicker");
                } else {
                    if (pos == 2) {
                        args.putBoolean("isEvent", true);
                    } else{
                        args.putBoolean("isEvent", false);
                    }
                    dialog_addVehicleEvent dialog_addVehicleEvent = new dialog_addVehicleEvent();
                    dialog_addVehicleEvent.setTargetFragment(fragment_add_vehicleEvent.this, 0);
                    dialog_addVehicleEvent.setArguments(args);
                    dialog_addVehicleEvent.show(fm, "dialog_addEvent");
                }
            }

            @Override
            public void onItemLongClick(View view, int pos) {
            }
        }));

        eventListAdapter = new adapter_add_vehicleEvent(dataSet, labels, false);
        eventList.setAdapter(eventListAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        String labelResult = data.getStringExtra("label");
        String valueResult = data.getStringExtra("value");

        dataSet.put(labelResult, valueResult);

        eventListAdapter = new adapter_add_vehicleEvent(dataSet, labels, true);
        eventList.swapAdapter(eventListAdapter, false);
    }

    @Override
    public void onResume(){
        super.onResume();

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

                int pos = singleVehicle.indexOf(vehicleSpinner.getText().toString());
                ArrayList<String> rosterKeys = new ArrayList<>(roster.keySet());
                String refID = rosterKeys.get(pos);

                vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(getActivity().getApplicationContext());
                if(!isNew){
                    vehicleDB.deleteEntry(getActivity().getApplicationContext(), editData);
                }
                vehicleDB.saveEntry(getActivity().getApplicationContext(), refID, dataSet);

                Toast.makeText(this.getActivity(), "History Updated", Toast.LENGTH_SHORT).show(); //TODO make string
                getActivity().finish();

                return true;

            case R.id.menu_cancel:
                getActivity().finish();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    public ArrayAdapter<String> setVehicles(){
        fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
        roster = new HashMap<>(fltjson.getEntries(getActivity().getApplicationContext()));
        HashMap<String, HashMap> vehicle;
        HashMap<String, String> gen;
        singleVehicle = new ArrayList<>();

        if(roster.containsKey(null)){
            Toast.makeText(this.getActivity(), R.string.empty_db, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }else {
            for(String key : roster.keySet()) {
                vehicle = roster.get(key);
                gen = vehicle.get("General");
                String name = gen.get("Year") + " " + gen.get("Make") + " " + gen.get("Model");
                singleVehicle.add(name);
            }
        }

        return new ArrayAdapter<> (getActivity(), android.R.layout.simple_spinner_item, singleVehicle);
    }
}
