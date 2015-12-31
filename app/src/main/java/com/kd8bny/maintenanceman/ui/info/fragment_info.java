package com.kd8bny.maintenanceman.ui.info;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.backupRestoreHelper;
import com.kd8bny.maintenanceman.classes.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.classes.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_vehicleHistory;
import com.kd8bny.maintenanceman.ui.edit.activity_edit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class fragment_info extends Fragment {
    private static final String TAG = "frgmnt_inf";

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView cardList, histList;
    private RecyclerView.LayoutManager cardMan, histMan;
    private RecyclerView.Adapter cardListAdapter, histListAdapter;

    private Context context;
    private SharedPreferences sharedPreferences;

    private Vehicle vehicle;
    private String refID;
    private String prefUnit;
    private ArrayList<ArrayList> vehicleHist;

    public fragment_info() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        registerForContextMenu(view);

        vehicle = (Vehicle) getActivity().getIntent().getSerializableExtra("vehicle");
        refID = vehicle.getRefID();
        context = getActivity().getApplicationContext();
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, 0);
        prefUnit = sharedPreferences.getString("prefUnitDist", getResources().getString(R.string.pref_unit_dist_default));

        //Toolbar top
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        //Task History
        histList = (RecyclerView) view.findViewById(R.id.histList);
        histMan = new LinearLayoutManager(getActivity());
        histList.setLayoutManager(histMan);
        histList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(context, histList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                final ArrayList<String> temp = vehicleHist.get(pos);
                if (!temp.get(2).equals(getActivity().getApplicationContext()
                        .getResources().getString(R.string.no_history))) {

                    Bundle args = new Bundle();
                    args.putSerializable("event", vehicleHist.get(pos));

                    FragmentManager fm = ((FragmentActivity) view.getContext()).getFragmentManager();
                    dialog_vehicleHistory dialog_vehicleHistory = new dialog_vehicleHistory();
                    dialog_vehicleHistory.setTargetFragment(fragment_info.this, 0);
                    dialog_vehicleHistory.setArguments(args);
                    dialog_vehicleHistory.show(fm, "dialog_vehicle_history");
                }
            }

            @Override
            public void onItemLongClick(final View view, int pos) {
                final ArrayList<String> temp = vehicleHist.get(pos);
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu_history, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                Intent editIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                                editIntent.putExtra("dataSet", temp);
                                editIntent.putExtra("refID", refID);
                                getActivity().startActivity(editIntent);

                                return true;

                            case R.id.menu_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setCancelable(true);
                                builder.setTitle("Delete Item?");
                                builder.setMessage(temp.get(4) + " completed on " + temp.get(2));
                                builder.setNegativeButton("No", null);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(getActivity().getApplicationContext());
                                        vehicleDB.deleteEntry(getActivity().getApplicationContext(), temp);

                                        //vehicleHist = sort(vehicleDB.getEntries(getActivity().getApplicationContext(), refID));

                                        backupRestoreHelper mbackupRestoreHelper = new backupRestoreHelper();
                                        mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "backup", false);

                                        onResume();
                                    }
                                }).show();

                                return true;

                            default:
                                return false;}}});

                if (!temp.get(2).equals(context.getResources().getString(R.string.no_history))) {
                    popupMenu.show();
                }}}));

        //Info Cards
        cardList = (RecyclerView) view.findViewById(R.id.info_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setLayoutManager(cardMan);

        //menu_overview_fab
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                addIntent.putExtra("refID", refID);
                getActivity().startActivity(addIntent);
            }});

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        cardListAdapter = new adapter_info(vehicle, vehicleHist);
        cardList.setAdapter(cardListAdapter);

        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        //vehicleHist = this.sort(vehicleDB.getEntries(getActivity().getApplicationContext(), refID));//TODO have SQL sort by date
        histListAdapter = new adapter_history(vehicleHist, vehicle.getVehicleType(), prefUnit);
        histList.setAdapter(histListAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        //vehicleHist = sort(vehicleDB.getEntries(getActivity().getApplicationContext(), refID));
        cardListAdapter = new adapter_info(vehicle, vehicleHist);
        cardList.swapAdapter(cardListAdapter, false);
        //histListAdapter = new adapter_history(vehicleHist, vehicle.getVehicleType(), prefUnit);
        histList.swapAdapter(histListAdapter, false);
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
                Intent editIntent = new Intent(getActivity(), activity_edit.class);
                editIntent.putExtra("refID", refID);
                getActivity().startActivity(editIntent);

                return true;

            default:
                return false;
        }
    }

    public ArrayList<ArrayList> sort(ArrayList<ArrayList> vehicleHist){
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> eventPacket;
        HashMap<String, ArrayList> eventPackets = new HashMap<>();

        for (int i = 0; i < vehicleHist.size(); i++){
            eventPacket = vehicleHist.get(i);
            String date = eventPacket.get(2) + ":" + i + "";
            dates.add(date);
            eventPackets.put(date, eventPacket);
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
