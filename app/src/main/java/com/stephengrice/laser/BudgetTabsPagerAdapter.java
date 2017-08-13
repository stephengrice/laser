package com.stephengrice.laser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stephengrice.laser.db.DbHelper;
import com.stephengrice.laser.fragment.BudgetChartFragment;

public class BudgetTabsPagerAdapter extends FragmentPagerAdapter {
    public BudgetTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BudgetChartFragment.newInstance(DbHelper.CountMode.POSITIVE);
            case 1:
                return BudgetChartFragment.newInstance(DbHelper.CountMode.NEGATIVE);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "In";
            case 1:
                return "Out";
            default:
                return null;
        }
    }
}
