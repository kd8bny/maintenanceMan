package com.kd8bny.maintenanceman.ui.history;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.edit.activity_edit;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;


public class fragment_history extends Fragment {
    private static final String TAG = "fragment_history";

    private Toolbar toolbar, toolbarBottom;
    private SlidingUpPanelLayout addEvent;
    RecyclerView cardList, histList;
    RecyclerView.LayoutManager cardMan, histMan;
    RecyclerView.Adapter cardListAdapter, histListAdapter;

    private String refID;
    public HashMap<String, String> vehicleSent;

    private ArrayList<ArrayList> vehicleHist;

    public fragment_history() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history, container, false);

        vehicleSent = (HashMap<String, String>) getActivity().getIntent().getSerializableExtra("vehicleSent");
        refID = vehicleSent.get("refID");

        //Toolbar top
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Toolbar bottom
        toolbarBottom = (Toolbar) view.findViewById(R.id.tool_bar_bottom);
        toolbarBottom.setTitle(R.string.title_info);
        toolbarBottom.setNavigationIcon(R.drawable.ic_action_up);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_del:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Are you sure you would like to delete this vehicle?");
                        builder.setNegativeButton("No", null);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(getActivity());
                                fleetDB.deleteEntry(getActivity(), refID);

                                getActivity().finish();
                            }
                        });

                        builder.show();

                        return true;

                    case R.id.menu_edit:
                        Intent editIntent = new Intent(getActivity(), activity_edit.class);
                        editIntent.putExtra("vehicleSent", vehicleSent);
                        getActivity().startActivity(editIntent);

                        return true;

                    default:
                        return false;
                }
            }
        });

        //Task History
        histList = (RecyclerView) view.findViewById(R.id.histList);
        histMan = new LinearLayoutManager(getActivity());
        histList.setHasFixedSize(true);
        histList.setItemAnimator(new DefaultItemAnimator());
        histList.setLayoutManager(histMan);
        populateAdapter();
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
                toolbarBottom.getMenu().clear();
                toolbarBottom.setNavigationIcon(R.drawable.ic_action_up);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
                }
            }

            @Override
            public void onPanelExpanded(View view) {
                toolbarBottom.inflateMenu(R.menu.menu_info);
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
                if( keyCode == KeyEvent.KEYCODE_BACK ){
                    if(addEvent.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                        addEvent.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                        return true;
                    }else {

                        return false;
                    }
                }
                return false;
            }
        });

        //cards
        View vslideInfo = view.findViewById(R.id.sliding_layout);
        cardList = (RecyclerView) vslideInfo.findViewById(R.id.info_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setHasFixedSize(true);
        cardList.setItemAnimator(new DefaultItemAnimator());
        cardList.setLayoutManager(cardMan);
        populateCards();
        cardList.setAdapter(cardListAdapter);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        populateAdapter();
        histList.setAdapter(histListAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch (menuitem.getItemId()) {
            case R.id.menu_add:
                Intent addIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                getActivity().startActivity(addIntent);

                return true;

            default:
                return super.onOptionsItemSelected(menuitem);
        }
    }

    public void populateCards(){
        Map<String, Map> vehicleInfo = new LinkedHashMap<>();
        Map<String, String> tempGeneral = new LinkedHashMap<>();
        Map<String, String> tempEngine = new LinkedHashMap<>();
        Map<String, String> tempTires = new LinkedHashMap<>();

        if(!(
                (vehicleSent.get("year")).isEmpty() &&
                (vehicleSent.get("make")).isEmpty() &&
                (vehicleSent.get("model")).isEmpty() &&
                (vehicleSent.get("plate")).isEmpty()
        )) {
            tempGeneral.put("year", vehicleSent.get("year"));
            tempGeneral.put("make", vehicleSent.get("make"));
            tempGeneral.put("model", vehicleSent.get("model"));
            tempGeneral.put("plate", vehicleSent.get("plate"));

            vehicleInfo.put("gen", tempGeneral);
        }

        if(!(
                (vehicleSent.get("engine")).isEmpty() &&
                (vehicleSent.get("oilFilter")).isEmpty() &&
                (vehicleSent.get("oilWeight")).isEmpty()
        )) {
            tempEngine.put("engine", vehicleSent.get("engine"));
            tempEngine.put("oilFilter", vehicleSent.get("oilFilter"));
            tempEngine.put("oilWeight", vehicleSent.get("oilWeight"));

            vehicleInfo.put("eng", tempEngine);
        }

        if(!(
                (vehicleSent.get("tireSummer")).isEmpty() &&
                (vehicleSent.get("tireWinter")).isEmpty()
        )){

            tempTires.put("tireSummer", vehicleSent.get("tireSummer"));
            tempTires.put("tireWinter", vehicleSent.get("tireWinter"));

            vehicleInfo.put("tires", tempTires);
        }

        cardListAdapter = new adapter_info(vehicleInfo);
    }

    public void populateAdapter(){
        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);

        histListAdapter = new adapter_history(vehicleHist);
    }
}
