package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Travel;

import java.util.ArrayList;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.AdapterViewHolder> {
    private static final String TAG = "adptr_hstry";

    private Context mContext;
    private Resources res;

    private ArrayList<Travel> mTravelList;
    private View itemView;

    public TravelAdapter(Context context, ArrayList<Travel> travelList) {
        mContext = context;
        mTravelList = travelList;
    }

    @Override
    public int getItemCount() {
        if (mTravelList.isEmpty()){
            return 1;
        }
        return mTravelList.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        res = viewGroup.getResources();
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_business, viewGroup, false);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder viewHolder, int i) {
       if(!mTravelList.isEmpty()){
           Travel travel = mTravelList.get(i);
           viewHolder.vDate.setText(travel.getDate());
           viewHolder.vDest.setText(travel.getDest());

           String delta;
           if (travel.getStop() == -1.0){
               viewHolder.vHeader.setBackgroundColor(ContextCompat.getColor(mContext, R.color.error));
               delta = String.format("In Progress");
           }else{
               delta = String.format("%f mi", travel.getDelta());
               viewHolder.vHeader.setBackgroundColor(ContextCompat.getColor(mContext, R.color.goodToGo));
           }
           viewHolder.vDelta.setText(delta);
       }else{
           viewHolder.vDate.setText(itemView.getResources().getString(R.string.error_no_history));
           viewHolder.vHeader.setBackgroundColor(ContextCompat.getColor(mContext, R.color.error));
       }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "adptr_bsness";

        protected ImageView vHeader;
        protected TextView vDate;
        protected TextView vDelta;
        protected TextView vDest;
        protected TextView vUnit;

        public AdapterViewHolder(View view) {
            super(view);

            vHeader = (ImageView) view.findViewById(R.id.header);
            vDate = (TextView) view.findViewById(R.id.val_date);
            vDelta = (TextView) view.findViewById(R.id.val_delta);
            vDest = (TextView) view.findViewById(R.id.val_dest);
            //vUnit = (TextView) view.findViewById(R.id.val_spec_unit);
        }
    }
}


