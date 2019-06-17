package com.ey.dgs.dashboard.questions;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountDetails;
import com.ey.dgs.model.AccountDetailsRequest;
import com.ey.dgs.model.BillingDetails;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.DialogHelper;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.views.BarChart;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

import static com.ey.dgs.dashboard.MyDashboardFragment.IS_THRESHOLD_SET;

public class MMCQuestionsFragment extends Fragment implements View.OnClickListener, View.OnKeyListener{

    private View rootView;
    private int displayCount;
    private LinearLayout layoutQuestion1;
    private RelativeLayout layoutQuestion2;
    private BarChart barChart;
    private boolean onQuestion2 = false;
    private AppCompatEditText thresholdAnswer;
    private QuestionsViewModel questionsViewModel;
    private String peopleInProperty, thresholdValue;
    private Context context;
    private BillingDetails[] billingDetails;
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
        thresholdAnswer = rootView.findViewById((R.id.etAnswer));
        thresholdAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("Done button","Enter pressed");
                    Utils.hideKeyBoard(getActivity());
                    String strThreshold = thresholdAnswer.getText().toString();
                    if (!TextUtils.isEmpty(strThreshold) && Integer.valueOf(strThreshold) > 0) {
                        float threshold = Float.parseFloat(strThreshold);
                        setChartData(barChart, billingHistory, threshold);
                    } else {
                        Utils.showToast(getActivity(), "Please enter value");
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribe();
    }

    private void subscribe() {
        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        questionsViewModel.setContext(getActivity());
    }

    public void showProgress(boolean isProgress) {
        if (isProgress) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
            if(questionsViewModel.isSuccess()) {
                showSuccessPopup();
            }
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
        if(!onQuestion2) {
            layoutQuestion1.setVisibility(View.GONE);
            onQuestion2 = true;
            layoutQuestion2.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            this.billingDetails = gson.fromJson(billingHistory.getBillingDetails(), BillingDetails[].class);
            setChartData(billingDetails);
            /*if(account.getUserThreshold() != null) {
                setChartData(barChart, billingHistory, Float.parseFloat(account.getUserThreshold()));
            } else {
                setChartData(barChart, billingHistory);
            }*/
        } else {
            callAccountDetailsAPiService();
        }
    }

    private void callAccountDetailsAPiService() {
        peopleInProperty = numberDisplay.getText().toString().trim();
        thresholdValue = thresholdAnswer.getText().toString().trim();
        if(!TextUtils.isEmpty(thresholdValue) && Integer.valueOf(thresholdValue) > 0) {
            AccountDetails[] accountDetailsArray = new AccountDetails[1];
            accountDetailsArray[0] = new AccountDetails(account.getAccountNumber(),
                    peopleInProperty, thresholdValue);
            AccountDetailsRequest accountDetailsRequest =
                    new AccountDetailsRequest("Admin@xyzmail.com", accountDetailsArray);

            showProgress(true);
            questionsViewModel.updateAccountDetailsInServer(accountDetailsRequest);
            questionsViewModel.getLoaderData().observe(getViewLifecycleOwner(), showProgress -> {
                showProgress(showProgress);
            });
        }
        else {
            Utils.showToast(getActivity(), "Please enter value");
        }
    }

    public void showSuccessPopup() {
        DialogHelper.showSuccessDialog(account, thresholdValue, getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.hidePopup();
                getFragmentManager().popBackStack();
            }
        });
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    Utils.hideKeyBoard(getActivity());
                    String strThreshold = thresholdAnswer.getText().toString();
                    if (!TextUtils.isEmpty(strThreshold) || Integer.valueOf(strThreshold) > 0) {
                        Float threshold = Float.parseFloat(strThreshold);
                        setChartData(barChart, billingHistory, threshold);
                    } else {
                        Utils.showToast(getActivity(), "Please enter value");
                    }
                    return true;
                default:
                    break;
            }
        }
        return false;
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
        barChart.setData(chartDatum)
                .setTitle(null)
                .setBarUnit("RM")
                .setSelectionRequired(true);
    }

    private void setChartData(BarChart bar_chart, BillingHistory billingHistory, float threshold) {
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
            bar_chart.setData(chartDatum)
                    .setTitle(startDate + " - " + endDate)
                    .setBarUnit("RM")
                    .setThreshold(true, threshold)
                    .setSelectionRequired(true);
        }
    }

    private void setChartData(BillingDetails[] billingDetails) {
        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        String startDate = Utils.formatAccountDate(account.getBillingCycleStartDate());
        String endDate = Utils.formatAccountDate(account.getBillingCycleEndDate());
        if (!IS_THRESHOLD_SET) {
            for (int i = 0; i < billingDetails.length; i++) {
                BillingDetails billingDetail = billingDetails[i];
                chartData = new ChartData();
                chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
                chartData.setVal(billingDetail.getBilledValue());
                chartDatum.add(chartData);
            }
            if (account.isThreshold()) {
                barChart.setData(chartDatum)
                        .setBarUnit("RM")
                        .setThreshold(true, Float.parseFloat(account.getUserThreshold()))
                        .setSelectionRequired(true);
            } else {
                barChart.setData(chartDatum)
                        .setBarUnit("RM")
                        .setSelectionRequired(false);
            }
        } else {
            for (int i = 0; i < billingDetails.length; i++) {
                BillingDetails billingDetail = billingDetails[i];
                chartData = new ChartData();
                chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
                chartData.setVal(billingDetail.getBilledValue());
                chartDatum.add(chartData);
            }
            barChart = rootView.findViewById(R.id.accountChart);
            ViewGroup.LayoutParams tmpLayParams = barChart.getLayoutParams();
            ((ViewGroup) barChart.getParent()).removeView(barChart);

            BarChart tmpBarChart = new BarChart(context);
            tmpBarChart.setLayoutParams(tmpLayParams);
            tmpBarChart.setId(R.id.accountChart);
            ((ViewGroup) rootView.findViewById(R.id.rlChart)).addView(tmpBarChart);

            tmpBarChart.setData(chartDatum)
                    .setTitle(startDate + " - " + endDate)
                    .setBarUnit("RM")
                    .setThreshold(true, Float.parseFloat(account.getUserThreshold()))
                    //.setThreshold(selectedAccount.isThreshold(), Float.parseFloat(energyConsumptions.getUserThreshold()))
                    .setSelectionRequired(true); //.updateData(); // .invalidate();

            IS_THRESHOLD_SET = false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }
}
