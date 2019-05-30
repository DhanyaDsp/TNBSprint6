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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.AccountAdapter;
import com.ey.dgs.adapters.AccountPagerAdapter;
import com.ey.dgs.adapters.AccountSpinnerAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.databinding.DashboardFragmentBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.User;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.DialogHelper;
import com.ey.dgs.utils.FragmentUtils;

import java.io.Serializable;
import java.util.ArrayList;

import static com.ey.dgs.utils.FragmentUtils.INDEX_MY_ACCOUNT;

public class DashboardFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener, AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE_SET_THRESHOLD = 101;
    public static boolean IS_THRESHOLD_SET = false;
    private View rootView;
    private RecyclerView rvLeaves;
    private LinearLayoutManager rvLayoutManager;
    ArrayList<Account> accounts = new ArrayList<>();
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
    AppCompatImageView ivBanner;
    View loader;
    private AccountPagerAdapter accountPagerAdapter;
    private boolean billingDetailsServiceCalled;
    AppCompatSpinner spAccounts;
    AccountSpinnerAdapter accountSpinnerAdapter;


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
        subscribe();
        return rootView;
    }

    private void initView() {
        loader = rootView.findViewById(R.id.loader);
        ivBanner = rootView.findViewById(R.id.ivBanner);
        btnSetPrimaryAccount = rootView.findViewById(R.id.btnSetPrimaryAccount);
        btnSetPrimaryAccount.setOnClickListener(this);
        subscribePopup = rootView.findViewById(R.id.subscribePopup);
        vpAccounts = rootView.findViewById(R.id.vpAccounts);
        vpAccounts.addOnPageChangeListener(this);
        rvLeaves = rootView.findViewById(R.id.rvAccounts);
        rvLeaves.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvLeaves.setLayoutManager(rvLayoutManager);
        itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        tvSubscribe = rootView.findViewById(R.id.tvSubscribe);
        tvSubscribe.setOnClickListener(this);
        rvLeaves.addItemDecoration(itemDecorator);
        rvLeaves.setItemAnimator(new DefaultItemAnimator());
        if (mListener != null) {
            mListener.onFragmentInteraction("");
        }
        accountPagerAdapter = new AccountPagerAdapter(this, getActivity(), this.accounts);
        vpAccounts.setAdapter(accountPagerAdapter);
        spAccounts = rootView.findViewById(R.id.spAccounts);
        spAccounts.setOnItemSelectedListener(this);
        accountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(), this.accounts);
        spAccounts.setAdapter(accountSpinnerAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void subscribe() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        dashboardViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        dashboardViewModel.setContext(getActivity());
        showProgress(true);
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            onUserDetailsLoaded(user);
        });
        dashboardViewModel.getLoaderData().observe(getViewLifecycleOwner(), showProgress -> {
            showProgress(showProgress);
        });
        dashboardViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
        dashboardViewModel.getAccounts().observeForever(accounts -> {
            showProgress(false);
            if (accounts.size() > 0) {
                this.accounts.clear();
                this.accounts.addAll(accounts);
                for (Account account : this.accounts) {
                    if (account.isPrimaryAccount()) {
                        this.selectedAccount = account;
                        vpAccounts.setCurrentItem(accounts.indexOf(account));
                        spAccounts.setSelection(accounts.indexOf(account));
                    }
                }
                accountPagerAdapter.notifyDataSetChanged();
                accountSpinnerAdapter.notifyDataSetChanged();
                if (selectedAccount == null) {
                    selectedAccount = accounts.get(0);
                }

                loginFragmentBinding.setSelectedAccount(selectedAccount);
                dashboardViewModel.setSelectedAccount(selectedAccount);
                if (!billingDetailsServiceCalled) {
                    getBillingDetailsForAccount(accounts);
                    billingDetailsServiceCalled = true;
                }
            }
        });
        /*MOCK RESPONSE UNCOMMENT THIS*/
        dashboardViewModel.isPrimaryAccountSet().observe(getViewLifecycleOwner(), isPrimaryAccountSet -> {
            if (isPrimaryAccountSet) {

                //updateOtherAccounts(accounts);
                user.setPrimaryAccountSet(true);
                loginViewModel.update(user);
                //onUserDetailsLoaded(user);
                showProgress(false);
                showPrimaryAccountPopup();
            }
        });
        /*MOCK RESPONSE*/
    }

    private void updateOtherAccounts(ArrayList<Account> accounts) {
        selectedAccount.setPrimaryAccount(true);
        for (Account account : accounts) {
            if (!selectedAccount.getAccountNumber().equalsIgnoreCase(account.getAccountNumber())) {
                if (account.isPrimaryAccount()) {
                    account.setPrimaryAccount(false);
                    dashboardViewModel.updateAccount(account);
                }
            } else {
                account.setPrimaryAccount(true);
                dashboardViewModel.setPrimaryAccount(account);
                selectedAccount = account;
            }
        }
    }

    private void getBillingDetailsForAccount(ArrayList<Account> accounts) {
        if (user != null) {
            for (Account account : accounts) {
                dashboardViewModel.getBillingHistoryFromServer(user, account);
            }
        }
    }

    private void onUserDetailsLoaded(User user) {
        if (user != null) {
            this.user = user;
            loginFragmentBinding.setUser(this.user);
            if (user.isPrimaryAccountSet()) {
                btnSetPrimaryAccount.setVisibility(View.GONE);
            } else {
                btnSetPrimaryAccount.setVisibility(View.VISIBLE);
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
            default:
                break;
        }
    }

    private void setPrimaryAccount() {
        if (selectedAccount != null) {
            showProgress(true);
            dashboardViewModel.setPrimaryAccountInServer(user, selectedAccount);
            /*Demo*/
            /*user.setPrimaryAccountSet(true);
            loginViewModel.update(user);
            updateOtherAccounts(accounts);
            showPrimaryAccountPopup();*/
        }

    }

    private void showPrimaryAccountPopup() {
        DialogHelper.showPrimaryAccountDialog(selectedAccount, getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.hidePopup();
                moveToMyDashboardPage(selectedAccount);
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

    public void moveToMyDashboardPage(Account selectedAccount) {
        if (selectedAccount != null) {
           /* Intent intent = new Intent(getActivity(), QuestionActivity.class);
            intent.putExtra("account", (Serializable) selectedAccount);
            getActivity().startActivity(intent);*/
            FragmentUtils.newInstance(getFragmentManager())
                    .addFragment(FragmentUtils.INDEX_MY_DASHBOARD_FRAGMENT, selectedAccount, MyDashboardFragment.class.getName(), R.id.homeFlContainer);

        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        selectedAccount = accounts.get(i);
        loginFragmentBinding.setSelectedAccount(selectedAccount);
        spAccounts.setOnItemSelectedListener(null);
        spAccounts.setSelection(i);
        spAccounts.setOnItemSelectedListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedAccount = accounts.get(position);
        vpAccounts.addOnPageChangeListener(null);
        vpAccounts.setCurrentItem(position);
        vpAccounts.addOnPageChangeListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
    }
}
