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
import android.widget.EditText;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<String> vehicleSent = getActivity().getIntent().getStringArrayListExtra("vehicleSent");
        refID = vehicleSent.get(0);

        return inflater.inflate(R.layout.fragment_add_vehicle_event, container, false);
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

                Toast.makeText(this.getActivity(), "New Vehicle Saved", Toast.LENGTH_SHORT).show();
                getActivity().finish();

            case R.id.menu_cancel:
                getActivity().finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getValues(){
        date = ((EditText) getActivity().findViewById(R.id.date)).getText().toString();
        task = ((EditText) getActivity().findViewById(R.id.task)).getText().toString();
        odo = ((EditText) getActivity().findViewById(R.id.odo)).getText().toString();
    }
}
