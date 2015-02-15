package com.kd8bny.maintenanceman.ui.edit;

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
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;

import java.util.HashMap;


public class fragment_edit extends Fragment {
    private static final String TAG = "fragment_history";

    private Toolbar toolbar;

    private String refID;
    private String myear;
    private String mmake;
    private String mmodel;
    private String mengine;
    private String mplate;
    private String moil_filter;
    private String moil_weight;
    private String mtire_winter;
    private String mtire_summer;

    public HashMap<String, String> vehicleSent;

    public fragment_edit() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_fleet_roster, container, false);

        vehicleSent = (HashMap<String, String>) getActivity().getIntent().getSerializableExtra("vehicleSent");
        refID = vehicleSent.get("refID");

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        setValues(view);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                Context context = getActivity().getApplicationContext();

                getValues();

                fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(context);
                fleetDB.deleteEntry(context, refID);
                fleetDB.saveEntry(context, refID, mmake, mmodel, myear, mengine, mplate, moil_filter, moil_weight, mtire_summer, mtire_winter);//TODO Arraylist or dict

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

    public void setValues(View view){
        ((EditText) view.findViewById(R.id.val_spec_year)).setText((vehicleSent.get("year")));
        ((EditText) view.findViewById(R.id.val_spec_make)).setText((vehicleSent.get("make")));
        ((EditText) view.findViewById(R.id.val_spec_model)).setText((vehicleSent.get("model")));
        ((EditText) view.findViewById(R.id.val_spec_engine)).setText((vehicleSent.get("engine")));

        ((EditText) view.findViewById(R.id.val_spec_plate)).setText((vehicleSent.get("plate")));
        ((EditText) view.findViewById(R.id.val_spec_oil_filter)).setText((vehicleSent.get("oilFilter")));
        ((EditText) view.findViewById(R.id.val_spec_oil_weight)).setText((vehicleSent.get("oilWeight")));
        ((EditText) view.findViewById(R.id.val_spec_tire_size_winter)).setText((vehicleSent.get("tireWinter")));
        ((EditText) view.findViewById(R.id.val_spec_tire_size_summer)).setText((vehicleSent.get("tireSummer")));

    }

    public void getValues(){
        myear = ((EditText) getActivity().findViewById(R.id.val_spec_year)).getText().toString();
        mmake = ((EditText) getActivity().findViewById(R.id.val_spec_make)).getText().toString();
        mmodel = ((EditText) getActivity().findViewById(R.id.val_spec_model)).getText().toString();
        mengine = ((EditText) getActivity().findViewById(R.id.val_spec_engine)).getText().toString();

        mplate = ((EditText) getActivity().findViewById(R.id.val_spec_plate)).getText().toString();
        moil_filter = ((EditText) getActivity().findViewById(R.id.val_spec_oil_filter)).getText().toString();
        moil_weight = ((EditText) getActivity().findViewById(R.id.val_spec_oil_weight)).getText().toString();
        mtire_winter = ((EditText) getActivity().findViewById(R.id.val_spec_tire_size_winter)).getText().toString();
        mtire_summer = ((EditText) getActivity().findViewById(R.id.val_spec_tire_size_summer)).getText().toString();

    }
}
