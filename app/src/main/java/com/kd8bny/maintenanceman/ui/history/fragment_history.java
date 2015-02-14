package com.kd8bny.maintenanceman.ui.history;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.edit.activity_edit;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import java.util.ArrayList;


public class fragment_history extends Fragment {
    private static final String TAG = "fragment_history";

    private Toolbar toolbar, toolbarBottom;
    private SlidingUpPanelLayout addEvent;
    RecyclerView cardList, histList;
    RecyclerView.LayoutManager cardMan, histMan;
    RecyclerView.Adapter cardListAdapter, histListAdapter;

    private String refID;
    public ArrayList<String> vehicleSent;

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

        vehicleSent = getActivity().getIntent().getStringArrayListExtra("vehicleSent");
        refID = vehicleSent.get(0);

        //Toolbar top
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Toolbar bottom
        toolbarBottom = (Toolbar) view.findViewById(R.id.tool_bar_bottom);
        toolbarBottom.setTitle(R.string.title_info);
        //toolbarBottom.inflateMenu(R.menu.menu_info);

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
                        editIntent.putStringArrayListExtra("vehicleSent", vehicleSent);
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
                if (slideOffset > 0.2) {
                    toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                } else {
                    toolbar.animate().translationY(+toolbar.getTop()).setInterpolator(new AccelerateInterpolator()).start();
                }

            }

            @Override
            public void onPanelCollapsed(View view) {
                toolbarBottom.getMenu().clear();
                toolbarBottom.setNavigationIcon(R.drawable.ic_action_up);
            }

            @Override
            public void onPanelExpanded(View view) {
                toolbarBottom.inflateMenu(R.menu.menu_info);
                toolbarBottom.setNavigationIcon(R.drawable.ic_action_down);
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

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

    /*@Override //TODO
    public void onBackPressed() {
        if (addEvent != null &&
                (addEvent.getPanelState() == PanelState.EXPANDED || addEvent.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            addEvent.collapsePanel();
        } else {
            super.onBackPressed();
        }
    }*/

    public void populateCards(){
        //year make model engine plate oilFilter oilWeight tireSummer tireWinter
        ArrayList<ArrayList> vehicleInfo = new ArrayList<>();
        ArrayList<String> tempGeneral = new ArrayList<>();
        ArrayList<String> tempEngine = new ArrayList<>();
        ArrayList<String> tempTires = new ArrayList<>();

        tempGeneral.add(vehicleSent.get(1));
        tempGeneral.add(vehicleSent.get(2));
        tempGeneral.add(vehicleSent.get(3));
        tempGeneral.add(vehicleSent.get(5));
        vehicleInfo.add(tempGeneral);

        tempEngine.add(vehicleSent.get(4));
        tempEngine.add(vehicleSent.get(6));
        tempEngine.add(vehicleSent.get(7));
        vehicleInfo.add(tempEngine);

        tempTires.add(vehicleSent.get(8));
        tempTires.add(vehicleSent.get(9));
        vehicleInfo.add(tempTires);

        cardListAdapter = new adapter_info(vehicleInfo);
    }

    public void populateAdapter(){
        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);

        histListAdapter = new adapter_history(vehicleHist);
    }
}
