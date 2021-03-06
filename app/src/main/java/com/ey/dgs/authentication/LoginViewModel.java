package com.ey.dgs.authentication;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.api_response.UserDetailResponse;
import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;


public class LoginViewModel extends ViewModel implements DatabaseCallback, APICallback {

    private Context context;
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<Boolean> offlineData = new MutableLiveData<>();
    private MutableLiveData<Boolean> showProgress = new MutableLiveData<>();
    private AppPreferences appPreference;

    public void addUserToLocalDB(User user) {
        DatabaseClient.getInstance(context).addUser(User.REQUEST_CODE_INSET_USER, user, this);
    }

    public void deleteUserFromDB(User user) {
        DatabaseClient.getInstance(context).deleteUser(User.REQUEST_CODE_DELETE_USER, user, this);
    }

    public MutableLiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    public void setShowProgress(Boolean showProgress) {
        this.showProgress.postValue(showProgress);
    }

    public void getUserDetail(int user_id) {
        DatabaseClient.getInstance(context).getUser(User.REQUEST_CODE_GET_USER_DETAIL, user_id, this);
    }

    public void getUserDetailFromServer(User user) {
        setShowProgress(true);
        new ApiClient().getUserDetail(appPreference.getAuthToken(), user, this);
    }

    public void updateUserInServer(User user) {
        new ApiClient().updateUser(appPreference.getAuthToken(), user, this);
    }

    public void update(User user) {
        DatabaseClient.getInstance(context).updateUser(User.REQUEST_CODE_UPDATE_USER_DETAIL, user, this);
    }

    public void setContext(Context context) {
        this.context = context;
        appPreference = new AppPreferences(context);
    }

    @Override
    public void onInsert(Object object, int requestCode, int responseCode) {
        /*if (requestCode == User.REQUEST_CODE_INSET_USER) {
            localUser = (User) object;
            setUserData(localUser);
        }*/
    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {
        if (requestCode == User.REQUEST_CODE_UPDATE_USER_DETAIL) {
            setUserData((User) object);
        }
    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        if (requestCode == User.REQUEST_CODE_GET_USER_DETAIL) {
            setUserData((User) object);
        }
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }

    public MutableLiveData<User> getUserDetail() {
        return userData;
    }

    public void setUserData(User user) {
        this.userData.postValue(user);
    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {

        if (requestCode == ApiClient.REQUEST_CODE_UPDATE_USER) {
            User user = (User) obj;
            update(user);
        } else if (requestCode == ApiClient.REQUEST_CODE_GET_USER) {
            setShowProgress(false);
            UserDetailResponse userDetailResponse = (UserDetailResponse) obj;
            User user = userDetailResponse.getUser();
            user.setUserId(1);
            update(user);
            getAccountDetails(user);
        }
    }

    public MutableLiveData<Boolean> getOfflineData() {
        return offlineData;
    }

    public void setOfflineData(Boolean isOffline) {
        this.offlineData.postValue(isOffline);
    }

    public void getAccountDetails(User user) {
        DatabaseClient.getInstance(context).getAccountDetails(User.REQUEST_CODE_UPDATE_USER_DETAIL, user, this);
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        setShowProgress(false);
        Utils.showToast(context, (String) obj);
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }

    @Override
    public void onOffline(int requestCode, boolean isLoading) {
        setOfflineData(isLoading);
    }
}
