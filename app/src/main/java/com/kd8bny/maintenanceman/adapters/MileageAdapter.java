package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MileageAdapter extends RecyclerView.Adapter<MileageAdapter.AdapterViewHolder> {
    private static final String TAG = "adptr_mileage";

    private Context mContext;
    private View itemView;

    private ArrayList<Mileage> mMileageList;
    private String UNIT_DIST;
    private String UNIT_MILEAGE;
    private Double highMileage;
    private Double lowMileage;

    public MileageAdapter(Context context, Vehicle vehicle, ArrayList<Mileage> mileageList) {
        mContext = context;
        mMileageList = mileageList;
        UNIT_DIST = vehicle.getUnitMileage();
        UNIT_MILEAGE = vehicle.getUnitMileage();

        if (!mMileageList.isEmpty()) {
            ArrayList<Double> mileages = new ArrayList<>();
            Double tolerance = 3.0;
            for (Mileage m: mileageList) {
                mileages.add(m.getMileage());
            }
            highMileage = Collections.max(mileages) - tolerance;
            lowMileage = Collections.min(mileages) + tolerance;
        }
    }

    @Override
    public int getItemCount() {
        if (mMileageList.isEmpty()){
            return 1;
        }
        return mMileageList.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_mileage, viewGroup, false);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder viewHolder, int i) {
        if(!mMileageList.isEmpty()){
            int color;
            Mileage mileage = mMileageList.get(i);
            Double mileageVal = mileage.getMileage();
            if (mileageVal >= highMileage){
                color = ContextCompat.getColor(mContext, R.color.goodToGo);
                viewHolder.vIcon.setImageResource(R.drawable.np_fuel_full);
            }else if (mileageVal <= lowMileage){
                color = ContextCompat.getColor(mContext, R.color.error);
                viewHolder.vIcon.setImageResource(R.drawable.np_fuel_empty);
            }else{
                color = ContextCompat.getColor(mContext, R.color.mediocre);
                viewHolder.vIcon.setImageResource(R.drawable.np_fuel_med);
            }

            viewHolder.vIconBackground.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            viewHolder.vMileage.setText(String.format(Locale.ENGLISH, "%1$,.2f %2$s", mileage.getMileage(), UNIT_MILEAGE));
            viewHolder.vDate.setText(mileage.getDate());
            viewHolder.vTripo.setText(String.format(Locale.ENGLISH, "%1$,.1f %2$s", mileage.getTripometer(), UNIT_DIST));
            viewHolder.vPrice.setText(String.format(Locale.ENGLISH, "%1$s %2$,.2f", "@", mileage.getPrice()));
       }else{
            viewHolder.vMileage.setText(mContext.getResources().getString(R.string.error_no_history));
            viewHolder.vIconBackground.getDrawable()
                   .setColorFilter(ContextCompat.getColor(mContext, R.color.error), PorterDuff.Mode.SRC_ATOP);
       }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "adptr_mileage";

        protected ImageView vIcon;
        protected ImageView vIconBackground;
        protected TextView vMileage;
        protected TextView vDate;
        protected TextView vTripo;
        protected TextView vPrice;

        public AdapterViewHolder(View view) {
            super(view);

            vIcon = (ImageView) view.findViewById(R.id.icon);
            vIconBackground = (ImageView) view.findViewById(R.id.circle);
            vMileage = (TextView) view.findViewById(R.id.val_mileage);
            vDate = (TextView) view.findViewById(R.id.val_date);
            vTripo = (TextView) view.findViewById(R.id.val_tripo);
            vPrice = (TextView) view.findViewById(R.id.val_price);
        }
    }
}


