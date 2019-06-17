package com.ey.dgs.notifications.settings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ey.dgs.model.NotificationSetting;

import java.util.ArrayList;

public class NotificationSettingsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<NotificationSetting>> notificationSettings = new MutableLiveData<ArrayList<NotificationSetting>>();

    public NotificationSettingsViewModel() {
        ArrayList<NotificationSetting> notificationSettings = new ArrayList<>();
        notificationSettings.add(new NotificationSetting("Customise the type of notifications you wish to receive", false, true, false));
        notificationSettings.add(new NotificationSetting("Billing and Payment", true, false, false));
        notificationSettings.add(new NotificationSetting("Account related matters", true, false, false));
        notificationSettings.add(new NotificationSetting("Energy Consumption", false, false, false));
        notificationSettings.add(new NotificationSetting("Service disruption", false, false, false));
        notificationSettings.add(new NotificationSetting("Select how you wish to receive your notifications", false, true, false));
        notificationSettings.add(new NotificationSetting("Push notifications", true, false, false));
        notificationSettings.add(new NotificationSetting("SMS", false, false, false));
        setNotificationSettings(notificationSettings);
    }

    public LiveData<ArrayList<NotificationSetting>> getNotificationSettings() {
        return notificationSettings;
    }

    private void setNotificationSettings(ArrayList<NotificationSetting> notificationSettings) {
        this.notificationSettings.postValue(notificationSettings);
    }

}
