package com.kd8bny.maintenanceman.ui.add;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_addField;

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_addField_required;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class fragment_add_fleetRoster extends Fragment {
    private static final String TAG = "frg_add_fltRstr";

    private Toolbar toolbar;
    private Context context;

    private MaterialBetterSpinner vehicleSpinner;
    private RecyclerView addList;
    private RecyclerView.LayoutManager addMan;
    private RecyclerView.Adapter addListAdapter;
    private FloatingActionButton fab;

    private Vehicle vehicle;
    private ArrayList<Vehicle> roster;
    private ArrayList<ArrayList> allSpecs = new ArrayList<>();
    private HashMap<String, String> generalSpecs;
    private HashMap<String, String> engineSpecs;
    private HashMap<String, String> powerTrainSpecs;
    private HashMap<String, String> otherSpecs;
    private String [] mvehicleTypes;

    public fragment_add_fleetRoster(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        roster = (ArrayList<Vehicle>) getActivity().getIntent().getSerializableExtra("roster");

        FragmentManager fm = getFragmentManager();
        dialog_addField_required dialog = new dialog_addField_required();
        dialog.setTargetFragment(fragment_add_fleetRoster.this, 0);
        dialog.show(fm, "dialog_add_field");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_fleet_roster, container, false);
        context = getActivity().getApplicationContext();

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Spinner
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_vehicle_type);
        mvehicleTypes = context.getResources().getStringArray(R.array.vehicle_type);
        vehicleSpinner.setAdapter(new ArrayAdapter<> (getActivity(), R.layout.spinner_drop_item, mvehicleTypes));

        //Recycler View
        addList = (RecyclerView) view.findViewById(R.id.add_fleet_roster_list_car);
        addMan = new LinearLayoutManager(getActivity());
        addList.setLayoutManager(addMan);
        addList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity().getApplicationContext(), addList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                FragmentManager fm = getFragmentManager();
                Bundle args = new Bundle();

                if(i > 0){
                    args.putSerializable("field", allSpecs.get(i));
                    dialog_addField dialog_addField = new dialog_addField();
                    dialog_addField.setTargetFragment(fragment_add_fleetRoster.this, 0);
                    dialog_addField.setArguments(args);
                    dialog_addField.show(fm, "dialog_add_field");
                }else {
                    args.putSerializable("year", vehicle.getReservedSpecs());
                    dialog_addField_required dialog_addField_required = new dialog_addField_required();
                    dialog_addField_required.setTargetFragment(fragment_add_fleetRoster.this, 0);
                    dialog_addField_required.setArguments(args);
                    dialog_addField_required.show(fm, "dialog_addField_required");
                }
            }

            @Override
            public void onItemLongClick(View view, int i) {
                final int pos = i;

                if(i > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(true);
                    builder.setTitle("Are you sure you would like to delete this field?");
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            removeEntry(pos);
                        }});

                    builder.show();
                }else{
                    Snackbar.make(view.getRootView().findViewById(R.id.snackbar), getString(R.string.error_required), Snackbar.LENGTH_SHORT)
                            .setActionTextColor(ContextCompat.getColor(context, R.color.error)).show();
                }
            }}));

        //menu_overview_fab
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();

                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_add_fleetRoster.this, 0);
                dialog_addField.show(fm, "dialog_add_field");
            }
        });



        return view;
    }

    public void onStart(){
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> result = data.getStringArrayListExtra("fieldData");

        switch (resultCode){
            case (0):
                Log.d(TAG, result.toString());
                allSpecs.add(result);
                vehicle = new Vehicle(vehicleSpinner.getText().toString(), result.get(0),
                        result.get(1), result.get(2));

                addListAdapter = new adapter_add_fleetRoster(allSpecs);
                addList.setAdapter(addListAdapter);
                break;

            case (1):
                allSpecs.add(result);
                switch (result.get(0)){
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

            default:
                break;
        }

        addListAdapter = new adapter_add_fleetRoster(allSpecs);
        addList.swapAdapter(addListAdapter, false);
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
                if(!isLegit()) {
                    roster.add(vehicle);
                    SaveLoadHelper saveLoadHelper = new SaveLoadHelper(context);
                    saveLoadHelper.save(roster);
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

        addListAdapter = new adapter_add_fleetRoster(allSpecs);
        addList.swapAdapter(addListAdapter, false);
    }
}

