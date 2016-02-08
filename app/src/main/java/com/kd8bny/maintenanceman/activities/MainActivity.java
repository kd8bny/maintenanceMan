package com.kd8bny.maintenanceman.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.dialogs.dialog_donate;
import com.kd8bny.maintenanceman.fragments.fragment_overview;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "activity_main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);


        //Drawer
        final DrawerBuilder drawerBuilder = new DrawerBuilder(this);
        drawerBuilder.withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withCloseOnClick(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSelectedItem(-1)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.title_add_fleet_roster).withIcon(R.drawable.ic_action_add_fleet),
                        new PrimaryDrawerItem().withName(R.string.title_add_vehicle_event).withIcon(R.drawable.ic_action_add_event),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.title_settings),
                        new SecondaryDrawerItem().withName(R.string.title_donate),
                        new SecondaryDrawerItem().withName(R.string.drawer_view_community));

        final Drawer drawer = drawerBuilder.build();
        //drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        /*drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                Intent intent = new Intent(getActivity(), VehicleActivity.class);
                Bundle bundle = new Bundle();
                switch (i) {
                    case 1: //Add Vehicle
                        bundle.putInt("caseID", 0);
                        bundle.putParcelableArrayList("roster", roster);
                        bundle.putInt("vehiclePos", -1);
                        intent.putExtra("bundle", bundle);
                        view.getContext().startActivity(intent);
                        drawer.closeDrawer();

                        return true;

                    case 2: //Add Event
                        bundle.putInt("caseID", 1);
                        bundle.putParcelableArrayList("roster", roster);
                        intent.putExtra("bundle", bundle);
                        view.getContext().startActivity(intent);
                        drawer.closeDrawer();

                        return true;

                    case 4: //Settings
                        Intent settingsIntent = new Intent(view.getContext(), SettingsActivity.class);
                        view.getContext().startActivity(settingsIntent);
                        drawer.closeDrawer();

                        return true;

                    case 5: //Donate
                        FragmentManager fm = getFragmentManager();

                        dialog_donate dialog_donate = new dialog_donate();
                        dialog_donate.show(fm, "dialog_donate");
                        drawer.closeDrawer();

                        return true;

                    case 6: //Community
                        Uri gplus = Uri.parse("https://plus.google.com/u/0/communities/102216501931497148667");
                        Intent gplusIntent = new Intent(Intent.ACTION_VIEW, gplus);
                        startActivity(gplusIntent);
                        drawer.closeDrawer();

                        return true;
                }
                return false;
            }});*/



        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_main);

        if (fragment == null){
            fragment = new fragment_overview();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
