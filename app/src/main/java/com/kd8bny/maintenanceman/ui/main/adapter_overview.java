package com.kd8bny.maintenanceman.ui.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.history.activity_history;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_overview extends RecyclerView.Adapter<adapter_overview.AdapterViewHolder>{
    private static final String TAG = "adapter_overview";

    public ArrayList<HashMap> vehicleList = new ArrayList<>();
    private boolean DBisEmpty;
    private TypedArray headerColors;
    private int errorColor;
    private View itemView;

    public adapter_overview(ArrayList vehicleList) {
        this.vehicleList = vehicleList;

    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(determineLayout(), viewGroup, false);

        headerColors = itemView.getResources().obtainTypedArray(R.array.header_color);
        errorColor = itemView.getResources().getColor(R.color.error);

        return new AdapterViewHolder(itemView, vehicleList, DBisEmpty);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        HashMap<String, String> vehicleSpecs = new HashMap<>();
        vehicleSpecs.putAll(vehicleList.get(i));

        if(!DBisEmpty) {
            (itemView.findViewById(R.id.carPic)).setBackgroundColor(headerColors.getColor(i%5, 0));

            adapterViewHolder.vyear.setText(vehicleSpecs.get("year"));
            adapterViewHolder.vmake.setText(vehicleSpecs.get("make"));
            adapterViewHolder.vmodel.setText(vehicleSpecs.get("model"));
            adapterViewHolder.vplate.setText(vehicleSpecs.get("plate"));

        }else{
            (itemView.findViewById(R.id.carPic)).setBackgroundColor(errorColor);
            adapterViewHolder.vyear.setText(vehicleSpecs.get(null));
        }
    }

    public int determineLayout(){
        HashMap<String, String> vehicleSpecs = new HashMap<>();
        vehicleSpecs.putAll(vehicleList.get(0));

        if(vehicleSpecs.containsKey(null)) {
            DBisEmpty = true;

            return R.layout.overview_card_empty;
        }else{
            DBisEmpty = false;

            return R.layout.overview_card;
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView vyear;
        protected TextView vmake;
        protected TextView vmodel;
        protected TextView vplate;


        public AdapterViewHolder(View view, final ArrayList<HashMap> vehicleList, final Boolean DBisEmpty) {
            super(view);

            view.setTag(R.id.tag_0, vehicleList);
            view.setTag(R.id.tag_1, DBisEmpty);
            view.setOnClickListener(this);

            vyear = (TextView) view.findViewById(R.id.year);
            vmake = (TextView) view.findViewById(R.id.make);
            vmodel = (TextView) view.findViewById(R.id.model);
            vplate = (TextView) view.findViewById(R.id.plate);
        }

        @Override
        public void onClick(View view) {
            ArrayList<HashMap> vehicleList = (ArrayList<HashMap>) view.getTag(R.id.tag_0);
            Boolean DBisEmpty = (Boolean) view.getTag(R.id.tag_1);

            if(!DBisEmpty) {
                Intent viewIntent = new Intent(view.getContext(), activity_history.class);
                viewIntent.putExtra("vehicleSent", vehicleList.get(getPosition()));
                view.getContext().startActivity(viewIntent);
            }else{
                Intent viewAddIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                view.getContext().startActivity(viewAddIntent);
            }
        }
    }
}


