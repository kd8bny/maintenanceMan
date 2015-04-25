package com.kd8bny.maintenanceman.ui.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.drawer.adapter_drawer;
import com.kd8bny.maintenanceman.ui.info.activity_info;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_overview extends Fragment {
    private static final String TAG = "frg_ovrvw";

    private Toolbar toolbar;
    private RecyclerView cardList, drawerList;
    private RecyclerView.LayoutManager cardMan, drawerMan;
    private RecyclerView.Adapter cardListAdapter;
    private DrawerLayout Drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private FloatingActionButton fabAddVehicle, fabAddEvent;

    private HashMap<String, HashMap> roster;
    private Boolean DBisEmpty = false;

    public fragment_overview() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //cards
        fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
        roster = new HashMap<>(fltjson.getEntries(getActivity().getApplicationContext()));

        cardList = (RecyclerView) view.findViewById(R.id.overview_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setHasFixedSize(true);
        cardList.setItemAnimator(new DefaultItemAnimator());
        cardList.setLayoutManager(cardMan);

        if (roster.containsKey(null)){
             DBisEmpty = true;
        }
        cardListAdapter = new adapter_overview(roster);
        cardList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity().getApplicationContext(), cardList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (!DBisEmpty) {
                    ArrayList<String> refIDs = new ArrayList<>();
                    refIDs.addAll(roster.keySet());
                    Intent viewIntent = new Intent(getActivity().getApplicationContext(), activity_info.class);
                    viewIntent.putExtra("refID", refIDs.get(pos));
                    view.getContext().startActivity(viewIntent);
                } else {
                    Intent viewAddIntent = new Intent(getActivity().getApplicationContext(), activity_add_fleetRoster.class);
                    view.getContext().startActivity(viewAddIntent);
                }
            }

            @Override
            public void onItemLongClick(View view, int pos) {

            }
        }));
        cardList.setAdapter(cardListAdapter);

        //fab
        final FloatingActionMenu fabMenu = (FloatingActionMenu) view.findViewById(R.id.fabmenu);
        fabAddVehicle = (FloatingActionButton) view.findViewById(R.id.fab_add_vehicle);
        fabAddEvent = (FloatingActionButton) view.findViewById(R.id.fab_add_event);

        fabMenu.setAnimated(true);

        view.findViewById(R.id.fab_add_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                startActivity(addIntent);
            }
        });
        view.findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                startActivity(addIntent);
            }
        });

        //drawer_item
        drawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        drawerMan = new LinearLayoutManager(getActivity());
        drawerList.setHasFixedSize(true);
        drawerList.setItemAnimator(new DefaultItemAnimator());

        drawerList.setLayoutManager(drawerMan);
        drawerList.setAdapter(populateDrawer());
        Drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),Drawer,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                fabMenu.hideMenuButton(true);

            }
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                fabMenu.hideMenuButton(false);
            }
        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
        roster = new HashMap<>(fltjson.getEntries(getActivity().getApplicationContext()));

        if (roster.containsKey(null)){
            DBisEmpty = true;
        }else{
            DBisEmpty = false;
        }

        cardListAdapter = new adapter_overview(roster);
        cardList.setAdapter(cardListAdapter);
    }

    public adapter_drawer populateDrawer(){
        String[] mMenuTitles = getResources().getStringArray(R.array.drawer_items);
        int[] icons = new int[]{R.drawable.ic_action_car_gray, R.drawable.ic_action_card, R.drawable.ic_action_donate}; //R.drawable.ic_action_settings,
        ArrayList<String> singleDrawerItems = new ArrayList<>();

        singleDrawerItems.add(mMenuTitles[0]);
        singleDrawerItems.add(mMenuTitles[1]);
        //singleDrawerItems.add(mMenuTitles[2]); //TODO add settings back
        singleDrawerItems.add(mMenuTitles[3]);

        return new adapter_drawer(singleDrawerItems, icons);
    }
}
