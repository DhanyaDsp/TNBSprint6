package com.ey.dgs.notifications.pushnotifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ey.dgs.utils.AppPreferences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.microsoft.windowsazure.messaging.NotificationHub;

/**
 * Created by Sooraj.HS on 01-10-2018.
 */

public class AzureRegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    private NotificationHub hub;

    public AzureRegistrationIntentService() {
        super(TAG);
    }

    private String emailTag = "";

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "3PO_fcm_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "3PO FCM Channel",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);

            ((android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        emailTag = intent.getStringExtra("emailTag");
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppPreferences sharedPreferences = new AppPreferences(this);
        String resultString = null;
        String regID = "";
        String storedToken = "";

        try {
            String FCM_token = FirebaseInstanceId.getInstance().getToken();
            storedToken = sharedPreferences.getFcm_token(); //.getString("FCMtoken", "");
            // Storing the registration id that indicates whether the generated token has been
            // sent to your server. If it is not stored, send the token to your server,
            // otherwise your server should have already received the token.
            if (((regID = sharedPreferences.getAzureRegId()) == null)) {

                NotificationHub hub = new NotificationHub(PushNotificationSettings.HubName, PushNotificationSettings.HubListenConnectionString, this);
                //if(hub != null)
                try {
                    regID = hub.register(FCM_token, emailTag).getRegistrationId();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
                // If you want to use tags...
                // Refer to : https://azure.microsoft.com/en-us/documentation/articles/notification-hubs-routing-tag-expressions/
                // regID = hub.register(token, "tag1,tag2").getRegistrationId();

                resultString = "New NH Registration Successfu+" +
                        "+lly - RegId : " + regID;

                sharedPreferences.setAzureRegId(regID); //.edit().putString("registrationID", regID).apply();
                sharedPreferences.setFcm_token(FCM_token); //.edit().putString("FCMtoken", FCM_token).apply();
            }

            // Check if the token may have been compromised and needs refreshing.\

            else if (storedToken != null && storedToken.length() > 0 && storedToken != FCM_token) {

                NotificationHub hub = new NotificationHub(PushNotificationSettings.HubName, PushNotificationSettings.HubListenConnectionString, this);
                try {
                    regID = hub.register(FCM_token, emailTag).getRegistrationId();
                } catch (Exception e) {
                    Log.d("", "");
                }

                // If you want to use tags...
                // Refer to : https://azure.microsoft.com/en-us/documentation/articles/notification-hubs-routing-tag-expressions/
                // regID = hub.register(token, "tag1,tag2").getRegistrationId();

                resultString = "Notification ID Registration Successfully - RegId : " + regID;

                sharedPreferences.setAzureRegId(regID); //.edit().putString("registrationID", regID).apply();
                sharedPreferences.setFcm_token(FCM_token); //.edit().putString("FCMtoken", FCM_token).apply();
            } else {
                resultString = "Previously Registered Successfully - RegId : " + regID;
            }
        } catch (Exception e) {
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }

    }
}