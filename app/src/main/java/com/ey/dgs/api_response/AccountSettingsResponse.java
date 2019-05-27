package com.ey.dgs.api_response;

import com.ey.dgs.model.AccountSettings;
import com.google.gson.annotations.SerializedName;

public class AccountSettingsResponse {

    @SerializedName("result")
    private AccountSettings result;

    private String success;

    private String message;

    public AccountSettings getResult() {
        return result;
    }

    public void setResult(AccountSettings result) {
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
        return "ClassPojo [result = " + result + ", success = " + success + "]";
    }
}
