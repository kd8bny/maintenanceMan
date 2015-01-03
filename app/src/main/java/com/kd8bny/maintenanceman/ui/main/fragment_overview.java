package com.kd8bny.maintenanceman.ui.main;



import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;

import java.util.ArrayList;
import java.util.List;


public class fragment_overview extends ListFragment {
    private static final String TAG = "fragment_overview";


    public fragment_overview() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Set loader to update Screen when SQL changes
        //TODO getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        ArrayList<String> vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, vehicleList);

        //ListView listView = (ListView) getActivity().findViewById(android.R.id.list);
        //listView.setAdapter(adapter);
        setListAdapter(adapter);
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_overview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_add:
                Intent intent = new Intent(getActivity(), activity_add_fleetRoster.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
