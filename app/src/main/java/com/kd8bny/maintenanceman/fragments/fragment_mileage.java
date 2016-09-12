package com.kd8bny.maintenanceman.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.kd8bny.maintenanceman.adapters.MileageAdapter;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.utils.Export;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.dialogs.dialog_addMileageEntry;
import com.kd8bny.maintenanceman.interfaces.SyncFinished;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class fragment_mileage extends Fragment implements SyncFinished {
    private static final String TAG = "frgmnt_mileage";

    private RecyclerView mileageList;

    private Context mContext;

    private ArrayList<Vehicle> mRoster;
    private Vehicle vehicle;
    private int vehiclePos;
    private String refID;
    private ArrayList<Mileage> mMileageHist;

    public fragment_mileage() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        mRoster = bundle.getParcelableArrayList("roster");
        vehiclePos = bundle.getInt("vehiclePos", -1);
        vehicle = mRoster.get(vehiclePos);
        refID = vehicle.getRefID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mileageList = (RecyclerView) view.findViewById(R.id.cardList);
        mileageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mileageList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mContext, mileageList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (!mMileageHist.isEmpty()) { //TODO?

                }
            }

            @Override
            public void onItemLongClick(final View view, int pos) {
                if (!mMileageHist.isEmpty()) {
                    final Mileage mileage = mMileageHist.get(pos);
                    final int mileagePos = pos;
                    PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu_history, popupMenu.getMenu());
                    //TODO vibrate

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_edit:
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList("roster", mRoster);
                                    bundle.putSerializable("event", mileage);
                                    bundle.putInt("pos", mileagePos);
                                    android.support.v4.app.FragmentManager fm = getFragmentManager();
                                    dialog_addMileageEntry dialog = new dialog_addMileageEntry();
                                    dialog.setTargetFragment(fragment_mileage.this, 90);
                                    dialog.setArguments(bundle);
                                    dialog.show(fm, "dialog_add_mileage");

                                    return true;

                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Delete Item?");
                                    builder.setMessage(String.format(Locale.ENGLISH, "%1$,.2f completed on %2$s",
                                            mileage.getMileage(), mileage.getDate()));
                                    builder.setNegativeButton("No", null);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            VehicleLogDBHelper vehicleDB = VehicleLogDBHelper.getInstance(mContext);
                                            vehicleDB.deleteEntry(mileage);
                                            onResume();
                                        }
                                    }).show();

                                    return true;

                                default:
                                    return false;}}});
                        popupMenu.show();
                }}}));

        //fab
        final FloatingActionMenu fabMenu = (FloatingActionMenu) view.findViewById(R.id.fabmenu);
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.setAnimated(true);

        view.findViewById(R.id.fab_add_spec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_mileage.this, 0);
                dialog_addField.show(fm, "dialog_add_field");
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
                dialog.setTargetFragment(fragment_mileage.this, 0);
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
        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        mMileageHist = sort(vehicleLogDBHelper.getMileageEntries(refID));
        mileageList.setAdapter(new MileageAdapter(mContext, mMileageHist));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        VehicleLogDBHelper vehicleDB;
        switch (resultCode) {
            case (0): //Add
                final Calendar cal = java.util.Calendar.getInstance();
                String date = cal.get(java.util.Calendar.MONTH) + 1
                        + "/" + cal.get(java.util.Calendar.DAY_OF_MONTH)
                        + "/" + cal.get(java.util.Calendar.YEAR);

                Mileage mileage = new Mileage(mRoster.get(bundle.getInt("pos")).getRefID());
                mileage.setDate(date);
                mileage.setMileage(bundle.getDouble("trip"), bundle.getDouble("fill"), bundle.getDouble("price"));

                vehicleDB = new VehicleLogDBHelper(this.getActivity());
                vehicleDB.insertEntry(mileage);

                new SaveLoadHelper(mContext, this).save(mRoster);
                onResume();
                break;

            case (1):
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

            case (90): //Edit
                vehicleDB = new VehicleLogDBHelper(this.getActivity());
                vehicleDB.deleteEntry((Mileage) bundle.getSerializable("event"));
                final Calendar cal_90 = java.util.Calendar.getInstance();
                String date_90 = cal_90.get(java.util.Calendar.MONTH) + 1
                        + "/" + cal_90.get(java.util.Calendar.DAY_OF_MONTH)
                        + "/" + cal_90.get(java.util.Calendar.YEAR);

                Mileage mileage_90 = new Mileage(mRoster.get(bundle.getInt("pos")).getRefID());
                mileage_90.setDate(date_90);
                mileage_90.setMileage(bundle.getDouble("trip"), bundle.getDouble("fill"), bundle.getDouble("price"));

                vehicleDB.insertEntry(mileage_90);

                new SaveLoadHelper(mContext, this).save(mRoster);
                onResume();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!mMileageHist.isEmpty()){
            inflater.inflate(R.menu.menu_mileage, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch (menuitem.getItemId()) {
            case R.id.menu_export_csv:
                Export export = new Export();
                Uri uri = export.mileageToCSV(vehicle.getTitle(), mMileageHist);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sendIntent.setType("text/csv");
                startActivity(Intent.createChooser(sendIntent, mContext.getResources().getString(R.string.share)));

                return true;

            default:
                return false;
        }
    }

    private ArrayList<Mileage> sort(ArrayList<Mileage> mileageHist){
        ArrayList<String> dates = new ArrayList<>();

        HashMap<String, Mileage> eventPackets = new HashMap<>();

        for (int i = 0; i < mileageHist.size(); i++){
            Mileage mileage = mileageHist.get(i);
            String date = String.format("%s:%s", mileage.getDate(), i);
            dates.add(date);
            eventPackets.put(date, mileage);
        }

        Collections.sort(dates, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("MM/dd/yyyy:HH");
            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o2).compareTo(f.parse(o1));
                }catch (ParseException e) {throw new IllegalArgumentException(e);}
            }});

        mileageHist.clear();
        for (String date : dates) {
            mileageHist.add(eventPackets.get(date));
        }

        return mileageHist;
    }

    public void onDownloadComplete(Boolean isComplete){
        if (isComplete) {
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_update_ui), Snackbar.LENGTH_SHORT).show();
            onResume();
        }
    }
}
