package com.kd8bny.maintenanceman.ui.settings;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;

import java.util.ArrayList;

public class activity_sub_settings extends ListActivity {

    //TODO Use fragment and actionbar ativity


    private Toolbar toolbar;

    ArrayList<ArrayList> vehicleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.w("##############subsettings","TODO Use fragment and actionbar ativity");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, getVehicles());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this);
        ArrayList<String> singleVehicle = vehicleList.get(position);
        String refID = singleVehicle.get(0);

        fleetDB.deleteEntry(this, refID);
        this.finish();

    }

    public ArrayList<String> getVehicles() {
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this);
        vehicleList = fleetDB.getEntries(this.getApplicationContext());
        ArrayList<String> finalVehicleList = new ArrayList<>();
        ArrayList<String> singleVehicle;

        for (int i = 0; i < vehicleList.size(); i++) {
            singleVehicle = vehicleList.get(i);
            finalVehicleList.add(singleVehicle.get(1));
        }
        return finalVehicleList;
    }
}
