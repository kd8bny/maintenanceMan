package com.kd8bny.maintenanceman.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_donate;
import com.kd8bny.maintenanceman.ui.info.activity_info;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_overview extends Fragment {
    private static final String TAG = "frg_ovrvw";

    private Toolbar toolbar;
    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;

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
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

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
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.setAnimated(true);

        view.findViewById(R.id.fab_add_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                startActivity(addIntent);
                fabMenu.close(true);
            }
        });
        view.findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                startActivity(addIntent);
                fabMenu.close(true);
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (fabMenu.isOpened()) {
                        fabMenu.close(true);

                        return true;
                    }
                }

                return false;
            }
        });

        //Material Drawer Header
        AccountHeader.Result headerResult = new AccountHeader()
                .withActivity(getActivity())
                .withHeaderBackground(R.drawable.header_blank)
                .build();

        //Material Drawer
        Drawer.Result result = new Drawer()
            .withActivity(getActivity())
            .withTranslucentStatusBar(true)
            .withToolbar(toolbar)
            .withSelectedItem(-1)
            .withAccountHeader(headerResult)
            .addDrawerItems(
                    new PrimaryDrawerItem().withName(R.string.title_add_fleet_roster).withIcon(R.drawable.ic_action_add_fleet),
                    new PrimaryDrawerItem().withName(R.string.title_add_vehicle_event).withIcon(R.drawable.ic_action_add_event),
                    new DividerDrawerItem(),
                    new SecondaryDrawerItem().withName(R.string.title_donate))
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                    switch (i) {
                        case 0: //Add Vehicle
                            Intent addFleetIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                            view.getContext().startActivity(addFleetIntent);

                            break;

                        case 1: //Add Event
                            Intent addEventIntent = new Intent(view.getContext(), activity_vehicleEvent.class);
                            view.getContext().startActivity(addEventIntent);

                            break;

                        /*case 2: //Settings
                            Intent settingsIntent = new Intent(view.getContext(), activity_settings.class);
                            view.getContext().startActivity(settingsIntent);
                            mDrawerLayout.closeDrawers();
                            break;*/

                        case 3: //Donate
                            FragmentManager fm = getFragmentManager();

                            dialog_donate dialog_donate = new dialog_donate();
                            dialog_donate.show(fm, "dialog_donate");

                            break;

                        /*case 4:
                            //Intent aboutIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                            //view.getContext().startActivity(aboutIntent);
                            Toast.makeText(view.getContext(),"Daryl Bennett",Toast.LENGTH_LONG).show();
                            mDrawerLayout.closeDrawers();
                            break;*/
                    }
                }
            })
            .build();

            result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

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
}
