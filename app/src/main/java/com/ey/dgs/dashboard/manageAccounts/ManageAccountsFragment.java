package com.ey.dgs.dashboard.manageAccounts;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.ManageAccountsAdapter;
import com.ey.dgs.dashboard.DashboardViewModel;
import com.ey.dgs.model.Account;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.FragmentUtils;

import java.util.ArrayList;

import static com.ey.dgs.utils.FragmentUtils.INDEX_MANAGE_ACCOUNTS_QUESTIONS;

public class ManageAccountsFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private RecyclerView rvAccounts;
    private ManageAccountsAdapter accountsAdapter;
    private LinearLayoutManager rvLayoutManager;
    private Context context;
    private ArrayList<Account> accounts = new ArrayList<>();
    AppPreferences appPreferences;
    private DashboardViewModel dashboardViewModel;

    public static ManageAccountsFragment newInstance() {
        return new ManageAccountsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manage_account_fragment, container, false);
        appPreferences = new AppPreferences(getActivity());
        initViews();
        subscribe();
        return rootView;
    }

    private void subscribe() {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
        dashboardViewModel.getAccounts().observeForever(accounts -> {
            this.accounts.clear();
            Account addAccount = new Account();
            addAccount.setNeededAccount(true);
            accounts.add(addAccount);
            this.accounts.addAll(accounts);
            accountsAdapter.notifyDataSetChanged();
        });
    }

    private void initViews() {
        rvAccounts = rootView.findViewById(R.id.rvManageAccounts);
        rvAccounts.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvAccounts.setLayoutManager(rvLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvAccounts.addItemDecoration(itemDecorator);
        accountsAdapter = new ManageAccountsAdapter(this, getActivity() ,accounts);
        rvAccounts.setAdapter(accountsAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    private ArrayList<String> getPeopleInProperty(ArrayList<String> accountNumbers) {
        ArrayList<String> peopleInProperty = new ArrayList<>();
        for(String accountNo: accountNumbers) {
            for(Account account: accounts) {
                if(accountNo == account.getAccountNumber()) {
                    peopleInProperty.add(account.getPeopleInProperty());
                }
            }
        }
        return peopleInProperty;
    }

    private ArrayList<String> getThresholdValue(ArrayList<String> accountNumbers) {
        ArrayList<String> thresholdValue = new ArrayList<>();
        for(String accountNo: accountNumbers) {
            for(Account account: accounts) {
                if(accountNo == account.getAccountNumber()) {
                    thresholdValue.add(account.getUserThreshold());
                }
            }
        }
        return  thresholdValue;
    }

    public void openManageAccountsFragment(int index, ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        if (index == INDEX_MANAGE_ACCOUNTS_QUESTIONS) {
            getPeopleInProperty(accountNumber);
            getThresholdValue(accountNumber);
            FragmentUtils.newInstance(((HomeActivity) getActivity()).getSupportFragmentManager())
                    .addFragment(index, nicknames, accountNumber, getPeopleInProperty(accountNumber),
                            getThresholdValue(accountNumber), MMCManageAccountsFragment.class.getName(),
                            R.id.homeFlContainer);
        }
    }
}
