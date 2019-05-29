package com.ey.dgs.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    public static int REQUEST_CODE_INSET_USER = 1;
    public static int REQUEST_CODE_GET_USERS = 2;
    public static int REQUEST_CODE_GET_USER_DETAIL = 3;
    public static int REQUEST_CODE_UPDATE_USER_DETAIL = 4;
    public static int REQUEST_CODE_DELETE_USER = 5;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int userId;

    @SerializedName("userName")
    private String email;

    @Ignore
    private String password;

    private boolean isRememberMe;

    private boolean saAlertFlag;

    private boolean mmcAlertFlag;

    private boolean notificationFlag;

    private boolean isPrimaryAccountSet;

    @Ignore
    private Account[] accountDetails;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return isRememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        isRememberMe = rememberMe;
    }

    public boolean isNotificationFlag() {
        return notificationFlag;
    }

    public void setNotificationFlag(boolean notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    public boolean isSaAlertFlag() {
        return saAlertFlag;
    }

    public void setSaAlertFlag(boolean saAlertFlag) {
        this.saAlertFlag = saAlertFlag;
    }

    public boolean isMmcAlertFlag() {
        return mmcAlertFlag;
    }

    public void setMmcAlertFlag(boolean mmcAlertFlag) {
        this.mmcAlertFlag = mmcAlertFlag;
    }

    @Ignore
    public Account[] getAccountDetails() {
        return accountDetails;
    }

    @Ignore
    public void setAccountDetails(Account[] accountDetails) {
        this.accountDetails = accountDetails;
    }

    public boolean isPrimaryAccountSet() {
        return isPrimaryAccountSet;
    }

    public void setPrimaryAccountSet(boolean primaryAccountSet) {
        isPrimaryAccountSet = primaryAccountSet;
    }


}
