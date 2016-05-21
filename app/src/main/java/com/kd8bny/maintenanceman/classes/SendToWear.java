package com.kd8bny.maintenanceman.classes;

import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by kd8bny on 4/29/16.
 */
public class SendToWear extends Thread{
    private static final String TAG = "send2wear";
    private GoogleApiClient mGoogleApiClient;
    private String WEAR_FILE_PATH;
    private DataMap mDataMap;

    public SendToWear(GoogleApiClient g, String p, DataMap d) {
        Log.d(TAG, "made it");
        mGoogleApiClient = g;
        WEAR_FILE_PATH = p;
        mDataMap = d;
    }

    public void run() {Log.d(TAG, "run?");
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WEAR_FILE_PATH);
        DataMap dataMap = putDataMapRequest.getDataMap();
        putDataMapRequest.getDataMap().putAll(mDataMap);
        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataRequest).await();
        if (!result.getStatus().isSuccess()) {
            Log.e(TAG, "Wear failed to transfer");
        }
    }
}
