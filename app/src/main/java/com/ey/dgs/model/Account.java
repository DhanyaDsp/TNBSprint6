package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Account implements Serializable {

    public static int REQUEST_CODE_ADD_ACCOUNTS = 5;
    public static int REQUEST_CODE_UPDATE_ACCOUNT = 15;
    public static int REQUEST_CODE_SET_PRIMARY_ACCOUNT = 16;
    public static int REQUEST_CODE_GET_PRIMARY_ACCOUNT = 17;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int accountId;
    private String accountNumber;
    private String nickName;
    private String lastBilledDate;
    private String lastBilledAmount;
    private String billingCycleStartDate;
    private String billingCycleEndDate;
    private String name;
    private String date;
    private String rm;
    private String energyTip;
    private int user_id;
    private boolean isAccount;
    private boolean isPrimaryAccount;
    private boolean isThreshold;
    @Ignore
    private boolean isSelected = false;
    @Ignore
    BillingDetails[] billingDetails;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLastBilledDate() {
        return lastBilledDate;
    }

    public void setLastBilledDate(String lastBilledDate) {
        this.lastBilledDate = lastBilledDate;
    }

    public String getLastBilledAmount() {
        return lastBilledAmount;
    }

    public void setLastBilledAmount(String lastBilledAmount) {
        this.lastBilledAmount = lastBilledAmount;
    }

    public String getBillingCycleStartDate() {
        return billingCycleStartDate;
    }

    public void setBillingCycleStartDate(String billingCycleStartDate) {
        this.billingCycleStartDate = billingCycleStartDate;
    }

    public String getBillingCycleEndDate() {
        return billingCycleEndDate;
    }

    public void setBillingCycleEndDate(String billingCycleEndDate) {
        this.billingCycleEndDate = billingCycleEndDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isAccount() {
        return isAccount;
    }

    public void setAccount(boolean account) {
        isAccount = account;
    }

    public boolean isPrimaryAccount() {
        return isPrimaryAccount;
    }

    public void setPrimaryAccount(boolean primaryAccount) {
        isPrimaryAccount = primaryAccount;
    }

    @Ignore
    public BillingDetails[] getBillingDetails() {
        return billingDetails;
    }

    @Ignore
    public void setBillingDetails(BillingDetails[] billingDetails) {
        this.billingDetails = billingDetails;
    }

    public String getEnergyTip() {
        return energyTip;
    }

    public void setEnergyTip(String energyTip) {
        this.energyTip = energyTip;
    }

    public boolean isThreshold() {
        return isThreshold;
    }

    public void setThreshold(boolean threshold) {
        isThreshold = threshold;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
