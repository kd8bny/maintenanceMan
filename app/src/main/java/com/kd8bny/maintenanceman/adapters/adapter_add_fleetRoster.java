package com.kd8bny.maintenanceman.adapters;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

public class adapter_add_fleetRoster extends RecyclerView.Adapter<adapter_add_fleetRoster.AdapterViewHolder> {
    private static final String TAG = "adapter_add_flt_rtr";

    private View vVehicleName;
    private View vVehicleSpec;

    private static final int VIEW_NAME = 0;
    private static final int VIEW_SPEC = 1;

    private ArrayList<ArrayList> allSpecs;
    private TypedArray headerColors;


    public adapter_add_fleetRoster(ArrayList<ArrayList> allSpecs) {
        Log.d(TAG, allSpecs.toString());
        this.allSpecs = allSpecs;
    }

    public int getItemViewType(int i){
        if(i == VIEW_NAME) {
            return VIEW_NAME;
        }else{
            return VIEW_SPEC;
        }
    }

    @Override
    public int getItemCount() {
        return allSpecs.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i){
            case VIEW_NAME:
                vVehicleName = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_add_fleet_roster, viewGroup, false);
                return new AdapterViewHolder(vVehicleName);
            default:
                vVehicleSpec = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_add_fleet_roster, viewGroup, false);
                return new AdapterViewHolder(vVehicleSpec);
        }
        //headerColors = .getResources().obtainTypedArray(R.array.header_color);
    }


    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        ArrayList<String> tempField = allSpecs.get(i);
        //TODO cat????
        //TODO drawable

        switch (i){
            case VIEW_NAME:
                adapterViewHolder.vcat.setText(tempField.get(1));
                adapterViewHolder.vvalue.setText(tempField.get(2));
                break;
            default:
                adapterViewHolder.vcat.setText(tempField.get(1));
                adapterViewHolder.vvalue.setText(tempField.get(2));
                break;
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vcat;
        protected TextView vvalue;

        public AdapterViewHolder(View view) {
            super(view);

            vcat = (TextView) view.findViewById(R.id.category);
            vvalue = (TextView) view.findViewById(R.id.value);
        }
    }
}



