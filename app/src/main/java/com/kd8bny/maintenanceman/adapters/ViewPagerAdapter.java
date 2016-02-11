package com.kd8bny.maintenanceman.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kd8bny.maintenanceman.R;
import com.kd8bny.maintenanceman.fragments.fragment_history;
import com.kd8bny.maintenanceman.fragments.fragment_info;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new fragment_info();
            case 1:
                return new fragment_history();
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
        }

        return null;
    }
}
