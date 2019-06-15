package com.ey.dgs.model;

import java.io.Serializable;

public class AccountDetails implements Serializable {

    private String accountNumber;
    private String peopleInProperty;
    private String userThreshold;

    public AccountDetails(String accountNumber, String peopleInProperty, String userThreshold) {
        this.accountNumber = accountNumber;
        this.peopleInProperty = peopleInProperty;
        this.userThreshold = userThreshold;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPeopleInProperty() {
        return peopleInProperty;
    }

    public void setPeopleInProperty(String peopleInProperty) {
        this.peopleInProperty = peopleInProperty;
    }

    public String getUserThreshold() {
        return userThreshold;
    }

    public void setUserThreshold(String userThreshold) {
        this.userThreshold = userThreshold;
    }
}
