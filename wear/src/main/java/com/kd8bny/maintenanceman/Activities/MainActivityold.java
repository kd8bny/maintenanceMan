/*package com.kd8bny.maintenanceman.Activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.kd8bny.maintenanceman.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.R;

public class MainActivityold extends ListActivity {
    private static final String TAG = "act_main";

    private ArrayList<Vehicle> roster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Gson gson = new Gson();
        //TODO get json from broadcast listener
        //roster = gson.fromJson(json, new TypeToken<ArrayList<Vehicle>>(){}.getType());

    }
}*/
