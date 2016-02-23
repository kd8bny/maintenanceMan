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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.activities.VehicleActivity;
import com.kd8bny.maintenanceman.adapters.TravelAdapter;
import com.kd8bny.maintenanceman.classes.Vehicle.Travel;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.dialogs.dialog_addTravelEntry;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class fragment_travel_view extends Fragment {
    private static final String TAG = "frgmnt_bsnss";

    private RecyclerView businessList;
    private RecyclerView.LayoutManager businessMan;
    private RecyclerView.Adapter businessListAdapter;

    private Context mContext;

    private ArrayList<Vehicle> mRoster;
    private Vehicle mVehicle;
    private int mVehiclePos;
    private String mRefID;
    private ArrayList<Travel> mTravelLog;

    public fragment_travel_view() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        mRoster = bundle.getParcelableArrayList("roster");
        mVehiclePos = bundle.getInt("vehiclePos", -1);
        mVehicle = mRoster.get(mVehiclePos);
        mRefID = mVehicle.getRefID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        registerForContextMenu(view);

        //Task History
        businessList = (RecyclerView) view.findViewById(R.id.cardList);
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
                        dialog.setTargetFragment(fragment_travel_view.this, 4);
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
                                case R.id.menu_edit: //TODO not done
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("caseID", 4);
                                    bundle.putSerializable("event", travel);
                                    bundle.putParcelableArrayList("roster", mRoster);
                                    bundle.putInt("vehiclePos", mVehiclePos);
                                    getActivity().startActivity(new Intent(getActivity(), VehicleActivity.class)
                                            .putExtra("bundle", bundle));

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

        //menu_overview_fab
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("caseID", 4);
                bundle.putParcelableArrayList("roster", mRoster);
                startActivity(new Intent(getActivity(), VehicleActivity.class)
                        .putExtra("bundle", bundle));
            }});

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        mTravelLog = sort(vehicleLogDBHelper.getFullBusinessEntries(mRefID));
        businessListAdapter = new TravelAdapter(mContext, mTravelLog);
        businessList.setAdapter(businessListAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        Travel travel = mTravelLog.get(bundle.getInt("pos"));
        travel.setStop(Double.parseDouble(bundle.getString("value")));

        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        vehicleLogDBHelper.deleteEntry(travel);
        vehicleLogDBHelper.insertEntry(travel);
        onResume();
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
