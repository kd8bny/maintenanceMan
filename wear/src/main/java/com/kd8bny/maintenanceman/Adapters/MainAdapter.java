package com.kd8bny.maintenanceman.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kd8bny.maintenanceman.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

public class MainAdapter extends WearableListView.Adapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private ArrayList<Vehicle> mRoster = new ArrayList<>();

    public MainAdapter(Context context, ArrayList<Vehicle> roster) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mRoster = roster;
    }

    @Override
    public int getItemCount() {
        if (mRoster == null){
            return 0;
        }
        return mRoster.size();
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int i) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        holder.itemView.setTag(i);
        if(!mRoster.isEmpty()) {
        Vehicle vehicle = mRoster.get(i);
        switch (vehicle.getVehicleType()){
            case "Automobile":
                itemHolder.mImageView.setImageResource(R.drawable.np_car);
                break;

            case "Motorcycle":
                itemHolder.mImageView.setImageResource(R.drawable.np_motorcycle);
                break;

            case "Utility":
                itemHolder.mImageView.setImageResource(R.drawable.np_utility);
                break;

            case "Marine":
                itemHolder.mImageView.setImageResource(R.drawable.np_marine);
                break;

            case "Lawn and Garden":
                itemHolder.mImageView.setImageResource(R.drawable.np_tractor);
                break;

            case "Trailer":
                itemHolder.mImageView.setImageResource(R.drawable.np_trailer);
                break;
        }
            view.setText(vehicle.getTitle());
        }

        //adapterViewHolder.vRect.setBackgroundColor(color);
        //GradientDrawable shapeDrawable = (GradientDrawable) adapterViewHolder.vCarPicBack.getBackground();
        //shapeDrawable.setColor(color);
    }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        private ImageView mImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.name);
            mImageView = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}

