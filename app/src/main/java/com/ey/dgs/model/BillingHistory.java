package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class BillingHistory implements Serializable {

    public static final int REQUEST_CODE_ADD_BILLING_HISTORY = 18;
    public static final int REQUEST_CODE_GET_BILLING_HISTORY = 19;
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @Ignore
    private Account account;

    private String accountNumber;

    private String billingDetails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(String billingDetails) {
        this.billingDetails = billingDetails;
    }

    @Ignore
    public Account getAccount() {
        return account;
    }

    @Ignore
    public void setAccount(Account account) {
        this.account = account;
    }
}
