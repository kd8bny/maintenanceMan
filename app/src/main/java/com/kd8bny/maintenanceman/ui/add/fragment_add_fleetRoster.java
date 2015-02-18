package com.kd8bny.maintenanceman.ui.add;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;

import java.util.HashMap;


public class fragment_add_fleetRoster extends Fragment {
    private static final String TAG = "fragment_add_fleetRoster";

    private Toolbar toolbar;

    public fragment_add_fleetRoster(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_fleet_roster, container, false);

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Headers
        View vheader;
        ImageView iheader;

        vheader = view.findViewById(R.id.header_carSpecs);
        iheader = (ImageView) vheader.findViewById(R.id.header_icon);
        iheader.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_car));

        vheader = view.findViewById(R.id.header_engineSpecs);
        iheader = (ImageView) vheader.findViewById(R.id.header_icon);
        iheader.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_engine));

        vheader = view.findViewById(R.id.header_tiresSpecs);
        iheader = (ImageView) vheader.findViewById(R.id.header_icon);
        iheader.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_tires));

        return view;
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

                fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(context);
                fleetDB.saveEntry(context, getValues());

                Toast.makeText(this.getActivity(), "New Vehicle Saved", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                return true;

            case R.id.menu_cancel:
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public HashMap<String, String> getValues(){
        HashMap<String, String> vehicleInfo = new HashMap<>();

        vehicleInfo.put("refID", null);
        vehicleInfo.put("year", ((EditText) getActivity().findViewById(R.id.val_spec_year)).getText().toString());
        vehicleInfo.put("make", ((EditText) getActivity().findViewById(R.id.val_spec_make)).getText().toString());
        vehicleInfo.put("model", ((EditText) getActivity().findViewById(R.id.val_spec_model)).getText().toString());
        vehicleInfo.put("engine", ((EditText) getActivity().findViewById(R.id.val_spec_engine)).getText().toString());

        vehicleInfo.put("plate", ((EditText) getActivity().findViewById(R.id.val_spec_plate)).getText().toString());
        vehicleInfo.put("oilFilter", ((EditText) getActivity().findViewById(R.id.val_spec_oil_filter)).getText().toString());
        vehicleInfo.put("oilWeight", ((EditText) getActivity().findViewById(R.id.val_spec_oil_weight)).getText().toString());
        vehicleInfo.put("tireWinter", ((EditText) getActivity().findViewById(R.id.val_spec_tire_size_winter)).getText().toString());
        vehicleInfo.put("tireSummer", ((EditText) getActivity().findViewById(R.id.val_spec_tire_size_summer)).getText().toString());

        return vehicleInfo;
    }
}

