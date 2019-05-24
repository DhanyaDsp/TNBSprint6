package com.ey.dgs.dashboard;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.notifications.NotificationViewModel;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardViewModel extends ViewModel implements DatabaseCallback {

    private MutableLiveData<ArrayList<Account>> accounts = new MutableLiveData<>();
    private MutableLiveData<Account> selectedAccount = new MutableLiveData<>();
    private NotificationViewModel notificationViewModel;
    private Context context;
    private AppPreferences appPreferences;

    public DashboardViewModel() {
        if (accounts == null) {
            accounts = new MutableLiveData<ArrayList<Account>>();
        }
    }

    public void loadAccountsFromLocalDB(int user_id) {
        DatabaseClient.getInstance(context).getAccounts(Account.REQUEST_CODE_ADD_ACCOUNTS, user_id, this);
    }

    public LiveData<ArrayList<Account>> getAccounts() {
        return accounts;
    }

    private void setAccounts(ArrayList<Account> accounts) {
        this.accounts.postValue(accounts);
    }

    public void setContext(Context context) {
        this.context = context;
        appPreferences = new AppPreferences(context);
    }

    public Context getContext() {
        return context;
    }

    public MutableLiveData<Account> getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount.postValue(selectedAccount);
    }

    public void addAccountsToLocalDB(List<Account> accounts) {
        for (Account account : accounts) {
            account.setUser_id(appPreferences.getUser_id());
        }
        DatabaseClient.getInstance(context).addAccounts(Account.REQUEST_CODE_ADD_ACCOUNTS, accounts, this);
    }


    @Override
    public void onInsert(Object object, int requestCode, int responseCode) {
        if (requestCode == Account.REQUEST_CODE_ADD_ACCOUNTS) {
            if (object != null) {
                //ArrayList<Account> accounts =object;
                ArrayList<Account> accounts = new ArrayList((List<Account>) object);
                //ArrayList<Account> accounts = new ArrayList<Account>(Arrays.asList(object));
                setAccounts(accounts);
                //setupNotifications(accounts);
            }
        }
    }

    private void setupNotifications(ArrayList<Account> accounts) {
        notificationViewModel = ViewModelProviders.of((FragmentActivity) context).get(NotificationViewModel.class);
        appPreferences = new AppPreferences(context);
        for (Account account : accounts) {
            notificationViewModel.addDummyNotifications(account.getAccountId(), appPreferences.getUser_id());
        }
    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {
        if (requestCode == Account.REQUEST_CODE_UPDATE_ACCOUNT) {
        }
    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        if (requestCode == Account.REQUEST_CODE_ADD_ACCOUNTS) {
            setAccounts((ArrayList<Account>) object);
        }
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }

    public void addDummyAccounts(int user_id) {
        /*ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1, "Home", "20-09-2009", "109", user_id, false));
        accounts.add(new Account(2, "Saujana Alam Indiah 2 ", "20-09-2009", "110", user_id, false));
        accounts.add(new Account(3, "Satori Residence", "20-09-2009", "89", user_id, false));
        addAccountsToLocalDB(accounts);*/
    }

    public void updateAccount(Account selectedAccount) {
        DatabaseClient.getInstance(context).updateAccount(Account.REQUEST_CODE_UPDATE_ACCOUNT, selectedAccount, this);
    }
}
