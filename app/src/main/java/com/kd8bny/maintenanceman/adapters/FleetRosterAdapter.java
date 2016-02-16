package com.kd8bny.maintenanceman.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

public class FleetRosterAdapter extends RecyclerView.Adapter<FleetRosterAdapter.AdapterViewHolder> {
    private static final String TAG = "adapter_flt_rtr";

    private View vVehicleSpec;
    private ArrayList<ArrayList> mAllSpecs;

    public FleetRosterAdapter(ArrayList<ArrayList> allSpecs) {
        mAllSpecs = allSpecs;
    }


    @Override
    public int getItemCount() {
        return mAllSpecs.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        vVehicleSpec = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_fleet_roster, viewGroup, false);
        return new AdapterViewHolder(vVehicleSpec);
    }


    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        ArrayList<String> tempField = mAllSpecs.get(i);
        adapterViewHolder.vCategory.setText(tempField.get(0));
        adapterViewHolder.vLabel.setText(tempField.get(1));
        adapterViewHolder.vValue.setText(tempField.get(2));
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vCategory;
        protected TextView vLabel;
        protected TextView vValue;

        public AdapterViewHolder(View view) {
            super(view);

            vCategory = (TextView) view.findViewById(R.id.category);
            vLabel = (TextView) view.findViewById(R.id.label);
            vValue = (TextView) view.findViewById(R.id.value);
        }
    }
}



