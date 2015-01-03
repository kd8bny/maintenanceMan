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

    public ArrayAdapter<String> adapter;


    public fragment_overview() {
        // Required empty public constructor
    }

    public void poplulateAdapter(){
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        ArrayList<String> vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, vehicleList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //TODO getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        poplulateAdapter();
        setListAdapter(adapter);
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        poplulateAdapter();
        setListAdapter(adapter);
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
