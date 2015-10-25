package com.kd8bny.maintenanceman.ui.add;

import android.content.res.TypedArray;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_add_vehicleEvent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "adptr_add_vhcl_evnt";

    private HashMap<String, String> dataSet = new HashMap<>();
    private ArrayList<String> fields;
    private ArrayList<String> values = new ArrayList<>();
    private TypedArray headerColors;

    protected View itemViewIcon;
    protected View itemViewData;

    private static final int VIEW_ICON = 0;
    private static final int VIEW_DATA = 1;

    public adapter_add_vehicleEvent(HashMap<String, String> dataSet, ArrayList<String> fields) {
        this.dataSet = dataSet;
        this.fields = fields;
        for (String key : dataSet.keySet()) {
            this.values.add(dataSet.get(key));
        }
    }

    @Override
    public int getItemViewType(int i) {
        if(i == 0) {
            return VIEW_ICON;
        }else {
            return VIEW_DATA;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        headerColors = viewGroup.getResources().obtainTypedArray(R.array.header_color);
        switch (getItemViewType(i)) {
            case VIEW_ICON:
                itemViewIcon = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_add_vehicle_icon, viewGroup, false);

                return new ViewHolderIcon(itemViewIcon, "0");

            default:
                itemViewData = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_add_vehicle_event, viewGroup, false);

                return new ViewHolderData(itemViewData, fields, values, -1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (getItemViewType(i)) {
            case VIEW_ICON:
                new ViewHolderIcon(itemViewIcon, dataSet.get("icon"));

                break;

            default:
                new ViewHolderData(itemViewData, fields, values, i);

                break;

        }
    }


   public static class ViewHolderIcon extends RecyclerView.ViewHolder {
        public ViewHolderIcon(View view, String position) {
            super(view);
            if (position.isEmpty()){
                position = "0";
            }
            TypedArray icons = view.getContext().getResources().obtainTypedArray(R.array.icon_event);
            ImageView imageView = (ImageView) view.findViewById(R.id.eventIcon);
            imageView.setImageResource(icons.getResourceId(Integer.parseInt(position), 0));
            icons.recycle();
        }
    }

    public static class ViewHolderData extends RecyclerView.ViewHolder {
        public ViewHolderData(View view, ArrayList<String> fields, ArrayList<String> values, int i) {
            super(view);

            if (i > -1) {
                TextView mvalue = (TextView) view.findViewById(R.id.value);

                mvalue.setHint(fields.get(i));
                mvalue.setText(values.get(i));
            }
        }
    }
}


