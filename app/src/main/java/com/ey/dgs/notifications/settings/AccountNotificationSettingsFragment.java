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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.NotificationSettingsAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.myaccount.AccountSettingsViewModel;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.databinding.FragmentAccountNotificationSettingsBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.NotificationSetting;
import com.ey.dgs.model.User;

import java.io.Serializable;
import java.util.ArrayList;

public class AccountNotificationSettingsFragment extends Fragment {

    private View view;
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
        initViews();
        accountSettingsViewModel = ViewModelProviders.of(getActivity()).get(AccountSettingsViewModel.class);
        account = (Account) getArguments().getSerializable("account");
        accountSettingsViewModel.loadAccountSettingsFromLocalDB(account.getAccountNumber());
        accountSettingsViewModel.getAccountSettingsData().observeForever(accountSettings -> {
            this.accountSettings = accountSettings;
            notificationSettingsAdapter.setAccountSettings(accountSettings);
            notificationSettingsAdapter.notifyDataSetChanged();
            if (accountSettings == null) {
                accountSettingsViewModel.getNotificationsFromServer(account.getAccountNumber());
            }
        });
        return binding.getRoot();
    }

    private void initViews() {
        if (mListener != null) {
            mListener.onFragmentInteraction("My Account");
        }
        rvNotificationSettings = view.findViewById(R.id.rvNotificationSettings);
        rvNotificationSettings.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvNotificationSettings.setLayoutManager(rvLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvNotificationSettings.addItemDecoration(itemDecorator);
        rvNotificationSettings.setItemAnimator(new DefaultItemAnimator());
        notificationSettingsAdapter = new NotificationSettingsAdapter(rvNotificationSettings, this, getActivity(), notificationSettings);
        rvNotificationSettings.setAdapter(notificationSettingsAdapter);
        mViewModel = ViewModelProviders.of(this).get(NotificationSettingsViewModel.class);
        mViewModel.getNotificationSettings().observeForever(notificationSettings -> {
            this.notificationSettings.clear();
            this.notificationSettings.addAll(notificationSettings);
            notificationSettingsAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

}
