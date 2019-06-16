package com.ey.dgs.dashboard.questions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.model.AccountDetailsRequest;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import static com.ey.dgs.webservice.ApiClient.REQUEST_CODE_ANSWER_QUESTIONS;

public class QuestionsViewModel extends ViewModel implements APICallback {

    private Context context;
    AppPreferences appPreferences;
    boolean isSuccess = false;
    private MutableLiveData<Boolean> loaderData = new MutableLiveData<>();

    public void setContext(Context context) {
        this.context = context;
        appPreferences = new AppPreferences(context);
    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        if (requestCode == REQUEST_CODE_ANSWER_QUESTIONS) {
            setLoader(false);
            setSuccess(true);
        }
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        Utils.showToast(context, (String) obj);
        setLoader(false);
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }

    public void updateAccountSettingsInServer(AccountDetailsRequest accountDetailsRequest) {
        new ApiClient().updateAccountDetails(appPreferences.getAuthToken(), accountDetailsRequest, this);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public MutableLiveData<Boolean> getLoaderData() {
        return loaderData;
    }

    public void setLoader(boolean showLoader) {
        loaderData.postValue(showLoader);
    }
}
