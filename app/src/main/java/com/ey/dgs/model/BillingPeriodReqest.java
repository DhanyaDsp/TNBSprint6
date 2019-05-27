package com.ey.dgs.model;

public class BillingPeriodReqest {

    private String AccountNumber;

    private int BillingPeriod;

    public BillingPeriodReqest(String accountNumber, int billingPeriod) {
        AccountNumber = accountNumber;
        BillingPeriod = billingPeriod;
    }
}
