package com.kd8bny.maintenanceman.ui.history;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.ListView;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import java.util.ArrayList;


public class fragment_history extends Fragment {
    private static final String TAG = "fragment_add_vehicleEvent";

    private Toolbar toolbar;
    private SlidingUpPanelLayout addEvent;

    private String date;
    private String odo;
    private String task;
    private String refID;

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

        ArrayList<String> vehicleSent = getActivity().getIntent().getStringArrayListExtra("vehicleSent");
        refID = vehicleSent.get(0);

        //Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Slidy up menu
        ListView taskHist = (ListView) view.findViewById(R.id.taskList);
        taskHist.setAdapter(poplulateAdapter());

        addEvent = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        addEvent.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {


            }

            @Override
            public void onPanelCollapsed(View view) {
                setMenuVisibility(false);

            }

            @Override
            public void onPanelExpanded(View view) {
                setMenuVisibility(true);

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_fleet_roster, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.menu_save:
                Context context = getActivity().getApplicationContext();

                this.getValues();

                vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(context);
                vehicleDB.saveEntry(context, refID, date, odo, task);

                Toast.makeText(this.getActivity(), "History Updated", Toast.LENGTH_SHORT).show();
                ListView taskHist = (ListView) getActivity().findViewById(R.id.taskList);
                taskHist.setAdapter(poplulateAdapter());

                return true;


            case R.id.menu_cancel:
                getActivity().finish();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
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


    public ArrayAdapter poplulateAdapter(){
        ArrayList<String> histEvent = new ArrayList<>();
        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        ArrayList<ArrayList> vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);

        ArrayList<String> temp;
        for(int i = 0; i < vehicleHist.size(); i++){
            Log.i(TAG,vehicleHist.toString());
            temp = vehicleHist.get(i);
            if(temp.size()>2) {
                histEvent.add(temp.get(1) + " " + temp.get(2) + " " + temp.get(3));
            }else{
                histEvent.add(temp.get(1));
            }
        }

        return new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, histEvent);
    }

    public void getValues(){
        date = ((EditText) getActivity().findViewById(R.id.val_spec_date)).getText().toString();
        task = ((EditText) getActivity().findViewById(R.id.val_spec_event)).getText().toString();
        odo = ((EditText) getActivity().findViewById(R.id.val_spec_odo)).getText().toString();
    }


}
