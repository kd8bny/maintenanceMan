package com.kd8bny.maintenanceman.ui.info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.backupRestoreHelper;
import com.kd8bny.maintenanceman.classes.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.classes.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;

import java.util.ArrayList;

public class fragment_info extends Fragment {
    private static final String TAG = "frgmnt_inf";

    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;

    private Context context;
    private SharedPreferences sharedPreferences;

    private Vehicle vehicle;
    private ArrayList<Vehicle> roster;
    private String refID;

    public fragment_info() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        roster = (ArrayList) getActivity().getIntent().getSerializableExtra("roster");
        int pos = getActivity().getIntent().getIntExtra("pos", -1);
        vehicle = roster.get(pos);
        refID = vehicle.getRefID();
        context = getActivity().getApplicationContext();
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        registerForContextMenu(view);

        //Info Cards
        cardList = (RecyclerView) view.findViewById(R.id.info_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setLayoutManager(cardMan);

        //menu_overview_fab
        final FloatingActionMenu fabMenu = (FloatingActionMenu) getActivity().findViewById(R.id.fabmenu);
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.setAnimated(true);

        getActivity().findViewById(R.id.fab_add_spec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                addIntent.putExtra("vehicle", roster);
                startActivity(addIntent);
                fabMenu.close(true);
            }
        });
        getActivity().findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                startActivity(addIntent);
                fabMenu.close(true);
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        cardListAdapter = new adapter_info(vehicle);
        cardList.setAdapter(cardListAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        cardListAdapter = new adapter_info(vehicle);
        cardList.swapAdapter(cardListAdapter, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch (menuitem.getItemId()) {
            case R.id.menu_del:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Are you sure you would like to delete this vehicle?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
                        fltjson.deleteEntry(getActivity(), refID);

                        vehicleLogDBHelper vhclDBHlpr = new vehicleLogDBHelper(getActivity().getApplicationContext());
                        vhclDBHlpr.purgeHistory(getActivity().getApplicationContext(), refID);

                        backupRestoreHelper mbackupRestoreHelper = new backupRestoreHelper();
                        mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "backup", false);

                        getActivity().finish();
                    }});

                builder.show();

                return true;

            case R.id.menu_edit:
                Intent editIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                editIntent.putExtra("roster", roster);
                editIntent.putExtra("vehiclePos", roster.indexOf(vehicle));
                getActivity().startActivity(editIntent);

                return true;

            default:
                return false;
        }
    }
}
