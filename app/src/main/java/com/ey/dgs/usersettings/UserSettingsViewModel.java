package com.ey.dgs.usersettings;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.api_response.UserSettingsResponse;
import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.User;
import com.ey.dgs.model.UserSettings;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import java.util.ArrayList;
import java.util.List;


public class UserSettingsViewModel extends ViewModel implements APICallback, DatabaseCallback {

    private MutableLiveData<UserSettings> userSettingsData = new MutableLiveData<>();
    private MutableLiveData<Boolean> showProgress = new MutableLiveData<>();
    private Context context;

    public void setContext(Context ctx) {
        context = ctx;
    }

    public MutableLiveData<UserSettings> getUserSettings() {
        return userSettingsData;
    }

    public void setUserSettingsData(UserSettings userSettings) {
        this.userSettingsData.postValue(userSettings);
    }

    public void getUserSettingsFromServer(String token, User user) {
        new ApiClient().getUserSettings(token, user, this);
    }

    public MutableLiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    public void setShowProgress(Boolean showProgress) {
        this.showProgress.postValue(showProgress);
    }

    public void getUserSettingsFromLocalDB(int userId) {
        DatabaseClient.getInstance(context).getUserSettings(UserSettings.REQUEST_CODE_GET_USER_SETTINGS, userId, this);
    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        if (requestCode == ApiClient.REQUEST_CODE_GET_USER_SETTINGS) {
            UserSettingsResponse userSettingsResponse = (UserSettingsResponse) obj;
            List<Account> accounts = new ArrayList<>();
            for (AccountSettings accountSettings : userSettingsResponse.getUserSettings().getAccountSettings()) {
                Account account = new Account();
                account.setAccountId(Integer.parseInt(accountSettings.getAccountNumber()));
                account.setNickName(accountSettings.getNickName());
                account.setAccountNumber(accountSettings.getAccountNumber());
                account.setUserThreshold(accountSettings.getUserThreshold());
                account.setUserThresholdSet(accountSettings.isUserThresholdSet());
                account.setHasConsumptionReached(accountSettings.isHasConsumptionReached());
                account.setOutageAlertFlag(accountSettings.isOutageAlertFlag());
                account.setRestoreAlertFlag(accountSettings.isRestoreAlertFlag());
                account.setUser_id(1);
                accounts.add(account);
            }
            addAccountsToLocalDB(accounts);
            addUserSettingsToLocalDB(userSettingsResponse.getUserSettings());
        } else if (requestCode == ApiClient.REQUEST_CODE_UPDATE_USER_SETTINGS) {
            UserSettings userSettings = (UserSettings) obj;
            updateUserSettingsToLocalDB(userSettings);
        }
    }

    private void addUserSettingsToLocalDB(UserSettings userSettings) {
        DatabaseClient.getInstance(context).addUserSettings(UserSettings.REQUEST_CODE_ADD_USER_SETTINGS, userSettings, this);
    }

    private void addAccountsToLocalDB(List<Account> accounts) {
        DatabaseClient.getInstance(context).addAccounts(Account.REQUEST_CODE_ADD_ACCOUNTS, accounts, this);
    }

    private void updateUserSettingsToLocalDB(UserSettings userSettings) {
        DatabaseClient.getInstance(context).updateUserSettings(UserSettings.REQUEST_CODE_UPDATE_USER_SETTINGS, userSettings, this);
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        if (requestCode == ApiClient.REQUEST_CODE_GET_USER_SETTINGS) {
            Utils.showToast(context, "Failed to load User Settings");
        }
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }

    @Override
    public void onInsert(Object object, int requestCode, int responseCode) {
        if (requestCode == UserSettings.REQUEST_CODE_ADD_USER_SETTINGS) {
            UserSettings userSettings = (UserSettings) object;
            setUserSettingsData(userSettings);
        }
    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {
        if (requestCode == UserSettings.REQUEST_CODE_UPDATE_USER_SETTINGS) {
            setUserSettingsData((UserSettings) object);
        }
    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        setUserSettingsData((UserSettings) object);
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }

    public void updateUserSettings(UserSettings userSettings) {
        DatabaseClient.getInstance(context).updateUserSettings(UserSettings.REQUEST_CODE_UPDATE_USER_SETTINGS, userSettings, this);
    }

    public void updateUserSettingsInServer(String authToken, UserSettings userSettings) {

        new ApiClient().updateUserSettingsInServer(authToken, userSettings, this);
    }
}
