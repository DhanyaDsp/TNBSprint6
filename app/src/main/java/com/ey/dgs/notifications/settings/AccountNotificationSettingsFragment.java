package com.ey.dgs.notifications.settings;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.NotificationSettingsAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.dashboard.DashboardViewModel;
import com.ey.dgs.dashboard.MyDashboardFragment;
import com.ey.dgs.dashboard.myaccount.AccountSettingsViewModel;
import com.ey.dgs.databinding.FragmentAccountNotificationSettingsBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.NotificationSetting;
import com.ey.dgs.model.NotificationSettingsRequest;
import com.ey.dgs.model.Setting;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class AccountNotificationSettingsFragment extends Fragment {

    private View view, loader;
    private RecyclerView rvNotificationSettings;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<NotificationSetting> notificationSettings = new ArrayList<>();
    private NotificationSettingsAdapter notificationSettingsAdapter;
    private NotificationSettingsViewModel mViewModel;
    User user = new User();
    FragmentAccountNotificationSettingsBinding binding;
    private OnFragmentInteractionListener mListener;
    Account account;
    AccountSettings accountSettings;
    AccountSettingsViewModel accountSettingsViewModel;
    LoginViewModel loginViewModel;
    DashboardViewModel dashboardViewModel;
    private EnergyConsumptions energyConsumptions;
    AppPreferences appPreferences;
    Activity activity;
    private boolean isProgressing;
    private boolean serverCalled;
    private ArrayList<NotificationSetting> notificationSettingsCopy;

    public AccountNotificationSettingsFragment() {
    }

    public static AccountNotificationSettingsFragment newInstance(Account account) {
        AccountNotificationSettingsFragment fragment = new AccountNotificationSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable("account", (Serializable) account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_notification_settings, container, false);
        binding.setFragment(this);
        view = binding.getRoot();
        account = (Account) getArguments().getSerializable("account");
        appPreferences = new AppPreferences(getActivity());
        initViews();
        accountSettingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        accountSettingsViewModel.setContext(getActivity());
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setContext(getActivity());
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.setContext(getActivity());
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
        });
        showProgress(true);
        accountSettingsViewModel.loadAccountSettingsFromLocalDB(account.getAccountNumber());
        accountSettingsViewModel.getAccountSettingsData().observe(getViewLifecycleOwner(), accountSettings -> {
            if (accountSettings == null) {
                accountSettingsViewModel.getAccountSettingsFromServer(user.getEmail(), account.getAccountNumber());
            } else {
                this.accountSettings = accountSettings;
                accountSettingsViewModel.loadEnergyConsumptionsFromLocalDB(accountSettings.getAccountNumber());
                accountSettingsViewModel.getEnergyConsumptions().observe(getViewLifecycleOwner(), energyConsumptions -> {
                    this.energyConsumptions = energyConsumptions;
                    if (energyConsumptions != null) {
                        notificationSettingsAdapter = new NotificationSettingsAdapter(rvNotificationSettings, this, getActivity(), notificationSettings);
                        notificationSettingsAdapter.setNotificationSettings(notificationSettingsCopy);
                        notificationSettingsAdapter.setAccountSettings(this.accountSettings);
                        notificationSettingsAdapter.setEnergyConsumptions(this.energyConsumptions);
                        rvNotificationSettings.setAdapter(notificationSettingsAdapter);
                        showProgress(false);
                    }
                });
                if (!serverCalled) {
                    accountSettingsViewModel.getAccountSettingsFromServer(user.getEmail(), account.getAccountNumber());
                    serverCalled = true;
                }
            }
        });
        accountSettingsViewModel.getIsAccountDetailsUpdated().observe(getViewLifecycleOwner(), isUserUpdated -> {
            showProgress(false);
            if (isUserUpdated) {
                notificationSettingsAdapter.setUpdated(true);
                if (activity != null) {
                    account.setThreshold(true);
                    dashboardViewModel.updateAccount(account);
                    Utils.hideKeyBoard(activity);
                    DashboardFragment.IS_THRESHOLD_SET = true;
                    MyDashboardFragment.IS_THRESHOLD_SET = true;
                    ((NotificationSettingsActivity) activity).onBackPressed();
                }
            } else {

                Utils.showToast(getActivity(), "Failed to Update Settings");
            }
        });
        accountSettingsViewModel.getLoaderData().observe(getViewLifecycleOwner(), showProgress -> {
            showProgress(showProgress);
        });
        return binding.getRoot();
    }

    private void initViews() {
        if (mListener != null) {
            mListener.onFragmentInteraction("My Account");
        }
        ((NotificationSettingsActivity) getActivity()).getSupportActionBar().setTitle(account.getNickName());
        rvNotificationSettings = view.findViewById(R.id.rvNotificationSettings);
        rvNotificationSettings.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvNotificationSettings.setLayoutManager(rvLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvNotificationSettings.addItemDecoration(itemDecorator);
        rvNotificationSettings.setItemAnimator(new DefaultItemAnimator());
        loader = view.findViewById(R.id.loader);
        mViewModel = ViewModelProviders.of(this).get(NotificationSettingsViewModel.class);
        mViewModel.getNotificationSettings().observe(getViewLifecycleOwner(), notificationSettings -> {
            if (notificationSettings != null) {
                this.notificationSettings.clear();
                this.notificationSettings.addAll(notificationSettings);
                this.notificationSettingsCopy = notificationSettings;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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

    public void updateAccountDetails() {
        Utils.hideKeyBoard(getActivity());
        if (!isProgressing) {
            AccountSettings accountSettings = notificationSettingsAdapter.getAccountSettings();
            EnergyConsumptions energyConsumptions;
            energyConsumptions = notificationSettingsAdapter.getEnergyConsumptions();
            NotificationSettingsRequest notificationSettingsRequest = new NotificationSettingsRequest();
            notificationSettingsRequest.setUserName(user.getEmail());
            notificationSettingsRequest.setAccountNumber(accountSettings.getAccountNumber());
            Setting setting = new Setting();
            setting.setEnergyConsumptions(energyConsumptions);
            setting.setPushNotificationFlag(accountSettings.isPushNotificationFlag());
            setting.setServiceAvailabilityFlag(accountSettings.isServiceAvailabilityFlag());
            setting.setSmsNotificationFlag(accountSettings.isSmsNotificationFlag());
            notificationSettingsRequest.setSetting(setting);
            showProgress(true);
            if (!TextUtils.isEmpty(notificationSettingsRequest.getSetting().getEnergyConsumptions().getUserThreshold())
                    && Integer.parseInt(notificationSettingsRequest.getSetting().getEnergyConsumptions().getUserThreshold()) > 0) {
                accountSettingsViewModel.updateAccountSettingsInServer(notificationSettingsRequest);
            } else {
                Utils.showToast(activity, "Please Enter a Threshold Value Great than Zero and Non Empty");
            }
            if (notificationSettingsAdapter.isThresholdChanged()) {
                user.setMmcAlertFlag(false);
                loginViewModel.updateUserInServer(user);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

    public NotificationSettingsAdapter getNotificationSettingsAdapter() {
        return notificationSettingsAdapter;
    }


    public void showProgress(boolean isVisible) {
        isProgressing = isVisible;
        if (isVisible) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }

    public boolean isThresholdOrSubscribe() {
        return ((NotificationSettingsActivity) getActivity()).isAddThreshold
                || ((NotificationSettingsActivity) getActivity()).isComingFromPopup;
    }


}

