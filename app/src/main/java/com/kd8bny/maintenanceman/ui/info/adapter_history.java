package com.kd8bny.maintenanceman.ui.info;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

public class adapter_history extends RecyclerView.Adapter<adapter_history.AdapterViewHolder>{
    private static final String TAG = "adptr_hstry";

    private ArrayList<ArrayList> vehicleHist = new ArrayList<>();
    private String [] vehicleTypes;
    private String type;
    private View itemView;
    private Resources res;
    private Drawable drawable;
    private int iconColor, iconColorError;

    public adapter_history(ArrayList vehicleList, String type) {
        this.vehicleHist = vehicleList;
        this.type = type;
    }

    @Override
    public int getItemCount() {
        return vehicleHist.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_list_hist, viewGroup, false);

        res = itemView.getResources();
        iconColor = res.getColor(R.color.primary_light);
        iconColorError = res.getColor(R.color.error);
        drawable = res.getDrawable(R.drawable.circle);
        vehicleTypes = res.getStringArray(R.array.vehicle_type);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder adapterViewHolder, int i) {
        final ArrayList<String> histEvent = new ArrayList<>();
        histEvent.addAll(vehicleHist.get(i));

        if (histEvent.get(0) != null) {
            drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
            (itemView.findViewById(R.id.circle)).setBackground(drawable);

            adapterViewHolder.vdate.setText(histEvent.get(1));
            adapterViewHolder.vodo.setText(histEvent.get(2));
            adapterViewHolder.vevent.setText(histEvent.get(3));
            if (type.equals(vehicleTypes[0]) | type.equals(vehicleTypes[1])){
                adapterViewHolder.vunit.setText(res.getString(R.string.unit_dist_us));
            }else {
                adapterViewHolder.vunit.setText(res.getString(R.string.unit_time));
            }
        }else {
            drawable.setColorFilter(iconColorError, PorterDuff.Mode.SRC_ATOP);
            (itemView.findViewById(R.id.circle)).setBackground(drawable);
            adapterViewHolder.vevent.setText(histEvent.get(1));
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vevent;
        protected TextView vodo;
        protected TextView vdate;
        protected TextView vunit;

        public AdapterViewHolder(View view) {
            super(view);

            vevent = (TextView) view.findViewById(R.id.val_spec_event);
            vodo = (TextView) view.findViewById(R.id.val_spec_odo);
            vdate = (TextView) view.findViewById(R.id.val_spec_date);
            vunit = (TextView) view.findViewById(R.id.val_spec_unit);
        }
    }
}


