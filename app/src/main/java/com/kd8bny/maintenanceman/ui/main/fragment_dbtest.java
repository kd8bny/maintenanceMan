package com.kd8bny.maintenanceman.ui.main;



import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.vehicleLog;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;
import com.kd8bny.maintenanceman.data.vehicleLog_DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class fragment_dbtest extends ListFragment {
    private static final String TAG = "vehicleLog";

    private vehicleLog_DataSource dataSource;




    public fragment_dbtest() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        dataSource = new vehicleLog_DataSource(this.getActivity());
        List<vehicleLog> values = dataSource.getAllvehicleLog();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<vehicleLog> adapter = new ArrayAdapter<vehicleLog>(getActivity(), android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dbtest, container, false);

        final View view = inflater.inflate(R.layout.fragment_dbtest,container,false);
        final View addButton = view.findViewById(R.id.add);
        final View delButton = view.findViewById(R.id.delete);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleLogDBHelper.addShit(v);
            }
        });
        delButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                vehicleLogDBHelper.addShit(v);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }
}