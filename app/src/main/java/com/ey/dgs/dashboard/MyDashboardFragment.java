package com.ey.dgs.dashboard;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.dashboard.questions.MMCQuestionsFragment;
import com.ey.dgs.databinding.MyDashboardFragmentBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.User;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.DialogHelper;
import com.ey.dgs.utils.FragmentUtils;
import com.ey.dgs.views.BarChart;

import java.io.Serializable;
import java.util.ArrayList;

import static com.ey.dgs.utils.FragmentUtils.INDEX_MY_ACCOUNT;

public class MyDashboardFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_SET_THRESHOLD = 101;
    public static boolean IS_THRESHOLD_SET = false;
    private View rootView;
    ArrayList<Account> accounts = new ArrayList<>();
    private Context context;
    private DashboardViewModel dashboardViewModel;
    private OnFragmentInteractionListener mListener;
    AppPreferences appPreferences;
    private LoginViewModel loginViewModel;
    private User user;
    Account selectedAccount;
    private MyDashboardFragmentBinding myDashboardFragmentBinding;
    LinearLayout llManageBtns;
    AppCompatButton btnManageConsumption;
    RelativeLayout rlChart;
    BarChart barChart;
    View loader;
    private boolean billingDetailsServiceCalled;

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

        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        for (int i = 0; i < 8f; i++) {
            chartData = new ChartData();
            chartData.setTag("LB" + (i + 1));
            chartData.setVal((float)(i+1)*3);
            chartDatum.add(chartData);
        }

        barChart.setData(chartDatum)
                .setTitle("4 Apr - 24 Mar")
                .setBarUnit("RM")
                .setThreshold(true, 11.0f)
                .setSelectionRequired(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void subscribe() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.setContext(getActivity());
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            onUserDetailsLoaded(user);
        });
        dashboardViewModel.getLoaderData().observe(getViewLifecycleOwner(), this::showProgress);
        dashboardViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            if (accounts.size() > 0) {
                this.accounts.clear();
                this.accounts.addAll(accounts);
                for (Account account : this.accounts) {
                    if (account.isPrimaryAccount()) {
                        this.selectedAccount = account;
                        //user.setPrimaryAccountSet(true);
                    }
                }
                if (selectedAccount == null) {
                    selectedAccount = accounts.get(0);
                }

                myDashboardFragmentBinding.setSelectedAccount(selectedAccount);
                dashboardViewModel.setSelectedAccount(selectedAccount);
                if (!billingDetailsServiceCalled) {
                    getBillingDetailsForAccount(accounts);
                    billingDetailsServiceCalled = true;
                }
            }
        });
        dashboardViewModel.isPrimaryAccountSet().observe(getViewLifecycleOwner(), isPrimaryAccountSet -> {
            if (isPrimaryAccountSet) {
                for (Account account : accounts) {
                    if (!selectedAccount.getAccountNumber().equalsIgnoreCase(account.getAccountNumber())) {
                        if (account.isPrimaryAccount()) {
                            account.setPrimaryAccount(false);
                            dashboardViewModel.updateAccount(account);
                        }
                    } else {
                        account.setPrimaryAccount(true);
                        selectedAccount = account;
                    }
                }
                user.setPrimaryAccountSet(true);
                onUserDetailsLoaded(user);
                showPrimaryAccountPopup();
            }
        });
    }

    private void getBillingDetailsForAccount(ArrayList<Account> accounts) {
        if (user != null) {
            for (Account account : accounts) {
                dashboardViewModel.getBillingHistoryFromServer(user, account);
            }
        }
    }

    private void onUserDetailsLoaded(User user) {
    }


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
    public void onResume() {
        super.onResume();

        if (IS_THRESHOLD_SET) {
            loginViewModel.getUserDetail(appPreferences.getUser_id());
            loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
                this.user = user;
                this.user.setPrimaryAccountSet(true);
                onUserDetailsLoaded(this.user);
            });
            IS_THRESHOLD_SET = false;
        }
        //subscribe();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    public void openFragment(int index, Object obj) {
        if (index == INDEX_MY_ACCOUNT) {
            dashboardViewModel.setSelectedAccount((Account) obj);
            FragmentUtils.newInstance(((HomeActivity) getActivity()).getSupportFragmentManager())
                    .addFragment(index, obj, MyAccountFragment.class.getName(), R.id.homeFlContainer);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubscribe:
                moveToNotificationSettingsPage();
                break;
            case R.id.btnSetPrimaryAccount:
                setPrimaryAccount();
                break;
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

    private void setPrimaryAccount() {
        if (selectedAccount != null) {
            //dashboardViewModel.setPrimaryAccountInServer(user, selectedAccount);
            /*Demo*/
            user.setPrimaryAccountSet(true);
            selectedAccount.setPrimaryAccount(true);
            showPrimaryAccountPopup();
        }

    }

    private void showPrimaryAccountPopup() {
        DialogHelper.showPrimaryAccountDialog(selectedAccount, getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNotificationSettingsPage();
            }
        });
    }

    private void moveToNotificationSettingsPage() {
        if (accounts != null && accounts.size() > 0) {
            Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
            intent.putExtra("isComingFromPopup", true);
            intent.putExtra("account", (Serializable) selectedAccount);
            getActivity().startActivityForResult(intent, REQUEST_CODE_SET_THRESHOLD);
        }
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
}
