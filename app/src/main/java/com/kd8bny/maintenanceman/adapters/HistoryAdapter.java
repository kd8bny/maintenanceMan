package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.AdapterViewHolder> {
    private static final String TAG = "adptr_hstry";

    private Context mContext;
    private TypedArray icons;
    private TypedArray headerColors;
    private String[] vehicleTypes;
    private Resources res;

    private ArrayList<ArrayList> mVehicleHist = new ArrayList<>();
    private String mType;
    private String mUnit;
    private View itemView;

    public HistoryAdapter(Context context, ArrayList<ArrayList> vehicleList, String type, String unit) {
        mContext = context;
        mVehicleHist = vehicleList;
        mType = type;
        mUnit = unit;
    }

    @Override
    public int getItemCount() {
        if (mVehicleHist.isEmpty()){
            return 1;
        }
        return mVehicleHist.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        res = viewGroup.getResources();
        icons = res.obtainTypedArray(R.array.icon_event);
        headerColors = res.obtainTypedArray(R.array.header_color);
        vehicleTypes = res.getStringArray(R.array.vehicle_type);

        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_hist, viewGroup, false);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder viewHolder, int i) {
       if(!mVehicleHist.isEmpty()){
            ArrayList<String> histEvent = mVehicleHist.get(i);
            if (histEvent.get(0).isEmpty()) {
                viewHolder.vIcon.setImageResource(icons.getResourceId(0, 0));
            } else {
                viewHolder.vIcon.setImageResource(icons.getResourceId(Integer.parseInt(histEvent.get(0)), 0));
            }
            int color = headerColors.getColor(i % 8, 0);
            viewHolder.vIconBackground.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

            viewHolder.vdate.setText(histEvent.get(1));
            viewHolder.vodo.setText(histEvent.get(2));
            viewHolder.vevent.setText(histEvent.get(3));
            if (mType.equals(vehicleTypes[0]) | mType.equals(vehicleTypes[1]) | mType.equals(vehicleTypes[5])) {
                viewHolder.vunit.setText(mUnit);
            } else {
                viewHolder.vunit.setText(res.getString(R.string.unit_time));
            }
        }else{
           viewHolder.vevent.setText(mContext.getResources().getString(R.string.error_no_history));
           viewHolder.vIconBackground.getDrawable()
                   .setColorFilter(mContext.getResources().getColor(R.color.error), PorterDuff.Mode.SRC_ATOP);
       }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "adptr_hstry";

        protected ImageView vIcon;
        protected ImageView vIconBackground;
        protected TextView vevent;
        protected TextView vodo;
        protected TextView vdate;
        protected TextView vunit;

        public AdapterViewHolder(View view) {
            super(view);

            vIcon = (ImageView) view.findViewById(R.id.icon);
            vIconBackground = (ImageView) view.findViewById(R.id.circle);
            vevent = (TextView) view.findViewById(R.id.val_spec_event);
            vodo = (TextView) view.findViewById(R.id.val_spec_odo);
            vdate = (TextView) view.findViewById(R.id.val_spec_date);
            vunit = (TextView) view.findViewById(R.id.val_spec_unit);
        }
    }
}


