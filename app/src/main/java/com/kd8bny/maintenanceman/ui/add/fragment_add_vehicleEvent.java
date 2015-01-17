package com.kd8bny.maintenanceman.ui.add;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;

import java.util.ArrayList;


public class fragment_add_vehicleEvent extends Fragment {
    private static final String TAG = "fragment_add_vehicleEvent";

    private Toolbar toolbar;
    private Spinner vehicleSpinner;
    private ArrayAdapter<String> adapter;

    private String date;
    private String odo;
    private String task;
    private String refID;

    ArrayList<ArrayList> vehicleList;

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

        vehicleSpinner = (Spinner) view.findViewById(R.id.vehicleSpinner);
        adapter = setVehicles();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(adapter);

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

                getValues();
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
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());
        ArrayList<String> temp;
        ArrayList<String> singleVehicle = new ArrayList<>();

        for(int i=0; i<vehicleList.size(); i++) {
            temp = (vehicleList.get(i));
            if(temp.get(0) != null){
                String name = temp.get(1) + " " + temp.get(2) + " " + temp.get(3);
                singleVehicle.add(name);
            }else {
                Toast.makeText(this.getActivity(), temp.get(1), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }

        return new ArrayAdapter<> (getActivity(), android.R.layout.simple_spinner_item, singleVehicle);
    }

    public void getValues(){
        int pos = vehicleSpinner.getSelectedItemPosition();
        ArrayList<String> temp = vehicleList.get(pos);
        refID = temp.get(0);
        date = ((EditText) getActivity().findViewById(R.id.val_spec_date)).getText().toString();
        task = ((EditText) getActivity().findViewById(R.id.val_spec_event)).getText().toString();
        odo = ((EditText) getActivity().findViewById(R.id.val_spec_odo)).getText().toString();
    }


}