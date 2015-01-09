package com.kd8bny.maintenanceman.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.List;

/**
 * Created by kd8bny on 1/7/15.
 */
public class adapter_overview extends RecyclerView.Adapter<adapter_overview.AdapterViewHolder> {

    private List<String> vehicleSpecs;

    public adapter_overview(List<String> vehicleSpecs) {
        this.vehicleSpecs = vehicleSpecs;
    }

    @Override
    public int getItemCount() {
        return vehicleSpecs.size();
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        adapterViewHolder.vyear.setText(vehicleSpecs.get(0));
        adapterViewHolder.vmake.setText(vehicleSpecs.get(1));
        adapterViewHolder.vmodel.setText(vehicleSpecs.get(2));
        adapterViewHolder.vengine.setText(vehicleSpecs.get(3));
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


