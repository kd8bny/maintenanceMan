package com.kd8bny.maintenanceman.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.kd8bny.maintenanceman.Adapters.MainAdapter;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.SaveLoadHelperWear;
import com.kd8bny.maintenanceman.Vehicle.Vehicle;

import java.util.ArrayList;

public class MainActivity extends Activity implements WearableListView.ClickListener{
    private static final String TAG = "wear_main";

    private static final String WEAR_FILE_PATH = "/files";

    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private WearableListView listView;
    private ArrayList<Vehicle> mRoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
        mContext = getApplicationContext();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        listView.setClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Wearable.DataApi.addListener(mGoogleApiClient, onDataChangedListener);
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                    }})
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "wear connection failed :(");
                    }})
            .addApi(Wearable.API)
            .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }

        mGoogleApiClient.connect();
        mRoster = new SaveLoadHelperWear().load();
        listView.setAdapter(new MainAdapter(getApplicationContext(), mRoster));
    }

    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer pos = (Integer) v.itemView.getTag();
        Bundle bundle = new Bundle();
        bundle.putParcelable("vehicle", mRoster.get(pos));
        startActivity(new Intent(this, InfoActivity.class).putExtra("bundle", bundle));
    }

    //TODO make this a service
    public DataApi.DataListener onDataChangedListener = new DataApi.DataListener() {
        @Override
        public void onDataChanged(DataEventBuffer dataEventBuffer) {
            for(DataEvent event : dataEventBuffer){
                if (event.getType() == DataEvent.TYPE_CHANGED &&
                        event.getDataItem().getUri().getPath().equals(WEAR_FILE_PATH)){
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    new SaveLoadHelperWear().save(dataMapItem.getDataMap().getString("roster"));
                    onStart();
                }
            }
        }
    };


    @Override
    public void onTopEmptyRegionClick() {
    }
}
