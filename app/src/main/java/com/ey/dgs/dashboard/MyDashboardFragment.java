package com.ey.dgs.dashboard;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ey.dgs.R;
import com.ey.dgs.adapters.NotificationSettingsAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.billing.BillingHistoryViewModel;
import com.ey.dgs.dashboard.myaccount.AccountSettingsViewModel;
import com.ey.dgs.dashboard.questions.MMCQuestionsFragment;
import com.ey.dgs.databinding.MyDashboardFragmentBinding;
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

import java.io.Serializable;
import java.util.ArrayList;

public class MyDashboardFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_SET_THRESHOLD = 101;
    public static boolean IS_THRESHOLD_SET = false;
    private View rootView;
    private OnFragmentInteractionListener mListener;
    AppPreferences appPreferences;
    Account selectedAccount;
    private MyDashboardFragmentBinding myDashboardFragmentBinding;
    LinearLayout llManageBtns;
    AppCompatButton btnManageConsumption;
    RelativeLayout rlChart;
    BarChart barChart;
    View loader;
    private boolean billingDetailsServiceCalled;
    BillingHistoryViewModel billingHistoryViewModel;
    LoginViewModel loginViewModel;
    private Context context;
    BillingDetails[] billingDetails;
    private User user;
    private AccountSettingsViewModel accountSettingsViewModel;
    private AccountSettings accountSettings;
    private EnergyConsumptions energyConsumptions;

    public static MyDashboardFragment newInstance(Account account) {
        MyDashboardFragment fragment = new MyDashboardFragment();
        Bundle args = new Bundle();
        args.putSerializable("account", (Serializable) account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedAccount = (Account) getArguments().getSerializable("account");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myDashboardFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.my_dashboard_fragment, container, false);
        rootView = myDashboardFragmentBinding.getRoot();
        myDashboardFragmentBinding.setSelectedAccount(selectedAccount);
        appPreferences = new AppPreferences(getActivity());
        initView();
        subscribe();
        return rootView;
    }

    private void initView() {
        loader = rootView.findViewById(R.id.loader);
        rlChart = rootView.findViewById(R.id.rlChart);
        barChart = rootView.findViewById(R.id.bar_chart);
        btnManageConsumption = rootView.findViewById(R.id.btnManageConsumption);
        btnManageConsumption.setOnClickListener(this);
        llManageBtns = rootView.findViewById(R.id.llManageBtns);
        if (mListener != null) {
            mListener.onFragmentInteraction("");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setContext(getActivity());
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        accountSettingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        accountSettingsViewModel.setContext(getActivity());
        billingHistoryViewModel = ViewModelProviders.of(this).get(BillingHistoryViewModel.class);
        billingHistoryViewModel.setContext(getActivity());
        billingHistoryViewModel.loadBillingHistoryFromLocalDB(selectedAccount.getAccountNumber());
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                this.user = user;
                accountSettingsViewModel.loadAccountSettingsFromLocalDB(selectedAccount.getAccountNumber());
                accountSettingsViewModel.getAccountSettingsData().observe(getViewLifecycleOwner(), accountSettings -> {
                    if (accountSettings == null) {
                        accountSettingsViewModel.getAccountSettingsFromServer(user.getEmail(), selectedAccount.getAccountNumber());
                    } else {
                        this.accountSettings = accountSettings;
                        accountSettingsViewModel.loadEnergyConsumptionsFromLocalDB(accountSettings.getAccountNumber());
                        accountSettingsViewModel.getEnergyConsumptions().observeForever(energyConsumptions -> {
                            this.energyConsumptions = energyConsumptions;
                            if (energyConsumptions != null) {
                                billingHistoryViewModel.getBillingHistory().observe(getViewLifecycleOwner(), billingHistory -> {
                                    if (billingHistory == null) {
                                        billingHistoryViewModel.getBillingHistoryFromServer(user, selectedAccount);
                                    } else {
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
        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        for (int i = 0; i < billingDetails.length; i++) {
            BillingDetails billingDetail = billingDetails[i];
            chartData = new ChartData();
            chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
            chartData.setVal(billingDetail.getBilledValue());
            chartDatum.add(chartData);
        }
        String startDate = Utils.formatAccountDate(billingDetails[0].getBilledDate());
        String endDate = Utils.formatAccountDate(billingDetails[billingDetails.length - 1].getBilledDate());

        barChart.setData(chartDatum)
                .setTitle(startDate + " - " + endDate)
                .setBarUnit("RM")
                .setThreshold(selectedAccount.isThreshold(), Float.parseFloat(energyConsumptions.getUserThreshold()))
                .setSelectionRequired(true);
    }

    private void subscribe() {
        ;
    }

    /*private void getBillingDetailsForAccount(ArrayList<Account> accounts) {
        if (user != null) {
            for (Account account : accounts) {
                dashboardViewModel.getBillingHistoryFromServer(user, account);
            }
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnManageConsumption:
                showQuestionsFragment();
                break;
            default:
                break;
        }
    }

    private void showQuestionsFragment() {
        FragmentUtils.newInstance(getFragmentManager()).addFragment(FragmentUtils.INDEX_QUESTIONS_FRAGMENT,
                selectedAccount, MMCQuestionsFragment.class.getName(), R.id.homeFlContainer);
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

    public void showProgress(boolean isProgress) {
        if (isProgress) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IS_THRESHOLD_SET) {
            accountSettingsViewModel.loadAccountSettingsFromLocalDB(selectedAccount.getAccountNumber());
            accountSettingsViewModel.getAccountSettingsData().observe(getViewLifecycleOwner(), accountSettings -> {
                if (accountSettings == null) {
                    accountSettingsViewModel.getAccountSettingsFromServer(user.getEmail(), selectedAccount.getAccountNumber());
                } else {
                    this.accountSettings = accountSettings;
                    accountSettingsViewModel.loadEnergyConsumptionsFromLocalDB(accountSettings.getAccountNumber());
                    accountSettingsViewModel.getEnergyConsumptions().observeForever(energyConsumptions -> {
                        this.energyConsumptions = energyConsumptions;
                        if (energyConsumptions != null) {
                            barChart.setThreshold(true, Float.parseFloat(energyConsumptions.getUserThreshold()));
                        }
                    });
                }
            });
        }
    }
}
