package com.kd8bny.maintenanceman.ui.add;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

public class adapter_add_fleetRoster extends RecyclerView.Adapter<adapter_add_fleetRoster.AdapterViewHolder> {
    private static final String TAG = "adapter_add_flt_rtr";

    private View itemView;

    private ArrayList<ArrayList> allSpecs;
    private TypedArray headerColors;


    public adapter_add_fleetRoster(ArrayList<ArrayList> allSpecs) {
        this.allSpecs = allSpecs;
    }

    @Override
    public int getItemCount() {
        return allSpecs.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_add_fleet_roster, viewGroup, false);

        headerColors = itemView.getResources().obtainTypedArray(R.array.header_color);

        return new AdapterViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        ArrayList<String> tempField = allSpecs.get(i);

        if(i == 0){

        }else{
            adapterViewHolder.vcat.setText(tempField.get(1));
            adapterViewHolder.vvalue.setText(tempField.get(2));
        }

        //TODO cat????
        adapterViewHolder.vcat.setText(tempField.get(1));
        adapterViewHolder.vvalue.setText(tempField.get(2));

        //TODO drawable
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



