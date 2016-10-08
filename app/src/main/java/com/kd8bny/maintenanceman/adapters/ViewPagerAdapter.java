package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.fragments.fragment_mileage;
import com.kd8bny.maintenanceman.fragments.fragment_travel;
import com.kd8bny.maintenanceman.fragments.fragment_maintenance;
import com.kd8bny.maintenanceman.fragments.fragment_info;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new fragment_info();
            case 1:
                return new fragment_maintenance();
            case 2:
                return new fragment_mileage();
            case 3:
                return new fragment_travel();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_info);
            case 1:
                return mContext.getString(R.string.title_history);
            case 2:
                return mContext.getString(R.string.title_mileage);
            case 3:
                return mContext.getString(R.string.title_travel);
        }

        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
