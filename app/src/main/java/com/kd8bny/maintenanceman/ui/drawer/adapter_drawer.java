package com.kd8bny.maintenanceman.ui.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;

import java.util.ArrayList;

/**
 * Created by kd8bny on 1/13/15.
 * Used:
 * http://www.androidhive.info/2013/11/android-sliding-menu-using-navigation-drawer_item/
 */
public class adapter_drawer extends BaseAdapter {

    private Context context;
    private ArrayList<drawer_items> drawerItems;

    public adapter_drawer(Context context, ArrayList<drawer_items> drawerItems){
        this.context = context;
        this.drawerItems = drawerItems;
    }
    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        txtTitle.setText(drawerItems.get(position).getTitle());

        return convertView;
    }

}