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

public class adapter_add_fleetRoster extends RecyclerView.Adapter<adapter_add_fleetRoster.AdapterViewHolder>{
    private static final String TAG = "adapter_add_flt_rtr";

    public ArrayList<ArrayList> vehicleData = new ArrayList<>();
    private TypedArray headerColors;
    private View itemView;

    public adapter_add_fleetRoster(ArrayList vehicleData) {
        this.vehicleData = vehicleData;
    }

    @Override
    public int getItemCount() {
        return vehicleData.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_list_add_fleet_roster, viewGroup, false);

        headerColors = itemView.getResources().obtainTypedArray(R.array.header_color);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        ArrayList<String> tempField = vehicleData.get(i);

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



