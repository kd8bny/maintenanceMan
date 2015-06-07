package com.kd8bny.maintenanceman.ui.info;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.PopupMenu;

import com.github.clans.fab.FloatingActionButton;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_vehicleHistory;
import com.kd8bny.maintenanceman.ui.edit.activity_edit;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class fragment_info extends Fragment {
    private static final String TAG = "frgmnt_inf";

    private Toolbar toolbar, toolbarBottom;
    private SlidingUpPanelLayout addEvent;
    private FloatingActionButton fab;
    private RecyclerView cardList, histList;
    private RecyclerView.LayoutManager cardMan, histMan;
    private RecyclerView.Adapter cardListAdapter, histListAdapter;

    private String refID;
    private HashMap<String, HashMap> roster;
    public HashMap<String, HashMap> vehicleSent;
    private ArrayList<ArrayList> vehicleHist;

    public fragment_info() {
        // Required empty public constructor
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

        refID = getActivity().getIntent().getStringExtra("refID");
        fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
        roster = new HashMap<>(fltjson.getEntries(getActivity().getApplicationContext()));
        vehicleSent = roster.get(refID);

        //Toolbar top
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Toolbar bottom
        toolbarBottom = (Toolbar) view.findViewById(R.id.tool_bar_bottom);
        toolbarBottom.setTitle(R.string.title_history);
        toolbarBottom.setNavigationIcon(R.drawable.ic_action_up);

        //Info Cards
        cardList = (RecyclerView) view.findViewById(R.id.info_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setHasFixedSize(true);
        cardList.setItemAnimator(new DefaultItemAnimator());
        cardList.setLayoutManager(cardMan);
        cardListAdapter = new adapter_info(vehicleSent);
        cardList.setAdapter(cardListAdapter);

        //Task History
        histList = (RecyclerView) view.findViewById(R.id.histList);
        histMan = new LinearLayoutManager(getActivity());
        histList.setHasFixedSize(true);
        histList.setItemAnimator(new DefaultItemAnimator());
        histList.setLayoutManager(histMan);
        histList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity().getApplicationContext(), histList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                final ArrayList<String> temp = vehicleHist.get(pos);
                if (!temp.get(1).equals(getActivity().getApplicationContext()
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
                                builder.setMessage(temp.get(3) + " completed on " + temp.get(1));
                                builder.setNegativeButton("No", null);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(getActivity().getApplicationContext());
                                        vehicleDB.deleteEntry(getActivity().getApplicationContext(), temp);

                                        vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);
                                        sort();
                                        histListAdapter = new adapter_history(vehicleHist, vehicleSent.get("General").get("type").toString());
                                        histList.setAdapter(histListAdapter);
                                    }
                                });

                                builder.show();

                                return true;

                            default:
                                return false;
                        }
                    }
                });

                if (!temp.get(1).equals(getActivity().getApplicationContext()
                        .getResources().getString(R.string.no_history))) {
                    popupMenu.show();
                }
            }
        }));
        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);
        this.sort();
        histListAdapter = new adapter_history(vehicleHist, vehicleSent.get("General").get("type").toString());
        histList.setAdapter(histListAdapter);

        //Slide-y up menu
        addEvent = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        addEvent.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float slideOffset) {
                toolbarBottom.getMenu().clear();

            }

            @Override
            public void onPanelCollapsed(View view) {
                toolbarBottom.setNavigationIcon(R.drawable.ic_action_up);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
                }
            }

            @Override
            public void onPanelExpanded(View view) {
                toolbarBottom.setNavigationIcon(R.drawable.ic_action_down);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.accent_dark));
                }
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (addEvent.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        addEvent.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                        return true;
                    } else {

                        return false;
                    }
                }
                return false;
            }
        });

        //menu_overview_fab
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                addIntent.putExtra("refID", refID);
                getActivity().startActivity(addIntent);
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        //Renew Vehicle Data
        fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
        roster = new HashMap<>(fltjson.getEntries(getActivity().getApplicationContext()));
        vehicleSent = roster.get(refID);
        cardListAdapter = new adapter_info(vehicleSent);
        cardList.setAdapter(cardListAdapter);

        //Renew maintenance history
        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);
        this.sort();
        histListAdapter = new adapter_history(vehicleHist, vehicleSent.get("General").get("type").toString());
        histList.setAdapter(histListAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

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

                        getActivity().finish();
                    }
                });

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

    public void sort(){
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> eventPacket;
        HashMap<String,ArrayList> eventPackets = new HashMap<>();

        for (int i =0; i<vehicleHist.size(); i++){
            eventPacket = vehicleHist.get(i);
            dates.add(eventPacket.get(1));
            eventPackets.put(eventPacket.get(1), eventPacket);
        }

        Collections.sort(dates, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("MM/dd/yyyy");
            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o2).compareTo(f.parse(o1));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        vehicleHist.clear();
        for (int i = 0; i<dates.size(); i++){
            vehicleHist.add(eventPackets.get(dates.get(i)));
        }
    }
}
