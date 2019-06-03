package com.ey.dgs.dashboard.billing;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.MyDashboardFragment;
import com.ey.dgs.dashboard.myaccount.AccountSettingsViewModel;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.BillingDetails;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.User;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.FragmentUtils;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.views.BarChart;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChartFragment extends Fragment {

    private static ChartFragment fragment;
    Account account;
    private View view;
    private LoginViewModel loginViewModel;
    private BillingHistoryViewModel billingHistoryViewModel;
    private AppPreferences appPreferences;
    private BarChart barChart;
    private User user;
    private AccountSettingsViewModel accountSettingsViewModel;
    private AccountSettings accountSettings;
    private EnergyConsumptions energyConsumptions;
    View overlay;
    AppCompatTextView tvEnergyTip;

    public ChartFragment() {
    }

    public static ChartFragment newInstance(Account account) {
        fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putSerializable("account", account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            account = (Account) getArguments().getSerializable("account");
        }
        appPreferences = new AppPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        overlay = view.findViewById(R.id.overlay);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account != null) {
                    FragmentUtils.newInstance(getFragmentManager())
                            .addFragment(FragmentUtils.INDEX_MY_DASHBOARD_FRAGMENT, account, MyDashboardFragment.class.getName(), R.id.homeFlContainer);
                }
            }
        });
        initViews();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
    }

    private void initViews() {
        barChart = view.findViewById(R.id.bar_chart);
        tvEnergyTip = view.findViewById(R.id.tvEnergyTip);
    }

    private void getData() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setContext(getActivity());
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        accountSettingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        accountSettingsViewModel.setContext(getActivity());
        billingHistoryViewModel = ViewModelProviders.of(this).get(BillingHistoryViewModel.class);
        billingHistoryViewModel.setContext(getActivity());
        billingHistoryViewModel.loadBillingHistoryFromLocalDB(account.getAccountNumber());
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                this.user = user;
                accountSettingsViewModel.loadAccountSettingsFromLocalDB(account.getAccountNumber());
                accountSettingsViewModel.getAccountSettingsData().observe(getViewLifecycleOwner(), accountSettings -> {
                    if (accountSettings == null) {
                        accountSettingsViewModel.getAccountSettingsFromServer(user.getEmail(), account.getAccountNumber());
                    } else {
                        this.accountSettings = accountSettings;
                        accountSettingsViewModel.loadEnergyConsumptionsFromLocalDB(accountSettings.getAccountNumber());
                        accountSettingsViewModel.getEnergyConsumptions().observeForever(energyConsumptions -> {
                            this.energyConsumptions = energyConsumptions;
                            if (energyConsumptions != null) {
                                billingHistoryViewModel.getBillingHistory().observe(getViewLifecycleOwner(), billingHistory -> {
                                    if (billingHistory == null) {
                                        billingHistoryViewModel.getBillingHistoryFromServer(user, account);
                                    } else {
                                        if (account.isThreshold()) {
                                            tvEnergyTip.setVisibility(View.VISIBLE);
                                            tvEnergyTip.setText(account.getEnergyTip());
                                        } else {
                                            tvEnergyTip.setVisibility(View.INVISIBLE);
                                        }
                                        Gson gson = new Gson();
                                        BillingDetails[] billingDetails = gson.fromJson(billingHistory.getBillingDetails(), BillingDetails[].class);
                                        setChartData(billingDetails);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    private void setChartData(BillingDetails[] billingDetails) {
        barChart = view.findViewById(R.id.bar_chart);
        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        for (int i = 0; i < billingDetails.length; i++) {
            BillingDetails billingDetail = billingDetails[i];
            chartData = new ChartData();
            chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
            chartData.setVal(billingDetail.getBilledValue());
            chartDatum.add(chartData);
        }
        String startDate = Utils.formatAccountDate(account.getBillingCycleStartDate());
        String endDate = Utils.formatAccountDate(account.getBillingCycleEndDate());

        ViewGroup parent = ((ViewGroup) barChart.getParent());
        ViewGroup.LayoutParams tmpLayParams = barChart.getLayoutParams();
        parent.removeView(barChart);

        BarChart tmpBarChart = new BarChart(getActivity());
        tmpBarChart.setLayoutParams(tmpLayParams);
        tmpBarChart.setId(R.id.bar_chart);
        parent.addView(tmpBarChart);

        tmpBarChart.setData(chartDatum)
                .setTitle(startDate + " - " + endDate)
                .setBarUnit("RM")
                .setThreshold(account.isThreshold(), Float.parseFloat(accountSettings.getEnergyConsumptions().getUserThreshold()))
                .setSelectionRequired(true);

        overlay.bringToFront();
/*        barChart.setData(chartDatum)
                .setTitle(startDate + " - " + endDate)
                .setBarUnit("RM")
                .setThreshold(account.isThreshold(), Float.parseFloat(accountSettings.getEnergyConsumptions().getUserThreshold()))
                .setSelectionRequired(true);*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
