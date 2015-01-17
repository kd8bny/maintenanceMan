package com.kd8bny.maintenanceman.ui.drawer;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.history.activity_history;
import com.kd8bny.maintenanceman.ui.settings.activity_settings;

import java.util.ArrayList;

public class adapter_drawer extends RecyclerView.Adapter<adapter_drawer.AdapterViewHolder>{
    private static final String TAG = "adapter_overview";

    public ArrayList<String> menuItems = new ArrayList<>();

    public adapter_drawer(ArrayList menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
            adapterViewHolder.vitemText.setText(menuItems.get(i));
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.drawer_item, viewGroup, false);
        View parent = (View) viewGroup.getParent();

        return new AdapterViewHolder(itemView, parent, menuItems);
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private DrawerLayout mDrawerLayout;
        protected TextView vitemText;

        public AdapterViewHolder(View view, View parent, final ArrayList<String> menuItems) {
            super(view);

            mDrawerLayout = (DrawerLayout) parent.findViewById(R.id.drawer_layout);
            view.setOnClickListener(this);

            vitemText = (TextView) view.findViewById(R.id.itemText);

        }

        @Override
        public void onClick(View view) {
            switch (getPosition()){
                case 0:
                    Intent addFleetIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                    view.getContext().startActivity(addFleetIntent);
                    mDrawerLayout.closeDrawers();
                    break;

                case 1:
                    Intent addEventIntent = new Intent(view.getContext(), activity_vehicleEvent.class);
                    view.getContext().startActivity(addEventIntent);
                    mDrawerLayout.closeDrawers();
                    break;

                case 2:
                    Intent settingsIntent = new Intent(view.getContext(), activity_settings.class);
                    view.getContext().startActivity(settingsIntent);
                    mDrawerLayout.closeDrawers();
                    break;

                case 3:
                    //Intent aboutIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                    //view.getContext().startActivity(aboutIntent);
                    Toast.makeText(view.getContext(),"Daryl Bennett",Toast.LENGTH_LONG).show();
                    mDrawerLayout.closeDrawers();
                    break;
        }

    }
    }
}


