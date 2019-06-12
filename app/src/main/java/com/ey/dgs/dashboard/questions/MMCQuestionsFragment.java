package com.ey.dgs.dashboard.questions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.BillingDetails;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.views.BarChart;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class MMCQuestionsFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private int displayCount;
    private LinearLayout layoutQuestion1;
    private RelativeLayout layoutQuestion2;
    private BarChart barChart;
    Account account;
    View loader;
    AppCompatTextView tvAccountName, tvPeopleQuestion;
    AppPreferences appPreferences;
    BillingHistory billingHistory;
    Button decrease, increase, numberDisplay, btnNext;


    public static MMCQuestionsFragment newInstance(BillingHistory billingHistory) {
        MMCQuestionsFragment fragment = new MMCQuestionsFragment();
        Bundle args = new Bundle();
        args.putSerializable("billingHistory", (Serializable) billingHistory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billingHistory = (BillingHistory) getArguments().getSerializable("billingHistory");
        account = billingHistory.getAccount();
        appPreferences = new AppPreferences(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mmcquestions_fragment, container, false);
        initViews();
        setData();
        subscribe();
        return rootView;
    }

    private void setData() {
        tvAccountName.setText(account.getNickName());
        tvPeopleQuestion.setText(rootView.getContext().getString(R.string.people_question, account.getNickName()));

    }

    private void initViews() {
        loader = rootView.findViewById(R.id.loader);
        tvAccountName = rootView.findViewById(R.id.tvAccountName);
        tvPeopleQuestion = rootView.findViewById(R.id.peopleQuestion);
        decrease = rootView.findViewById(R.id.decrease);
        increase = rootView.findViewById(R.id.increase);
        numberDisplay = rootView.findViewById(R.id.number_display);
        btnNext = rootView.findViewById(R.id.btnNext);
        decrease.setOnClickListener(this);
        increase.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        layoutQuestion1 = rootView.findViewById(R.id.layout_question1);
        layoutQuestion2 = rootView.findViewById(R.id.layout_question2);
        barChart = rootView.findViewById(R.id.bar_chart_questions);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribe();
    }

    private void subscribe() {
        /*manageConsumptionAdapter = new ManageConsumptionAdapter(getActivity());
        vpQuestions.setAdapter(manageConsumptionAdapter);*/
    }

    public void showProgress(boolean isProgress) {
        if (isProgress) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.decrease:
                decreaseInteger();
                break;
            case R.id.increase:
                increaseInteger();
                break;
            case R.id.btnNext:
                setViewForQuestion2();
                break;
            default:
                break;

        }
    }

    private void setViewForQuestion2() {
        layoutQuestion1.setVisibility(View.GONE);
        layoutQuestion2.setVisibility(View.VISIBLE);
        setChartData(barChart, billingHistory);
    }

    private void setChartData(BarChart barChart, BillingHistory billingHistory) {
        Gson gson = new Gson();
        BillingDetails[] billingDetails = gson.fromJson(billingHistory.getBillingDetails(), BillingDetails[].class);

        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        String startDate = Utils.formatAccountDate(billingHistory.getAccount().getBillingCycleStartDate());
        String endDate = Utils.formatAccountDate(billingHistory.getAccount().getBillingCycleEndDate());
        for (int i = 0; i < billingDetails.length; i++) {
            BillingDetails billingDetail = billingDetails[i];
            chartData = new ChartData();
            chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
            chartData.setVal(billingDetail.getBilledValue());
            chartDatum.add(chartData);
        }
        if (billingHistory.getAccount().isThreshold()) {
            barChart.setData(chartDatum)
                    .setTitle(startDate + " - " + endDate)
                    .setBarUnit("RM")
                    .setSelectionRequired(true);
        }
    }

    public void increaseInteger() {
        if(displayCount < 10) {
            displayCount = displayCount + 1;
            display(displayCount);
        }
    }

    public void decreaseInteger() {
        if(displayCount > 1) {
            displayCount = displayCount - 1;
            display(displayCount);
        }
    }
    private void display(int number) {
        numberDisplay.setText("" + number);
    }
}
