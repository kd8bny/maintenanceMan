package com.kd8bny.maintenanceman.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
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

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.TravelEventAdapter;
import com.kd8bny.maintenanceman.classes.Vehicle.Travel;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.dialogs.dialog_addTravelEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_datePicker;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;


public class fragment_travel_add extends Fragment {
    private static final String TAG = "frg_add_vhclEvnt";

    private Context mContext;
    private MaterialBetterSpinner vehicleSpinner;
    private ArrayAdapter<String> spinnerAdapter;

    private RecyclerView eventList;
    private RecyclerView.Adapter eventListAdapter;

    private ArrayList<Vehicle> roster;
    private Vehicle vehicle;
    private int vehiclePos;
    private String refID;
    private Travel mTravel;
    private ArrayList<String> singleVehicle = new ArrayList<>();

    public fragment_travel_add() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getArguments() ;
        roster = bundle.getParcelableArrayList("roster");
        vehiclePos = bundle.getInt("vehiclePos");
        vehicle = roster.get(vehiclePos);
        refID = vehicle.getRefID();

        mTravel = (Travel) bundle.getSerializable("event"); //TODO edit better
        if (mTravel == null) {
            mTravel = new Travel(refID);
            final Calendar cal = Calendar.getInstance();
            mTravel.setDate(cal.get(Calendar.MONTH) + 1
                    + "/" + cal.get(Calendar.DAY_OF_MONTH)
                    + "/" + cal.get(Calendar.YEAR));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_vehicle_event, container, false);

        //Spinner
        vehicleSpinner = (MaterialBetterSpinner) view.findViewById(R.id.vehicleSpinner);
        vehicleSpinner.setText(vehicle.getTitle());
        spinnerAdapter = setVehicles();
        vehicleSpinner.setAdapter(spinnerAdapter);

        //Recycler View
        eventList = (RecyclerView) view.findViewById(R.id.add_vehicle_event);
        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventList.setHasFixedSize(true);
        eventList.setItemAnimator(new DefaultItemAnimator());
        eventList.addOnItemTouchListener(
                new RecyclerViewOnItemClickListener(mContext, eventList,
                        new RecyclerViewOnItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int pos) {
                                FragmentManager fm = getFragmentManager();
                                Bundle bundle = new Bundle();
                                switch (pos){
                                    case 0:
                                        dialog_datePicker datePicker = new dialog_datePicker();
                                        datePicker.setTargetFragment(fragment_travel_add.this, 0);
                                        datePicker.show(fm, "datePicker");
                                        break;

                                    default:
                                        bundle.putSerializable("event", mTravel);
                                        dialog_addTravelEntry addBusinessEvent = new dialog_addTravelEntry();
                                        addBusinessEvent.setTargetFragment(fragment_travel_add.this, pos);
                                        addBusinessEvent.setArguments(bundle);
                                        addBusinessEvent.show(fm, "dialog_addEvent");
                                        break;
                                }}

                            @Override
                            public void onItemLongClick(View view, int pos) {}
                        }));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        String val = data.getBundleExtra("bundle").getString("value");
        switch (resultCode) {
            case 0:
                mTravel.setDate(val);
                break;
            case 1:
                mTravel.setStart(Double.parseDouble(val));
                break;
            case 2:
                mTravel.setStop(Double.parseDouble(val));
                break;
            case 3:
                mTravel.setDest(val);
                break;
            case 4:
                mTravel.setPurpose(val);
                break;
            default:
                Log.i(TAG, "No return");
        }
        eventListAdapter = new TravelEventAdapter(mTravel);
        eventList.setAdapter(eventListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        eventListAdapter = new TravelEventAdapter(mTravel);
        eventList.setAdapter(eventListAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fleet_roster_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_save:
                if(!isLegit()){
                    int pos = singleVehicle.indexOf(vehicleSpinner.getText().toString());
                    mTravel.setRefID(roster.get(pos).getRefID());

                    VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
                    vehicleLogDBHelper.insertEntry(mTravel);

                    Snackbar.make(getActivity().findViewById(R.id.snackbar),
                            getString(R.string.error_field_event), Snackbar.LENGTH_SHORT)
                            .setActionTextColor(getResources().getColor(R.color.error)).show(); //TODO snakz w/ right label

                    getActivity().finish();
                    return true;
                }

                return false;

            case R.id.menu_cancel:
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ArrayAdapter<String> setVehicles(){
        for(Vehicle v : roster) {
            singleVehicle.add(v.getTitle());
        }
        return new ArrayAdapter<>(getActivity(), R.layout.spinner_drop_item, singleVehicle);
    }

    public boolean isLegit(){
        if (singleVehicle.indexOf(vehicleSpinner.getText().toString()) == -1){
            vehicleSpinner.setError(getResources().getString(R.string.error_set_vehicle));
            return true;
        }
        if (mTravel.getStart() == null){
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.error_field_event), Snackbar.LENGTH_SHORT)
                    .setActionTextColor(getResources().getColor(R.color.error)).show();
            return true;
        }
        if (mTravel.getDest().isEmpty()){
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.error_field_event), Snackbar.LENGTH_SHORT)
                    .setActionTextColor(getResources().getColor(R.color.error)).show();
            return true;
        }

        return false;
    }
}
