package com.kd8bny.maintenanceman.ui.main;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class adapter_overview extends RecyclerView.Adapter<adapter_overview.AdapterViewHolder> {
    private static final String TAG = "adapter_overview";

    public ArrayList<ArrayList> vehicleList = new ArrayList<>();

    public adapter_overview(ArrayList vehicleList) {
        this.vehicleList = vehicleList;
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        ArrayList<String> vehicleSpecs = new ArrayList<>();
        vehicleSpecs.addAll(vehicleList.get(i));

        if(vehicleSpecs.get(0) != null) {
            adapterViewHolder.vyear.setText(vehicleSpecs.get(1));
            adapterViewHolder.vmake.setText(vehicleSpecs.get(2));
            adapterViewHolder.vmodel.setText(vehicleSpecs.get(3));
            adapterViewHolder.vengine.setText(vehicleSpecs.get(4));
        }else{
            adapterViewHolder.vyear.setText(vehicleSpecs.get(1));
        }
    }
    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.overview_card, viewGroup, false);
        return new AdapterViewHolder(itemView);
    }
    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vyear;
        protected TextView vmake;
        protected TextView vmodel;
        protected TextView vengine;
        public AdapterViewHolder(View v) {
            super(v);
            vyear = (TextView) v.findViewById(R.id.year);
            vmake = (TextView) v.findViewById(R.id.make);
            vmodel = (TextView) v.findViewById(R.id.model);
            vengine = (TextView) v.findViewById(R.id.engine);
        }
    }
}


