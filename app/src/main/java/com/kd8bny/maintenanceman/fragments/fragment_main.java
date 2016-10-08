package com.kd8bny.maintenanceman.fragments;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.kd8bny.maintenanceman.BuildConfig;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.activities.SettingsActivity;
import com.kd8bny.maintenanceman.activities.VehicleActivity;
import com.kd8bny.maintenanceman.activities.ViewPagerActivity;
import com.kd8bny.maintenanceman.adapters.OverviewAdapter;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.dialogs.dialog_addMaintenanceEvent;
import com.kd8bny.maintenanceman.dialogs.dialog_addMileageEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_addTravelEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_donate;
import com.kd8bny.maintenanceman.dialogs.dialog_sync;
import com.kd8bny.maintenanceman.interfaces.SyncData;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.dialogs.dialog_whatsNew;
import com.kd8bny.maintenanceman.activities.IntroActivity;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class fragment_main extends Fragment implements SyncData,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "frg_main";

    private final String SHARED_PREF = "com.kd8bny.maintenanceman_preferences";
    private static final String WEAR_FILE_PATH = "/files";

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;

    private SharedPreferences sharedPreferences;
    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;
    private RecyclerView.Adapter cardListAdapter;
    private FloatingActionButton fabBusiness;
    private dialog_sync mDialog_sync;

    private ArrayList<Vehicle> mRoster;
    private int mSortType = 0;

    public fragment_main() {}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();
        sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Toolbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        //Drawer
        final DrawerBuilder drawerBuilder = new DrawerBuilder(getActivity());
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
                        new PrimaryDrawerItem().withName(R.string.title_mileage_add).withIcon(R.drawable.ic_mileage_bk),
                        new PrimaryDrawerItem().withName(R.string.title_add_vehicle_event).withIcon(R.drawable.ic_action_add_event),
                        new PrimaryDrawerItem().withName(R.string.menu_add_business).withIcon(R.drawable.ic_speedo_blk),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.title_settings),
                        new SecondaryDrawerItem().withName(R.string.title_contact),
                        new SecondaryDrawerItem().withName(R.string.title_donate),
                        new SecondaryDrawerItem().withName(R.string.drawer_view_community));

        final Drawer drawer = drawerBuilder.build();
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                Intent intent = new Intent(getActivity(), VehicleActivity.class);
                Bundle bundle = new Bundle();
                switch (i) {
                    case 1: //Add Vehicle
                        bundle.putInt("caseID", 0);
                        bundle.putParcelableArrayList("roster", mRoster);
                        bundle.putInt("vehiclePos", -1);
                        intent.putExtra("bundle", bundle);
                        view.getContext().startActivity(intent);
                        drawer.closeDrawer();

                        return true;

                    case 2: //Add mileage
                        if (mRoster.isEmpty()){
                            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.empty_db),
                                    Snackbar.LENGTH_SHORT).show();
                        }else {
                            bundle.putParcelableArrayList("roster", mRoster);
                            dialog_addMileageEntry dialog = new dialog_addMileageEntry();
                            dialog.setTargetFragment(fragment_main.this, 2);
                            dialog.setArguments(bundle);
                            dialog.show(fm, "dialog_add_mileage");
                        }
                        drawer.closeDrawer();

                        return true;

                    case 3: //Add Event
                        if (mRoster.isEmpty()){
                            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.empty_db),
                                    Snackbar.LENGTH_SHORT).show();
                        }else {
                            bundle.putParcelableArrayList("roster", mRoster);
                            dialog_addMaintenanceEvent dialog = new dialog_addMaintenanceEvent();
                            dialog.setTargetFragment(fragment_main.this, 1);
                            dialog.setArguments(bundle);
                            dialog.show(fm, "dialog_add_maintenance");
                        }
                        drawer.closeDrawer();

                        return true;

                    case 4: //Travel Log
                        if (mRoster.isEmpty()){
                            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.empty_db),
                                    Snackbar.LENGTH_SHORT).show();
                        }else {
                            bundle.putParcelableArrayList("roster", mRoster);
                            dialog_addTravelEntry dialog = new dialog_addTravelEntry();
                            dialog.setTargetFragment(fragment_main.this, 3);
                            dialog.setArguments(bundle);
                            dialog.show(fm, "dialog_add_travel");
                        }
                        drawer.closeDrawer();

                        return true;

                    case 6: //Settings
                        Intent settingsIntent = new Intent(view.getContext(), SettingsActivity.class);
                        view.getContext().startActivity(settingsIntent);
                        drawer.closeDrawer();

                        return true;

                    case 7: //Contact Me
                        Intent contactIntent = new Intent(Intent.ACTION_SEND)
                                .setType("plain/text")
                                .putExtra(Intent.EXTRA_EMAIL, new String[]{ mContext.getString(R.string.to) })
                                .putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.subject))
                                .putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.body));

                        startActivity(Intent.createChooser(contactIntent, mContext.getString(R.string.action)));

                        return true;

                    case 8: //Donate
                        dialog_donate dialog_donate = new dialog_donate();
                        dialog_donate.show(fm, "dialog_donate");
                        drawer.closeDrawer();

                        return true;

                    case 9: //Community
                        Uri gplus = Uri.parse("https://plus.google.com/u/0/communities/102216501931497148667");
                        Intent gplusIntent = new Intent(Intent.ACTION_VIEW, gplus);
                        startActivity(gplusIntent);
                        drawer.closeDrawer();

                        return true;
                }
                return false;
            }});

        //cards
        cardList = (RecyclerView) view.findViewById(R.id.overview_cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setLayoutManager(cardMan);
        cardList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mContext, cardList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Bundle bundle = new Bundle();
                if (mRoster.isEmpty()) {
                    switch (pos){
                        case 0:
                            bundle.putInt("caseID", 0);
                            bundle.putInt("pos", -1);
                            view.getContext().startActivity(new Intent(getActivity(), VehicleActivity.class)
                                    .putExtra("bundle", bundle));
                            break;

                        case 1:
                            Intent settingsIntent = new Intent(view.getContext(), SettingsActivity.class);
                            view.getContext().startActivity(settingsIntent);
                            break;
                    }
                }else{
                    bundle.putParcelableArrayList("roster", mRoster);
                    bundle.putParcelable("vehicle", mRoster.get(pos));
                    bundle.putInt("pos", pos);
                    view.getContext().startActivity(new Intent(getActivity(), ViewPagerActivity.class)
                            .putExtra("bundle", bundle));
                }
            }

            @Override
            public void onItemLongClick(View view, int pos) {}}));

        //fab
        final FloatingActionMenu fabMenu = (FloatingActionMenu) view.findViewById(R.id.fabmenu);
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.setAnimated(true);

        view.findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRoster.isEmpty()){
                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.empty_db),
                            Snackbar.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("roster", mRoster);
                    dialog_addMaintenanceEvent dialog = new dialog_addMaintenanceEvent();
                    dialog.setTargetFragment(fragment_main.this, 1);
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "dialog_add_maintenance");
                    fabMenu.close(true);
                }
            }});

        view.findViewById(R.id.fab_add_mileage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRoster.isEmpty()){
                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.empty_db),
                            Snackbar.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("roster", mRoster);
                    dialog_addMileageEntry dialog = new dialog_addMileageEntry();
                    dialog.setTargetFragment(fragment_main.this, 2);
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "dialog_add_mileage");
                    fabMenu.close(true);
                }
            }});

        fabBusiness = (FloatingActionButton) view.findViewById(R.id.fab_add_business); //TODO clean this up
        fabBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRoster.isEmpty()){
                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.empty_db),
                            Snackbar.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("roster", mRoster);
                    dialog_addTravelEntry dialog = new dialog_addTravelEntry();
                    dialog.setTargetFragment(fragment_main.this, 3);
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "dialog_add_travel");
                    fabMenu.close(true);
                }
            }});

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (fabMenu.isOpened()) {
                        fabMenu.close(true);
                        return true;
                    }
                }
                return false;
            }});

        //Intro
        Boolean isFirstRun = sharedPreferences.getBoolean("firstRun", true);
        if (isFirstRun) {
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            Intent introIntent = new Intent(mContext, IntroActivity.class);
            view.getContext().startActivity(introIntent);
        }

        //Whats New!!!
        int oldAppVersion = sharedPreferences.getInt("appVersion", -1);
        if (BuildConfig.VERSION_CODE > oldAppVersion) {
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putInt("appVersion", BuildConfig.VERSION_CODE);
            editor.apply();

            FragmentManager fm = getFragmentManager();
            dialog_whatsNew dialog_whatsNew = new dialog_whatsNew();
            dialog_whatsNew.show(fm, "dialog_whatsNew");
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDialog_sync != null){
            mDialog_sync.dismiss();
        }
        mRoster = new SaveLoadHelper(mContext, this).load();
        if (mRoster != null) {
            cardListAdapter = new OverviewAdapter(mContext, mRoster);
            cardList.setAdapter(cardListAdapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        switch (resultCode){ //Mileage
            case 2:
                bundle = data.getBundleExtra("bundle");
                Mileage mileage = (Mileage) bundle.getSerializable("event");
                if (mileage != null) {
                    Snackbar.make(getActivity().findViewById(R.id.snackbar),
                            String.format(Locale.ENGLISH, "%1$s %2$.2f %3$s", getString(R.string.result_mileage),
                                    mileage.getMileage(), mRoster.get(bundle.getInt("pos")).getUnitMileage()),
                            Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch (menuitem.getItemId()) {
            case R.id.menu_sort:
                sort();
                return true;

            default:
                return false;
        }
    }

    private void sort(){
        ArrayList<String> values = new ArrayList<>();
        HashMap<String, Vehicle> sortedRoster = new HashMap<>();

        for (int i = 0; i < mRoster.size(); i++){
            Vehicle mVehicle = mRoster.get(i);
            String value;
            switch (mSortType){
                case 0:
                    value = String.format("%s:%s", mVehicle.getReservedSpecs().get("year"), i);
                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_sort_year), Snackbar.LENGTH_SHORT).show();

                    break;
                case 1:
                    value = String.format("%s:%s", mVehicle.getReservedSpecs().get("model"), i);
                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_sort_model), Snackbar.LENGTH_SHORT).show();
                    break;
                case 2:
                    value = String.format("%s:%s", mVehicle.getVehicleType(), i);
                    Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_sort_type), Snackbar.LENGTH_SHORT).show();
                    break;
                default:
                    Log.wtf(TAG, "No sort");
                    value = "";
                    break;
            }
            values.add(value);
            sortedRoster.put(value, mVehicle);
        }

        Collections.sort(values, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }});

        mRoster.clear();
        for (String year : values) {
            mRoster.add(sortedRoster.get(year));
        }

        mSortType = (mSortType + 1) % 3;
        cardListAdapter = new OverviewAdapter(mContext, mRoster);
        cardList.setAdapter(cardListAdapter);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WEAR_FILE_PATH);
                    DataMap dataMap = putDataMapRequest.getDataMap();
                    dataMap.putLong("time", System.currentTimeMillis());
                    dataMap.putString("roster", new Gson().toJson(mRoster));
                    Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest());
                } catch (Exception e){
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        thread.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    public void onDownloadComplete(Boolean isComplete){
        if (isComplete) {
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_update_ui), Snackbar.LENGTH_SHORT).show();
            onResume();
        }
    }

    public void onDownloadStart(){
        FragmentManager fm = getFragmentManager();
        mDialog_sync = new dialog_sync();
        mDialog_sync.show(fm, "dialog_sync");
    }
}
