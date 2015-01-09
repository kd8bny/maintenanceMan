package com.kd8bny.maintenanceman.ui.main;



import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.kd8bny.maintenanceman.ui.settings.activity_settings;

import java.util.ArrayList;
import java.util.List;


public class fragment_overview extends Fragment {
    private static final String TAG = "fragment_overview";

    public ArrayList<ArrayList> vehicleList = new ArrayList<ArrayList>();
    public List<String> vehicleSpecs= new ArrayList<String>();


    public fragment_overview() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onResume();

        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //setListAdapter(poplulateAdapter());
        testRecycle();
    }

    public void testRecycle() {


        vehicleSpecs.add("year");
        vehicleSpecs.add("make");
        vehicleSpecs.add("model");
        vehicleSpecs.add("Engine");

    getActivity().setContentView(R.layout.fragment_overview);

    RecyclerView recList = (RecyclerView) getActivity().findViewById(R.id.cardList);
    recList.setHasFixedSize(true);
    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
    llm.setOrientation(LinearLayoutManager.VERTICAL);
    recList.setLayoutManager(llm);

        adapter_overview ca = new adapter_overview(vehicleSpecs);
        recList.setAdapter(ca);
    }

   /* @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if((vehicleList.get(position)).get(0)!=null){
            Intent intent = new Intent(getActivity(), activity_vehicleEvent.class);

            intent.putStringArrayListExtra("vehicleSent",vehicleList.get(position));
            startActivity(intent);
        }

    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_overview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;

            case R.id.menu_add:
                Intent addIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                startActivity(addIntent);
                return true;

            case R.id.menu_settings:
                Intent settingsIntent = new Intent(getActivity(), activity_settings.class);
                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public ArrayAdapter poplulateAdapter(){
        ArrayList<String> singleVehicleList = new ArrayList<String>();
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());

        ArrayList<String> temp;
        for(int i = 0; i < vehicleList.size(); i++){
            temp = vehicleList.get(i);
            if(temp.size()>2) {
                singleVehicleList.add(temp.get(1) + " " + temp.get(2) + " " + temp.get(3));
            }else{
                singleVehicleList.add(temp.get(1));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, singleVehicleList);
        Log.i(TAG,adapter.toString());
        return adapter;
    }*/

}
