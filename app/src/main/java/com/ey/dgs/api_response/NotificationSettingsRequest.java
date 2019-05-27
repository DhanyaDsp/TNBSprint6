package com.ey.dgs.api_response;

import com.ey.dgs.model.Setting;

public class NotificationSettingsRequest {

    String AccountNumber;

    Setting setting;

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}
