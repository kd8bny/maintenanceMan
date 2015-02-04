package com.kd8bny.maintenanceman.ui.drawer;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
//import com.kd8bny.maintenanceman.billing.activity_billing;
import com.kd8bny.maintenanceman.ui.add.activity_add_fleetRoster;
import com.kd8bny.maintenanceman.ui.add.activity_vehicleEvent;
import com.kd8bny.maintenanceman.ui.dialogs.dialog_donate;
import com.kd8bny.maintenanceman.ui.settings.activity_settings;

import java.util.ArrayList;

public class adapter_drawer extends RecyclerView.Adapter<adapter_drawer.AdapterViewHolder>{
    private static final String TAG = "adapter_overview";

    public ArrayList<String> menuItems = new ArrayList<>();
    private int [] icons;

    public adapter_drawer(ArrayList menuItems, int [] icons) {
        this.menuItems = menuItems;
        this.icons = icons;
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int i) {
            adapterViewHolder.vitemText.setText(menuItems.get(i));
            adapterViewHolder.vitemIcon.setImageResource(icons[i]);
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
        protected ImageView vitemIcon;

        public AdapterViewHolder(View view, View parent, final ArrayList<String> menuItems) {
            super(view);

            mDrawerLayout = (DrawerLayout) parent.findViewById(R.id.drawer_layout);
            view.setOnClickListener(this);

            vitemText = (TextView) view.findViewById(R.id.itemText);
            vitemIcon = (ImageView) view.findViewById(R.id.itemIcon);

        }

        @Override
        public void onClick(View view) {
            switch (getPosition()){
                case 0: //Add Vehicle
                    Intent addFleetIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                    view.getContext().startActivity(addFleetIntent);
                    mDrawerLayout.closeDrawers();
                    break;

                case 1: //Add Event
                    Intent addEventIntent = new Intent(view.getContext(), activity_vehicleEvent.class);
                    view.getContext().startActivity(addEventIntent);
                    mDrawerLayout.closeDrawers();
                    break;

                /*case 2: //Settings
                    Intent settingsIntent = new Intent(view.getContext(), activity_settings.class);
                    view.getContext().startActivity(settingsIntent);
                    mDrawerLayout.closeDrawers();
                    break;*/

                case 2: //Donate
                    FragmentManager fm = ((FragmentActivity) view.getContext()).getFragmentManager();

                    dialog_donate dialog_donate = new dialog_donate();
                    dialog_donate.show(fm, "dialog_donate");
                    mDrawerLayout.closeDrawers();
                    break;

                /*case 4:
                    //Intent aboutIntent = new Intent(view.getContext(), activity_add_fleetRoster.class);
                    //view.getContext().startActivity(aboutIntent);
                    Toast.makeText(view.getContext(),"Daryl Bennett",Toast.LENGTH_LONG).show();
                    mDrawerLayout.closeDrawers();
                    break;*/
        }

    }
    }
}


