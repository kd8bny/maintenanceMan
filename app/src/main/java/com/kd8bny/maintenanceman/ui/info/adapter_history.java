package com.kd8bny.maintenanceman.ui.info;

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

public class adapter_history extends RecyclerView.Adapter<adapter_history.AdapterViewHolder> {
    private static final String TAG = "adptr_hstry";

    private TypedArray icons;
    private TypedArray headerColors;
    private String[] vehicleTypes;
    private Resources res;

    private ArrayList<ArrayList> mVehicleHist = new ArrayList<>();
    private String mType;
    private String mUnit;
    private View itemView;

    public adapter_history(ArrayList vehicleList, String type, String unit) {
        mVehicleHist = vehicleList;
        mType = type;
        mUnit = unit;
    }

    @Override
    public int getItemCount() {
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
                .inflate(R.layout.item_list_hist, viewGroup, false);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder viewHolder, int i) {
        final ArrayList<String> histEvent = new ArrayList<>();
        histEvent.addAll(mVehicleHist.get(i));

        if (histEvent != null) {
            if (histEvent.get(0) != null) {
                if (histEvent.get(1).isEmpty()) {
                    viewHolder.vIcon.setImageResource(icons.getResourceId(0, 0));
                } else {
                    viewHolder.vIcon.setImageResource(icons.getResourceId(Integer.parseInt(histEvent.get(1)), 0));
                }
                int color = headerColors.getColor(i % 8, 0);
                viewHolder.vIconBackground.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

                viewHolder.vdate.setText(histEvent.get(2));
                viewHolder.vodo.setText(histEvent.get(3));
                viewHolder.vevent.setText(histEvent.get(4));
                if (mType.equals(vehicleTypes[0]) | mType.equals(vehicleTypes[1]) | mType.equals(vehicleTypes[5])) {
                    viewHolder.vunit.setText(mUnit);
                } else {
                    viewHolder.vunit.setText(res.getString(R.string.unit_time));
                }
            } else {
                viewHolder.vIconBackground.getDrawable().setColorFilter(res.getColor(R.color.error), PorterDuff.Mode.SRC_ATOP);

                viewHolder.vevent.setText(histEvent.get(2));
            }
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


