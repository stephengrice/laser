package com.stephengrice.laser;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

import com.stephengrice.laser.db.DbHelper;
import com.stephengrice.laser.fragment.BudgetChartFragment;
import com.stephengrice.laser.fragment.BudgetFragment;

public class BudgetTabsPagerAdapter extends FragmentPagerAdapter {

    private BudgetFragment mBudgetFragment;
    private FragmentManager mFragmentManager;
    private Context mContext;

    public BudgetTabsPagerAdapter(BudgetFragment budgetFragment, FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        mBudgetFragment = budgetFragment;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mBudgetFragment.getChartFragmentSpent();
            case 1:
                return mBudgetFragment.getChartFragmentEarned();
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
            case 1:
                return mContext.getString(R.string.budget_tab_in);
            case 0:
                return mContext.getString(R.string.budget_tab_out);
            default:
                return null;
        }
    }

    // THIS of all things fixed the budget disappearing chart problem.
    @Override
    public long getItemId(int position) {
        return System.currentTimeMillis();
    }
}
