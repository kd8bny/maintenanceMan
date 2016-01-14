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

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.backupRestoreHelper;
import com.kd8bny.maintenanceman.classes.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.classes.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;

import java.util.ArrayList;

public class fragment_info extends Fragment {
    private static final String TAG = "frgmnt_inf";

    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;

    private Context context;
    private SharedPreferences sharedPreferences;

    private ArrayList<Vehicle> roster;
    private int vehiclePos;
    private Vehicle vehicle;
    private String refID;

    public fragment_info() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        roster = bundle.getParcelableArrayList("roster");
        vehiclePos = bundle.getInt("pos", -1);

        vehicle = roster.get(vehiclePos);
        refID = vehicle.getRefID();

        sharedPreferences = context.getSharedPreferences(SHARED_PREF, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "create view");
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        registerForContextMenu(view);

        //Info Cards
        cardList = (RecyclerView) view.findViewById(R.id.info_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setLayoutManager(cardMan);
        cardListAdapter = new adapter_info(vehicle);
        cardList.setAdapter(cardListAdapter);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onResume(){
        super.onResume();

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
                Intent viewIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                Bundle bundle = new Bundle();
                //bundle.putParcelableArrayList("roster", roster);
                bundle.putInt("pos", roster.indexOf(vehicle));
                viewIntent.putExtra("bundle", bundle);
                startActivity(viewIntent);
                return true;

            default:
                return false;
        }
    }
}
