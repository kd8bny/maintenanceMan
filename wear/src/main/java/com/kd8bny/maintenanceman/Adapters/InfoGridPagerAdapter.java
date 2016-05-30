package com.kd8bny.maintenanceman.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.view.Gravity;

import com.kd8bny.maintenanceman.Fragments.EngineFragment;
import com.kd8bny.maintenanceman.Fragments.GeneralFragment;
import com.kd8bny.maintenanceman.Fragments.InfoFragment;
import com.kd8bny.maintenanceman.Fragments.OtherFragment;
import com.kd8bny.maintenanceman.Fragments.PowerTrainFragment;
import com.kd8bny.maintenanceman.Vehicle.Vehicle;

import java.util.HashMap;


public class InfoGridPagerAdapter extends FragmentGridPagerAdapter {
    private static final String TAG = "wear_grd_pg";

    private Context mContext;
    private Vehicle mVehicle;
    private Fragment [][] mPages;

    public InfoGridPagerAdapter(Context context, FragmentManager fm, Vehicle vehicle) {
        super(fm);
        mContext = context;
        mVehicle = vehicle;
        mPages = initPages();
    }

    private Fragment[][] initPages() {//TODO dont gen pages for nullz
        Fragment[][] PAGES = {
                {
                        new GeneralFragment(),
                        new EngineFragment(),
                        new PowerTrainFragment(),
                        new OtherFragment(),
                },
        };

        return PAGES;
    }

    @Override
    public int getRowCount() {
        return mPages.length;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return mPages[rowNum].length;
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Fragment page = mPages[row][col];

        return page;
    }
}