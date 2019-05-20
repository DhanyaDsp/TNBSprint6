package com.ey.dgs.dashboard.myaccount;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.NotificationSettingsRequest;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

public class AccountSettingsViewModel extends ViewModel implements DatabaseCallback, APICallback {

    private MutableLiveData<AccountSettings> accountSettingsData = new MutableLiveData<>();
    private MutableLiveData<EnergyConsumptions> energyConsumptionData = new MutableLiveData<>();
    private Context context;
    private AppPreferences appPreferences;
    private MutableLiveData<Boolean> isAccountDetailsUpdated = new MutableLiveData<>();

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

    public LiveData<Boolean> getIsAccountDetailsUpdated() {
        return isAccountDetailsUpdated;
    }

    private void setAccountDetailsUpdated(boolean isUserUpdated) {
        this.isAccountDetailsUpdated.postValue(isUserUpdated);
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
        setAccountDetailsUpdated(true);
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
            setEnergyCOnsumptions(notificationSettingsRequest.getSetting().getEnergyConsumptions());
            AccountSettings accountSettings = new AccountSettings();
            accountSettings.setAccountId(notificationSettingsRequest.getSetting().getEnergyConsumptions().getAccountId());
            accountSettings.setAccountNumber(notificationSettingsRequest.getAccountNumber());
            accountSettings.setSmsNotificationFlag(notificationSettingsRequest.getSetting().getSmsNotificationFlag());
            accountSettings.setPushNotificationFlag(notificationSettingsRequest.getSetting().getPushNotificationFlag());
            accountSettings.setServiceAvailability(notificationSettingsRequest.getSetting().getServiceAvailability());
            updateAccountSettingsInLocalDB(accountSettings);
            updateEnergyConsumptionsInLocalDB(notificationSettingsRequest.getSetting().getEnergyConsumptions());
        } else if (requestCode == ApiClient.REQUEST_CODE_GET_ACCOUNT_DETAILS) {
            AccountSettings accountSettings = (AccountSettings) obj;
            addAccountSettingsToLocalDB(accountSettings);
        }
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        if (requestCode == ApiClient.REQUEST_CODE_UPDATE_ACCOUNT_DETAILS) {
            Utils.showToast(context, (String) obj);
            setAccountDetailsUpdated(true);
        }
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }

    public void updateAccountSettingsInServer(NotificationSettingsRequest notificationSettingsRequest) {
        new ApiClient().updateAccountSettingsInServer(notificationSettingsRequest, this);
    }
}
