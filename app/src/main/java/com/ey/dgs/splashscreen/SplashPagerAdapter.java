package com.ey.dgs.splashscreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ey.dgs.dashboard.billing.ChartFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.SplashItem;

import java.util.ArrayList;

public class SplashPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<SplashItem> splashItems;

    public SplashPagerAdapter(FragmentManager fragmentManager, ArrayList<SplashItem> splashItems) {
        super(fragmentManager);
        this.splashItems = splashItems;
    }

    @Override
    public int getCount() {
        return splashItems.size();
    }

    @Override
    public Fragment getItem(int position) {
        return SplashItemFragment.newInstance(splashItems.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}