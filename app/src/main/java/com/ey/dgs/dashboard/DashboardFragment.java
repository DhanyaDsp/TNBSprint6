package com.ey.dgs.dashboard;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.AccountAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.User;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.FragmentUtils;

import java.util.ArrayList;

import static com.ey.dgs.utils.FragmentUtils.INDEX_MY_ACCOUNT;

public class DashboardFragment extends Fragment implements View.OnClickListener {

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

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        appPreferences = new AppPreferences(getActivity());
        initView();
        if (mListener != null) {
            mListener.onFragmentInteraction(getString(R.string.all_accounts));
        }
        return rootView;
    }

    private void initView() {
        subscribePopup = rootView.findViewById(R.id.subscribePopup);
        rvLeaves = rootView.findViewById(R.id.rvAccounts);
        rvLeaves.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvLeaves.setLayoutManager(rvLayoutManager);
        itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        tvSubscribe = rootView.findViewById(R.id.tvSubscribe);
        tvSubscribe.setOnClickListener(this);
        //itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.leave_divider));
        rvLeaves.addItemDecoration(itemDecorator);
        rvLeaves.setItemAnimator(new DefaultItemAnimator());
        accountAdapter = new AccountAdapter(this, getActivity(), accounts);
        rvLeaves.setAdapter(accountAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void subcribe() {
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        dashboardViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        dashboardViewModel.setContext(getActivity());
        loginViewModel.getUserDetail().observeForever(user -> {
            if (user != null) {
                this.user = user;
                if (this.user.isMmcAlertFlag()) {
                    subscribePopup.setVisibility(View.VISIBLE);
                } else {
                    subscribePopup.setVisibility(View.GONE);
                }
            }
        });
        //dashboardViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
        dashboardViewModel.getAccounts().observeForever(accounts -> {
            this.accounts.clear();
            this.accounts.addAll(accounts);
            Account addAccount = new Account();
            addAccount.setAccount(true);
            this.accounts.add(addAccount);
            if (accounts.size() > 0) {
                dashboardViewModel.setSelectedAccount(accounts.get(0));
            }
            accountAdapter.notifyDataSetChanged();
        });
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
        subcribe();
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
            default:
                break;
        }
    }

    private void moveToNotificationSettingsPage() {
        if (accounts != null && accounts.size() > 0) {
            Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
            intent.putExtra("isComingFromPopup", true);
            intent.putExtra("account", accounts.get(0));
            getActivity().startActivity(intent);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }
}
