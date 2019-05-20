package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class EnergyConsumptions implements Serializable {

    public static int REQUEST_CODE_ADD_CONSUMPTION = 9;
    public static int REQUEST_CODE_GET_CONSUMPTION = 13;
    public static int REQUEST_CODE_UPDATE_CONSUMPTION = 14;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int accountId;
    private String accountNumber;

    private String userThreshold;

    private boolean energyConsumptionFlag;

    private String averageConsumption;

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

    public String getUserThreshold() {
        return userThreshold;
    }

    public void setUserThreshold(String userThreshold) {
        this.userThreshold = userThreshold;
    }

    public boolean getEnergyConsumptionFlag() {
        return energyConsumptionFlag;
    }

    public void setEnergyConsumptionFlag(boolean energyConsumptionFlag) {
        this.energyConsumptionFlag = energyConsumptionFlag;
    }

    public String getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(String averageConsumption) {
        this.averageConsumption = averageConsumption;
    }
}
