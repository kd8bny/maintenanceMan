package com.kd8bny.maintenanceman.ui.add;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;


public class fragment_add_vehicleEvent extends Fragment {
    private static final String TAG = "fragment_add_vehicleEvent";

    private SlidingUpPanelLayout addEvent;

    private String date;
    private String odo;
    private String task;
    private String refID;

    ArrayList<ArrayList> vehicleList;

    public fragment_add_vehicleEvent() {
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

        return inflater.inflate(R.layout.fragment_add_vehicle_event, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();

        setVehicles();
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

                this.getValues();

                vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(context);
                vehicleDB.saveEntry(context, refID, date, odo, task);

                Toast.makeText(this.getActivity(), "History Updated", Toast.LENGTH_SHORT).show();

                getActivity().finish();

                return true;


            case R.id.menu_cancel:
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setVehicles(){
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> singleVehicle = new ArrayList<String>();

        for(int i=0; i<vehicleList.size(); i++) {
            temp = (vehicleList.get(i));
            String name = temp.get(1)+temp.get(2)+temp.get(3);
            singleVehicle.add(name);
        }

        Spinner vehicleSpinner = (Spinner) getActivity().findViewById(R.id.vehicleSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), android.R.layout.simple_spinner_item, singleVehicle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(adapter);

    }

    public void getValues(){
        date = ((EditText) getActivity().findViewById(R.id.val_spec_date)).getText().toString();
        task = ((EditText) getActivity().findViewById(R.id.val_spec_event)).getText().toString();
        odo = ((EditText) getActivity().findViewById(R.id.val_spec_odo)).getText().toString();
    }


}
