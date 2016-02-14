package com.kd8bny.maintenanceman.adapters;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Event;

public class VehicleEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "adptr_vhcl_evnt";

    private Event mEvent;

    protected View itemViewIcon;
    protected View itemViewData;

    private static final int VIEW_ICON = 0;
    private static final int VIEW_DATA = 1;

    public VehicleEventAdapter(Event event) {
        mEvent = event;
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
        return 6;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (getItemViewType(i)) {
            case VIEW_ICON:
                itemViewIcon = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_add_vehicle_icon, viewGroup, false);

                return new ViewHolderIcon(itemViewIcon, 0);

            default:
                itemViewData = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.card_add_vehicle_event, viewGroup, false);

                return new ViewHolderData(itemViewData, mEvent, -1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (getItemViewType(i)) {
            case VIEW_ICON:
                new ViewHolderIcon(itemViewIcon, mEvent.getIcon());
                break;
            default:
                new ViewHolderData(itemViewData, mEvent, i);
                break;
        }
    }


    public static class ViewHolderIcon extends RecyclerView.ViewHolder {
        public ViewHolderIcon(View view, int icon) {
            super(view);
            TypedArray icons = view.getResources().obtainTypedArray(R.array.icon_event);
            ImageView imageView = (ImageView) view.findViewById(R.id.eventIcon);
            imageView.setImageResource(icons.getResourceId(icon, 0));
            icons.recycle();
        }
    }

    public static class ViewHolderData extends RecyclerView.ViewHolder {
        public ViewHolderData(View view, Event event, int pos) {
            super(view);

            TextView mValue = (TextView) view.findViewById(R.id.value);
            Resources mRes = view.getResources();

            switch (pos) {
                case 1:
                    mValue.setHint(mRes.getString(R.string.field_date));
                    mValue.setText(event.getDate());
                    break;
                case 2:
                    mValue.setHint(mRes.getString(R.string.field_odo));
                    mValue.setText(event.getOdometer());
                    break;
                case 3:
                    mValue.setHint(mRes.getString(R.string.field_event));
                    mValue.setText(event.getEvent());
                    break;
                case 4:
                    mValue.setHint(mRes.getString(R.string.field_price));
                    mValue.setText(event.getPrice());
                    break;
                case 5:
                    mValue.setHint(mRes.getString(R.string.field_comment));
                    mValue.setText(event.getComment());
                    break;

                default:
                    break;

            }
        }
    }
}


