package com.kd8bny.maintenanceman.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.FleetRosterAdapter;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_fleetRoster_edit extends Fragment {
    private static final String TAG = "frg_fltRstr_edit";

    private Context mContext;

    private MaterialBetterSpinner vehicleSpinner;
    private RecyclerView addList;
    private RecyclerView.LayoutManager addMan;
    private RecyclerView.Adapter addListAdapter;
    private FloatingActionButton fab;
    private CheckBox businessVal;

    private String [] mvehicleTypes;
    private ArrayList<Vehicle> roster;
    private Vehicle vehicle;
    private int vehiclePos;

    private ArrayList<ArrayList> allSpecs = new ArrayList<>();
    private HashMap<String, String> generalSpecs = new HashMap<>();
    private HashMap<String, String> engineSpecs = new HashMap<>();
    private HashMap<String, String> powerTrainSpecs = new HashMap<>();
    private HashMap<String, String> otherSpecs = new HashMap<>();

    public fragment_fleetRoster_edit(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getArguments();
        roster = bundle.getParcelableArrayList("roster");
        vehiclePos = bundle.getInt("vehiclePos", -1);
        vehicle = roster.get(vehiclePos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_fleet_roster, container, false);
        mContext = getActivity().getApplicationContext();

        //Spinner
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_vehicle_type);
        mvehicleTypes = mContext.getResources().getStringArray(R.array.vehicle_type);
        vehicleSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vehicle.setVehicleType(vehicleSpinner.getText().toString());
            }});
        vehicleSpinner.setAdapter(new ArrayAdapter<> (getActivity(), R.layout.spinner_drop_item, mvehicleTypes));
        businessVal = (CheckBox) view.findViewById(R.id.checkbox_business);

        //Recycler View
        addList = (RecyclerView) view.findViewById(R.id.add_fleet_roster_list_car);
        addMan = new LinearLayoutManager(getActivity());
        addList.setLayoutManager(addMan);
        addListAdapter = new FleetRosterAdapter(allSpecs);
        addList.setAdapter(addListAdapter);

        addList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mContext, addList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        FragmentManager fm = getFragmentManager();
                        Bundle args = new Bundle();
                        args.putSerializable("field", allSpecs.get(i));
                        args.putInt("pos", i);
                        dialog_addField dialog_addField = new dialog_addField();
                        dialog_addField.setTargetFragment(fragment_fleetRoster_edit.this, 1);
                        dialog_addField.setArguments(args);
                        dialog_addField.show(fm, "dialog_add_field");
                    }

                    @Override
                    public void onItemLongClick(View view, int i) {
                        final int pos = i;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Are you sure you would like to delete this field?");
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeEntry(pos);
                            }});

                        builder.show();
                }}));

        //menu_overview_fab
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_fleetRoster_edit.this, 0);
                dialog_addField.show(fm, "dialog_add_field");
            }
        });

        return view;
    }

    public void onStart(){
        super.onStart();
        prepEdit();
    }

    @Override
    public void onResume() {
        super.onResume();
        addListAdapter = new FleetRosterAdapter(allSpecs);
        addList.setAdapter(addListAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        ArrayList<String> result = bundle.getStringArrayList("fieldData");

        switch (resultCode){
            case (1):
                allSpecs.add(result);
                switch (result.get(0)) {
                    case "General":
                        generalSpecs.put(result.get(1), result.get(2));
                        break;
                    case "Engine":
                        engineSpecs.put(result.get(1), result.get(2));
                        break;
                    case "Power Train":
                        powerTrainSpecs.put(result.get(1), result.get(2));
                        break;
                    case "Other":
                        otherSpecs.put(result.get(1), result.get(2));
                        break;
                }
                break;

            case (2):
                int pos = bundle.getInt("pos");
                switch (result.get(0)) {
                    case "General":
                        generalSpecs.remove(allSpecs.get(pos).get(1));
                        generalSpecs.put(result.get(1), result.get(2));
                        break;
                    case "Engine":
                        engineSpecs.remove(allSpecs.get(pos).get(1));
                        engineSpecs.put(result.get(1), result.get(2));
                        break;
                    case "Power Train":
                        powerTrainSpecs.remove(allSpecs.get(pos).get(1));
                        powerTrainSpecs.put(result.get(1), result.get(2));
                        break;
                    case "Other":
                        otherSpecs.remove(allSpecs.get(pos).get(1));
                        otherSpecs.put(result.get(1), result.get(2));
                        break;
                }
                allSpecs.set(pos, result);
                break;
        }

        addListAdapter = new FleetRosterAdapter(allSpecs);
        addList.setAdapter(addListAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fleet_roster_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_save:
                vehicle.setGeneralSpecs(generalSpecs);
                vehicle.setEngineSpecs(engineSpecs);
                vehicle.setPowerTrainSpecs(powerTrainSpecs);
                vehicle.setOtherSpecs(otherSpecs);
                vehicle.setBusiness(businessVal.isChecked());

                SaveLoadHelper saveLoadHelper = new SaveLoadHelper(mContext);
                final ArrayList<Vehicle> roster = new ArrayList<>(saveLoadHelper.load());
                if(!vehicle.equals(roster.get(vehiclePos))) {
                    roster.set(vehiclePos, vehicle);
                    saveLoadHelper.save(roster);
                }

                getActivity().setResult(90);
                getActivity().finish();

                return true;

            case R.id.menu_cancel:
                getActivity().finish();
                return true;

            case R.id.menu_del:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Are you sure you would like to delete " + vehicle.getTitle() + "?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        VehicleLogDBHelper.getInstance(mContext).purgeVehicle(vehicle.getRefID());

                        SaveLoadHelper saveLoadHelper = new SaveLoadHelper(mContext);
                        ArrayList<Vehicle> temp = saveLoadHelper.load();
                        temp.remove(vehiclePos);
                        saveLoadHelper.save(temp);

                        getActivity().setResult(91);
                        getActivity().finish();
                    }});

                builder.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepEdit(){
        generalSpecs = vehicle.getGeneralSpecs();
        engineSpecs = vehicle.getEngineSpecs();
        powerTrainSpecs = vehicle.getPowerTrainSpecs();
        otherSpecs = vehicle.getOtherSpecs();
        businessVal.setChecked(vehicle.getBusiness());

        vehicleSpinner.setText(vehicle.getVehicleType());
        for (String key : generalSpecs.keySet()) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add("General");
            temp.add(key);
            temp.add(generalSpecs.get(key));
            allSpecs.add(temp);
        }
        for (String key : engineSpecs.keySet()) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add("Engine");
            temp.add(key);
            temp.add(engineSpecs.get(key));
            allSpecs.add(temp);
        }
        for (String key : powerTrainSpecs.keySet()) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add("Power Train");
            temp.add(key);
            temp.add(powerTrainSpecs.get(key));
            allSpecs.add(temp);
        }
        for (String key : otherSpecs.keySet()) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add("Other");
            temp.add(key);
            temp.add(otherSpecs.get(key));
            allSpecs.add(temp);
        }
    }

    private void removeEntry(int i){
        ArrayList<String> temp = allSpecs.get(i);
        if(generalSpecs.containsKey(temp.get(1))){
            generalSpecs.remove(temp.get(1));
        }else if(engineSpecs.containsKey(temp.get(1))){
            engineSpecs.remove(temp.get(1));
        }else if(powerTrainSpecs.containsKey(temp.get(1))){
            powerTrainSpecs.remove(temp.get(1));
        }else if(otherSpecs.containsKey(temp.get(1))){
            otherSpecs.remove(temp.get(1));
        }
        allSpecs.remove(i);

        onResume();
    }
}

