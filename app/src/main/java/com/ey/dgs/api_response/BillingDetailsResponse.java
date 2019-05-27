package com.ey.dgs.api_response;

import com.ey.dgs.model.BillingDetails;

public class BillingDetailsResponse {

    private Result result;

    private String success;

    private String message;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BillingDetailsResponse{" +
                "result=" + result +
                ", success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public class Result {
        private BillingDetails[] billingDetails;

        private String accountNumber;

        public BillingDetails[] getBillingDetails() {
            return billingDetails;
        }

        public void setBillingDetails(BillingDetails[] billingDetails) {
            this.billingDetails = billingDetails;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        @Override
        public String toString() {
            return "ClassPojo [billingDetails = " + billingDetails + ", accountNumber = " + accountNumber + "]";
        }
    }
}
