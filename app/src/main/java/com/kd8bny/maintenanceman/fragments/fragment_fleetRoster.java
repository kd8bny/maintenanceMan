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

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.FleetRosterAdapter;
import com.kd8bny.maintenanceman.classes.data.FirestoreHelper;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.dialogs.dialog_addVehicle;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_fleetRoster extends Fragment {
    private static final String TAG = "frg_fltRstr";

    private static final int ADD_VEHICLE = 0;
    private static final int ADD_FIELD = 1;
    private static final int EDIT_FIELD = 2;

    private Context mContext;
    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private RecyclerView addList;
    private RecyclerView.LayoutManager addMan;
    private RecyclerView.Adapter addListAdapter;
    private FloatingActionButton fab;
    private Boolean isCommercial;

    private String [] mvehicleTypes;
    private ArrayList<Vehicle> roster;
    private Vehicle vehicle;
    private int vehiclePos;

    private String mYear;
    private String mMake;
    private String mModel;
    private String mVehicleType;

    private ArrayList<ArrayList> allSpecs = new ArrayList<>();
    private HashMap<String, String> generalSpecs = new HashMap<>();
    private HashMap<String, String> engineSpecs = new HashMap<>();
    private HashMap<String, String> powerTrainSpecs = new HashMap<>();
    private HashMap<String, String> otherSpecs = new HashMap<>();



    public fragment_fleetRoster(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getArguments();
        roster = bundle.getParcelableArrayList("roster");
        if(roster == null) {
            roster = new ArrayList<>();
        }

        vehiclePos = bundle.getInt("pos", -1);
        if (vehiclePos == -1){
            FragmentManager fm = getChildFragmentManager();
            dialog_addVehicle dialog = new dialog_addVehicle();
            dialog.setTargetFragment(fragment_fleetRoster.this, 0);

            dialog_addField dialogField = new dialog_addField();
            dialogField.setTargetFragment(fragment_fleetRoster.this, 1);
            dialog.setArguments(bundle);

            dialogField.show(fm, "dialog_add_field");
            dialog.show(fm, "dialog_add_field");
        }else{
            vehicle = roster.get(vehiclePos);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_fleet_roster, container, false);
        mContext = getActivity().getApplicationContext();

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
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("field", allSpecs.get(i));
                        bundle.putInt("pos", i);
                        dialog_addField dialog_addField = new dialog_addField();
                        dialog_addField.setTargetFragment(fragment_fleetRoster.this, 2);
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
                dialog_addField.setTargetFragment(fragment_fleetRoster.this, 0);
                dialog_addField.show(fm, "dialog_add_field");
            }
        });

        return view;
    }

    public void onStart(){
        super.onStart();
        if (vehiclePos != -1) {
            prepEdit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addList.setAdapter(new FleetRosterAdapter(allSpecs));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        ArrayList<String> result = bundle.getStringArrayList("fieldData");
        //TODO fix this result

        switch (resultCode){
            case ADD_VEHICLE:
                vehicle = new Vehicle(null);
                vehicle.setVehicleType(bundle.getString("type"));
                vehicle.setYear(bundle.getString("year"));
                vehicle.setMake(bundle.getString("make"));
                vehicle.setModel(bundle.getString("model"));
                vehicle.setIsCommercial(bundle.getBoolean("commercial"));

                break;

            case ADD_FIELD:

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

            case EDIT_FIELD:

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
        addList.setAdapter(new FleetRosterAdapter(allSpecs));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (vehiclePos == -1) {
            inflater.inflate(R.menu.menu_add, menu);
        } else {
            inflater.inflate(R.menu.menu_edit, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_save:
                Boolean isUS = true, useDist = true;
                /*String type = vehicleSpinner.getText().toString();
                if (!mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                        .getString("prefUnitDist", "mi").equals("mi")) {
                    isUS = false;
                }

                if (type.equals("Utility") || type.equals("Marine") || type.equals("Lawn and Garden")){

                    useDist = false;
                }*/

                /*String unitDist, unitMileage;
                if (useDist) {
                    if (isUS) {
                        unitDist = mContext.getResources().getString(R.string.unit_dist_us);
                        unitMileage = mContext.getResources().getString(R.string.unit_mileage_us);
                    }else{
                        unitDist = mContext.getResources().getString(R.string.unit_dist_metric);
                        unitMileage = mContext.getResources().getString(R.string.unit_mileage_metric);
                    }
                }else{
                    if (isUS) {
                        unitMileage = mContext.getResources().getString(R.string.unit_mileage_time_us);
                    }else{
                        unitMileage = mContext.getResources().getString(R.string.unit_mileage_time_metric);
                    }
                    unitDist = mContext.getResources().getString(R.string.unit_time);
                }*/

               // vehicle.setUnitDist(unitDist);
               // vehicle.setUnitMileage(unitMileage);
                vehicle.setGeneralSpecs(generalSpecs);
                vehicle.setEngineSpecs(engineSpecs);
                vehicle.setPowerTrainSpecs(powerTrainSpecs);
                vehicle.setOtherSpecs(otherSpecs);

                FirestoreHelper firestoreHelper = FirestoreHelper.getInstance(null);
                firestoreHelper.addToFleet(vehicle);

                /*SaveLoadHelper saveLoadHelper = new SaveLoadHelper(mContext, null);
                final ArrayList<Vehicle> roster = new ArrayList<>(saveLoadHelper.load());
                if (vehiclePos != -1) {
                    if (!vehicle.equals(roster.get(vehiclePos))) {
                        roster.set(vehiclePos, vehicle);
                        saveLoadHelper.save(roster);
                    }
                }else{
                    roster.add(vehicle);
                    saveLoadHelper.save(roster);
                }*/

                getActivity().setResult(90);
                getActivity().finish();

                return true;

            case R.id.menu_cancel:
                getActivity().finish();
                return true;

            case R.id.menu_del:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                //builder.setTitle("Are you sure you would like to delete " + vehicle.getTitle() + "?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


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
        /*generalSpecs = vehicle.getGeneralSpecs();
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
        */
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

