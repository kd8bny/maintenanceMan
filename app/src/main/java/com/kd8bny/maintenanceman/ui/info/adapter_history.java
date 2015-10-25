package com.kd8bny.maintenanceman.ui.info;

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

public class adapter_history extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "adptr_hstry";

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_list_hist, viewGroup, false);

        return new AdapterViewHolder(itemView, null, mType, mUnit, i);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final ArrayList<String> histEvent = new ArrayList<>();
        histEvent.addAll(mVehicleHist.get(i));

        new AdapterViewHolder(itemView, histEvent, mType, mUnit, i);
    }
}

class AdapterViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "adptr_hstry";

    public AdapterViewHolder(View view, ArrayList<String> histEvent, String type, String unit, int positon) {
        super(view);

        TypedArray icons = view.getResources().obtainTypedArray(R.array.icon_event);
        TypedArray headerColors = view.getResources().obtainTypedArray(R.array.header_color);
        String [] vehicleTypes = view.getResources().getStringArray(R.array.vehicle_type);

        ImageView vIcon = (ImageView) view.findViewById(R.id.icon);
        ImageView vIconBackground = (ImageView) view.findViewById(R.id.circle);
        TextView vevent = (TextView) view.findViewById(R.id.val_spec_event);
        TextView vodo = (TextView) view.findViewById(R.id.val_spec_odo);
        TextView vdate = (TextView) view.findViewById(R.id.val_spec_date);
        TextView vunit = (TextView) view.findViewById(R.id.val_spec_unit);

        if (histEvent != null) {
            if (histEvent.get(0) != null) {
                int color = headerColors.getColor(positon % 8, 0);
                vIconBackground.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

                vIcon.setImageResource(icons.getResourceId(Integer.parseInt(histEvent.get(1)), 0));
                vdate.setText(histEvent.get(2));
                vodo.setText(histEvent.get(3));
                vevent.setText(histEvent.get(4));
                if (type.equals(vehicleTypes[0]) | type.equals(vehicleTypes[1]) | type.equals(vehicleTypes[5])) {
                    vunit.setText(unit);
                } else {
                    vunit.setText(view.getResources().getString(R.string.unit_time));
                }
            } else {
                vIconBackground.getDrawable().setColorFilter(view.getResources().getColor(R.color.error), PorterDuff.Mode.SRC_ATOP);

                vevent.setText(histEvent.get(2));
            }
        }
    }


}


