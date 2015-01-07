package com.kd8bny.maintenanceman.ui.add;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.NavUtils;
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
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;

import java.util.ArrayList;


public class fragment_add_vehicleEvent extends Fragment {
    private static final String TAG = "fragment_add_vehicleEvent";

    private String date;
    private String odo;
    private String task;
    private String refID;

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
        ArrayList<String> vehicleSent = getActivity().getIntent().getStringArrayListExtra("vehicleSent");
        refID = vehicleSent.get(0);

        return inflater.inflate(R.layout.fragment_add_vehicle_event, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        ListView taskHist = (ListView) getActivity().findViewById(R.id.taskList);
        taskHist.setAdapter(poplulateAdapter());
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

                vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(context);
                vehicleDB.saveEntry(context, refID, date, odo, task);

                Toast.makeText(this.getActivity(), "History Updated", Toast.LENGTH_SHORT).show();
                //getActivity().finish();
                ListView taskHist = (ListView) getActivity().findViewById(R.id.taskList);
                taskHist.setAdapter(poplulateAdapter());


            case R.id.menu_cancel:
                getActivity().finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public ArrayAdapter poplulateAdapter(){
        ArrayList<String> histEvent = new ArrayList<String>();
        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        ArrayList<ArrayList> vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext());

        ArrayList<String> temp;
        for(int i = 0; i < vehicleHist.size(); i++){
            Log.i(TAG,vehicleHist.toString());
            temp = vehicleHist.get(i);
            if(temp.size()>2) {
                histEvent.add(temp.get(1) + " " + temp.get(2) + " " + temp.get(3));
            }else{
                histEvent.add(temp.get(1));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, histEvent);
        return adapter;
    }

    public void getValues(){
        date = ((EditText) getActivity().findViewById(R.id.val_spec_date)).getText().toString();
        task = ((EditText) getActivity().findViewById(R.id.val_spec_event)).getText().toString();
        odo = ((EditText) getActivity().findViewById(R.id.val_spec_odo)).getText().toString();
    }


}
