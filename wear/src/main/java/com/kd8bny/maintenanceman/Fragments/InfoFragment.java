package com.kd8bny.maintenanceman.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kd8bny.maintenanceman.Adapters.InfoItemAdapter;
import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.Vehicle.Vehicle;

import java.util.HashMap;

public class InfoFragment extends Fragment {
    private static final String TAG = "info_frg";

    protected Context mContext;
    protected Vehicle mVehicle;
    protected HashMap<String, String> mCardInfo;

    public InfoFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();
        mVehicle = (Vehicle) getActivity().getIntent().getBundleExtra("bundle").get("vehicle");
        dataInit();
    }

    public void dataInit(){
        /**
        * Exists to override only!
         **/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);

        WatchViewStub stub = (WatchViewStub) view.findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override public void onLayoutInflated(WatchViewStub stub) {
                RecyclerView cardList = (RecyclerView) view.findViewById(R.id.infoRecycler);
                LinearLayoutManager cardMan = new LinearLayoutManager(getActivity());
                cardMan.setReverseLayout(true);
                cardList.setLayoutManager(cardMan);
                cardList.setAdapter(new InfoItemAdapter(mCardInfo));
            }
        });

        return view;
    }
}
