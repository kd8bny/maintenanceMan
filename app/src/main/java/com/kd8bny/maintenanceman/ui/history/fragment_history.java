package com.kd8bny.maintenanceman.ui.history;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.fleetRosterDBHelper;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.ui.main.adapter_overview;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import java.util.ArrayList;


public class fragment_history extends Fragment {
    private static final String TAG = "fragment_history";

    private Toolbar toolbar;
    private SlidingUpPanelLayout addEvent;
    private ImageView direction;
    RecyclerView cardList;
    RecyclerView.LayoutManager cardMan;
    RecyclerView.Adapter cardListAdapter;


    private String refID;
    public ArrayList<ArrayList> vehicleList;
    public ArrayList<String> vehicleSent;

    public fragment_history() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        vehicleSent = getActivity().getIntent().getStringArrayListExtra("vehicleSent");
        refID = vehicleSent.get(0);

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Task History
        ListView taskHist = (ListView) view.findViewById(R.id.taskList);
        taskHist.setAdapter(poplulateAdapter());

        //Slide-y up menu
        final View include;

        include = view.findViewById(R.id.slide_bar);
        direction = (ImageView) include.findViewById(R.id.slide_icon);

        addEvent = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        addEvent.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {


            }

            @Override
            public void onPanelCollapsed(View view) {
                //setMenuVisibility(false);
                direction.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_up));

            }

            @Override
            public void onPanelExpanded(View view) {
                //setMenuVisibility(true);
                direction.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_down));

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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_add_fleet_roster, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return super.onOptionsItemSelected(item);
        }

    /*@Override
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

    public ArrayAdapter poplulateAdapter(){
        ArrayList<String> histEvent = new ArrayList<>();
        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        ArrayList<ArrayList> vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);

        ArrayList<String> temp;
        for(int i = 0; i < vehicleHist.size(); i++){
            temp = vehicleHist.get(i);
            if(temp.size()>2) {
                histEvent.add(temp.get(1) + " " + temp.get(2) + " " + temp.get(3));
            }else{
                histEvent.add(temp.get(1));
            }
        }

        return new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, histEvent);
    }

}
