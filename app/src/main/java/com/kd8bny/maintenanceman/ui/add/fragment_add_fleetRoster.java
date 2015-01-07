package com.kd8bny.maintenanceman.ui.add;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
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


public class fragment_add_fleetRoster extends Fragment {
    private static final String TAG = "fragment_add_fleetRoster";

    private String myear;
    private String mmake;
    private String mmodel;
    private String mengine;

    public void fleetRoster(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_fleet_roster, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, data.toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_fleet_roster, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;

            case R.id.menu_save:
                Context context = getActivity().getApplicationContext();

                this.getValues();

                fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(context);
                fleetDB.saveEntry(context, mmake, mmodel, myear, mengine);

                Toast.makeText(this.getActivity(), "New Vehicle Saved", Toast.LENGTH_SHORT).show();
                getActivity().finish();

            case R.id.menu_cancel:
                getActivity().finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getValues(){
        myear = ((EditText) getActivity().findViewById(R.id.val_spec_year)).getText().toString();
        mmake = ((EditText) getActivity().findViewById(R.id.val_spec_make)).getText().toString();
        mmodel = ((EditText) getActivity().findViewById(R.id.val_spec_model)).getText().toString();
        mengine = ((EditText) getActivity().findViewById(R.id.val_spec_engine)).getText().toString();
    }

}

