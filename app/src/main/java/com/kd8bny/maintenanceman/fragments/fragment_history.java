package com.kd8bny.maintenanceman.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.activities.VehicleActivity;
import com.kd8bny.maintenanceman.adapters.HistoryAdapter;
import com.kd8bny.maintenanceman.classes.data.SaveLoadHelper;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.utils.Export;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.dialogs.dialog_addMileageEntry;
import com.kd8bny.maintenanceman.interfaces.SyncFinished;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.dialogs.dialog_vehicleHistory;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class fragment_history extends Fragment implements SyncFinished {
    private static final String TAG = "frgmnt_hist";

    private View vFilterView;
    private RecyclerView histList;
    private RecyclerView.LayoutManager histMan;

    private Context mContext;

    private ArrayList<Vehicle> mRoster;
    private Vehicle vehicle;
    private int vehiclePos;
    private String refID;
    private ArrayList<Maintenance> mVehicleHist;
    private ArrayList<Maintenance> mUnfilteredHist;
    private int mSortType = 0;
    private Boolean mFilter = false;

    public fragment_history() {}

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
        vFilterView = inflater.inflate(R.layout.dialog_filter, null);
        ((RelativeLayout) view.findViewById(R.id.recycler_view)).addView(vFilterView);
        vFilterView.setVisibility(View.INVISIBLE);
        registerForContextMenu(view);

        //Task History
        histList = (RecyclerView) view.findViewById(R.id.cardList);
        histMan = new LinearLayoutManager(getActivity());
        histList.setLayoutManager(histMan);
        histList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mContext, histList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (!mVehicleHist.isEmpty()) {
                    final Maintenance maintenance = mVehicleHist.get(pos);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("event", maintenance);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    dialog_vehicleHistory dialog = new dialog_vehicleHistory();
                    dialog.setTargetFragment(fragment_history.this, 0);
                    dialog.setArguments(bundle);
                    dialog.show(fm, "dialog_vehicle_history");
                }
            }

            @Override
            public void onItemLongClick(final View view, int pos) {
                if (!mVehicleHist.isEmpty()) {
                    final Maintenance maintenance = mVehicleHist.get(pos);
                    PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu_history, popupMenu.getMenu());
                    //TODO vibrate

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_edit:
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("caseID", 1);
                                    bundle.putSerializable("event", maintenance);
                                    bundle.putParcelableArrayList("roster", mRoster);
                                    bundle.putInt("vehiclePos", vehiclePos);
                                    getActivity().startActivity(new Intent(getActivity(), VehicleActivity.class)
                                            .putExtra("bundle", bundle));

                                    return true;

                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Delete Item?");
                                    builder.setMessage(String.format("%s completed on %s",
                                            maintenance.getEvent(), maintenance.getDate()));
                                    builder.setNegativeButton("No", null);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            VehicleLogDBHelper vehicleDB = VehicleLogDBHelper.getInstance(mContext);
                                            vehicleDB.deleteEntry(maintenance);
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
                dialog_addField.setTargetFragment(fragment_history.this, 0);
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
                dialog.setTargetFragment(fragment_history.this, 0);
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

        //Filter dialog
        final MaterialAutoCompleteTextView tFilter = (MaterialAutoCompleteTextView) vFilterView.findViewById(R.id.met_filter);
        tFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    mVehicleHist = mUnfilteredHist;
                }else {
                    mVehicleHist = filter(s.toString());
                }
                histList.setAdapter(new HistoryAdapter(mContext, mVehicleHist));
            }
        });
        (view.findViewById(R.id.button_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFilterView.setVisibility(View.INVISIBLE);
            }
        });
        (view.findViewById(R.id.button_not_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilter = !mFilter;
                tFilter.setText("");
                mVehicleHist = mUnfilteredHist;
                histList.setAdapter(new HistoryAdapter(mContext, mVehicleHist));
                vFilterView.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        mVehicleHist = sort(vehicleLogDBHelper.getFullVehicleEntries(refID));
        histList.setAdapter(new HistoryAdapter(mContext, mVehicleHist));
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
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!mVehicleHist.isEmpty()){
            inflater.inflate(R.menu.menu_history, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch (menuitem.getItemId()) {
            case R.id.menu_search:
                mFilter = !mFilter;
                if (mFilter) {
                    showSearch(true);
                    mUnfilteredHist = mVehicleHist;
                }else{
                    showSearch(false);
                    mVehicleHist = mUnfilteredHist;
                }

                return true;

            case R.id.menu_sort:
                mSortType = (mSortType + 1) % 2;//TODO sanckz
                histList.setAdapter(new HistoryAdapter(mContext, sort(mVehicleHist)));

                return true;

            case R.id.menu_export_csv:
                Export export = new Export();
                Uri uri = export.maintenanceToCSV(vehicle.getTitle(), mVehicleHist);

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

    private ArrayList<Maintenance> sort(ArrayList<Maintenance> vehicleHist){
        ArrayList<String> dates = new ArrayList<>();

        HashMap<String, Maintenance> eventPackets = new HashMap<>();

        for (int i = 0; i < vehicleHist.size(); i++){
            Maintenance maintenance = vehicleHist.get(i);
            String date = String.format("%s:%s", maintenance.getDate(), i);
            dates.add(date);
            eventPackets.put(date, maintenance);
        }

        Collections.sort(dates, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("MM/dd/yyyy:HH");
            @Override
            public int compare(String o1, String o2) {
                try {
                    if (mSortType == 0) {
                        return f.parse(o2).compareTo(f.parse(o1));
                    }else{
                        return f.parse(o1).compareTo(f.parse(o2));
                    }
                }catch (ParseException e) {throw new IllegalArgumentException(e);}
            }});

        vehicleHist.clear();
        for (String date : dates) {
            vehicleHist.add(eventPackets.get(date));
        }

        return vehicleHist;
    }

    private ArrayList<Maintenance> filter(String EXPRESSION){
        ArrayList<Maintenance> filterList = new ArrayList<>();
        for (Maintenance m : mVehicleHist) {
            if (m.getEvent().contains(EXPRESSION)){
                filterList.add(m);
            }
        }

        if (filterList.isEmpty()){
            return mVehicleHist;
        }

        return sort(filterList);
    }

    private void showSearch(Boolean show){
        TranslateAnimation translateAnim;
        if(show){
            vFilterView.setVisibility(View.VISIBLE);
            translateAnim = new TranslateAnimation(0, 0, -500, 0);
            translateAnim.setDuration(500);
        }else{
            translateAnim = new TranslateAnimation(0, 0, 0, -500);
            translateAnim.setDuration(500);
            vFilterView.setVisibility(View.INVISIBLE);
        }
        vFilterView.startAnimation(translateAnim);
    }

    public void onDownloadComplete(Boolean isComplete){
        if (isComplete) {
            Snackbar.make(getActivity().findViewById(R.id.snackbar), getString(R.string.toast_update_ui), Snackbar.LENGTH_SHORT).show();
            onResume();
        }
    }
}
