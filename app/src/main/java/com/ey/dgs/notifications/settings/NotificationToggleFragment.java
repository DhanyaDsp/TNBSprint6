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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.NotificationAccountAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.DashboardViewModel;
import com.ey.dgs.databinding.FragmentNotificationToggleBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.AppPreferences;

import java.util.ArrayList;

public class NotificationToggleFragment extends Fragment {

    private View view;
    private RecyclerView rvAccounts;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<Account> accounts = new ArrayList<>();
    private NotificationAccountAdapter accountAdapter;
    private DashboardViewModel mViewModel;
    private LoginViewModel loginViewModel;
    User user = new User();
    FragmentNotificationToggleBinding binding;
    private OnFragmentInteractionListener mListener;
    private AppPreferences appPreferences;
    AppCompatTextView tvLabel;

    public NotificationToggleFragment() {
    }

    public static NotificationToggleFragment newInstance() {
        return new NotificationToggleFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_toggle, container, false);
        binding.setFragment(this);
        user.setNotificationFlag(true);
        binding.setUser(user);
        view = binding.getRoot();
        appPreferences = new AppPreferences(getActivity());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        if (mListener != null) {
            mListener.onFragmentInteraction("Notifications");
        }
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
        mViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        loginViewModel.getUserDetail().observeForever(user -> {
            this.user = user;
            binding.setUser(this.user);
            if (this.user.isNotificationFlag()) {
                rvAccounts.setVisibility(View.VISIBLE);
                tvLabel.setVisibility(View.VISIBLE);
            } else {
                rvAccounts.setVisibility(View.GONE);
                tvLabel.setVisibility(View.GONE);
            }
        });
        mViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
        mViewModel.getAccounts().observeForever(accounts -> {
            this.accounts.clear();
            this.accounts.addAll(accounts);
            accountAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void toggleNotification(View view, boolean isChecked) {
        user.setNotificationFlag(isChecked);
        loginViewModel.updateUserInServer(user);
        if (isChecked) {
            rvAccounts.setVisibility(View.VISIBLE);
            tvLabel.setVisibility(View.VISIBLE);
        } else {
            rvAccounts.setVisibility(View.GONE);
            tvLabel.setVisibility(View.GONE);
        }
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

}
