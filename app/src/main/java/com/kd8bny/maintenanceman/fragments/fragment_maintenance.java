package com.kd8bny.maintenanceman.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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

import com.github.clans.fab.FloatingActionMenu;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.adapters.MaintenanceAdapter;
import com.kd8bny.maintenanceman.classes.utils.Utils;
import com.kd8bny.maintenanceman.classes.vehicle.Maintenance;
import com.kd8bny.maintenanceman.classes.data.VehicleLogDBHelper;
import com.kd8bny.maintenanceman.classes.utils.Export;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.dialogs.dialog_addMaintenanceEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_addMileageEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_addTravelEntry;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;
import com.kd8bny.maintenanceman.dialogs.dialog_maintenanceHistory;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class fragment_maintenance extends fragment_vehicleInfo {
    private static final String TAG = "frgmnt_hist";

    private View mView;
    private View vFilterView;
    private RecyclerView histList;
    private RecyclerView.LayoutManager histMan;

    private ArrayList<Maintenance> mVehicleHist;
    private ArrayList<Maintenance> mUnfilteredHist;
    private Boolean mSortDesc = true, mFilter = false;

    public fragment_maintenance() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        vFilterView = inflater.inflate(R.layout.dialog_filter, null);
        ((CoordinatorLayout) mView.findViewById(R.id.snackbar)).addView(vFilterView);
        vFilterView.setVisibility(View.INVISIBLE);
        registerForContextMenu(mView);

        //Task History
        histList = (RecyclerView) mView.findViewById(R.id.cardList);
        histMan = new LinearLayoutManager(getActivity());
        histList.setLayoutManager(histMan);
        histList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mContext, histList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (!mVehicleHist.isEmpty()) {
                    final Maintenance maintenance = mVehicleHist.get(pos);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("vehicle", mVehicle);
                    bundle.putSerializable("event", maintenance);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    dialog_maintenanceHistory dialog = new dialog_maintenanceHistory();
                    dialog.setTargetFragment(fragment_maintenance.this, 0);
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
                                    bundle.putParcelableArrayList("roster", mRoster);
                                    bundle.putInt("pos", mPos);
                                    bundle.putSerializable("event", maintenance);
                                    dialog_addMaintenanceEntry dialog = new dialog_addMaintenanceEntry();
                                    dialog.setTargetFragment(fragment_maintenance.this, 1);
                                    dialog.setArguments(bundle);
                                    dialog.show(getFragmentManager(), "dialog_add_maintenance");

                                    return true;

                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Delete Item?");
                                    builder.setMessage(String.format("%s completed on %s",
                                            maintenance.getEvent(),
                                            new Utils(getContext()).toFriendlyDate(new DateTime(maintenance.getDate()))));
                                    builder.setNegativeButton("No", null);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            VehicleLogDBHelper.getInstance(mContext).deleteEntry(maintenance);
                                            onResume(); //TODO
                                            //getTargetFragment().onActivityResult(getTargetRequestCode(), -1, new Intent());
                                        }
                                    }).show();

                                    return true;

                                default:
                                    return false;}}});
                        popupMenu.show();
                }}}));

        //fab
        final FloatingActionMenu fabMenu = (FloatingActionMenu) mView.findViewById(R.id.fabmenu);
        fabMenu.setClosedOnTouchOutside(true);
        fabMenu.setAnimated(true);

        mView.findViewById(R.id.fab_add_spec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_addField dialog_addField = new dialog_addField();
                dialog_addField.setTargetFragment(fragment_maintenance.this, 0);
                dialog_addField.show(getFragmentManager(), "dialog_add_field");
                fabMenu.close(true);
            }
        });

        mView.findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("roster", mRoster);
                bundle.putInt("pos", mPos);
                dialog_addMaintenanceEntry dialog = new dialog_addMaintenanceEntry();
                dialog.setTargetFragment(fragment_maintenance.this, 1);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialog_add_maintenance");
                fabMenu.close(true);
            }});

        mView.findViewById(R.id.fab_add_mileage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("roster", mRoster);
                bundle.putInt("pos", mPos);
                dialog_addMileageEntry dialog = new dialog_addMileageEntry();
                dialog.setTargetFragment(fragment_maintenance.this, 2);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialog_add_mileage");
                fabMenu.close(true);
            }});

        mView.findViewById(R.id.fab_add_business).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("roster", mRoster);
                bundle.putInt("pos", mPos);
                dialog_addTravelEntry dialog = new dialog_addTravelEntry();
                dialog.setTargetFragment(fragment_maintenance.this, 3);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialog_add_travel");
                fabMenu.close(true);
            }});

        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
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
                histList.setAdapter(new MaintenanceAdapter(mContext, mVehicle, mVehicleHist));
            }
        });
        (mView.findViewById(R.id.button_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFilterView.setVisibility(View.INVISIBLE);
            }
        });
        (mView.findViewById(R.id.button_not_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilter = !mFilter;
                tFilter.setText("");
                mVehicleHist = mUnfilteredHist;
                histList.setAdapter(new MaintenanceAdapter(mContext, mVehicle,mVehicleHist));
                vFilterView.setVisibility(View.INVISIBLE);
            }
        });

        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
        VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        mVehicleHist = vehicleLogDBHelper.getMaintenanceEntries(mVehicle.getRefID(), mSortDesc);
        histList.setAdapter(new MaintenanceAdapter(mContext, mVehicle, mVehicleHist));
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
                mSortDesc = !mSortDesc;
                String sort = (mSortDesc) ? "Descending" : "Ascending";
                Snackbar.make(getActivity().findViewById(R.id.snackbar),
                        String.format("Sort %s", sort), Snackbar.LENGTH_SHORT).show();
                onResume();

                return true;

            case R.id.menu_export_csv:
                Export export = new Export();
                Uri uri = export.maintenanceToCSV(mVehicle.getTitle(), mVehicleHist);

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

        //return sort(filterList);
        return filterList;
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
}
