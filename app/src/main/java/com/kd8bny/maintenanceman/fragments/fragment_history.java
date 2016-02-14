package com.kd8bny.maintenanceman.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.activities.VehicleActivity;
import com.kd8bny.maintenanceman.adapters.HistoryAdapter;
import com.kd8bny.maintenanceman.classes.Vehicle.Event;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.dialogs.dialog_vehicleHistory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class fragment_history extends Fragment {
    private static final String TAG = "frgmnt_hist";

    private RecyclerView histList;
    private RecyclerView.LayoutManager histMan;
    private RecyclerView.Adapter histListAdapter;

    private Context mContext;

    private ArrayList<Vehicle> roster;
    private Vehicle vehicle;
    private int vehiclePos;
    private String refID;
    private ArrayList<Event> vehicleHist;

    public fragment_history() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        roster = bundle.getParcelableArrayList("roster");
        vehiclePos = bundle.getInt("vehiclePos", -1);
        vehicle = roster.get(vehiclePos);
        refID = vehicle.getRefID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        registerForContextMenu(view);

        //Task History
        histList = (RecyclerView) view.findViewById(R.id.histList);
        histMan = new LinearLayoutManager(getActivity());
        histList.setLayoutManager(histMan);
        histList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mContext, histList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (!vehicleHist.isEmpty()) {
                    final Event event = vehicleHist.get(pos);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("event", event);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    dialog_vehicleHistory dialog = new dialog_vehicleHistory();
                    dialog.setTargetFragment(fragment_history.this, 0);
                    dialog.setArguments(bundle);
                    dialog.show(fm, "dialog_vehicle_history");
                }
            }

            @Override
            public void onItemLongClick(final View view, int pos) {
                if (!vehicleHist.isEmpty()) {
                    final Event event = vehicleHist.get(pos);
                    PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu_history, popupMenu.getMenu());
                    //TODO vibrate

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_edit: //TODO not done
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("caseID", 3);
                                    bundle.putSerializable("event", event);
                                    bundle.putParcelableArrayList("roster", roster);
                                    bundle.putInt("vehiclePos", vehiclePos);
                                    getActivity().startActivity(new Intent(getActivity(), VehicleActivity.class)
                                            .putExtra("bundle", bundle));

                                    return true;

                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Delete Item?");
                                    builder.setMessage(event.getEvent() + " completed on " + event.getDate());
                                    builder.setNegativeButton("No", null);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            VehicleLogDBHelper vehicleDB = VehicleLogDBHelper.getInstance(mContext);
                                            vehicleDB.deleteEntry(event);
                                            onResume();
                                        }
                                    }).show();

                                    return true;

                                default:
                                    return false;}}});
                        popupMenu.show();
                }}}));

        //menu_overview_fab
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("caseID", 1);
                bundle.putParcelableArrayList("roster", roster);
                startActivity(new Intent(getActivity(), VehicleActivity.class)
                        .putExtra("bundle", bundle));
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        vehicleHist = sort(vehicleLogDBHelper.getFullVehicleEntries(refID));
        Log.d(TAG,vehicleHist.toString());
        histListAdapter = new HistoryAdapter(mContext, vehicleHist);
        histList.setAdapter(histListAdapter);
    }

    public ArrayList<Event> sort(ArrayList<Event> vehicleHist){
        ArrayList<String> dates = new ArrayList<>();

        HashMap<String, Event> eventPackets = new HashMap<>();

        for (int i = 0; i < vehicleHist.size(); i++){
            Event event = vehicleHist.get(i);
            String date = event.getDate() + ":" + i + "";
            dates.add(date);
            eventPackets.put(date, event);
        }

        Collections.sort(dates, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("MM/dd/yyyy:HH");
            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o2).compareTo(f.parse(o1));
                }catch (ParseException e) {throw new IllegalArgumentException(e);}
            }});

        vehicleHist.clear();
        for (String date : dates) {
            vehicleHist.add(eventPackets.get(date));
        }

        return vehicleHist;
    }
}
