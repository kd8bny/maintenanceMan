package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MileageAdapter extends RecyclerView.Adapter<MileageAdapter.AdapterViewHolder> {
    private static final String TAG = "adptr_mileage";

    private Context mContext;
    private View itemView;
    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private ArrayList<Mileage> mMileageList;
    private String UNIT_MILEAGE;
    private String UNIT_DIST;
    private Double highMileage;
    private Double lowMileage;

    public MileageAdapter(Context context, ArrayList<Mileage> mileageList) {
        mContext = context;
        mMileageList = mileageList;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        if (sharedPreferences.getString("prefUnitDist", "mi").equals("mi")){
            UNIT_MILEAGE = mContext.getResources().getString(R.string.unit_mileage_us);
            UNIT_DIST = mContext.getResources().getString(R.string.unit_dist_us);
        }else{
            UNIT_MILEAGE = mContext.getResources().getString(R.string.unit_mileage_metric);
            UNIT_DIST = mContext.getResources().getString(R.string.unit_dist_metric);
        }

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
            viewHolder.vMileage.setText(String.format(Locale.ENGLISH, "%1$,.2f", mileage.getMileage()));
            viewHolder.vMileageUnit.setText(UNIT_MILEAGE);
            viewHolder.vDate.setText(mileage.getDate());
            viewHolder.vTripo.setText(String.format(Locale.ENGLISH, "%1$,.1f", mileage.getTripometer()));
            viewHolder.vTripoUnit.setText(UNIT_DIST);
            viewHolder.vPrice.setText(String.format(Locale.ENGLISH, "%1$,.2f", mileage.getPrice()));
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
        protected TextView vMileageUnit;
        protected TextView vDate;
        protected TextView vTripo;
        protected TextView vTripoUnit;
        protected TextView vPrice;

        public AdapterViewHolder(View view) {
            super(view);

            vIcon = (ImageView) view.findViewById(R.id.icon);
            vIconBackground = (ImageView) view.findViewById(R.id.circle);
            vMileage = (TextView) view.findViewById(R.id.val_mileage);
            vMileageUnit = (TextView) view.findViewById(R.id.val_mileage_unit);
            vDate = (TextView) view.findViewById(R.id.val_date);
            vTripo = (TextView) view.findViewById(R.id.val_tripo);
            vTripoUnit = (TextView) view.findViewById(R.id.val_tripo_unit);
            vPrice = (TextView) view.findViewById(R.id.val_price);
        }
    }
}


