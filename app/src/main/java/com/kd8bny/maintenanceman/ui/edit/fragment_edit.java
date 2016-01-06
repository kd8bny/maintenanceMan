package com.kd8bny.maintenanceman.ui.edit;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.backupRestoreHelper;
import com.kd8bny.maintenanceman.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.add.adapter_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_addField;

import com.github.clans.fab.FloatingActionButton;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_edit extends Fragment {
    private static final String TAG = "frg_edit";

    private Toolbar toolbar;
    private MaterialBetterSpinner vehicleSpinner;

    private RecyclerView addList;
    private RecyclerView.LayoutManager addMan;
    private RecyclerView.Adapter addListAdapter;
    private FloatingActionButton fab;

    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<ArrayList> vehicleDataAll = new ArrayList<>();
    private String refID;
    private HashMap<String, HashMap> roster;
    public HashMap<String, HashMap> vehicleSent;
    public String [] mvehicleTypes;

    public fragment_edit(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        refID = getActivity().getIntent().getStringExtra("refID");

        fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
        roster = new HashMap<>(fltjson.getEntries(getActivity().getApplicationContext()));
        vehicleSent = roster.get(refID);

        for (String key : vehicleSent.keySet()){
            HashMap<String, String> fieldTemp = vehicleSent.get(key);
            for(String fieldKey : fieldTemp.keySet()) {
                ArrayList<String> temp = new ArrayList<>();
                if (!fieldKey.equals("type")){
                    temp.add(key);
                    temp.add(fieldKey);
                    temp.add(fieldTemp.get(fieldKey));
                    if(fieldKey.equals("Make") | fieldKey.equals("Model") | fieldKey.equals("Year")){
                        vehicleDataAll.add(0, temp);
                    }else {
                        vehicleDataAll.add(temp);
                    }
                }
            }
        }
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
        vehicleSpinner.setText(vehicleSent.get("General").get("type").toString());
        mvehicleTypes = getActivity().getResources().getStringArray(R.array.vehicle_type);
        spinnerAdapter = new ArrayAdapter<> (getActivity(), android.R.layout.simple_spinner_dropdown_item, mvehicleTypes);
        vehicleSpinner.setAdapter(spinnerAdapter);

        //Recycler View
        addList = (RecyclerView) view.findViewById(R.id.add_fleet_roster_list_car);
        addMan = new LinearLayoutManager(getActivity());
        addList.setHasFixedSize(true);
        addList.setItemAnimator(new DefaultItemAnimator());
        addList.setLayoutManager(addMan);
        addList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity().getApplicationContext(),
                addList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Bundle args = new Bundle();
                if(pos > 2){
                    args.putBoolean("isRequired", false);
                }else {
                    args.putBoolean("isRequired", true);
                }
                args.putSerializable("field", vehicleDataAll.get(pos));
                args.putInt("pos", pos);
                FragmentManager fm = ((FragmentActivity) view.getContext()).getFragmentManager();

                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_edit.this, 0);
                dialog_addField.setArguments(args);
                dialog_addField.show(fm, "dialog_add_field");
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
                    Snackbar.make(view.getRootView().findViewById(R.id.snackbar), getString(R.string.error_required), Snackbar.LENGTH_SHORT)
                            .setActionTextColor(getResources().getColor(R.color.error)).show();
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
                FragmentManager fm = ((FragmentActivity) view.getContext()).getFragmentManager();

                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_edit.this, 0);
                dialog_addField.show(fm, "dialog_add_field");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        ArrayList<String> result = data.getStringArrayListExtra("fieldData");
        int pos = data.getIntExtra("pos", -1);

        switch (data.getStringExtra("action")){
            case ("edit"):
                if (pos != -1) {
                    vehicleDataAll.set(pos,result);
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
                ArrayList<String> temp = new ArrayList<>();
                temp.add("General");
                temp.add("type");
                temp.add(vehicleSpinner.getText().toString());
                vehicleDataAll.add(temp);
                Context context = getActivity().getApplicationContext();

                fleetRosterJSONHelper fleetDB = new fleetRosterJSONHelper();
                fleetDB.deleteEntry(context, refID);
                fleetDB.saveEntry(context, refID, vehicleDataAll);

                backupRestoreHelper mbackupRestoreHelper = new backupRestoreHelper();
                mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "backup", false);

                Toast.makeText(this.getActivity(), "Vehicle Saved", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                return true;

            case R.id.menu_cancel:
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

