package com.kd8bny.maintenanceman.ui.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kd8bny.maintenanceman.R;


public class slide_add extends Fragment {
    private static final String TAG = "sld_add";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        return inflater.inflate(R.layout.slide_add, container, false);
    }
}
