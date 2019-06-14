package com.ey.dgs.api_response;

import com.ey.dgs.model.User;
import com.ey.dgs.model.UserSettings;
import com.google.gson.annotations.SerializedName;

public class UserSettingsResponse {

    @SerializedName("result")
    private UserSettings userSettings;

    private String message;

    private boolean success;

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "UserSettingsResponse{" +
                "userSettings=" + userSettings +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
