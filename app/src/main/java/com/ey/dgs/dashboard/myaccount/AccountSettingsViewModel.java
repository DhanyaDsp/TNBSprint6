package com.ey.dgs.dashboard.myaccount;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.NotificationSettingsRequest;
import com.ey.dgs.model.UserSettings;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import java.util.ArrayList;

public class AccountSettingsViewModel extends ViewModel implements DatabaseCallback, APICallback {

    private MutableLiveData<AccountSettings> accountSettingsData = new MutableLiveData<>();
    private MutableLiveData<EnergyConsumptions> energyConsumptionData = new MutableLiveData<>();
    private Context context;
    private AppPreferences appPreferences;
    private MutableLiveData<Boolean> isAccountSettingsUpdated = new MutableLiveData<>();
    private MutableLiveData<Boolean> loaderData = new MutableLiveData<>();
    private ArrayList<Account> accounts;

    public AccountSettingsViewModel() {

    }

    public void loadAccountSettingsFromLocalDB(String accountNumber) {
        DatabaseClient.getInstance(context).getAccountSettings(AccountSettings.REQUEST_CODE_GET_ACCOUNT_SETTINGS, accountNumber, this);
    }

    public void loadEnergyConsumptionsFromLocalDB(String accountNumber) {
        DatabaseClient.getInstance(context).getEnergyConsumptions(EnergyConsumptions.REQUEST_CODE_GET_CONSUMPTION, accountNumber, this);
    }

    public LiveData<EnergyConsumptions> getEnergyConsumptions() {
        return energyConsumptionData;
    }

    public MutableLiveData<Boolean> getLoaderData() {
        return loaderData;
    }

    public void setLoader(boolean showLoader) {
        loaderData.postValue(showLoader);
    }

    public LiveData<Boolean> getIsAccountSettingsUpdated() {
        return isAccountSettingsUpdated;
    }

    private void setAccountSettingsUpdated(boolean isUserUpdated) {
        this.isAccountSettingsUpdated.postValue(isUserUpdated);
    }

    public LiveData<AccountSettings> getAccountSettings() {
        return accountSettingsData;
    }

    private void setAccountSettings(AccountSettings accountSettings) {
        this.accountSettingsData.postValue(accountSettings);
    }

    private void setEnergyCOnsumptions(EnergyConsumptions energyConsumptions) {
        this.energyConsumptionData.postValue(energyConsumptions);
    }

