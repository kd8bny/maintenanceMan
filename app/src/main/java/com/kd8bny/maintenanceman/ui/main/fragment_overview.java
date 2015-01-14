package com.kd8bny.maintenanceman.ui.main;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.kd8bny.maintenanceman.ui.drawer.drawer_items;
import com.kd8bny.maintenanceman.ui.settings.activity_settings;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class fragment_overview extends Fragment {
    private static final String TAG = "fragment_overview";

    public ArrayList<ArrayList> vehicleList = new ArrayList<ArrayList>();

    public fragment_overview() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onResume();

        return inflater.inflate(R.layout.fragment_overview, container, false); //TODO need this???? YES but should take out of return and move things from onResume
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setContentView(R.layout.fragment_overview);

        RecyclerView cardList = (RecyclerView) getActivity().findViewById(R.id.overview_cardList);

        //cards
        LinearLayoutManager linMan = new LinearLayoutManager(getActivity());
        cardList.setHasFixedSize(true);
        cardList.setItemAnimator(new DefaultItemAnimator());
        linMan.setOrientation(LinearLayoutManager.VERTICAL);
        cardList.setLayoutManager(linMan);
        cardList.setAdapter(poplulateAdapter());

        //fab
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.attachToRecyclerView(cardList);
        getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                startActivity(addIntent);
            }
        });

        //drawer_item
        ListView drawerList = (ListView) getActivity().findViewById(R.id.list_slidermenu);
        drawerList.setAdapter(populateDrawer());

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);



    }

    public adapter_overview poplulateAdapter(){
        fleetRosterDBHelper fleetDB = new fleetRosterDBHelper(this.getActivity());
        vehicleList = fleetDB.getEntries(getActivity().getApplicationContext());

        adapter_overview adapter = new adapter_overview(vehicleList);

        return adapter;
    }

    public ArrayAdapter<String> populateDrawer(){
        String[] mMenuTitles = getResources().getStringArray(R.array.drawer_items);
        DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        ArrayList<String> singleDrawerItems = new ArrayList<String>();

        singleDrawerItems.add(mMenuTitles[0]);
        singleDrawerItems.add(mMenuTitles[1]);
        singleDrawerItems.add(mMenuTitles[2]);
        singleDrawerItems.add(mMenuTitles[3]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.drawer_item , singleDrawerItems);

        return adapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_overview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(getActivity(), activity_settings.class);
                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }*/

    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //selectItem(position);

            switch (position){
                case 0:
                    Intent addFleetIntent = new Intent(getActivity(), activity_add_fleetRoster.class);
                    startActivity(addFleetIntent);
                    break;

                case 1:
                    /*Intent addEventIntent = new Intent(getActivity(), activity_vehicleEvent.class);
                    startActivity(addEventIntent);*/
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
    }
}
