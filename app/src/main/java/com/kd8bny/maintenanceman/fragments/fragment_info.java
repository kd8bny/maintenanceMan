package com.kd8bny.maintenanceman.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.activities.VehicleActivity;
import com.kd8bny.maintenanceman.adapters.InfoAdapter;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.dialogs.dialog_addMileageEntry;
import com.kd8bny.maintenanceman.interfaces.SyncFinished;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class fragment_info extends Fragment implements SyncFinished {
    private static final String TAG = "frgmnt_inf";

    private RecyclerView cardList;
    private RecyclerView.LayoutManager cardMan;

    private Context mContext;
    private ArrayList<Vehicle> mRoster;
    private int vehiclePos;
    private Vehicle vehicle;
    private ArrayList<Maintenance> mVehicleHist;
    private ArrayList<Mileage> mMileage;

    public fragment_info() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        mRoster = bundle.getParcelableArrayList("roster");
        vehiclePos = bundle.getInt("vehiclePos", -1);
        vehicle = mRoster.get(vehiclePos);

        VehicleLogDBHelper vehicleDB = new VehicleLogDBHelper(this.getActivity());

        final Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.MONTH) + 1
                + "/" + cal.get(Calendar.DAY_OF_MONTH)
                + "/" + cal.get(Calendar.YEAR);

        mVehicleHist = vehicleDB.getCostByYear(vehicle.getRefID(), date);
        mMileage = vehicleDB.getMileageEntriesByYear(vehicle.getRefID(), date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        //Info Cards
        cardList = (RecyclerView) view.findViewById(R.id.cardList);
        cardMan = new LinearLayoutManager(getActivity());
        cardList.setLayoutManager(cardMan);

        //fab
        final FloatingActionMenu fabMenu = (FloatingActionMenu) view.findViewById(R.id.fabmenu);
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.setAnimated(true);

        view.findViewById(R.id.fab_add_spec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_info.this, 0);
                dialog_addField.show(fm, "dialog_add_field");
                fabMenu.close(true);
            }
        });

        view.findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("caseID", 1);
                bundle.putParcelableArrayList("roster", mRoster);
                startActivity(new Intent(getActivity(), VehicleActivity.class)
                        .putExtra("bundle", bundle));
                fabMenu.close(true);
            }});

        view.findViewById(R.id.fab_add_mileage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("roster", mRoster);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                dialog_addMileageEntry dialog = new dialog_addMileageEntry();
                dialog.setTargetFragment(fragment_info.this, 0);
                dialog.setArguments(bundle);
                dialog.show(fm, "dialog_add_mileage");
                fabMenu.close(true);
            }});

        view.findViewById(R.id.fab_add_business).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("caseID", 4);
                bundle.putParcelableArrayList("roster", mRoster);
                startActivity(new Intent(getActivity(), VehicleActivity.class)
                        .putExtra("bundle", bundle));
                fabMenu.close(true);
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

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        cardList.setAdapter(new InfoAdapter(vehicle, mVehicleHist, mMileage));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        switch (resultCode) {
            case (0):
                bundle = data.getBundleExtra("bundle");
                final Calendar cal = java.util.Calendar.getInstance();
                String date = cal.get(java.util.Calendar.MONTH) + 1
                        + "/" + cal.get(java.util.Calendar.DAY_OF_MONTH)
                        + "/" + cal.get(java.util.Calendar.YEAR);

                Mileage mileage = new Mileage(mRoster.get(bundle.getInt("pos")).getRefID());
                mileage.setDate(date);
                mileage.setMileage(bundle.getDouble("trip"), bundle.getDouble("fill"), bundle.getDouble("price"));

                VehicleLogDBHelper vehicleDB = new VehicleLogDBHelper(this.getActivity());
                vehicleDB.insertEntry(mileage);

                new SaveLoadHelper(mContext, this).save(mRoster);
                break;

            case (1):
                bundle = data.getBundleExtra("bundle");
                ArrayList<String> result = bundle.getStringArrayList("fieldData");
                HashMap<String, String> temp;
                switch (result.get(0)) {
                    case "General":
                        temp = vehicle.getGeneralSpecs();
                        temp.put(result.get(1), result.get(2));
                        vehicle.setGeneralSpecs(temp);
                        break;
                    case "Engine":
                        temp = vehicle.getEngineSpecs();
                        temp.put(result.get(1), result.get(2));
                        vehicle.setEngineSpecs(temp);
                        break;
                    case "Power Train":
                        temp = vehicle.getPowerTrainSpecs();
                        temp.put(result.get(1), result.get(2));
                        vehicle.setPowerTrainSpecs(temp);
                        break;
                    case "Other":
                        temp = vehicle.getOtherSpecs();
                        temp.put(result.get(1), result.get(2));
                        vehicle.setOtherSpecs(temp);
                        break;
                }
                mRoster.set(vehiclePos, vehicle);
                new SaveLoadHelper(mContext, this).save(mRoster);
                break;

            case(90)://Saved
                mRoster = new SaveLoadHelper(mContext, this).load();
                vehicle = mRoster.get(vehiclePos);
                break;

            case(91)://Delete vehicle
                getActivity().finish();
                break;

            default:
                break;
        }

        onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch (menuitem.getItemId()) {
            case R.id.menu_edit:
                Bundle bundle = new Bundle();
                bundle.putInt("caseID", 0);
                bundle.putInt("vehiclePos", vehiclePos);
                bundle.putParcelableArrayList("roster", mRoster);
                startActivityForResult(new Intent(getActivity(),
                        VehicleActivity.class).putExtra("bundle", bundle), 90);

                return true;

            default:
                return false;
        }
    }

    public void onDownloadComplete(Boolean isComplete){
        if (isComplete) {
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_update_ui), Snackbar.LENGTH_SHORT).show();
            onResume();
        }
    }
}
