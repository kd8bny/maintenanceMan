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
import com.kd8bny.maintenanceman.adapters.VehicleEventAdapter;
import com.kd8bny.maintenanceman.classes.Vehicle.Event;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.dialogs.dialog_addVehicleEvent;
import com.kd8bny.maintenanceman.dialogs.dialog_datePicker;
import com.kd8bny.maintenanceman.dialogs.dialog_iconPicker;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public class fragment_vehicleEvent_edit extends Fragment {
    private static final String TAG = "frg_add_vhclEvnt";

    private Context mContext;
    private MaterialBetterSpinner vehicleSpinner;
    private ArrayAdapter<String> spinnerAdapter;

    private RecyclerView eventList;
    private RecyclerView.Adapter eventListAdapter;

    private ArrayList<Vehicle> roster;
    private int vehiclePos;
    private Vehicle vehicle;
    private String refID;
    private ArrayList<String> singleVehicle = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();
    private Event mEvent;


    public fragment_vehicleEvent_edit() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getArguments();
        mEvent = (Event) bundle.getSerializable("event");
        roster = bundle.getParcelableArrayList("roster");
        vehiclePos = bundle.getInt("vehiclePos");
        vehicle = roster.get(vehiclePos);
        refID = vehicle.getRefID();
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
                        dialog_iconPicker iconPicker = new dialog_iconPicker();
                        iconPicker.setTargetFragment(fragment_vehicleEvent_edit.this, 0);
                        iconPicker.show(fm, "dialogIconPicker");
                        break;

                    case 1:
                        dialog_datePicker datePicker = new dialog_datePicker();
                        datePicker.setTargetFragment(fragment_vehicleEvent_edit.this, 1);
                        datePicker.show(fm, "datePicker");
                        break;

                    default:
                        bundle.putSerializable("event", mEvent);
                        dialog_addVehicleEvent dialog_addVehicleEvent = new dialog_addVehicleEvent();
                        dialog_addVehicleEvent.setTargetFragment(fragment_vehicleEvent_edit.this, pos);
                        dialog_addVehicleEvent.setArguments(bundle);
                        dialog_addVehicleEvent.show(fm, "dialog_addEvent");
                        break;
                }}

            @Override
            public void onItemLongClick(View view, int pos) {}
        }));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        eventListAdapter = new VehicleEventAdapter(mEvent);
        eventList.setAdapter(eventListAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0:
                mEvent.setIcon(data.getIntExtra("value", 0));
                break;
            case 1:
                mEvent.setDate(data.getStringExtra("value"));
                break;
            case 2:
                mEvent.setOdometer(data.getStringExtra("value"));
                break;
            case 3:
                mEvent.setEvent(data.getStringExtra("value"));
                break;
            case 4:
                mEvent.setPrice(data.getStringExtra("value"));
                break;
            case 5:
                mEvent.setComment(data.getStringExtra("value"));
                break;
            default:
                Log.i(TAG, "No return");
        }
        eventListAdapter = new VehicleEventAdapter(mEvent);
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
                    int pos = singleVehicle.indexOf(vehicleSpinner.getText().toString()); //In case of vehicle change
                    mEvent.setRefID(roster.get(pos).getRefID());

                    VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
                    vehicleLogDBHelper.deleteEntry(mEvent);
                    vehicleLogDBHelper.insertEntry(mEvent);

                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.error_field_event), Snackbar.LENGTH_SHORT)
                            .setActionTextColor(getResources().getColor(R.color.error)).show(); ///TODO snakz w/ right label

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
        if (mEvent.getEvent().isEmpty()){
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.error_field_event), Snackbar.LENGTH_SHORT)
                    .setActionTextColor(getResources().getColor(R.color.error)).show();
            return true;
        }

        return false;
    }
}
