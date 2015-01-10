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

    public ArrayList<ArrayList> vehicleList = new ArrayList<>();

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

        return inflater.inflate(R.layout.fragment_overview, container, false); //TODO need this????
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setContentView(R.layout.fragment_overview);

        RecyclerView cardList = (RecyclerView) getActivity().findViewById(R.id.overview_cardList);
        LinearLayoutManager linMan = new LinearLayoutManager(getActivity());

        cardList.setHasFixedSize(true);

        linMan.setOrientation(LinearLayoutManager.VERTICAL);
        cardList.setLayoutManager(linMan);

        cardList.setAdapter(poplulateAdapter());
    }

    public adapter_overview poplulateAdapter(){
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());

        adapter_overview adapter = new adapter_overview(vehicleList);

        return adapter;
    }

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

}
