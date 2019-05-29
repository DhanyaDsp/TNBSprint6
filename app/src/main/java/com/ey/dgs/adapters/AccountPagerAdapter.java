package com.ey.dgs.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.views.BarChart;

import java.util.ArrayList;

public class AccountPagerAdapter extends PagerAdapter {

    private final DashboardFragment dashboardFragment;
    private Context mContext;
    ArrayList<Account> accounts;

    public AccountPagerAdapter(DashboardFragment dashboardFragment, Context context, ArrayList<Account> accounts) {
        mContext = context;
        this.accounts = accounts;
        this.dashboardFragment = dashboardFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        Account account = accounts.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.account_pager_item, viewGroup, false);
        viewGroup.addView(layout);

        BarChart barChart = layout.findViewById(R.id.bar_chart);
        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        for (int i = 0; i < 6; i++) {
            chartData = new ChartData();
            chartData.setTag("LB" + (i + 1));
            chartData.setVal(20.0f);
            chartDatum.add(chartData);
        }

        barChart.setData(chartDatum).setTitle(account.getNickName());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardFragment.moveToMyDashboardPage(account);
            }
        });
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object view) {
        viewGroup.removeView((View) view);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}