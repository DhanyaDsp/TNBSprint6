package com.ey.dgs.dashboard.myaccount;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.notifications.NotificationViewModel;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class AccountSettingsViewModel extends ViewModel implements DatabaseCallback, APICallback {

    private MutableLiveData<AccountSettings> accountSettingsData = new MutableLiveData<>();
    private Context context;
    private AppPreferences appPreferences;

    public AccountSettingsViewModel() {

    }

    public void loadAccountSettingsFromLocalDB(String accountNumber) {
        DatabaseClient.getInstance(context).getAccountSettings(Account.REQUEST_CODE_ADD_ACCOUNTS, accountNumber, this);
    }

    public LiveData<AccountSettings> getAccountSettings() {
        return accountSettingsData;
    }

    private void setAccountSettings(AccountSettings accountSettings) {
        this.accountSettingsData.postValue(accountSettings);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public MutableLiveData<AccountSettings> getAccountSettingsData() {
        return accountSettingsData;
    }

    public void addAccountSettingsToLocalDB(AccountSettings accountSettings) {
        DatabaseClient.getInstance(context).addAccountSettings(AccountSettings.REQUEST_CODE_ADD_ACCOUNT_SETTINGS, accountSettings, this);
    }

    public void getNotificationsFromServer(String accountNumber) {
        new ApiClient().getAccountSettingsFromServer(accountNumber, this);
    }

    @Override
    public void onInsert(Object object, int requestCode, int responseCode) {
        if (requestCode == AccountSettings.REQUEST_CODE_ADD_ACCOUNT_SETTINGS) {
            setAccountSettings((AccountSettings) object);
        }
    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {

    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        setAccountSettings((AccountSettings) object);
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        AccountSettings accountSettings = (AccountSettings) obj;
        addAccountSettingsToLocalDB(accountSettings);
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {

    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }
}
