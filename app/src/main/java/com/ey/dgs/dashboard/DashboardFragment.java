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
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.AccountAdapter;
import com.ey.dgs.adapters.AccountPagerAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.dashboard.questions.QuestionActivity;
import com.ey.dgs.databinding.DashboardFragmentBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.User;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.DialogHelper;
import com.ey.dgs.utils.FragmentUtils;
import com.ey.dgs.views.BarChart;

import java.util.ArrayList;

import static com.ey.dgs.utils.FragmentUtils.INDEX_MY_ACCOUNT;

public class DashboardFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View rootView;
    private RecyclerView rvLeaves;
    private LinearLayoutManager rvLayoutManager;
    ArrayList<Account> accounts = new ArrayList<>();
    AccountAdapter accountAdapter;
    private DividerItemDecoration itemDecorator;
    private Context context;
    private DashboardViewModel dashboardViewModel;
    private OnFragmentInteractionListener mListener;
    AppCompatTextView tvSubscribe;
    AppPreferences appPreferences;
    private LoginViewModel loginViewModel;
    private User user;
    View subscribePopup;
    AppCompatButton btnSetPrimaryAccount;
    Account selectedAccount;
    ViewPager vpAccounts;
    private DashboardFragmentBinding loginFragmentBinding;
    LinearLayout llManageBtns;
    AppCompatButton btnManageConsumption;
    AppCompatImageView ivBanner;
    RelativeLayout rlChart;
    BarChart barChart;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false);
        rootView = loginFragmentBinding.getRoot();
        loginFragmentBinding.setSelectedAccount(selectedAccount);
        appPreferences = new AppPreferences(getActivity());
        initView();
        return rootView;
    }

    private void initView() {
        ivBanner = rootView.findViewById(R.id.ivBanner);
        rlChart = rootView.findViewById(R.id.rlChart);
        barChart = rootView.findViewById(R.id.bar_chart);
        btnSetPrimaryAccount = rootView.findViewById(R.id.btnSetPrimaryAccount);
        btnSetPrimaryAccount.setOnClickListener(this);
        subscribePopup = rootView.findViewById(R.id.subscribePopup);
        btnManageConsumption = rootView.findViewById(R.id.btnManageConsumption);
        btnManageConsumption.setOnClickListener(this);
        vpAccounts = rootView.findViewById(R.id.vpAccounts);
        vpAccounts.addOnPageChangeListener(this);
        rvLeaves = rootView.findViewById(R.id.rvAccounts);
        rvLeaves.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvLeaves.setLayoutManager(rvLayoutManager);
        itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        tvSubscribe = rootView.findViewById(R.id.tvSubscribe);
        tvSubscribe.setOnClickListener(this);
        llManageBtns = rootView.findViewById(R.id.llManageBtns);
        //itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.leave_divider));
        rvLeaves.addItemDecoration(itemDecorator);
        rvLeaves.setItemAnimator(new DefaultItemAnimator());
        accountAdapter = new AccountAdapter(this, getActivity(), accounts);
        rvLeaves.setAdapter(accountAdapter);
        if (mListener != null) {
            mListener.onFragmentInteraction("");
        }

        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        for(int i=0; i<6; i++){
            chartData = new ChartData();
            chartData.setTag("LB" + (i+1));
            chartData.setVal(20.0f);
            chartDatum.add(chartData);
        }

        barChart.setData(chartDatum).setTitle("Chart_Title");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void subscribe() {
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        dashboardViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        dashboardViewModel.setContext(getActivity());
        loginViewModel.getUserDetail().observeForever(user -> {
            onUserDetailsLoaded(user);
        });
        //dashboardViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
        dashboardViewModel.getAccounts().observeForever(accounts -> {
            if (accounts.size() > 0) {
                this.accounts.clear();
                this.accounts.addAll(accounts);
                Account addAccount = new Account();
                addAccount.setAccount(true);
                this.accounts.add(addAccount);
                if (user.isPrimaryAccount()) {
                    for (Account account : this.accounts) {
                        if (account.isPrimaryAccount()) {
                            this.selectedAccount = account;
                            if (!user.isPrimaryAccount()) {
                                user.setPrimaryAccount(true);
                                loginViewModel.update(user);
                            }
                        }
                    }
                } else {
                    this.selectedAccount = accounts.get(0);
                }
                loginFragmentBinding.setSelectedAccount(selectedAccount);
                dashboardViewModel.setSelectedAccount(selectedAccount);
                accountAdapter.notifyDataSetChanged();
                AccountPagerAdapter accountPagerAdapter = new AccountPagerAdapter(getActivity(), this.accounts);
                vpAccounts.setAdapter(accountPagerAdapter);
            }
        });
    }

    private void onUserDetailsLoaded(User user) {
        if (user != null) {
            this.user = user;
            loginFragmentBinding.setUser(this.user);
            if (this.user.isMmcAlertFlag()) {
                subscribePopup.setVisibility(View.GONE);
            } else {
                subscribePopup.setVisibility(View.GONE);
            }
            if (this.user.isPrimaryAccount()) {
                btnSetPrimaryAccount.setVisibility(View.INVISIBLE);
                llManageBtns.setVisibility(View.VISIBLE);
                ivBanner.setVisibility(View.INVISIBLE);
                rlChart.setVisibility(View.VISIBLE);
                vpAccounts.setVisibility(View.GONE);
            } else {
                btnSetPrimaryAccount.setVisibility(View.VISIBLE);
                llManageBtns.setVisibility(View.INVISIBLE);
                ivBanner.setVisibility(View.VISIBLE);
                rlChart.setVisibility(View.GONE);
                vpAccounts.setVisibility(View.VISIBLE);
            }
        }
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
        subscribe();
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
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        startActivity(intent);
    }

    private void setPrimaryAccount() {
        if (selectedAccount != null) {
            selectedAccount.setPrimaryAccount(true);
            dashboardViewModel.updateAccount(selectedAccount);
            user.setPrimaryAccount(true);
            loginViewModel.update(user);
        }
        showPrimaryAccountPopup();
    }

    private void showPrimaryAccountPopup() {
        new DialogHelper().showPrimaryAccountDialog(selectedAccount, getActivity(), new View.OnClickListener() {
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
            intent.putExtra("account", accounts.get(0));
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        selectedAccount = accounts.get(i);
        loginFragmentBinding.setSelectedAccount(selectedAccount);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }
}
