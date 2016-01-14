package com.kd8bny.maintenanceman.ui.info;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.kd8bny.maintenanceman.R;

/**
 * Created by kd8bny on 12/31/15.
 */
public class adapter_viewPager_info extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    private Context context;
    public adapter_viewPager_info(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new fragment_info();
            case 1:
                return new fragment_history();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.title_info);
            case 1:
                return context.getResources().getString(R.string.title_history);
            default:
                return null;
        }
    }
}
