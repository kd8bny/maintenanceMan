package com.kd8bny.maintenanceman.ui.drawer;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.ui.history.activity_history;

import java.util.ArrayList;

public class adapter_drawer extends RecyclerView.Adapter<adapter_drawer.AdapterViewHolder>{
    private static final String TAG = "adapter_overview";

    public ArrayList<String> menuItems = new ArrayList<String>();

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
        return new AdapterViewHolder(itemView, menuItems);
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView vitemText;

        public AdapterViewHolder(View view, final ArrayList<String> menuItems) {
            super(view);

            view.setTag(menuItems);
            view.setOnClickListener(this);

            vitemText = (TextView) view.findViewById(R.id.itemText);

        }

        @Override
        public void onClick(View view) {
            ArrayList<ArrayList> vehicleList = (ArrayList<ArrayList>) view.getTag();

            Intent viewIntent = new Intent(view.getContext(), activity_history.class);
            viewIntent.putStringArrayListExtra("vehicleSent", vehicleList.get(getPosition()));
            view.getContext().startActivity(viewIntent);
        }

}
}


