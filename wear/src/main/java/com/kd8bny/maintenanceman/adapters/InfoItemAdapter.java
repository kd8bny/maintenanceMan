package com.kd8bny.maintenanceman.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoItemAdapter extends RecyclerView.Adapter<InfoItemAdapter.AdapterViewHolder>{
    private static final String TAG = "adptr_inf_itm";

    private View itemView;

    private ArrayList<String> mLabels = new ArrayList<>();
    private ArrayList<String> mValues = new ArrayList<>();

    public InfoItemAdapter(HashMap<String, String> cardInfo) {
        for (String key : cardInfo.keySet()) {
            mLabels.add(key);
            mValues.add(cardInfo.get(key));
        }
    }

    @Override
    public int getItemCount() {
        if(mLabels.isEmpty()) {
            return 1;
        }

        return mLabels.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.info_item, viewGroup, false);

        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
        if(!mLabels.isEmpty()) {
            adapterViewHolder.vLabel.setText(mLabels.get(i));
            adapterViewHolder.vValue.setText(mValues.get(i));
            adapterViewHolder.vLabel.setSelected(true);
            adapterViewHolder.vValue.setSelected(true);
        }
    }


    public static class AdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView vLabel;
        private TextView vValue;

        public AdapterViewHolder(View view) {
            super(view);

            vLabel = (TextView) view.findViewById(R.id.label);
            vValue = (TextView) view.findViewById(R.id.value);
        }
    }
}


