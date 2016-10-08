package com.kd8bny.maintenanceman.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.TravelAdapter;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.utils.Export;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.dialogs.dialog_addMaintenanceEvent;
import com.kd8bny.maintenanceman.dialogs.dialog_addMileageEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_addTravelEntry;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class fragment_travel extends fragment_vehicleInfo {
    private static final String TAG = "frgmnt_bsnss";

    private Context mContext;
    private View mView;

    private RecyclerView businessList;
    private RecyclerView.LayoutManager businessMan;
    private RecyclerView.Adapter businessListAdapter;

    private ArrayList<Vehicle> mRoster;
    private Vehicle mVehicle;
    private int mPos;
    private String mRefID;
    private ArrayList<Travel> mTravelLog;

    public fragment_travel() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        mRoster = bundle.getParcelableArrayList("roster");
        mPos = bundle.getInt("pos", -1);
        mVehicle = mRoster.get(mPos);
        mRefID = mVehicle.getRefID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        registerForContextMenu(mView);

        //Task History
        businessList = (RecyclerView) mView.findViewById(R.id.cardList);
        businessMan = new LinearLayoutManager(getActivity());
        businessList.setLayoutManager(businessMan);
        businessList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mContext, businessList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (!mTravelLog.isEmpty()) {
                    final Travel travel = mTravelLog.get(pos);
                    if (travel.getStop() == -1.0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("event", travel);
                        bundle.putInt("pos", pos);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        dialog_addTravelEntry dialog = new dialog_addTravelEntry();
                        dialog.setTargetFragment(fragment_travel.this, 4);
                        dialog.setArguments(bundle);
                        dialog.show(fm, "dialog_addTravelEntry");
                    }
                }
            }

            @Override
            public void onItemLongClick(final View view, int pos) {
                if (!mTravelLog.isEmpty()) {
                    final Travel travel = mTravelLog.get(pos);
                    PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu_history, popupMenu.getMenu());
                    //TODO vibrate

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_edit:
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList("roster", mRoster);
                                    bundle.putInt("pos", mPos);
                                    bundle.putSerializable("event", travel);
                                    dialog_addTravelEntry dialog = new dialog_addTravelEntry();
                                    dialog.setTargetFragment(fragment_travel.this, 3);
                                    dialog.setArguments(bundle);
                                    dialog.show(getFragmentManager(), "dialog_add_travel");

                                    return true;

                                case R.id.menu_delete:
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Delete Item?");
                                    builder.setMessage(travel.getDest() + " completed on " + travel.getDate());
                                    builder.setNegativeButton("No", null);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
                                            vehicleLogDBHelper.deleteEntry(travel);
                                            onResume();
                                        }
                                    }).show();

                                    return true;

                                default:
                                    return false;}}});
                        popupMenu.show();
                }}}));

        //fab
        final FloatingActionMenu fabMenu = (FloatingActionMenu) mView.findViewById(R.id.fabmenu);
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.setAnimated(true);

        mView.findViewById(R.id.fab_add_spec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_travel.this, 0);
                dialog_addField.show(getFragmentManager(), "dialog_add_field");
                fabMenu.close(true);
            }
        });

        mView.findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("roster", mRoster);
                bundle.putInt("pos", mPos);
                dialog_addMaintenanceEvent dialog = new dialog_addMaintenanceEvent();
                dialog.setTargetFragment(fragment_travel.this, 1);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialog_add_maintenance");
                fabMenu.close(true);
            }});

        mView.findViewById(R.id.fab_add_mileage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("roster", mRoster);
                bundle.putInt("pos", mPos);
                dialog_addMileageEntry dialog = new dialog_addMileageEntry();
                dialog.setTargetFragment(fragment_travel.this, 2);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialog_add_mileage");
                fabMenu.close(true);
            }});

        mView.findViewById(R.id.fab_add_business).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("roster", mRoster);
                bundle.putInt("pos", mPos);
                dialog_addTravelEntry dialog = new dialog_addTravelEntry();
                dialog.setTargetFragment(fragment_travel.this, 3);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialog_add_travel");
                fabMenu.close(true);
            }});

        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (fabMenu.isOpened()) {
                        fabMenu.close(true);
                        return true;
                    }
                }
                return false;
            }});

        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        mTravelLog = this.sort(vehicleLogDBHelper.getFullBusinessEntries(mRefID));
        businessListAdapter = new TravelAdapter(mContext, mVehicle, mTravelLog);
        businessList.setAdapter(businessListAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mTravelLog.isEmpty()) {
            inflater.inflate(R.menu.menu_travel, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch (menuitem.getItemId()) {
            case R.id.menu_export_csv:
                Export export = new Export();
                Uri uri = export.travelToCSV(mVehicle.getTitle(), mTravelLog);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sendIntent.setType("text/csv");
                startActivity(Intent.createChooser(sendIntent, mContext.getResources().getString(R.string.share)));

                return true;
        }

        return false;
    }

    public ArrayList<Travel> sort(ArrayList<Travel> vehicleHist){
        ArrayList<String> dates = new ArrayList<>();

        HashMap<String, Travel> eventPackets = new HashMap<>();

        for (int i = 0; i < vehicleHist.size(); i++){
            Travel travel = vehicleHist.get(i);
            String date = travel.getDate() + ":" + i + "";
            dates.add(date);
            eventPackets.put(date, travel);
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
