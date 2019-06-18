package com.ey.dgs.notifications.pushnotifications;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.ey.dgs.R;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.Notification;
import com.ey.dgs.notifications.NotificationDetailPage;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.NotificationHelper;
import com.ey.dgs.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sooraj.HS on 27-09-2018.
 */

public class FireBaseMessagingService extends FirebaseMessagingService {

    private static int count = 0;
    AppPreferences appPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Calling method to generate notification
        //String from = remoteMessage.getFrom();
        Map<String, String> dataObj = remoteMessage.getData();
        String collapseKey = remoteMessage.getCollapseKey();

        Set<String> keys = dataObj.keySet();
        if (keys != null && keys.toArray() != null && keys.toArray().length > 0) {
            if (keys.toArray().length >= 2) {
                showNotification(dataObj.get(keys.toArray()[0]), dataObj.get(keys.toArray()[1]), dataObj);
            } else {
                showNotification("MyTNB App", dataObj.get(keys.toArray()[0]), dataObj);
            }
        }
    }

    //This method is only generating push notification
    private void showNotification(String title, String messageBody, Map<String, String> dataObj) {

        Gson gson = new Gson();
        Notification notification = gson.fromJson(messageBody, Notification.class);
        notification.setDate(Utils.formatNotificationDate(new Date()));
        DatabaseClient.getInstance(getApplication()).addNotification(Notification.REQUEST_CODE_ADD_NOTIFICATIONS, notification, null);

        appPreferences = new AppPreferences(getApplicationContext());
        AccountSettings accountSettings = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().getAccountSettingsDao().getAccountSettings(notification.getAccountNumber());
        if (appPreferences.isLoginned() && accountSettings.isPushNotificationFlag()) {
            new NotificationHelper(getApplicationContext()).createNotification(title, notification, notification.getMessage());
        }
    }
}


