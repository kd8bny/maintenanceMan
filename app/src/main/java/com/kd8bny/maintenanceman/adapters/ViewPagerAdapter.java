package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.classes.Vehicle.Business;
import com.kd8bny.maintenanceman.classes.Vehicle.Vehicle;
import com.kd8bny.maintenanceman.fragments.fragment_business_view;
import com.kd8bny.maintenanceman.fragments.fragment_history;
import com.kd8bny.maintenanceman.fragments.fragment_info;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private boolean mIsBusiness;

    public ViewPagerAdapter(Context context, FragmentManager fm, boolean isBusiness) {
        super(fm);
        mContext = context;
        mIsBusiness = isBusiness;
    }

    @Override
    public int getCount() {
        if (mIsBusiness) {
            return 3;
        }
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new fragment_info();
            case 1:
                return new fragment_history();
            case 2:
                return new fragment_business_view();
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
                return mContext.getString(R.string.title_business);
        }

        return null;
    }
}
