package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;

import java.util.ArrayList;
import java.util.Locale;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.AdapterViewHolder>{
    private static final String TAG = "adptr_ovrvw";

    private Context mContext;
    private View itemView;
    private int recycler_i = 0;
    private static final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";

    private VehicleLogDBHelper mVehicleLogDBHelper;
    private ArrayList<Vehicle> mRoster;
    private TypedArray headerColors;
    private String UNIT_DIST;
    private String UNIT_TIME;

    public OverviewAdapter(Context context, ArrayList<Vehicle> roster) {
        mContext = context;
        mVehicleLogDBHelper = VehicleLogDBHelper.getInstance(context);
        mRoster = roster;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        if (sharedPreferences.getString("prefUnitDist", "mi").equals("mi")){
            UNIT_DIST = mContext.getResources().getString(R.string.unit_dist_us);
        }else{
            UNIT_DIST = mContext.getResources().getString(R.string.unit_dist_metric);
        }

        UNIT_TIME =mContext.getResources().getString(R.string.unit_time);
    }

    @Override
    public int getItemCount() {
        if(mRoster.isEmpty()) {
            return 2;
        }

        return mRoster.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(determineLayout(), viewGroup, false);

        headerColors = itemView.getResources().obtainTypedArray(R.array.header_color);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        if(!mRoster.isEmpty()) {
            int color = headerColors.getColor(i % 8, 0);
            Vehicle vehicle = mRoster.get(i);
            Boolean useDist = true;
            switch (vehicle.getVehicleType()){
                case "Automobile":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_car);
                    break;

                case "Motorcycle":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_motorcycle);
                    break;

                case "Utility":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_utility);
                    useDist = false;
                    break;

                case "Marine":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_marine);
                    useDist = false;
                    break;

                case "Lawn and Garden":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_tractor);
                    useDist = false;
                    break;

                case "Trailer":
                    adapterViewHolder.vCarPic.setImageResource(R.drawable.np_trailer);
                    break;
            }
            adapterViewHolder.vTitle.setText(vehicle.getTitle());
            adapterViewHolder.vRect.setBackgroundColor(color);
            GradientDrawable shapeDrawable = (GradientDrawable) adapterViewHolder.vCarPicBack.getBackground();
            shapeDrawable.setColor(color);

            ArrayList<Maintenance> temp = mVehicleLogDBHelper.getFullVehicleEntries(vehicle.getRefID());
            if (!temp.isEmpty()) {
                adapterViewHolder.vLastLabel.setText(itemView.getResources().getString(R.string.last_event));
                adapterViewHolder.vEvent.setText(temp.get(temp.size()-1).getEvent());
                adapterViewHolder.vTitle.setSelected(true);

                if (useDist) {
                    adapterViewHolder.vOdo.setText(String.format(Locale.ENGLISH, "%1$,.1f %2$s",
                            Double.parseDouble(temp.get(temp.size() - 1).getOdometer()), UNIT_DIST));
                }else {
                    adapterViewHolder.vOdo.setText(String.format(Locale.ENGLISH, "%1$,.1f %2$s",
                            Double.parseDouble(temp.get(temp.size() - 1).getOdometer()), UNIT_TIME));
                }
            }
        }
    }

    private int determineLayout(){
        if(mRoster.isEmpty()) {
            if (recycler_i == 0) {
                recycler_i++;
                return R.layout.card_overview_empty;
            }else{
                return R.layout.card_overview_empty_dbx;
            }
        }else{
            return R.layout.card_overview;
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{

        protected ImageView vRect;
        protected ImageView vCarPic;
        protected ImageView vCarPicBack;
        protected TextView vTitle;
        protected TextView vLastLabel;
        protected TextView vEvent;
        protected TextView vOdo;

        public AdapterViewHolder(View view) {
            super(view);

            vRect = (ImageView) view.findViewById(R.id.rect);
            vCarPic = (ImageView) view.findViewById(R.id.carPic);
            vCarPicBack = (ImageView) view.findViewById(R.id.carPicBack);
            vTitle = (TextView) view.findViewById(R.id.vehicle_title);

            vLastLabel = (TextView) view.findViewById(R.id.last_label);
            vEvent = (TextView) view.findViewById(R.id.val_event);
            vOdo = (TextView) view.findViewById(R.id.val_odo);
        }
    }
}


