package com.kd8bny.maintenanceman.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kd8bny.maintenanceman.BuildConfig;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.activities.VehicleActivity;
import com.kd8bny.maintenanceman.adapters.adapter_overview;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.classes.data.backupRestoreHelper;
import com.kd8bny.maintenanceman.interfaces.UpdateUI;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.dialogs.dialog_whatsNew;
import com.kd8bny.maintenanceman.activities.IntroActivity;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;


public class fragment_overview extends Fragment implements UpdateUI{
    private static final String TAG = "frg_ovrvw";

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private Context context;
    private SharedPreferences sharedPreferences;
    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;

    private ArrayList<Vehicle> roster;
    private String mUnit;

    public fragment_overview() {}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        roster = new ArrayList<>(new SaveLoadHelper(context).load());
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, 0);

        //Data
        backupRestoreHelper mbackupRestoreHelper = new backupRestoreHelper(); //TODO update splash screen
        mbackupRestoreHelper.updateUI = this;
        mbackupRestoreHelper.startAction(context, "restore", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //cards
        mUnit = sharedPreferences.getString("prefUnitDist", "");
        cardList = (RecyclerView) view.findViewById(R.id.overview_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setLayoutManager(cardMan);
        cardListAdapter = new adapter_overview(context, roster, mUnit);
        cardList.setAdapter(cardListAdapter);
        cardList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(context, cardList, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Bundle bundle = new Bundle();
                if (roster.isEmpty()) {
                    bundle.putInt("caseID", 0);
                    bundle.putInt("vehiclePos", -1);
                }else{
                    bundle.putParcelable("vehicle", roster.get(pos));
                    bundle.putInt("vehiclePos", pos);
                }

                Intent intent = new Intent(getActivity(), VehicleActivity.class);
                intent.putExtra("bundle", bundle);
                view.getContext().startActivity(intent);
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

                Intent intent = new Intent(getActivity(), VehicleActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
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

                    Intent addIntent = new Intent(getActivity(), Vehicle.class);
                    startActivity(addIntent);
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

            FragmentManager fm = getFragmentManager();//TODO marqee view

            dialog_whatsNew dialog_whatsNew = new dialog_whatsNew();
            dialog_whatsNew.show(fm, "dialog_whatsNew");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cardListAdapter = new adapter_overview(context, roster, mUnit);
        cardList.swapAdapter(cardListAdapter, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onUpdate(Boolean doUpdate){
        if (doUpdate) {
            onResume();
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_update_ui), Snackbar.LENGTH_SHORT).show();
        }
    }
}
