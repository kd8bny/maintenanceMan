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


public class fragment_edit_old extends Fragment {
    private static final String TAG = "frgmnt_hstry";

    private Toolbar toolbar;

    private String refID;
    public HashMap<String, HashMap> vehicleSent;

    public fragment_edit_old() {
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

        vehicleSent = (HashMap<String, HashMap>) getActivity().getIntent().getSerializableExtra("vehicleSent");
        refID = getActivity().getIntent().getStringExtra("refID");

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //setValues(view);

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
                //TODO type
                Context context = getActivity().getApplicationContext();

                //getValues();

                //fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(context);
                //fleetDB.deleteEntry(context, refID);
                //fleetDB.saveEntry(context, getValues());

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

    public void setValues(){


    }
}
