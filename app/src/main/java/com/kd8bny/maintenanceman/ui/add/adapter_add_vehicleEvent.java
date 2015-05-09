package com.kd8bny.maintenanceman.ui.add;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_add_vehicleEvent extends RecyclerView.Adapter<adapter_add_vehicleEvent.AdapterViewHolder>{
    private static final String TAG = "adptr_add_vhcl_evnt";

    private HashMap<String, String> dataSet = new HashMap<>();
    private ArrayList<String> fields;
    private ArrayList<String> values = new ArrayList<>();
    private TypedArray headerColors;
    private Boolean isSwaped;

    public adapter_add_vehicleEvent(HashMap<String, String> dataSet, ArrayList<String> fields, Boolean isSwaped) {
        this.isSwaped = isSwaped;
        this.dataSet = dataSet;
        this.fields = fields;
        for (String key : dataSet.keySet()){
            this.values.add(dataSet.get(key));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_add_vehicle_event, viewGroup, false);

        headerColors = itemView.getResources().obtainTypedArray(R.array.header_color);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        adapterViewHolder.vfield.setText(fields.get(i));
        adapterViewHolder.vvalue.setText(values.get(i));
        if(!isSwaped) {
            adapterViewHolder.vfield.setBackgroundColor(headerColors.getColor(i % 5, 0));
        }

    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vfield;
        protected TextView vvalue;

        public AdapterViewHolder(View view) {
            super(view);

            vfield = (TextView) view.findViewById(R.id.category);
            vvalue = (TextView) view.findViewById(R.id.value);
        }
    }
}



