package com.kd8bny.maintenanceman.ui.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.history.activity_history;

import java.util.ArrayList;

public class adapter_overview extends RecyclerView.Adapter<adapter_overview.AdapterViewHolder>{
    private static final String TAG = "adapter_overview";

    public ArrayList<ArrayList> vehicleList = new ArrayList<ArrayList>();
    private boolean DBisEmpty;

    public adapter_overview(ArrayList vehicleList) {
        this.vehicleList = vehicleList;
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        ArrayList<String> vehicleSpecs = new ArrayList<String>();
        vehicleSpecs.addAll(vehicleList.get(i));

        if(!DBisEmpty) {
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
                inflate(determineLayout(), viewGroup, false);
        return new AdapterViewHolder(itemView, vehicleList, DBisEmpty);
    }


    public int determineLayout(){
        ArrayList<String> vehicleSpecs = new ArrayList<String>();
        vehicleSpecs.addAll(vehicleList.get(0));

        if(vehicleSpecs.get(0) != null) {
            DBisEmpty = false;
            return R.layout.overview_card;
        }else{
            DBisEmpty = true;
            return R.layout.overview_card_empty;
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView vyear;
        protected TextView vmake;
        protected TextView vmodel;
        protected TextView vengine;

        public AdapterViewHolder(View view, final ArrayList<ArrayList> vehicleList, final Boolean DBisEmpty) {
            super(view);

            view.setTag(vehicleList);
            if(!DBisEmpty) {
                view.setOnClickListener(this);
            }

            vyear = (TextView) view.findViewById(R.id.year);
            vmake = (TextView) view.findViewById(R.id.make);
            vmodel = (TextView) view.findViewById(R.id.model);
            vengine = (TextView) view.findViewById(R.id.engine);
        }

        @Override
        public void onClick(View view) {
            ArrayList<ArrayList> vehicleList = (ArrayList<ArrayList>) view.getTag();

            Intent viewIntent = new Intent(view.getContext(), activity_history.class);
            viewIntent.putStringArrayListExtra("vehicleSent", vehicleList.get(getPosition()));
            view.getContext().startActivity(viewIntent);
        }

}
}


