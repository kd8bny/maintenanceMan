package com.kd8bny.maintenanceman.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Business;

public class BusinessEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "adptr_vhcl_evnt";

    protected View itemViewData;
    private Business mBusiness;

    public BusinessEventAdapter(Business business) {
        mBusiness = business;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemViewData = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.card_add_vehicle_event, viewGroup, false);

        return new ViewHolderData(itemViewData, mBusiness, -1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        new ViewHolderData(itemViewData, mBusiness, i);
    }

    public static class ViewHolderData extends RecyclerView.ViewHolder {
        public ViewHolderData(View view, Business business, int pos) {
            super(view);

            TextView mValue = (TextView) view.findViewById(R.id.value);
            Resources mRes = view.getResources();

            switch (pos) {
                case 0:
                    mValue.setHint(mRes.getString(R.string.field_date));
                    mValue.setText(business.getDate());
                    break;
                case 1:
                    mValue.setHint(mRes.getString(R.string.field_start));
                    if (business.getStart() != null) {
                        mValue.setText(business.getStart().toString());
                    }
                    break;
                case 2:
                    mValue.setHint(mRes.getString(R.string.field_end));
                    if (business.getStop() != -1.0){
                        mValue.setText(business.getStop().toString());
                    }
                    break;
                case 3:
                    mValue.setHint(mRes.getString(R.string.field_dest));
                    mValue.setText(business.getDest());
                    break;
                case 4:
                    mValue.setHint(mRes.getString(R.string.field_purpose));
                    mValue.setText(business.getPurpose());
                    break;

                default:
                    break;

            }
        }
    }
}


