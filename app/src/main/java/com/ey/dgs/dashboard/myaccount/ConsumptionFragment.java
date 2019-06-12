package com.ey.dgs.dashboard.myaccount;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;

import com.ey.dgs.R;
import com.ey.dgs.adapters.DaysAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.billing.BillingHistoryViewModel;
import com.ey.dgs.databinding.FragmentConsumptionBinding;
import com.ey.dgs.databinding.FragmentMyAccountBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.BillingDetails;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.User;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.views.BarChart;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.ey.dgs.model.BillingHistory.DAILY;
import static com.ey.dgs.model.BillingHistory.MONTHLY;
import static com.ey.dgs.model.BillingHistory.WEEKLY;

public class ConsumptionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private View view;
    private BarChart barChart;
    Account account;
    private BillingHistoryViewModel billingHistoryViewModel;
    private LoginViewModel loginViewModel;
    private BillingHistory billingHistory;
    LinearLayout llDays;
    private BillingDetails[] billingDetails;
    User user;
    LinearLayout llTabs;
    AppCompatButton btnDaily, btnMonthly, btnYearly;
    View loader;
    Gallery glDays;
    DaysAdapter daysAdapter;
    private ArrayList<String> days;

    public ConsumptionFragment() {
    }


    public static ConsumptionFragment newInstance(Account account) {
        ConsumptionFragment fragment = new ConsumptionFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentConsumptionBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_consumption, container, false);
        binding.setFragment(this);
        binding.setAccount(account);
        view = binding.getRoot();
        initViews();
        subscribe();
        return view;
    }

    private void subscribe() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getUserDetail(1);
        billingHistoryViewModel = ViewModelProviders.of(this).get(BillingHistoryViewModel.class);
        billingHistoryViewModel.loadBillingHistoryFromLocalDB(account.getAccountNumber());
        billingHistoryViewModel.setContext(getActivity());
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            billingHistoryViewModel.getBillingHistory().observe(getViewLifecycleOwner(), billingHistory -> {
                if (billingHistory == null) {
                    billingHistoryViewModel.getBillingHistoryFromServer(user, account);
                } else {
                    this.billingHistory = billingHistory;
                    Gson gson = new Gson();
                    this.billingDetails = gson.fromJson(billingHistory.getBillingDetails(), BillingDetails[].class);
                    setChartData(billingDetails);
                    showProgress(false);
                }
            });
        });
        billingHistoryViewModel.getDays().observe(getViewLifecycleOwner(), days -> {
            this.days = days;
            daysAdapter = new DaysAdapter(getActivity(), this.days);
            glDays.setAdapter(daysAdapter);
        });
    }

    private void setChartData(BillingDetails[] billingDetails) {
        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        String startDate = Utils.formatAccountDate(account.getBillingCycleStartDate());
        String endDate = Utils.formatAccountDate(account.getBillingCycleEndDate());
        for (int i = 0; i < billingDetails.length; i++) {
            BillingDetails billingDetail = billingDetails[i];
            chartData = new ChartData();
            chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
            chartData.setVal(billingDetail.getBilledValue());
            chartDatum.add(chartData);
        }
        barChart.setData(chartDatum)
                .setTitle(null)
                .setBarUnit("RM")
                .setSelectionRequired(true);
    }

    private void initViews() {
        loader = view.findViewById(R.id.loader);
        llTabs = view.findViewById(R.id.llTabs);
        llTabs.setClipToOutline(true);
        barChart = view.findViewById(R.id.bar_chart);
        barChart.setSelectionRequired(true);
        btnDaily = view.findViewById(R.id.btnDaily);
        btnMonthly = view.findViewById(R.id.btnMonthly);
        btnYearly = view.findViewById(R.id.btnYearly);
        btnDaily.setOnClickListener(this);
        btnMonthly.setOnClickListener(this);
        btnYearly.setOnClickListener(this);
        btnDaily.setSelected(true);
        llDays = view.findViewById(R.id.llDays);
        glDays = view.findViewById(R.id.glDays);
        glDays.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnDaily:
                btnDaily.setSelected(true);
                btnMonthly.setSelected(false);
                btnYearly.setSelected(false);
                getBillingHistory(DAILY);
                llDays.setVisibility(View.VISIBLE);
                break;

            case R.id.btnMonthly:
                btnDaily.setSelected(false);
                btnMonthly.setSelected(true);
                btnYearly.setSelected(false);
                getBillingHistory(WEEKLY);
                llDays.setVisibility(View.GONE);
                break;

            case R.id.btnYearly:
                btnDaily.setSelected(false);
                btnMonthly.setSelected(false);
                btnYearly.setSelected(true);
                getBillingHistory(WEEKLY);
                llDays.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    private void getBillingHistory(String period) {
        showProgress(true);
        billingHistoryViewModel.getBillingHistoryFromServer(user, account);
    }

    public void showProgress(boolean show) {
        if (show) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        daysAdapter.selectItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
