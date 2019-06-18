package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class UserSettings implements Serializable {

    public static int REQUEST_CODE_ADD_USER_SETTINGS = 20;
    public static int REQUEST_CODE_GET_USER_SETTINGS = 21;
    public static int REQUEST_CODE_UPDATE_USER_SETTINGS = 22;
    public static int REQUEST_CODE_TOGGLE_PUSH = 25;

    @PrimaryKey
    private int userId;

    private String UserName;

    private boolean showSplashScreen = true;

    private boolean pushNotificationFlag;

    private boolean smsNotificationFlag;

    private boolean outageAlertAcknowledgementFlag;

    private boolean restoreAlertAcknowledgementFlag;

    @Ignore
    AccountSettings[] accountSettings;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isShowSplashScreen() {
        return showSplashScreen;
    }

    public void setShowSplashScreen(boolean showSplashScreen) {
        this.showSplashScreen = showSplashScreen;
    }

    public boolean isPushNotificationFlag() {
        return pushNotificationFlag;
    }

    public void setPushNotificationFlag(boolean pushNotificationFlag) {
        this.pushNotificationFlag = pushNotificationFlag;
    }

    public boolean isSmsNotificationFlag() {
        return smsNotificationFlag;
    }

    public void setSmsNotificationFlag(boolean smsNotificationFlag) {
        this.smsNotificationFlag = smsNotificationFlag;
    }

    public boolean isOutageAlertAcknowledgementFlag() {
        return outageAlertAcknowledgementFlag;
    }

    public void setOutageAlertAcknowledgementFlag(boolean outageAlertAcknowledgementFlag) {
        this.outageAlertAcknowledgementFlag = outageAlertAcknowledgementFlag;
    }

    public boolean isRestoreAlertAcknowledgementFlag() {
        return restoreAlertAcknowledgementFlag;
    }

    public void setRestoreAlertAcknowledgementFlag(boolean restoreAlertAcknowledgementFlag) {
        this.restoreAlertAcknowledgementFlag = restoreAlertAcknowledgementFlag;
    }

    public AccountSettings[] getAccountSettings() {
        return accountSettings;
    }

    public void setAccountSettings(AccountSettings[] accountSettings) {
        this.accountSettings = accountSettings;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
