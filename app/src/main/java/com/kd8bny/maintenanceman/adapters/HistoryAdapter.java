package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Event;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.AdapterViewHolder> {
    private static final String TAG = "adptr_hstry";

    private Context mContext;
    private TypedArray icons;
    private TypedArray headerColors;
    private Resources res;

    private ArrayList<Event> mEventList;
    private View itemView;

    public HistoryAdapter(Context context, ArrayList<Event> eventList) {
        mContext = context;
        mEventList = eventList;
    }

    @Override
    public int getItemCount() {
        if (mEventList.isEmpty()){
            return 1;
        }
        return mEventList.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        res = viewGroup.getResources();
        icons = res.obtainTypedArray(R.array.icon_event);
        headerColors = res.obtainTypedArray(R.array.header_color);
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_hist, viewGroup, false);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder viewHolder, int i) {
       if(!mEventList.isEmpty()){
            int color = headerColors.getColor(i % 8, 0);
            viewHolder.vIconBackground.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

            Event event = mEventList.get(i);
            viewHolder.vIcon.setImageResource(icons.getResourceId(event.getIcon(), 0));
            viewHolder.vdate.setText(event.getDate());
            viewHolder.vodo.setText(event.getOdometer());
            viewHolder.vevent.setText(event.getEvent());
            viewHolder.vunit.setText(event.getUnit());
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

