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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

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

    private Toolbar toolbar;
    private SlidingUpPanelLayout addEvent;
    private ImageView direction;
    private ImageButton button_del, button_edit;
    RecyclerView cardList;
    RecyclerView.LayoutManager cardMan;
    RecyclerView.Adapter cardListAdapter;

    private ArrayAdapter taskAdapter;
    private String refID;
    public ArrayList<ArrayList> vehicleList;
    public ArrayList<String> vehicleSent;

    private ArrayList<String> histEvent;
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
        poplulateAdapter();
        taskHist.setAdapter(taskAdapter);
        taskHist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Delete " + histEvent.get(pos));
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        taskAdapter.remove(taskAdapter.getItem(pos));

                        ArrayList<String> temp = vehicleHist.get(pos);
                        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(getActivity());
                        vehicleDB.deleteEntry(getActivity(), temp.get(1), temp.get(2), temp.get(3));

                    }

                });
                builder.show();
                return true;
            }
        });

        //Slide-y up menu
        final View include = view.findViewById(R.id.slide_bar);
        direction = (ImageView) include.findViewById(R.id.slide_icon);
        button_del = (ImageButton) include.findViewById(R.id.slide_button_del);
        button_edit = (ImageButton) include.findViewById(R.id.slide_button_edit);

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(getActivity(), activity_edit.class);
                editIntent.putStringArrayListExtra("vehicleSent", vehicleSent);
                getActivity().startActivity(editIntent);
            }
        });

        button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        button_edit.setVisibility(View.INVISIBLE);
        button_del.setVisibility(View.INVISIBLE);

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
                setMenuVisibility(false);
                button_edit.setVisibility(View.INVISIBLE);
                button_del.setVisibility(View.INVISIBLE);
                direction.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_up));

            }

            @Override
            public void onPanelExpanded(View view) {
                setMenuVisibility(true);
                button_edit.setVisibility(View.VISIBLE);
                button_del.setVisibility(View.VISIBLE);
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
        inflater.inflate(R.menu.menu_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent addIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                getActivity().startActivity(addIntent);

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

    public void poplulateAdapter(){
        histEvent = new ArrayList<>();
        vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(this.getActivity());
        vehicleHist = vehicleDB.getEntries(getActivity().getApplicationContext(), refID);

        ArrayList<String> temp;
        for(int i = 0; i < vehicleHist.size(); i++){
            temp = vehicleHist.get(i);
            if(temp.size()>2) {
                histEvent.add(temp.get(1) + " " + temp.get(2) + " " + temp.get(3));
            }else{
                histEvent.add(temp.get(1));
            }
        }

        taskAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, histEvent);
    }

}
