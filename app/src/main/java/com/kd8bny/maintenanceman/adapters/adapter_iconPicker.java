package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.kd8bny.maintenanceman.R;

public class adapter_iconPicker extends BaseAdapter {
    private static final String TAG = "adptr_icnPckr";

    private Context mContext;
    private TypedArray icons;

    public adapter_iconPicker(Context context) {
        mContext = context;
        icons = mContext.getResources().obtainTypedArray(R.array.icon_event);
    }

    @Override
    public int getCount() {

        return mContext.getResources().getStringArray(R.array.icon_event).length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setColorFilter(Color.argb(255, 255, 255, 255));

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(icons.getResourceId(position, -1));

        return imageView;
    }
}