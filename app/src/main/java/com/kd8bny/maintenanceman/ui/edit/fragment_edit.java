package com.kd8bny.maintenanceman.ui.edit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
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
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.add.adapter_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_addField;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_edit extends Fragment {
    private static final String TAG = "frg_edit";

    private Toolbar toolbar;
    private Spinner vehicleSpinner;

    private RecyclerView addList;
    private RecyclerView.LayoutManager addMan;
    private RecyclerView.Adapter addListAdapter;
    private FloatingActionButton fab;

    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<ArrayList> vehicleDataAll = new ArrayList<>();
    private String refID;

    public fragment_edit(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        HashMap<String, HashMap> vehicleSent = (HashMap<String, HashMap>) getActivity().getIntent().getSerializableExtra("vehicleSent");
        for (String key : vehicleSent.keySet()){
            HashMap<String, String> fieldTemp = vehicleSent.get(key);
            for(String fieldKey : fieldTemp.keySet()) {
                ArrayList<String> temp = new ArrayList<>();
                if (!fieldKey.equals("type")){
                    switch (key) {
                        case ("gen"):
                            temp.add("General");
                            break;
                        case ("eng"):
                            temp.add("Engine");
                            break;
                        case ("pwr"):
                            temp.add("Power Train");
                            break;
                        case ("other"):
                            temp.add("Other");
                            break;
                        default:
                            Log.e(TAG, "No case");
                    }
                    temp.add(fieldKey);
                    temp.add(fieldTemp.get(fieldKey));
                    vehicleDataAll.add(temp);
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
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Spinner
        vehicleSpinner = (Spinner) view.findViewById(R.id.spinner_vehicle_type);
        final String [] mvehicleTypes = getActivity().getResources().getStringArray(R.array.vehicle_type);
        spinnerAdapter = new ArrayAdapter<> (getActivity(), android.R.layout.simple_spinner_dropdown_item, mvehicleTypes);
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
                Bundle args = new Bundle();
                args.putSerializable("field", vehicleDataAll.get(pos));
                FragmentManager fm = ((FragmentActivity) view.getContext()).getFragmentManager();

                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_edit.this, 0);
                dialog_addField.setArguments(args);
                dialog_addField.show(fm, "dialog_add_field");
            }

            @Override
            public void onItemLongClick(View view, int pos) {

            }
        }));
        //RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        //recyclerView.addItemDecoration(itemDecoration);
        addListAdapter = new adapter_add_fleetRoster(vehicleDataAll);
        addList.setAdapter(addListAdapter);

        //fab
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToRecyclerView(addList);
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

        switch (data.getStringExtra("action")){
            case ("edit"): //TODO notify adapter
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
                ArrayList<String> temp = new ArrayList<>();
                temp.add("General");
                temp.add("type");
                temp.add((String)vehicleSpinner.getSelectedItem());
                vehicleDataAll.add(temp);
                Context context = getActivity().getApplicationContext();

                fleetRosterJSONHelper fleetDB = new fleetRosterJSONHelper();
                fleetDB.deleteEntry(context, refID);
                fleetDB.saveEntry(context, null, vehicleDataAll);

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

