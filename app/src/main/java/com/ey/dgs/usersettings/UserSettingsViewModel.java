package com.ey.dgs.usersettings;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Dao;
import android.content.Context;

import com.ey.dgs.api_response.UserSettingsResponse;
import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.SplashItem;
import com.ey.dgs.model.User;
import com.ey.dgs.model.UserSettings;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import java.util.ArrayList;


public class UserSettingsViewModel extends ViewModel implements APICallback, DatabaseCallback {

    private MutableLiveData<UserSettings> userSettingsData = new MutableLiveData<>();
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

    public void getUserSettingsFromLocalDB(int userId) {
        DatabaseClient.getInstance(context).getUserSettings(UserSettings.REQUEST_CODE_GET_USER_SETTINGS, userId, this);
    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        if (requestCode == ApiClient.REQUEST_CODE_GET_USER_SETTINGS) {
            UserSettingsResponse userSettingsResponse = (UserSettingsResponse) obj;
            addUserSettingsToLocalDB(userSettingsResponse.getUserSettings());
        }
    }

    private void addUserSettingsToLocalDB(UserSettings userSettings) {
        DatabaseClient.getInstance(context).addUserSettings(UserSettings.REQUEST_CODE_ADD_USER_SETTINGS, userSettings, this);
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
}
