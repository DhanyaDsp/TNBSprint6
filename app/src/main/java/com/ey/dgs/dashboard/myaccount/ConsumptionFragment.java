package com.ey.dgs.dashboard.myaccount;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.DaysAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.dashboard.billing.BillingHistoryViewModel;
import com.ey.dgs.dashboard.questions.MMCQuestionsFragment;
import com.ey.dgs.databinding.FragmentConsumptionBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.BillingDetails;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.User;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.FragmentUtils;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.views.BarChart;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import static com.ey.dgs.model.BillingHistory.DAILY;
import static com.ey.dgs.model.BillingHistory.MONTHLY;
import static com.ey.dgs.model.BillingHistory.WEEKLY;
import static com.ey.dgs.utils.FragmentUtils.INDEX_QUESTIONS_FRAGMENT;

public class ConsumptionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, BarChart.OnItemSelected {


    private View view;
    private BarChart barChart;
    Account account;
    private BillingHistoryViewModel billingHistoryViewModel;
    private LoginViewModel loginViewModel;
    private BillingHistory billingHistory, billingHistoryForQuestions;
    LinearLayout llDays;
    private BillingDetails[] billingDetails;
    User user;
    LinearLayout llTabs;
    AppCompatButton btnDaily, btnMonthly, btnYearly, btnManageConsumption;
    View loader, offlineView;
    Gallery glDays;
    DaysAdapter daysAdapter;
    private ArrayList<ChartData> chartDatum;
    private String chartPeriod = BillingHistory.MONTHLY;
    LinearLayout llAmount;
    AppCompatTextView tvDueAmount;
    AppCompatButton btnRefresh;

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
        setData();
        subscribe();
        return view;
    }

    private void setData() {
        tvDueAmount.setText(String.format("%.2f", account.getLastBilledAmount()));
    }

    private void subscribe() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getUserDetail(1);
        billingHistoryViewModel = ViewModelProviders.of(this).get(BillingHistoryViewModel.class);
        //billingHistoryViewModel.loadBillingHistoryFromLocalDB(account.getAccountNumber());
        billingHistoryViewModel.setContext(getActivity());
        billingHistoryViewModel.getLoaderData().observe(getViewLifecycleOwner(), this::showProgress);
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            if (user != null) {
                getBillingHistory(chartPeriod);
                billingHistoryViewModel.getBillingHistory().observe(getViewLifecycleOwner(), billingHistory -> {
                    if (billingHistory == null) {

                    } else {
                        this.billingHistory = billingHistory;
                        if (MONTHLY.equals(chartPeriod)) {
                            this.billingHistoryForQuestions = billingHistory;
                        }
                        Gson gson = new Gson();
                        this.billingDetails = gson.fromJson(billingHistory.getBillingDetails(), BillingDetails[].class);
                        setChartData(billingDetails);
                    }
                });
            }
        });
        billingHistoryViewModel.getOfflineData().observe(getViewLifecycleOwner(), isOffline -> {
            showOfflineView(isOffline);
        });
        billingHistoryViewModel.getSessionExpiredData().observe(getViewLifecycleOwner(), isSessionExpired -> {
            if (isSessionExpired) {
                ((HomeActivity) getActivity()).showSessionExpiredView(isSessionExpired);
            }
        });
    }

