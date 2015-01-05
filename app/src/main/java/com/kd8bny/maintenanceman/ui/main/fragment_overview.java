package com.kd8bny.maintenanceman.ui.main;



import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;

import java.util.ArrayList;


public class fragment_overview extends ListFragment {
    private static final String TAG = "fragment_overview";

    //public ArrayAdapter<String> adapter = new ArrayAdapter<String>(null);



    public fragment_overview() {
        // Required empty public constructor
    }

    public ArrayAdapter poplulateAdapter(){
        ArrayList<String> singleVehicleList = new ArrayList<String>();
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        ArrayList<ArrayList> vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());

        ArrayList<String> temp;
        Log.i(TAG,vehicleList.toString());
        for(int i = 0; i < vehicleList.size(); i++){
            Log.i(TAG,""+i);
            temp = vehicleList.get(i);
            if(temp.size()>1) {
                singleVehicleList.add(temp.get(1) + " " + temp.get(2) + " " + temp.get(3));
            }else{
                singleVehicleList.add(temp.get(0));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, singleVehicleList);
        Log.i(TAG,adapter.toString());
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //TODO getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onResume();
        //poplulateAdapter();
        //setListAdapter(poplulateAdapter());
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        //poplulateAdapter();
        setListAdapter(poplulateAdapter());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //This will add individual og stuff
        Intent intent = new Intent(getActivity(), activity_vehicleEvent.class);
        startActivity(intent);
        //Toast.makeText(getActivity(),(singleVehicleList.get(position)).toString(),Toast.LENGTH_SHORT).show();
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
