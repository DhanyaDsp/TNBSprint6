package com.ey.dgs.api_response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("token")
    private String token;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
