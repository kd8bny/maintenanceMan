package com.kd8bny.maintenanceman.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kd8bny.maintenanceman.BuildConfig;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.activities.SettingsActivity;
import com.kd8bny.maintenanceman.activities.VehicleActivity;
import com.kd8bny.maintenanceman.activities.ViewPagerActivity;
import com.kd8bny.maintenanceman.adapters.OverviewAdapter;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.classes.data.BackupRestoreHelper;
import com.kd8bny.maintenanceman.dialogs.dialog_donate;
import com.kd8bny.maintenanceman.interfaces.UpdateUI;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.dialogs.dialog_whatsNew;
import com.kd8bny.maintenanceman.activities.IntroActivity;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

public class fragment_main extends Fragment implements UpdateUI{
    private static final String TAG = "frg_ovrvw";

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private Context context;
    private SharedPreferences sharedPreferences;
    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;

    private ArrayList<Vehicle> roster;

    public fragment_main() {}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, 0);

        //Data //TODO
        /*BackupRestoreHelper mbackupRestoreHelper = new BackupRestoreHelper();
        mbackupRestoreHelper.updateUI = this;
        mbackupRestoreHelper.startAction(context, "restore", false);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Toolbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Drawer
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
                Intent intent = new Intent(getActivity(), VehicleActivity.class);
                Bundle bundle = new Bundle();
                switch (i) {
                    case 1: //Add Vehicle
                        bundle.putInt("caseID", 0);
                        bundle.putParcelableArrayList("roster", roster);
                        bundle.putInt("vehiclePos", -1);
                        intent.putExtra("bundle", bundle);
                        view.getContext().startActivity(intent);
                        drawer.closeDrawer();

                        return true;

                    case 2: //Add Event
                        bundle.putInt("caseID", 1);
                        bundle.putParcelableArrayList("roster", roster);
                        intent.putExtra("bundle", bundle);
                        view.getContext().startActivity(intent);
                        drawer.closeDrawer();

                        return true;

                    case 4: //Settings
                        Intent settingsIntent = new Intent(view.getContext(), SettingsActivity.class);
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
            }});

        //cards
        cardList = (RecyclerView) view.findViewById(R.id.overview_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setLayoutManager(cardMan);
        cardList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(context, cardList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Bundle bundle = new Bundle();
                if (roster.isEmpty()) {
                    bundle.putInt("caseID", 0);
                    bundle.putInt("vehiclePos", -1);
                    view.getContext().startActivity(new Intent(getActivity(), VehicleActivity.class)
                            .putExtra("bundle", bundle));
                }else{
                    bundle.putParcelableArrayList("roster", roster);
                    bundle.putParcelable("vehicle", roster.get(pos));
                    bundle.putInt("vehiclePos", pos);
                    view.getContext().startActivity(new Intent(getActivity(), ViewPagerActivity.class)
                            .putExtra("bundle", bundle));
                }
            }

            @Override
            public void onItemLongClick(View view, int pos) {}}));

        //fab
        final FloatingActionMenu fabMenu = (FloatingActionMenu) view.findViewById(R.id.fabmenu);
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.setAnimated(true);

        view.findViewById(R.id.fab_add_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("caseID", 0);
                bundle.putParcelableArrayList("roster", roster);
                startActivity(new Intent(getActivity(), VehicleActivity.class)
                        .putExtra("bundle", bundle));
                fabMenu.close(true);
            }});
        view.findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roster.isEmpty()){
                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.empty_db),
                            Snackbar.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("caseID", 1);
                    bundle.putParcelableArrayList("roster", roster);
                    startActivity(new Intent(getActivity(), VehicleActivity.class)
                            .putExtra("bundle", bundle));
                    fabMenu.close(true);
                }
            }});

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
            }});

        //Intro
        Boolean isFirstRun = sharedPreferences.getBoolean("firstRun", true);
        if (isFirstRun) {
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            Intent introIntent = new Intent(context, IntroActivity.class);
            view.getContext().startActivity(introIntent);
        }

        //Whats New!!!
        int oldAppVersion = sharedPreferences.getInt("appVersion", -1);
        if (BuildConfig.VERSION_CODE > oldAppVersion) {
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
    public void onResume() {
        super.onResume();
        roster = new ArrayList<>(new SaveLoadHelper(context).load());
        cardListAdapter = new OverviewAdapter(roster);
        cardList.setAdapter(cardListAdapter);
    }

    public void onUpdate(Boolean doUpdate){ //TODO test to see if working
        if (doUpdate) {
            roster = new ArrayList<>(new SaveLoadHelper(context).load());
            cardListAdapter = new OverviewAdapter(roster);
            cardList.setAdapter(cardListAdapter);
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_update_ui), Snackbar.LENGTH_SHORT).show();
        }
    }
}
