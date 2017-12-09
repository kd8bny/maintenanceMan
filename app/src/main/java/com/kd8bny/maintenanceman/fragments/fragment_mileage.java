package com.kd8bny.maintenanceman.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
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
import com.kd8bny.maintenanceman.adapters.MileageAdapter;
import com.kd8bny.maintenanceman.classes.utils.Export;
import com.kd8bny.maintenanceman.classes.utils.Utils;
import com.kd8bny.maintenanceman.classes.vehicle.Mileage;
import com.kd8bny.maintenanceman.dialogs.dialog_addField;
import com.kd8bny.maintenanceman.dialogs.dialog_addMaintenanceEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_addMileageEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_addTravelEntry;
import com.kd8bny.maintenanceman.dialogs.dialog_mileageHistory;
import com.kd8bny.maintenanceman.listeners.RecyclerViewOnItemClickListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Locale;

public class fragment_mileage extends fragment_vehicleInfo {
    private static final String TAG = "frgmnt_mileage";

    private View mView;
    private RecyclerView mileageList;

    private ArrayList<Mileage> mMileageHist;

    public fragment_mileage() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mileageList = (RecyclerView) mView.findViewById(R.id.cardList);
        mileageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mileageList.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mContext, mileageList,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (!mMileageHist.isEmpty()) {
                    final Mileage mileage = mMileageHist.get(pos);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("vehicle", mVehicle);
                    bundle.putSerializable("event", mileage);
                    bundle.putSerializable("eventList", mMileageHist);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    dialog_mileageHistory dialog = new dialog_mileageHistory();
                    dialog.setTargetFragment(fragment_mileage.this, 0);
                    dialog.setArguments(bundle);
                    dialog.show(fm, "dialog_mileage_history");
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
                                    bundle.putInt("pos", mileagePos);
                                    bundle.putSerializable("event", mileage);

                                    dialog_addMileageEntry dialog = new dialog_addMileageEntry();
                                    dialog.setTargetFragment(fragment_mileage.this, 90);
                                    dialog.setArguments(bundle);
                                    dialog.show(getFragmentManager(), "dialog_add_mileage");

                                    return true;

                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Delete Item?");
                                    builder.setMessage(String.format(Locale.ENGLISH, "%1$,.2f completed on %2$s",
                                            mileage.getMileage(),
                                            new Utils(getContext()).toFriendlyDate(new DateTime(mileage.getDate()))));
                                    builder.setNegativeButton("No", null);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            //VehicleLogDBHelper.getInstance(mContext).deleteEntry(mileage);
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
                dialog_addField.setTargetFragment(fragment_mileage.this, 0);
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
                dialog.setTargetFragment(fragment_mileage.this, 1);
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
                dialog.setTargetFragment(fragment_mileage.this, 2);
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
                dialog.setTargetFragment(fragment_mileage.this, 3);
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

        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
        //VehicleLogDBHelper vehicleLogDBHelper = VehicleLogDBHelper.getInstance(mContext);
        //mMileageHist = vehicleLogDBHelper.getMileageEntries(mVehicle.getRefID(), true);
        mileageList.setAdapter(new MileageAdapter(mContext, mVehicle, mMileageHist));
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
                Uri uri = export.mileageToCSV("", mMileageHist);

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
}
