package com.ey.dgs.base;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.view.View;

import com.ey.dgs.model.Account;

import java.util.ArrayList;

public class BaseViewModel extends ViewModel {

    private MutableLiveData<Boolean> sessionExpiredData = new MutableLiveData<>();

    public MutableLiveData<Boolean> getSessionExpiredData() {
        return sessionExpiredData;
    }

    public void setSessionExpiredData(Boolean isSessionExpired) {
        this.sessionExpiredData.postValue(isSessionExpired);
    }
}
