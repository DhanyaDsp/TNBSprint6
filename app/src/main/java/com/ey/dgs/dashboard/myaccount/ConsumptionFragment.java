package com.ey.dgs.dashboard.myaccount;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ey.dgs.R;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.billing.BillingHistoryViewModel;
import com.ey.dgs.dashboard.questions.MMCQuestionsFragment;
import com.ey.dgs.dashboard.questions.QuestionActivity;
import com.ey.dgs.databinding.FragmentConsumptionBinding;
import com.ey.dgs.databinding.FragmentMyAccountBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.BillingDetails;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.User;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.FragmentUtils;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.views.BarChart;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsumptionFragment extends Fragment implements View.OnClickListener {


    private View view;
    private BarChart barChart;
    Account account;
    private BillingHistoryViewModel billingHistoryViewModel;
    private LoginViewModel loginViewModel;
    private BillingHistory billingHistory;
    private BillingDetails[] billingDetails;
    User user;
    LinearLayout llTabs;
    AppCompatButton btnDaily, btnMonthly, btnYearly, btnManageConsumption;

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
                }
            });
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
        llTabs = view.findViewById(R.id.llTabs);
        llTabs.setClipToOutline(true);
        barChart = view.findViewById(R.id.bar_chart);
        barChart.setSelectionRequired(true);
        btnDaily = view.findViewById(R.id.btnDaily);
        btnMonthly = view.findViewById(R.id.btnMonthly);
        btnYearly = view.findViewById(R.id.btnYearly);
        btnManageConsumption = view.findViewById(R.id.btnManageConsumption);
        btnManageConsumption.setOnClickListener(this);
        btnDaily.setOnClickListener(this);
        btnMonthly.setOnClickListener(this);
        btnYearly.setOnClickListener(this);
        btnDaily.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnDaily:
                btnDaily.setSelected(true);
                btnMonthly.setSelected(false);
                btnYearly.setSelected(false);
                break;

            case R.id.btnMonthly:
                btnDaily.setSelected(false);
                btnMonthly.setSelected(true);
                btnYearly.setSelected(false);
                break;

            case R.id.btnYearly:
                btnDaily.setSelected(false);
                btnMonthly.setSelected(false);
                btnYearly.setSelected(true);
                break;
            case R.id.btnManageConsumption:
                showQuestionsFragment();
                break;

            default:
                break;
        }
    }

    private void showQuestionsFragment() {
        if (billingHistory != null) {
            billingHistory.setAccount(account);
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            intent.putExtra("billingHistory",  billingHistory);
            getActivity().startActivity(intent);
        }
    }
}
