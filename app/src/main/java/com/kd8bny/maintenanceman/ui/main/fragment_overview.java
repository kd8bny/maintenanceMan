package com.kd8bny.maintenanceman.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.kd8bny.maintenanceman.BuildConfig;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.backupRestoreHelper;
import com.kd8bny.maintenanceman.data.fleetRosterJSONHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.interfaces.UpdateUI;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_donate;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_whatsNew;
import com.kd8bny.maintenanceman.ui.info.activity_info;
import com.kd8bny.maintenanceman.ui.intro.activity_intro;
import com.kd8bny.maintenanceman.ui.preferences.activity_settings;

import com.github.clans.fab.FloatingActionMenu;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_overview extends Fragment implements UpdateUI{
    private static final String TAG = "frg_ovrvw";

    private Toolbar toolbar;
    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;

    private HashMap<String, HashMap> roster;
    private ArrayList<ArrayList> eventData;
    private String mUnit;
    private Boolean DBisEmpty = false;

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    public fragment_overview() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
        view.setAnimation(anim);

        //Data
        backupRestoreHelper mbackupRestoreHelper = new backupRestoreHelper();
        mbackupRestoreHelper.updateUI = this;
        mbackupRestoreHelper.startAction(getActivity().getApplicationContext(), "restore", false);

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //cards
        mUnit = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREF, 0).getString("prefUnitDist", "");
        fleetRosterJSONHelper fltjson = new fleetRosterJSONHelper();
        roster = new HashMap<>(fltjson.getEntries(getActivity().getApplicationContext()));
        vehicleLogDBHelper vehicleLogDBHelper = new vehicleLogDBHelper(getActivity().getApplicationContext());
        eventData = new ArrayList<>();
        for (String key: roster.keySet()) {
            ArrayList<ArrayList> temp = vehicleLogDBHelper.getEntries(getActivity().getApplicationContext(), key);
            ArrayList<String> event = temp.get(0);
            ArrayList<String> datam = new ArrayList<>();
            if(event.get(0) != null) {
                datam.add(event.get(3)); //odo
                datam.add(event.get(4)); //event
            }else{
                datam.add(""); //odo
                datam.add(""); //event
            }
            eventData.add(datam);
        }

        cardList = (RecyclerView) view.findViewById(R.id.overview_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setHasFixedSize(true);
        cardList.setItemAnimator(new DefaultItemAnimator());
        cardList.setLayoutManager(cardMan);

        if (roster.containsKey(null)){//TODO clean the hell up
             DBisEmpty = true;
        }
        cardListAdapter = new adapter_overview(getActivity().getApplicationContext(), roster, eventData, mUnit);
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
        Animation cardListAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_left_fade_in);
        cardList.setAnimation(cardListAnim);

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

        final DrawerBuilder drawerBuilder = new DrawerBuilder(getActivity());
        drawerBuilder.withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withCloseOnClick(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSelectedItem(-1)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.title_add_fleet_roster).withIcon(R.drawable.ic_action_add_fleet),
                        new PrimaryDrawerItem().withName(R.string.title_add_vehicle_event).withIcon(R.drawable.ic_action_add_event),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.title_settings),
                        new SecondaryDrawerItem().withName(R.string.title_donate),
                        new SecondaryDrawerItem().withName(R.string.drawer_view_community));

        final Drawer drawer = drawerBuilder.build();
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                switch (i) {
                    case 1: //Add Vehicle
                        Intent addFleetIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                        view.getContext().startActivity(addFleetIntent);
                        drawer.closeDrawer();

                        return true;

                    case 2: //Add Event
                        Intent addEventIntent = new Intent(view.getContext(), activity_vehicleEvent.class);
                        view.getContext().startActivity(addEventIntent);
                        drawer.closeDrawer();

                        return true;

                    case 4: //Settings
                        Intent settingsIntent = new Intent(view.getContext(), activity_settings.class);
                        view.getContext().startActivity(settingsIntent);
                        drawer.closeDrawer();

                        return true;

                    case 5: //Donate
                        FragmentManager fm = getFragmentManager();

                        dialog_donate dialog_donate = new dialog_donate();
                        dialog_donate.show(fm, "dialog_donate");
                        drawer.closeDrawer();

                        return true;

                    case 6: //Community
                        Uri gplus = Uri.parse("https://plus.google.com/u/0/communities/102216501931497148667");
                        Intent gplusIntent = new Intent(Intent.ACTION_VIEW, gplus);
                        startActivity(gplusIntent);
                        drawer.closeDrawer();

                        return true;
                }
                return false;
                }
            });


        //Intro
        Boolean isFirstRun = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getBoolean("firstRun", true);
        if (isFirstRun) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            Intent introIntent = new Intent(view.getContext(), activity_intro.class);
            view.getContext().startActivity(introIntent);
        }
        //Whats New!!!
        int oldAppVersion = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getInt("appVersion", -1);
        if (BuildConfig.VERSION_CODE > oldAppVersion) {
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putInt("appVersion", BuildConfig.VERSION_CODE);
            editor.apply();

            FragmentManager fm = getFragmentManager();

            dialog_whatsNew dialog_whatsNew = new dialog_whatsNew();
            dialog_whatsNew.show(fm, "dialog_whatsNew");
        }

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
        vehicleLogDBHelper vehicleLogDBHelper = new vehicleLogDBHelper(getActivity().getApplicationContext());
        eventData = new ArrayList<>();
        for (String key: roster.keySet()) {
            ArrayList<ArrayList> temp = vehicleLogDBHelper.getEntries(getActivity().getApplicationContext(), key);
            ArrayList<String> event = temp.get(0);
            ArrayList<String> datam = new ArrayList<>();
            if(event.get(0) != null) {
                datam.add(event.get(3)); //odo
                datam.add(event.get(4)); //event
            }else{
                datam.add(""); //odo
                datam.add(""); //event
            }
            eventData.add(datam);
        }

        if (!roster.containsKey(null)){
            DBisEmpty = false;
        }
        cardListAdapter = new adapter_overview(getActivity().getApplicationContext(), roster, eventData, mUnit);
        cardList.setAdapter(cardListAdapter);
    }

<<<<<<< Updated upstream
    public void onUpdate(Boolean doUpdate) {
=======
<<<<<<< Updated upstream
    public void onUpdate(Boolean doUpdate){
>>>>>>> Stashed changes
        if (doUpdate) {
            onResume();
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_update_ui), Snackbar.LENGTH_SHORT).show();
        }
=======
    public void onPause(){
        super.onPause();
        View view = getView();

        Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_out);
        view.startAnimation(anim);
        anim.reset();
>>>>>>> Stashed changes
    }
}
