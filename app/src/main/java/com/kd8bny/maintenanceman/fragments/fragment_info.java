package com.kd8bny.maintenanceman.fragments;

import android.content.Context;
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

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.activities.VehicleActivity;
import com.kd8bny.maintenanceman.adapters.adapter_info;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_info extends Fragment {
    private static final String TAG = "frgmnt_inf";

    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;

    private Context context;
    private ArrayList<Vehicle> roster;
    private int vehiclePos;
    private Vehicle vehicle;

    public fragment_info() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        roster = bundle.getParcelableArrayList("roster");
        vehiclePos = bundle.getInt("vehiclePos", -1);
        vehicle = roster.get(vehiclePos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        registerForContextMenu(view);

        //Info Cards
        cardList = (RecyclerView) view.findViewById(R.id.info_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setLayoutManager(cardMan);
        cardListAdapter = new adapter_info(vehicle);
        cardList.setAdapter(cardListAdapter);

        //menu_overview_fab
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_info.this, 0);
                dialog_addField.show(fm, "dialog_add_field");
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        cardListAdapter = new adapter_info(vehicle);
        cardList.setAdapter(cardListAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "save");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> result = data.getStringArrayListExtra("fieldData");

        switch (resultCode) {
            case (1):
                HashMap<String, String> temp;
                switch (result.get(0)) {//TODO replace changed key
                    case "General":
                        temp = vehicle.getGeneralSpecs();
                        temp.put(result.get(1), result.get(2));
                        vehicle.setGeneralSpecs(temp);
                        break;
                    case "Engine":
                        temp = vehicle.getEngineSpecs();
                        temp.put(result.get(1), result.get(2));
                        vehicle.setEngineSpecs(temp);
                        break;
                    case "Power Train":
                        temp = vehicle.getPowerTrainSpecs();
                        temp.put(result.get(1), result.get(2));
                        vehicle.setPowerTrainSpecs(temp);
                        break;
                    case "Other":
                        temp = vehicle.getOtherSpecs();
                        temp.put(result.get(1), result.get(2));
                        vehicle.setOtherSpecs(temp);
                        break;
                }
            break;
        }
        roster.set(vehiclePos, vehicle);
        new SaveLoadHelper(context).save(roster);

        onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch (menuitem.getItemId()) {
            case R.id.menu_edit:
                Bundle bundle = new Bundle();
                bundle.putInt("caseID", 2);
                bundle.putInt("vehiclePos", vehiclePos);
                bundle.putParcelableArrayList("roster", roster);
                Log.d(TAG, roster.toString());
                startActivity(new Intent(getActivity(), VehicleActivity.class).putExtra("bundle", bundle));

                return true;

            default:
                return false;
        }
    }
}