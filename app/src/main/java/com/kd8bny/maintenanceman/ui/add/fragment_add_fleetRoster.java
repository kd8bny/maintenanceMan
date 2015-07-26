package com.kd8bny.maintenanceman.ui.add;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.backupRestoreHelper;
import com.kd8bny.maintenanceman.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_addField;

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_addField_required;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.EventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class fragment_add_fleetRoster extends Fragment {
    private static final String TAG = "frg_add_fltRstr";

    private Toolbar toolbar;
    private MaterialBetterSpinner vehicleSpinner;

    private RecyclerView addList;
    private RecyclerView.LayoutManager addMan;
    private RecyclerView.Adapter addListAdapter;
    private FloatingActionButton fab;

    private ArrayAdapter<String> spinnerAdapter;
    public ArrayList<ArrayList> vehicleDataAll = new ArrayList<>();
    public String [] mvehicleTypes;

    public fragment_add_fleetRoster(){
        ArrayList<String> tempYear = new ArrayList<>();
            tempYear.add("General");
            tempYear.add("Year");
            tempYear.add("");

        ArrayList<String> tempMake = new ArrayList<>();
            tempMake.add("General");
            tempMake.add("Make");
            tempMake.add("");

        ArrayList<String> tempModel = new ArrayList<>();
            tempModel.add("General");
            tempModel.add("Model");
            tempModel.add("");

        vehicleDataAll.add(tempYear);
        vehicleDataAll.add(tempMake);
        vehicleDataAll.add(tempModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_fleet_roster, container, false);

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Spinner
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.spinner_vehicle_type);
        mvehicleTypes = getActivity().getResources().getStringArray(R.array.vehicle_type);
        spinnerAdapter = new ArrayAdapter<> (getActivity(), R.layout.spinner_drop_item, mvehicleTypes);
        vehicleSpinner.setAdapter(spinnerAdapter);

        //Recycler View
        addList = (RecyclerView) view.findViewById(R.id.add_fleet_roster_list_car);
        addMan = new LinearLayoutManager(getActivity());
        addList.setHasFixedSize(true);
        addList.setItemAnimator(new DefaultItemAnimator());
        addList.setLayoutManager(addMan);
        addList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity().getApplicationContext(), addList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                FragmentManager fm = getFragmentManager();
                Bundle args = new Bundle();
                args.putSerializable("field", vehicleDataAll.get(pos));
                if(pos > 2){
                    dialog_addField dialog_addField = new dialog_addField();
                    dialog_addField.setTargetFragment(fragment_add_fleetRoster.this, 0);
                    dialog_addField.setArguments(args);
                    dialog_addField.show(fm, "dialog_add_field");
                }else {
                    dialog_addField_required dialog_addField_required = new dialog_addField_required();
                    dialog_addField_required.setTargetFragment(fragment_add_fleetRoster.this, 0);
                    dialog_addField_required.setArguments(args);
                    dialog_addField_required.show(fm, "dialog_addField_required");
                }
            }

            @Override
            public void onItemLongClick(View view, int pos) {
                final int itemPos = pos;

                if(itemPos > 2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(true);
                    builder.setTitle("Are you sure you would like to delete this field?");
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            vehicleDataAll.remove(itemPos);

                            addListAdapter = new adapter_add_fleetRoster(vehicleDataAll);
                            addList.swapAdapter(addListAdapter, false);
                        }
                    });

                    builder.show();
                }else{
                    SnackbarManager.show(
                        Snackbar.with(getActivity().getApplicationContext())
                            .color(getResources().getColor(R.color.error))
                            .text(R.string.error_required)
                            .eventListener(new EventListener() {
                                @Override
                                public void onShow(Snackbar snackbar) {
                                    fab.animate().translationY(-snackbar.getHeight());

                                }

                                @Override
                                public void onShowByReplace(Snackbar snackbar) {

                                }

                                @Override
                                public void onShown(Snackbar snackbar) {

                                }

                                @Override
                                public void onDismiss(Snackbar snackbar) {
                                    fab.animate().translationY(0);

                                }

                                @Override
                                public void onDismissByReplace(Snackbar snackbar) {

                                }

                                @Override
                                public void onDismissed(Snackbar snackbar) {

                                }
                            }), getActivity());
                }
            }
        }));

        addListAdapter = new adapter_add_fleetRoster(vehicleDataAll);
        addList.setAdapter(addListAdapter);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        ArrayList<String> result = data.getStringArrayListExtra("fieldData");

        switch (data.getStringExtra("action")){
            case ("edit"):
                for (int i =0; i<vehicleDataAll.size(); i++){
                    ArrayList<String> temp = vehicleDataAll.get(i);
                    if (temp.get(1).equals(result.get(1))){
                        vehicleDataAll.set(i, result);
                        break;
                    }
                }
                break;

            default: //new
                vehicleDataAll.add(result);

                break;
        }

        addListAdapter = new adapter_add_fleetRoster(vehicleDataAll);
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
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add("General");
                    temp.add("type");
                    temp.add(vehicleSpinner.getText().toString());
                    vehicleDataAll.add(temp);
                    Context context = getActivity().getApplicationContext();

                    fleetRosterJSONHelper fleetDB = new fleetRosterJSONHelper();
                    fleetDB.saveEntry(context, null, vehicleDataAll);

                    Toast.makeText(this.getActivity(), "New Vehicle Saved", Toast.LENGTH_SHORT).show();

                    backupRestoreHelper bku = new backupRestoreHelper(getActivity().getApplicationContext(), "backup");
                    bku.execute();

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

    public boolean isLegit(){
        Boolean error = false;
        if (Arrays.asList(mvehicleTypes).indexOf(vehicleSpinner.getText().toString()) == -1){
            vehicleSpinner.setError(getResources().getString(R.string.error_set_vehicle_type));

            error = true;
        }
        for (int i = 0; i < 2; i++) {
            ArrayList<String> temp = vehicleDataAll.get(i);

            if(temp.get(2).equals("")) {
                SnackbarManager.show(
                    Snackbar.with(getActivity().getApplicationContext())
                        .text(R.string.error_required_missing)
                        .eventListener(new EventListener() {
                            @Override
                            public void onShow(Snackbar snackbar) {
                                fab.animate().translationY(-snackbar.getHeight());

                            }

                            @Override
                            public void onShowByReplace(Snackbar snackbar) {

                            }

                            @Override
                            public void onShown(Snackbar snackbar) {

                            }

                            @Override
                            public void onDismiss(Snackbar snackbar) {
                                fab.animate().translationY(0);

                            }

                            @Override
                            public void onDismissByReplace(Snackbar snackbar) {

                            }

                            @Override
                            public void onDismissed(Snackbar snackbar) {

                            }
                        }), getActivity());

                error = true;
                break;
            }
        }

        return error;
    }
}

