package com.kd8bny.maintenanceman.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.kd8bny.maintenanceman.R;

public class MainActivity extends Activity {
    private static final String TAG = "wear_main";

    private static final String WEAR_FILE_PATH = "/files";

    private TextView mTextView;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG, "wear con");
                        mTextView.setText("is conn");
                        Wearable.DataApi.addListener(mGoogleApiClient, onDataChangedListener);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "wear suspended");
                    }
                }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "wear conn failed :(");
                    }
                })
        .addApi(Wearable.API)
        .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    public DataApi.DataListener onDataChangedListener = new DataApi.DataListener() {
        @Override
        public void onDataChanged(DataEventBuffer dataEventBuffer) {
            Log.d(TAG, "did data change?");
            for(DataEvent event : dataEventBuffer){
                if (event.getType() ==
                        DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().equals(WEAR_FILE_PATH)){
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    String roster = dataMapItem.getDataMap().getString("roster");
                    Log.d(TAG, "RX!");
                    mTextView.setText(roster);
                }
            }
        }
    };
}
