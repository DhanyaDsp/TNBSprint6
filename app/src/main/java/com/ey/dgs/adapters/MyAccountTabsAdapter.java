package com.ey.dgs.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;

import com.ey.dgs.R;

import java.util.ArrayList;
import java.util.List;

public class MyAccountTabsAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final Context context;
    private String[] tabTitles;

    public MyAccountTabsAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.context = context;
        tabTitles = new String[]{"Consumption", "Energy Sights"};
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.chart_tab_view, null);
        AppCompatTextView tvTitle = v.findViewById(R.id.title);
        tvTitle.setText(tabTitles[position]);
        return v;
    }
}



