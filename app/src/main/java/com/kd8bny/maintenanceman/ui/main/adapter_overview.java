package com.kd8bny.maintenanceman.ui.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;

import java.util.ArrayList;

public class adapter_overview extends RecyclerView.Adapter<adapter_overview.AdapterViewHolder>{
    private static final String TAG = "adptr_ovrvw";

    private Context context;
    private final String mUnit;
    private ArrayList<Vehicle> roster;
    private TypedArray headerColors;

    public adapter_overview(Context context, ArrayList<Vehicle> roster, String unit) {
        this.context = context;
        this.roster = roster;
        this.mUnit = unit;
    }

    @Override
    public int getItemCount() {
        if(roster.isEmpty()) {
            return 1;
        }

        return roster.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(determineLayout(), viewGroup, false);

        headerColors = itemView.getResources().obtainTypedArray(R.array.header_color);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        if(!roster.isEmpty()) {
            int color = headerColors.getColor(i % 8, 0);
            Vehicle vehicle = roster.get(i);
            switch (vehicle.getVehicleType()){
                case "Automobile":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_car);
                    break;

                case "Motorcycle":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_motorcycle);
                    break;

                case "Utility":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_utility);
                    break;

                case "Marine":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_marine);
                    break;

                case "Lawn and Garden":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_tractor);
                    break;

                case "Trailer":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_trailer);
                    break;
            }
            adapterViewHolder.vTitle.setText(vehicle.getTitle());
            adapterViewHolder.vRect.setBackgroundColor(color);
            adapterViewHolder.vCarPic.setBackgroundColor(color);
        }
    }

    public int determineLayout(){
        if(roster.isEmpty()) {
            return R.layout.card_overview_empty;
        }else{
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT ){//|| Build.VERSION.SDK_INT == Build.VERSION_CODES.x
                return R.layout.card_overview_v19;
            }else{
                return R.layout.card_overview;
            }
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{
        protected ImageView vRect;
        protected ImageView vCarPic;
        protected TextView vTitle;
        protected TextView vevent;
        protected TextView vodo;

        public AdapterViewHolder(View view) {
            super(view);

            vRect = (ImageView) view.findViewById(R.id.rect);
            vCarPic = (ImageView) view.findViewById(R.id.carPic);
            vTitle = (TextView) view.findViewById(R.id.vehicle_title);

            //vevent = (TextView) view.findViewById(R.id.event);
            //vodo = (TextView) view.findViewById(R.id.odo);
            //vevent.setSelected(true);
        }
    }
}


