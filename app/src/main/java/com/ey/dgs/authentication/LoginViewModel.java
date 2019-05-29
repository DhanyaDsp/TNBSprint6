package com.ey.dgs.authentication;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.api_response.UserDetailResponse;
import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.SetPrimaryAccountRequest;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import java.util.ArrayList;


public class LoginViewModel extends ViewModel implements DatabaseCallback, APICallback {

    private Context context;
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<User>> userListData = new MutableLiveData<>();
    private AppPreferences appPreference;

    public void addUserToLocalDB(User user) {
        DatabaseClient.getInstance(context).addUser(User.REQUEST_CODE_INSET_USER, user, this);
    }

    public void deleteUserFromDB(User user) {
        DatabaseClient.getInstance(context).deleteUser(User.REQUEST_CODE_DELETE_USER, user, this);
    }

    public void getUserDetail(int user_id) {
        DatabaseClient.getInstance(context).getUser(User.REQUEST_CODE_GET_USER_DETAIL, user_id, this);
    }

    public void getUserDetailFromServer(User user) {
        new ApiClient().getUser(appPreference.getAuthToken(), user, this);
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
        } else {
            UserDetailResponse userDetailResponse = (UserDetailResponse) obj;
            User serverUser = userDetailResponse.getUser();
          /*  serverUser.setUserId(1);
            serverUser.setRememberMe(true);*/
            //setUserData(serverUser);
            update(serverUser);
        }
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        Utils.showToast(context, (String) obj);
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }
}
