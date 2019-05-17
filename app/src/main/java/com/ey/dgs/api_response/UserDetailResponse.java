package com.ey.dgs.api_response;

import com.ey.dgs.model.User;
import com.google.gson.annotations.SerializedName;

public class UserDetailResponse {

    @SerializedName("result")
    private User user;

    private String message;

    private boolean success;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
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
        return "ClassPojo [user = " + user + ", success = " + success + "]";
    }
}
