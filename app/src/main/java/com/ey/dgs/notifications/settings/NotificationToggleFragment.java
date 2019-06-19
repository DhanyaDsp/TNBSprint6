package com.ey.dgs.notifications.settings;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ey.dgs.R;
import com.ey.dgs.adapters.NotificationAccountAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.dashboard.DashboardViewModel;
import com.ey.dgs.dashboard.MyDashboardFragment;
import com.ey.dgs.dashboard.myaccount.AccountSettingsViewModel;
import com.ey.dgs.databinding.FragmentNotificationToggleBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.NotificationSettingsRequest;
import com.ey.dgs.model.Setting;
import com.ey.dgs.model.User;
import com.ey.dgs.model.UserSettings;
import com.ey.dgs.usersettings.UserSettingsViewModel;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;

import java.util.ArrayList;

public class NotificationToggleFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private View view;
    private RecyclerView rvAccounts;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<Account> accounts = new ArrayList<>();
    private NotificationAccountAdapter accountAdapter;
    private DashboardViewModel mViewModel;
    UserSettingsViewModel userSettingsViewModel;
    AccountSettingsViewModel accountSettingsViewModel;
    User user;
    UserSettings userSettings, editedUserSettings;
    FragmentNotificationToggleBinding binding;
    private OnFragmentInteractionListener mListener;
    private AppPreferences appPreferences;
    AppCompatTextView tvLabel;
    SwitchCompat scPush, scSMS;
    private boolean isProgressing;

    public NotificationToggleFragment() {
    }

    public static NotificationToggleFragment newInstance() {
        return new NotificationToggleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences = new AppPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_toggle, container, false);
        binding.setFragment(this);
        binding.setUser(user);
        view = binding.getRoot();
        initViews();
        return view;
    }

    private void initViews() {
        if (mListener != null) {
            mListener.onFragmentInteraction("Notifications");
        }
        scPush = view.findViewById(R.id.scPush);
        scSMS = view.findViewById(R.id.scSMS);/*
        scPush.setOnCheckedChangeListener(this);
        scSMS.setOnCheckedChangeListener(this);*/
        tvLabel = view.findViewById(R.id.tvLabel);
        rvAccounts = view.findViewById(R.id.rvAccounts);
        rvAccounts.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvAccounts.setLayoutManager(rvLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvAccounts.addItemDecoration(itemDecorator);
        rvAccounts.setItemAnimator(new DefaultItemAnimator());
        accountAdapter = new NotificationAccountAdapter(this, getActivity(), accounts);
        rvAccounts.setAdapter(accountAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribe();
    }

    private void subscribe() {
        userSettingsViewModel = ViewModelProviders.of(this).get(UserSettingsViewModel.class);
        accountSettingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        accountSettingsViewModel.getLoaderData().observe(getViewLifecycleOwner(), show -> {
            ((NotificationSettingsActivity) getActivity()).showProgress(show);
        });
        userSettingsViewModel.getUserSettingsFromLocalDB(appPreferences.getUser_id());
        userSettingsViewModel.getUserSettings().observe(getViewLifecycleOwner(), userSettings -> {
            if (userSettings != null) {
                this.userSettings = userSettings;
                editedUserSettings = userSettings;
                binding.setUserSettings(userSettings);
            }
        });
        mViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        mViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
        mViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            this.accounts.clear();
            this.accounts.addAll(accounts);
            accountAdapter.notifyDataSetChanged();
        });
        accountSettingsViewModel.getIsAccountSettingsUpdated().observe(getViewLifecycleOwner(), isUserUpdated -> {
            isProgressing = false;
            if (isUserUpdated) {
                Utils.showToast(getActivity(),"User Settings Updated");
                getActivity().onBackPressed();
            } else {
            }
        });
    }

    public void togglePushNotification(View view, boolean isChecked) {
        editedUserSettings.setPushNotificationFlag(isChecked);
    }

    public void toggleSMSNotification(View view, boolean isChecked) {
        editedUserSettings.setSmsNotificationFlag(isChecked);
    }

    public void updateUserSettings() {
        if (!isProgressing) {
            accountSettingsViewModel.updateUserSettingsInServer(appPreferences.getAuthToken(), accounts, editedUserSettings);
            isProgressing = true;
        }
    }

    private void updateAllAccountSettings(ArrayList<Account> accounts) {
        for (Account account : accounts) {
            NotificationSettingsRequest notificationSettingsRequest = new NotificationSettingsRequest();
            notificationSettingsRequest.setUserName(user.getEmail());
            notificationSettingsRequest.setAccountNumber(account.getAccountNumber());
            Setting setting = new Setting();
            setting.setPushNotificationFlag(editedUserSettings.isPushNotificationFlag());
            setting.setSmsNotificationFlag(editedUserSettings.isSmsNotificationFlag());
            notificationSettingsRequest.setSetting(setting);
            accountSettingsViewModel.updateAccountSettingsInServer(notificationSettingsRequest);
        }

    }

    private void toggleSMSForAllAccounts(boolean b) {

    }

    public void moveToNotificationSettings(Account account) {
        ((NotificationSettingsActivity) getActivity()).moveToNotificationSettings(account);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.scPush:
                togglePushNotification(buttonView, isChecked);
                break;
            case R.id.scSMS:
                toggleSMSNotification(buttonView, isChecked);
                break;
            default:
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

}
