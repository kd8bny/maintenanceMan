package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.utils.Utils;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Locale;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.AdapterViewHolder> {
    private static final String TAG = "adptr_trvl";

    private Context mContext;
    private View itemView;

    private ArrayList<Travel> mTravelList;
    private String UNIT_DIST;

    public TravelAdapter(Context context, Vehicle vehicle, ArrayList<Travel> travelList) {
        mContext = context;
        mTravelList = travelList;
        UNIT_DIST = vehicle.getUnitDist();
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
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_travel, viewGroup, false);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder viewHolder, int i) {
       if(!mTravelList.isEmpty()){
           Travel travel = mTravelList.get(i);
           viewHolder.vDate.setText(new Utils(mContext).toFriendlyDate(new DateTime(travel.getDate())));

           if (travel.getStop() == -1.0){
               viewHolder.vHeader.setBackgroundColor(ContextCompat.getColor(mContext, R.color.error));
               viewHolder.vDest.setText(mContext.getString(R.string.field_in_progress));

           }else{
               viewHolder.vHeader.setBackgroundColor(ContextCompat.getColor(mContext, R.color.goodToGo));
               viewHolder.vDest.setText(String.format(Locale.ENGLISH, "%s %s",
                       mContext.getString(R.string.field_to), travel.getDest()));
               viewHolder.vDelta.setText(String.format(Locale.ENGLISH, "%1$.1f %2$s", travel.getDelta(), UNIT_DIST));
           }
       }else{
           viewHolder.vDest.setText(itemView.getResources().getString(R.string.error_no_history));
           viewHolder.vHeader.setBackgroundColor(ContextCompat.getColor(mContext, R.color.error));
       }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "adptr_bsness";

        protected ImageView vHeader;
        protected TextView vDest;
        protected TextView vDate;
        protected TextView vDelta;
        protected TextView vTime;


        public AdapterViewHolder(View view) {
            super(view);

            vHeader = (ImageView) view.findViewById(R.id.header);
            vDate = (TextView) view.findViewById(R.id.val_date);
            vDelta = (TextView) view.findViewById(R.id.val_delta);
            vDest = (TextView) view.findViewById(R.id.val_dest);
            vTime = (TextView) view.findViewById(R.id.val_time);
        }
    }
}


