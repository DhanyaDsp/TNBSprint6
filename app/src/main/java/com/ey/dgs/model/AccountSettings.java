package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class AccountSettings extends Account implements Serializable {

    public static final int REQUEST_CODE_ADD_ACCOUNT_SETTINGS = 6;
    public static final int REQUEST_CODE_GET_ACCOUNT_SETTINGS = 11;
    public static final int REQUEST_CODE_UPDATE_ACCOUNT_SETTINGS = 12;

    private boolean serviceAvailabilityFlag;
    private boolean pushNotificationFlag;
    private boolean smsNotificationFlag;
    private String message;

    @Ignore
    private EnergyConsumptions energyConsumptions;

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