    public void setContext(Context context) {
        this.context = context;
        appPreferences = new AppPreferences(context);
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

    public void updateAccountSettingsInLocalDB(AccountSettings accountSettings) {
        DatabaseClient.getInstance(context).updateAccountSettings(AccountSettings.REQUEST_CODE_UPDATE_ACCOUNT_SETTINGS, accountSettings, this);
    }

    public void updateEnergyConsumptionsInLocalDB(EnergyConsumptions energyConsumptions) {
        DatabaseClient.getInstance(context).updateEnergyConsumptions(EnergyConsumptions.REQUEST_CODE_UPDATE_CONSUMPTION, energyConsumptions, this);
    }

    public void getAccountSettingsFromServer(String userName, String accountNumber) {
        new ApiClient().getAccountSettingsFromServer(appPreferences.getAuthToken(), userName, accountNumber, this);
    }

    @Override
    public void onInsert(Object object, int requestCode, int responseCode) {
        if (requestCode == AccountSettings.REQUEST_CODE_ADD_ACCOUNT_SETTINGS) {
            setAccountSettings((AccountSettings) object);
        }
    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {
        if (requestCode == AccountSettings.REQUEST_CODE_UPDATE_ACCOUNT_SETTINGS) {
            Utils.showToast(context, "Account Details Updated");
            setAccountSettingsUpdated(true);
        }else if (requestCode == UserSettings.REQUEST_CODE_TOGGLE_PUSH) {
            Utils.showToast(context, "User Settings Updated");
            setAccountSettingsUpdated(true);
        }
        else if (requestCode == UserSettings.REQUEST_CODE_UPDATE_USER_SETTINGS) {
        }
    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        if (requestCode == AccountSettings.REQUEST_CODE_GET_ACCOUNT_SETTINGS) {
            setAccountSettings((AccountSettings) object);
        } else if (requestCode == EnergyConsumptions.REQUEST_CODE_GET_CONSUMPTION) {
            setEnergyCOnsumptions((EnergyConsumptions) object);
        }
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        if (requestCode == ApiClient.REQUEST_CODE_UPDATE_ACCOUNT_DETAILS) {
            NotificationSettingsRequest notificationSettingsRequest = (NotificationSettingsRequest) obj;
            AccountSettings accountSettings = new AccountSettings();
            accountSettings.setAccountNumber(notificationSettingsRequest.getAccountNumber());
            accountSettings.setAccountId(Integer.parseInt(notificationSettingsRequest.getAccountNumber()));
            accountSettings.setSmsNotificationFlag(notificationSettingsRequest.getSetting().getSmsNotificationFlag());
            accountSettings.setPushNotificationFlag(notificationSettingsRequest.getSetting().getPushNotificationFlag());
            accountSettings.setServiceAvailabilityFlag(notificationSettingsRequest.getSetting().getServiceAvailabilityFlag());
            accountSettings.setEnergyConsumptionFlag(notificationSettingsRequest.getSetting().isEnergyConsumptionFlag());
            updateAccountSettingsInLocalDB(accountSettings);
        } else if (requestCode == ApiClient.REQUEST_CODE_GET_ACCOUNT_DETAILS) {
            AccountSettings accountSettings = (AccountSettings) obj;
            addAccountSettingsToLocalDB(accountSettings);
        } else if (requestCode == ApiClient.REQUEST_CODE_UPDATE_USER_SETTINGS) {
            setLoader(false);
            UserSettings editedUserSettings = (UserSettings) obj;
            updateUserSettingsToLocalDB(editedUserSettings);
            toggleNotificationForAllAccountsInLocalDB(editedUserSettings.isPushNotificationFlag(), editedUserSettings.isSmsNotificationFlag(), accounts);
        }
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        if (requestCode == ApiClient.REQUEST_CODE_UPDATE_ACCOUNT_DETAILS) {
            Utils.showToast(context, (String) obj);
            setAccountSettingsUpdated(false);
        } else if (requestCode == ApiClient.REQUEST_CODE_GET_ACCOUNT_DETAILS) {
            setLoader(false);
            Utils.showToast(context, (String) obj);
        } else if (requestCode == ApiClient.REQUEST_CODE_UPDATE_USER_SETTINGS) {
            setLoader(false);
            Utils.showToast(context, (String) obj);
        }
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }

    public void updateAccountSettingsInServer(NotificationSettingsRequest notificationSettingsRequest) {
        setLoader(true);
        new ApiClient().updateAccountSettingsInServer(appPreferences.getAuthToken(), notificationSettingsRequest, this);
    }

    public void toggleNotificationForAllAccountsInLocalDB(boolean pushToggle, boolean smsToggle, ArrayList<Account> accounts) {
        DatabaseClient.getInstance(context).toggleNotificationForAllAccounts(UserSettings.REQUEST_CODE_TOGGLE_PUSH, pushToggle, smsToggle, accounts, this);
    }

    public void updateUserSettingsInServer(String authToken, ArrayList<Account> accounts, UserSettings userSettings) {
        setLoader(true);
        this.accounts = accounts;
        new ApiClient().updateUserSettingsInServer(authToken, userSettings, this);
    }

    private void updateUserSettingsToLocalDB(UserSettings userSettings) {
        DatabaseClient.getInstance(context).updateUserSettings(UserSettings.REQUEST_CODE_UPDATE_USER_SETTINGS, userSettings, this);
    }
}