/*    private void setChartData(BillingDetails[] billingDetails) {
        chartDatum = new ArrayList<>();
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
                .setThreshold(true, Float.parseFloat(account.getUserThreshold()))
                .setSelectionRequired(true);
    }*/

    private void setChartData(BillingDetails[] billingDetails) {
        chartDatum = new ArrayList<>();
        ChartData chartData;

        for (int i = 0; i < billingDetails.length; i++) {
            BillingDetails billingDetail = billingDetails[i];
            chartData = new ChartData();
            if (chartPeriod.equalsIgnoreCase(MONTHLY)) {
                chartData.setTag(Utils.formatMonthOnly(billingDetail.getBilledDate()));
            } else {
                chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
            }
            chartData.setVal(billingDetail.getBilledValue());
            chartDatum.add(chartData);
        }
        chartData = new ChartData();
        chartData.setIsSelected(true);
        chartData.setTag(Utils.formatMonthOnly(new Date()));
        if (chartPeriod.equalsIgnoreCase(MONTHLY)) {
            chartData.setVal(Float.parseFloat(account.getCurrentMonthConsumption()));
            chartDatum.add(chartData);
        }

        barChart = view.findViewById(R.id.bar_chart);
        barChart.setThresholdValue(Float.valueOf(account.getUserThreshold()));
        barChart.setPeriod(chartPeriod);
        ViewGroup.LayoutParams tmpLayParams = barChart.getLayoutParams();
        ((ViewGroup) barChart.getParent()).removeView(barChart);

        BarChart tmpBarChart = new BarChart(getActivity());
        tmpBarChart.setLayoutParams(tmpLayParams);
        tmpBarChart.setId(R.id.bar_chart);
        tmpBarChart.setPeriod(chartPeriod);
        ((ViewGroup) view.findViewById(R.id.rlChart)).addView(tmpBarChart);

       /* float thresholdLineValue;
        if (TextUtils.isEmpty(account.getUserThreshold())) {
            thresholdLineValue = Float.parseFloat(account.getUserThreshold());
            if (thresholdLineValue<=0){
                thresholdLineValue = Float.parseFloat(account.get());
            }
        }*/
        tmpBarChart.setThresholdValue(Float.valueOf(account.getUserThreshold()));
        tmpBarChart.setData(chartDatum)
                .setTitle("Suren")
                .setBarUnit("RM")
                .setThreshold(chartPeriod.equalsIgnoreCase(BillingHistory.MONTHLY), Float.parseFloat(account.getUserThreshold()))
                //.setThreshold(selectedAccount.isThreshold(), Float.parseFloat(energyConsumptions.getUserThreshold()))
                .setSelectionRequired(true); //.updateData(); // .invalidate();

        barChart = tmpBarChart;
        barChart.setItemSelectedListener(this);
        if (!TextUtils.isEmpty(chartPeriod) && chartPeriod.equalsIgnoreCase(BillingHistory.DAILY)) {
            daysAdapter = new DaysAdapter(getActivity(), this.chartDatum);
            glDays.setAdapter(daysAdapter);
        }
    }

    private void initViews() {
        loader = view.findViewById(R.id.loader);
        offlineView = view.findViewById(R.id.offlineView);
        btnRefresh = offlineView.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);
        llTabs = view.findViewById(R.id.llTabs);
        llTabs.setClipToOutline(true);
        tvDueAmount = view.findViewById(R.id.tvDueAmount);
        barChart = view.findViewById(R.id.bar_chart);
        barChart.setSelectionRequired(true);
        btnDaily = view.findViewById(R.id.btnDaily);
        btnMonthly = view.findViewById(R.id.btnMonthly);
        btnYearly = view.findViewById(R.id.btnWeekly);
        btnManageConsumption = view.findViewById(R.id.btnManageConsumption);
        btnManageConsumption.setOnClickListener(this);
        btnDaily.setOnClickListener(this);
        btnMonthly.setOnClickListener(this);
        btnYearly.setOnClickListener(this);
        btnMonthly.setSelected(true);
        llDays = view.findViewById(R.id.llDays);
        glDays = view.findViewById(R.id.glDays);
        glDays.setOnItemSelectedListener(this);
        llAmount = view.findViewById(R.id.llAmount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnDaily:
                btnDaily.setSelected(true);
                btnMonthly.setSelected(false);
                btnYearly.setSelected(false);
                getBillingHistory(DAILY);
                break;

            case R.id.btnMonthly:
                btnDaily.setSelected(false);
                btnMonthly.setSelected(true);
                btnYearly.setSelected(false);
                getBillingHistory(MONTHLY);
                break;

            case R.id.btnWeekly:
                btnDaily.setSelected(false);
                btnMonthly.setSelected(false);
                btnYearly.setSelected(true);
                getBillingHistory(WEEKLY);
                break;

            case R.id.btnManageConsumption:
                showQuestionsFragment();
                break;

            case R.id.btnRefresh:
                refreshPage();
                break;
            default:
                break;
        }
    }

    private void refreshPage() {
        billingHistoryViewModel.setLoaderData(true);
        billingHistoryViewModel.setOfflineData(false);
        getBillingHistory(chartPeriod);
    }

    private void getBillingHistory(String period) {
        this.chartPeriod = period;
        if (chartPeriod.equalsIgnoreCase(BillingHistory.DAILY)) {
            llDays.setVisibility(View.VISIBLE);
        } else {
            llDays.setVisibility(View.GONE);
        }
        if (chartPeriod.equalsIgnoreCase(BillingHistory.DAILY)) {
            llAmount.setVisibility(View.GONE);
        } else {
            llAmount.setVisibility(View.VISIBLE);
        }
        billingHistoryViewModel.getBillingHistoryFromServer(user, period, account);
    }

    public void showProgress(boolean show) {
        if (show) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }

    public void showOfflineView(boolean show) {
        if (show) {
            offlineView.setVisibility(View.VISIBLE);
        } else {
            offlineView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            daysAdapter.selectItem(position);
            if (chartDatum != null) {
                for (ChartData chartData : chartDatum) {
                    chartData.setIsSelected(false);
                }
                //chartDatum.get(position).setIsSelected(true);
                barChart.getBarsAdapter().toggleSelection(position);
                //barChart.getBarsAdapter().notifyDataSetChanged();
            }
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showQuestionsFragment() {
        if (billingHistoryForQuestions != null) {
            billingHistoryForQuestions.setAccount(account);
            Fragment fragment = getParentFragment();

            FragmentUtils.newInstance(getActivity().getSupportFragmentManager())
                    .addFragment(INDEX_QUESTIONS_FRAGMENT, billingHistoryForQuestions, DashboardFragment.class.getName(), fragment,
                            R.id.homeFlContainer);
        }
    }

    @Override
    public void onItemSelected(int position) {
        if (glDays != null) {
            glDays.setSelection(position, true);
            barChart.setTitle("" + chartDatum.get(position).getTag());
            //glDays.onSingleTapUp(null);
            //daysAdapter.selectItem(position);
            /*MotionEvent e1 = MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN, 89.333336f, 265.33334f, 0);
            MotionEvent e2 = MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_UP, 300.0f, 238.00003f, 0);

            glDays.onFling(e1, e2, -800, 0);*/
            //daysAdapter.notifyDataSetChanged();
        }
    }

    public void refresh(Account account) {
        this.account = account;
        setChartData(billingDetails);
    }
}
