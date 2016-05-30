package com.kd8bny.maintenanceman.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.kd8bny.maintenanceman.fragments.EngineFragment;
import com.kd8bny.maintenanceman.fragments.GeneralFragment;
import com.kd8bny.maintenanceman.fragments.OtherFragment;
import com.kd8bny.maintenanceman.fragments.PowerTrainFragment;
import com.kd8bny.maintenanceman.classes.vehicle.Vehicle;

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

    private Fragment[][] initPages() {
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