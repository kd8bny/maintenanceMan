package com.kd8bny.maintenanceman.ui.main;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_overview extends RecyclerView.Adapter<adapter_overview.AdapterViewHolder>{
    private static final String TAG = "adptr_ovrvw";

    public ArrayList<HashMap> vehicleList = new ArrayList<>();
    private boolean DBisEmpty;
    private TypedArray headerColors;
    private int errorColor;
    private View itemView;

    public adapter_overview(HashMap<String, HashMap> vehicleList) {
        this.vehicleList.addAll(vehicleList.values());
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

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        if(!DBisEmpty) {
            HashMap<String, HashMap> vehicle = vehicleList.get(i);

            HashMap<String, String> category = vehicle.get("General");
            (itemView.findViewById(R.id.carPic)).setBackgroundColor(headerColors.getColor(i%5, 0));
            adapterViewHolder.vyear.setText(category.get("Year"));
            adapterViewHolder.vmake.setText(category.get("Make"));
            adapterViewHolder.vmodel.setText(category.get("Model"));

            switch (category.get("type")){
                case "Automobile":
                    adapterViewHolder.carPic.setImageResource(R.drawable.car_icon_md_9);
                    break;

                case "Motorcycle":
                    adapterViewHolder.carPic.setImageResource(R.drawable.motorcycle_md_9);
                    break;

                case "Utility":
                    adapterViewHolder.carPic.setImageResource(R.drawable.utility_md_9);
                    break;

                case "Marine":
                    adapterViewHolder.carPic.setImageResource(R.drawable.boat_md_9);
                    break;

                case "Lawn and Garden":
                    adapterViewHolder.carPic.setImageResource(R.drawable.tractor_md_9);
                    break;

                default:
                    break;
            }

        }else{
            (itemView.findViewById(R.id.carPic)).setBackgroundColor(errorColor);
            adapterViewHolder.vyear.setText(R.string.empty_db);
        }
    }

    public int determineLayout(){
        HashMap<String, HashMap> vehicle = vehicleList.get(0);

        if(vehicle == null) {
            DBisEmpty = true;

            return R.layout.card_overview_empty;
        }else{
            DBisEmpty = false;

            return R.layout.card_overview;
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{
        protected TextView vyear;
        protected TextView vmake;
        protected TextView vmodel;
        protected ImageView carPic;

        public AdapterViewHolder(View view) {
            super(view);

            vyear = (TextView) view.findViewById(R.id.year);
            vmake = (TextView) view.findViewById(R.id.make);
            vmodel = (TextView) view.findViewById(R.id.model);
            carPic = (ImageView) view.findViewById(R.id.carPic);
        }
    }
}


