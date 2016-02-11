package com.kd8bny.maintenanceman.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.github.clans.fab.FloatingActionMenu;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.adapter_info;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class fragment_info extends Fragment {
    private static final String TAG = "frgmnt_inf";

    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;

    private Context context;
    private ArrayList<Vehicle> roster;
    private int vehiclePos;
    private Vehicle vehicle;

    public fragment_info() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();
        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        vehiclePos = bundle.getInt("vehiclePos", -1);
        vehicle = bundle.getParcelable("vehicle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "save");
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
                        /*fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
                        fltjson.deleteEntry(getActivity(), refID);

                        vehicleLogDBHelper vhclDBHlpr = new vehicleLogDBHelper(getActivity().getApplicationContext());
                        vhclDBHlpr.purgeHistory(getActivity().getApplicationContext(), refID);

                        backupRestoreHelper mbackupRestoreHelper = new backupRestoreHelper();
                        mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "backup", false);

                        getActivity().finish();*/
                    }});

                builder.show();

                return true;

            case R.id.menu_edit:
                Bundle bundle = new Bundle();
                bundle.putInt("vehiclePos", vehiclePos);
                bundle.putParcelable("vehicle", vehicle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new fragment_fleetRoster_edit();
                fragment.setArguments(bundle);
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack("info");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();

                return true;

            default:
                return false;
        }
    }
}
