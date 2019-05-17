package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class AccountSettings {

    public static final int REQUEST_CODE_ADD_ACCOUNT_SETTINGS = 6;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int accountId;
    private String accountNumber;
    private boolean serviceAvailability;
    private boolean pushNotificationFlag;
    private boolean smsNotificationFlag;
    private String message;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isServiceAvailability() {
        return serviceAvailability;
    }

    public void setServiceAvailability(boolean serviceAvailability) {
        this.serviceAvailability = serviceAvailability;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
