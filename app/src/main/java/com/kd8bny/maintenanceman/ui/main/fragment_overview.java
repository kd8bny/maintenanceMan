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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.drawer.adapter_drawer;
import com.kd8bny.maintenanceman.ui.settings.activity_settings;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;


public class fragment_overview extends Fragment {
    private static final String TAG = "fragment_overview";

    private Toolbar toolbar;
    RecyclerView cardList, drawerList;
    //RecyclerView.Adapter
    RecyclerView.LayoutManager cardMan, drawerMan;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;

    FloatingActionButton fab;

    public ArrayList<ArrayList> vehicleList = new ArrayList<ArrayList>();

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
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
        if (toolbar != null) {
            ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
            ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }

        //cards
        cardList = (RecyclerView) view.findViewById(R.id.overview_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setHasFixedSize(true);
        cardList.setItemAnimator(new DefaultItemAnimator());
        cardList.setLayoutManager(cardMan);
        //cardMan.setOrientation(LinearLayoutManager.VERTICAL);
        cardList.setAdapter(populateCards());

        //fab
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToRecyclerView(cardList);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
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
        //drawerMan.setOrientation(LinearLayoutManager.VERTICAL);
        drawerList.setLayoutManager(drawerMan);
        drawerList.setAdapter(populateDrawer());
        Drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),Drawer,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                //TODO hide fab

            }
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                //TODO
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

    }

    public adapter_overview populateCards(){
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());

        return new adapter_overview(vehicleList);
    }

    public adapter_drawer populateDrawer(){
        String[] mMenuTitles = getResources().getStringArray(R.array.drawer_items);
        ArrayList<String> singleDrawerItems = new ArrayList<String>();

        singleDrawerItems.add(mMenuTitles[0]);
        singleDrawerItems.add(mMenuTitles[1]);
        singleDrawerItems.add(mMenuTitles[2]);
        singleDrawerItems.add(mMenuTitles[3]);

        return new adapter_drawer(singleDrawerItems);
    }

    /*public class DrawerItemClickListener implements RecyclerView.RecyclerListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //selectItem(position);

            switch (position){
                case 0:
                    Intent addFleetIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                    startActivity(addFleetIntent);
                    break;

                case 1:
                    Intent addEventIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                    startActivity(addEventIntent);
                    break;

                case 2:
                    Intent settingsIntent = new Intent(getActivity(), activity_settings.class);
                    startActivity(settingsIntent);
                    break;

                case 3:

                    break;

                default:
                    break;


            }
        }
    }*/
}
