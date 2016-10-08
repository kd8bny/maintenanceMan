package com.kd8bny.maintenanceman.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Travel;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.dialogs.dialog_addMaintenanceEvent;
import com.kd8bny.maintenanceman.dialogs.dialog_addMileageEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_addTravelEntry;
import com.kd8bny.maintenanceman.interfaces.SyncData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class fragment_vehicleInfo extends Fragment implements SyncData {
    private static final String TAG = "frgmnt_inf";

    private Context mContext;
    private ArrayList<Vehicle> mRoster;
    private int mPos;
    private Vehicle mVehicle;

    public fragment_vehicleInfo() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        mRoster = bundle.getParcelableArrayList("roster");
        mPos = bundle.getInt("pos", -1);
        mVehicle = mRoster.get(mPos);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        switch (resultCode) {
            case (0):
                if (data != null) {
                    bundle = data.getBundleExtra("bundle");
                    ArrayList<String> result = bundle.getStringArrayList("fieldData");
                    HashMap<String, String> temp;
                    switch (result.get(0)) {
                        case "General":
                            temp = mVehicle.getGeneralSpecs();
                            temp.put(result.get(1), result.get(2));
                            mVehicle.setGeneralSpecs(temp);
                            break;
                        case "Engine":
                            temp = mVehicle.getEngineSpecs();
                            temp.put(result.get(1), result.get(2));
                            mVehicle.setEngineSpecs(temp);
                            break;
                        case "Power Train":
                            temp = mVehicle.getPowerTrainSpecs();
                            temp.put(result.get(1), result.get(2));
                            mVehicle.setPowerTrainSpecs(temp);
                            break;
                        case "Other":
                            temp = mVehicle.getOtherSpecs();
                            temp.put(result.get(1), result.get(2));
                            mVehicle.setOtherSpecs(temp);
                            break;
                    }
                    mRoster.set(mPos, mVehicle);
                    new SaveLoadHelper(mContext, this).save(mRoster);
                }
                break;

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

            case (90)://Saved
                mRoster = new SaveLoadHelper(mContext, this).load();
                mVehicle = mRoster.get(mPos);
                break;

            case (91)://Delete mVehicle
                getActivity().finish();
                break;

            default:
                break;
        }
            onResume();
    }

    public void onDownloadComplete(Boolean isComplete){
        if (isComplete) {
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_update_ui), Snackbar.LENGTH_SHORT).show();
            onResume();
        }
    }

    public void onDownloadStart(){
    }
}
