package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
import com.kd8bny.maintenanceman.classes.utils.Utils;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Locale;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.AdapterViewHolder> {
    private static final String TAG = "adptr_hstry";

    private Context mContext;
    private View itemView;
    private TypedArray icons;
    private TypedArray headerColors;
    private Resources res;

    private ArrayList<Maintenance> mMaintenanceList;
    private String UNIT_DIST;

    public MaintenanceAdapter(Context context, Vehicle vehicle, ArrayList<Maintenance> maintenanceList) {
        mContext = context;
        UNIT_DIST = ""; //vehicle.getUnitDist();
        mMaintenanceList = maintenanceList;
    }

    @Override
    public int getItemCount() {
        if (mMaintenanceList.isEmpty()){
            return 1;
        }
        return mMaintenanceList.size();
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
       if(!mMaintenanceList.isEmpty()){
           int color = headerColors.getColor(i % 8, 0);
           viewHolder.vIconBackground.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

           Maintenance maintenance = mMaintenanceList.get(i);
           viewHolder.vIcon.setImageResource(icons.getResourceId(maintenance.getIcon(), 0));
           viewHolder.vdate.setText(new Utils(mContext).toFriendlyDate(new DateTime(maintenance.getDate())));
           viewHolder.vevent.setText(maintenance.getEvent());
           try {
               viewHolder.vodo.setText(String.format(Locale.ENGLISH, "%1$,.1f %2$s",
                       Double.parseDouble(maintenance.getOdometer()), UNIT_DIST));
           }catch (Exception e){
               Log.wtf(TAG, "parse error:" + maintenance.getOdometer());
               viewHolder.vodo.setText(maintenance.getOdometer());
           }
       }else{
           viewHolder.vevent.setText(mContext.getResources().getString(R.string.error_no_history));
           viewHolder.vIconBackground.getDrawable()
                   .setColorFilter(ContextCompat.getColor(mContext, R.color.error), PorterDuff.Mode.SRC_ATOP);
       }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "adptr_hstry";

        protected ImageView vIcon;
        protected ImageView vIconBackground;
        protected TextView vevent;
        protected TextView vodo;
        protected TextView vdate;

        public AdapterViewHolder(View view) {
            super(view);

            vIcon = (ImageView) view.findViewById(R.id.icon);
            vIconBackground = (ImageView) view.findViewById(R.id.circle);
            vevent = (TextView) view.findViewById(R.id.val_spec_event);
            vodo = (TextView) view.findViewById(R.id.val_spec_odo);
            vdate = (TextView) view.findViewById(R.id.val_spec_date);
        }
    }
}


