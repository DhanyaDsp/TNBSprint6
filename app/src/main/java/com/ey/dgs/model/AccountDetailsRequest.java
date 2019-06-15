package com.ey.dgs.model;

public class AccountDetailsRequest {

    private String userName;
    private AccountDetails[] accountDetails;

    public AccountDetailsRequest(String userName, AccountDetails[] accountDetails) {
        this.userName = userName;
        this.accountDetails = accountDetails;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AccountDetails[] getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails[] accountDetails) {
        this.accountDetails = accountDetails;
    }
}
