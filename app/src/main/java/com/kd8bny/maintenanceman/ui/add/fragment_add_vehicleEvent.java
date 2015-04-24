package com.kd8bny.maintenanceman.ui.add;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_datePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class fragment_add_vehicleEvent extends Fragment {
    private static final String TAG = "f_add_vehicleEvent";

    private Toolbar toolbar;
    private Spinner vehicleSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> eventAdapter;


    private EditText dateEdit;
    private AutoCompleteTextView eventEdit;

    private String date;
    private String odo;
    private String task;
    private String refID;

    private ArrayList<HashMap> vehicleList;
    private ArrayList<String> eventList;

    private HashMap<String, HashMap> roster;

    public fragment_add_vehicleEvent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vehicle_event, container, false);

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Spinner
        vehicleSpinner = (Spinner) view.findViewById(R.id.vehicleSpinner);
        spinnerAdapter = setVehicles();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(spinnerAdapter);

        //Date
        dateEdit = (EditText) view.findViewById(R.id.val_spec_date);
        dateEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_datePicker datePickerFrag = new dialog_datePicker();
                datePickerFrag.show(getFragmentManager(), "datePicker");
            }
        });

        //Event
        eventEdit = (AutoCompleteTextView) view.findViewById(R.id.val_spec_event);
        if(getEvents() != null) {
            eventEdit.setAdapter(eventAdapter);
        }

        return view;
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
                Context context = getActivity().getApplicationContext();

                getVehicles();
                vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(context);
                vehicleDB.saveEntry(context, refID, date, odo, task);

                Toast.makeText(this.getActivity(), "History Updated", Toast.LENGTH_SHORT).show();
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
        ArrayList<String> singleVehicle = new ArrayList<>();
        Log.d(TAG,roster.keySet().toString());

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

    public void getVehicles(){
        int pos = vehicleSpinner.getSelectedItemPosition();
        ArrayList<String> rosterKeys = new ArrayList<>(roster.keySet());
        refID = rosterKeys.get(pos);

        date = ((EditText) getActivity().findViewById(R.id.val_spec_date)).getText().toString();
        task = ((EditText) getActivity().findViewById(R.id.val_spec_event)).getText().toString();
        odo = ((EditText) getActivity().findViewById(R.id.val_spec_odo)).getText().toString();
    }

    public ArrayAdapter<String> getEvents(){
        int pos = vehicleSpinner.getSelectedItemPosition();
        if(pos > -1) {
            ArrayList<String> rosterKeys = new ArrayList<>(roster.keySet());
            refID = rosterKeys.get(pos);

            vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
            ArrayList<ArrayList> tempEvents = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);
            eventList = new ArrayList<>();
            ArrayList<String> temp;

            for (int i = 0; i < tempEvents.size(); i++) {
                temp = tempEvents.get(i);
                if (temp.get(0) != null) {
                    eventList.add(temp.get(3));
                } else {
                    Log.i(TAG, "nothing to show");
                    return null;
                }
            }

            // Remove dup's
            HashSet tempHS = new HashSet();
            tempHS.addAll(eventList);
            eventList.clear();
            eventList.addAll(tempHS);

            eventAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, eventList);

            return eventAdapter;

        }else {

            return null;
        }
    }
}
