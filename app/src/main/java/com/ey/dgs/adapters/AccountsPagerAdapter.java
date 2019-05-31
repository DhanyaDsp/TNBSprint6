package com.ey.dgs.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ey.dgs.dashboard.billing.ChartFragment;
import com.ey.dgs.model.Account;

import java.util.ArrayList;

public class AccountsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Account> accounts;

    public AccountsPagerAdapter(FragmentManager fragmentManager, ArrayList<Account> accounts) {
        super(fragmentManager);
        this.accounts = accounts;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ChartFragment.newInstance(accounts.get(position));
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}


