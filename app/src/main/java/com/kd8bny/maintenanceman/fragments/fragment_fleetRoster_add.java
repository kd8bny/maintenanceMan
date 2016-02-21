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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.FleetRosterAdapter;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.dialogs.dialog_addVehicle;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class fragment_fleetRoster_add extends Fragment {
    private static final String TAG = "frg_add_fltRstr";

    private Context context;

    private MaterialBetterSpinner vehicleSpinner;
    private RecyclerView addList;
    private RecyclerView.LayoutManager addMan;
    private RecyclerView.Adapter addListAdapter;
    private FloatingActionButton fab;

    private String [] mvehicleTypes;
    private ArrayList<Vehicle> roster;
    private Vehicle vehicle;

    private ArrayList<ArrayList> allSpecs = new ArrayList<>();
    private HashMap<String, String> generalSpecs = new HashMap<>();
    private HashMap<String, String> engineSpecs = new HashMap<>();
    private HashMap<String, String> powerTrainSpecs = new HashMap<>();
    private HashMap<String, String> otherSpecs = new HashMap<>();

    public fragment_fleetRoster_add(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();

        Bundle bundle = getArguments();
        roster = bundle.getParcelableArrayList("roster");
        if(roster == null){
            roster = new ArrayList<>();
        }

        Bundle newBundle = new Bundle();
        newBundle.putBoolean("isEdit", false);
        FragmentManager fm = getChildFragmentManager();
        dialog_addVehicle dialog = new dialog_addVehicle();
        dialog.setTargetFragment(fragment_fleetRoster_add.this, 0);
        dialog.setArguments(newBundle);

        dialog_addField dialogField = new dialog_addField();
        dialogField.setTargetFragment(fragment_fleetRoster_add.this, 0);

        dialogField.show(fm, "dialog_add_field");
        dialog.show(fm, "dialog_add_field");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_fleet_roster, container, false);

        //Spinner
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_vehicle_type);
        mvehicleTypes = context.getResources().getStringArray(R.array.vehicle_type);
        vehicleSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vehicle.setVehicleType(vehicleSpinner.getText().toString());
            }});
        vehicleSpinner.setAdapter(new ArrayAdapter<> (getActivity(), R.layout.spinner_drop_item, mvehicleTypes));

        //Recycler View
        addList = (RecyclerView) view.findViewById(R.id.add_fleet_roster_list_car);
        addMan = new LinearLayoutManager(getActivity());
        addList.setLayoutManager(addMan);
        addListAdapter = new FleetRosterAdapter(allSpecs);
        addList.setAdapter(addListAdapter);

        addList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(context, addList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                FragmentManager fm = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("field", allSpecs.get(i));
                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_fleetRoster_add.this, 1);
                dialog_addField.setArguments(bundle);
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
                dialog_addField.setTargetFragment(fragment_fleetRoster_add.this, 1);
                dialog_addField.show(fm, "dialog_add_field");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        ArrayList<String> result = bundle.getStringArrayList("fieldData");

        switch (resultCode){
            case (0):
                vehicle = new Vehicle(vehicleSpinner.getText().toString(),
                            bundle.getBoolean("isBusiness"), result.get(0), result.get(1), result.get(2));
                break;

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
                        engineSpecs.put(result.get(1), result.get(2));
                        break;
                    case "Power Train":
                        powerTrainSpecs.put(result.get(1), result.get(2));
                        break;
                    case "Other":
                        otherSpecs.put(result.get(1), result.get(2));
                        break;
                }
                allSpecs.set(pos, result);
                break;
        }

        addListAdapter = new FleetRosterAdapter(allSpecs);
        addList.swapAdapter(addListAdapter, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fleet_roster_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_save:
                if(!isLegit()) {
                    vehicle.setGeneralSpecs(generalSpecs);
                    vehicle.setEngineSpecs(engineSpecs);
                    vehicle.setPowerTrainSpecs(powerTrainSpecs);
                    vehicle.setOtherSpecs(otherSpecs);

                    roster.add(vehicle);
                    new SaveLoadHelper(context).save(roster);

                    getActivity().finish();
                }

                return true;

            case R.id.menu_cancel:
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isLegit(){
        if (Arrays.asList(mvehicleTypes).indexOf(vehicleSpinner.getText().toString()) == -1){
            vehicleSpinner.setError(getResources().getString(R.string.error_set_vehicle_type));
            return true;
        }
        return false;
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
    }
}

