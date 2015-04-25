package com.kd8bny.maintenanceman.ui.info;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.data.vehicleLogDBHelper;

import java.util.ArrayList;

public class adapter_history extends RecyclerView.Adapter<adapter_history.AdapterViewHolder>{
    private static final String TAG = "adapter_history";

    public ArrayList<ArrayList> vehicleHist = new ArrayList<>();
    private View itemView;
    Resources res;
    Drawable drawable;
    int iconColor, iconColorError;

    public adapter_history(ArrayList vehicleList) {
        this.vehicleHist = vehicleList;
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

            final int pos = i;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Delete " + histEvent.get(3) + "on " + histEvent.get(1));
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            ArrayList<String> temp = vehicleHist.get(pos);
                            vehicleLogDBHelper vehicleDB = new vehicleLogDBHelper(view.getContext());
                            vehicleDB.deleteEntry(view.getContext(), temp.get(1), temp.get(2), temp.get(3));

                            vehicleHist.remove(pos);
                            notifyDataSetChanged();
                        }
                    });
                    builder.show();
                    return false;
                }

            });
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

        public AdapterViewHolder(View view) {
            super(view);

            vevent = (TextView) view.findViewById(R.id.event);
            vodo = (TextView) view.findViewById(R.id.val_spec_odo);
            vdate = (TextView) view.findViewById(R.id.date);
        }
    }
}


