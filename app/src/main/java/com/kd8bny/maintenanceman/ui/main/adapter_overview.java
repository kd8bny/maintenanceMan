package com.kd8bny.maintenanceman.ui.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.history.activity_history;

import java.util.ArrayList;

public class adapter_overview extends RecyclerView.Adapter<adapter_overview.AdapterViewHolder>{
    private static final String TAG = "adapter_overview";

    public ArrayList<ArrayList> vehicleList = new ArrayList<>();
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
            adapterViewHolder.pos = i;
            Log.i(TAG,i+"binf");
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
        ArrayList<String> vehicleSpecs = new ArrayList<>();
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
        protected TypedArray headerColors;
        protected int pos;



        public AdapterViewHolder(View view, final ArrayList<ArrayList> vehicleList, final Boolean DBisEmpty) {
            super(view);
            headerColors = view.getResources().obtainTypedArray(R.array.header_color);
            (view.findViewById(R.id.carPic)).setBackgroundColor(headerColors.getColor(pos%5, 0));

            Log.i(TAG,pos+"");
            view.setTag(R.id.tag_0, vehicleList);
            view.setTag(R.id.tag_1, DBisEmpty);
            view.setOnClickListener(this);

            vyear = (TextView) view.findViewById(R.id.year);
            vmake = (TextView) view.findViewById(R.id.make);
            vmodel = (TextView) view.findViewById(R.id.model);
            vengine = (TextView) view.findViewById(R.id.engine);
        }

        @Override
        public void onClick(View view) {
            ArrayList<ArrayList> vehicleList = (ArrayList<ArrayList>) view.getTag(R.id.tag_0);
            Boolean DBisEmpty = (Boolean) view.getTag(R.id.tag_1);

            if(!DBisEmpty) {
                Intent viewIntent = new Intent(view.getContext(), activity_history.class);
                viewIntent.putStringArrayListExtra("vehicleSent", vehicleList.get(getPosition()));
                view.getContext().startActivity(viewIntent);
            }else{
                Intent viewAddIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                view.getContext().startActivity(viewAddIntent);
            }
        }

}
}


