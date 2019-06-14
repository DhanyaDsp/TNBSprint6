package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;

@Entity
public class AccountSettings implements Serializable {

    public static final int REQUEST_CODE_ADD_ACCOUNT_SETTINGS = 6;
    public static final int REQUEST_CODE_GET_ACCOUNT_SETTINGS = 11;
    public static final int REQUEST_CODE_UPDATE_ACCOUNT_SETTINGS = 12;

    @PrimaryKey
    @NonNull
    private int accountId;
    private String accountNumber;
    private boolean serviceAvailabilityFlag;
    private boolean pushNotificationFlag;
    private boolean smsNotificationFlag;
    private String message;

    @Ignore
    private EnergyConsumptions energyConsumptions;

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

    public boolean isServiceAvailabilityFlag() {
        return serviceAvailabilityFlag;
    }

    public void setServiceAvailabilityFlag(boolean serviceAvailabilityFlag) {
        this.serviceAvailabilityFlag = serviceAvailabilityFlag;
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

    @Ignore
    public EnergyConsumptions getEnergyConsumptions() {
        return energyConsumptions;
    }

    @Ignore
    public void setEnergyConsumptions(EnergyConsumptions energyConsumptions) {
        this.energyConsumptions = energyConsumptions;
    }
}
