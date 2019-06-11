package com.ey.dgs.notifications;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.Notification;
import com.ey.dgs.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ey.dgs.model.Notification.REQUEST_CODE_DELETE_NOTIFICATIONS;
import static com.ey.dgs.model.Notification.REQUEST_CODE_GET_ALL_NOTIFICATIONS;

public class NotificationViewModel extends ViewModel implements DatabaseCallback {

    private MutableLiveData<ArrayList<Notification>> notifications = new MutableLiveData<>();
    Context context;

    public NotificationViewModel() {
    }

    public void loadNotificationsFromLocalDB(int userId, String accountNumber) {
        DatabaseClient.getInstance(context).getNotifications(REQUEST_CODE_GET_ALL_NOTIFICATIONS, userId, accountNumber, this);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LiveData<ArrayList<Notification>> getNotifications() {
        return notifications;
    }

    private void setNotifications(ArrayList<Notification> notifications) {
        this.notifications.postValue(notifications);
    }

    public void deleteNotificationsFromLocalDB() {
        DatabaseClient.getInstance(context).deleteNotifications(REQUEST_CODE_DELETE_NOTIFICATIONS, this);
    }

    public void addDummyNotifications(int accountId, int userId) {

        List<Notification> notifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setHeader("You have reached your threshold.");
        notification1.setNotificationType(Notification.MMC);
        notification1.setDate(Utils.formatNotificationDate());
        notification1.setMessage("Hi Zulkifli, your electric usage for this month has reached RM 100. With this trend, your estimated bill will be RM 350. To modify your energy consumption notification threshold, go to Settings in this app.");
        notification1.setEnergyTip("Here’s a tip! Bring in the sunlight! During daylight hours, switch off artificial lights and use windows and skylights to brighten your home.");

        Notification notification2 = new Notification();
        notification2.setHeader("You are about to reach your threshold.");
        notification2.setNotificationType(Notification.MMC);
        notification2.setDate(Utils.formatNotificationDate());
        notification2.setMessage("Hi Zulkifli, heads up! Your usage this month has reached RM 80. If you keep this up, you’ll reach RM 200 in 2 days. We’re here to help with your bill! To modify your energy consumption notification threshold, go to Settings in this app.");
        notification2.setEnergyTip("Here’s a tip! Bring in the sunlight! During daylight hours, switch off artificial lights and use windows and skylights to brighten your home.");

        Notification notification3 = new Notification();
        notification3.setHeader("You have abnormal usage activity.");
        notification3.setNotificationType(Notification.MMC);
        notification3.setDate(Utils.formatNotificationDate());
        notification3.setMessage("Hi Zulkifli, we noticed we noticed you’ve used RM 10 more than usual yesterday. Do you know that your washing machine and air-conditioners are some of the biggest energy consumers in your household. You may have forgotten to switch off some of these appliances and left them on overnight. This can cause a sudden increase in your consumption.");
        notification3.setEnergyTip("");

        Notification notification4 = new Notification();
        notification4.setHeader("Your projected bill amount.");
        notification4.setNotificationType(Notification.ADVANCED);
        notification4.setDate(Utils.formatNotificationDate());
        notification4.setMessage("Heads up Zulkifli! We forecast your bill amount to be RM 150 based on your usage trend. Say goodbye to bill shock!");
        notification4.setEnergyTip("Here’s a tip! Bring in the sunlight! During daylight hours, switch off artificial lights and use windows and skylights to brighten your home.");

        Notification notification6 = new Notification();
        notification6.setHeader("Energy tip of the month!");
        notification6.setNotificationType(Notification.UPOAM);
        notification6.setDate(Utils.formatNotificationDate());
        notification6.setMessage("Light less! Light your home efficiently. Make the upgrade to LED bulbs. They use 75 percent less energy and last 25 times longer than incandescent bulbs. Plus, they emit less heat, which means less work for your air-conditioner.");
        notification6.setEnergyTip("");

        Notification notification7 = new Notification();
        notification7.setHeader("Planned Outage");
        notification7.setNotificationType(Notification.POAN);
        notification7.setDate(Utils.formatNotificationDate());
        notification7.setMessage("nHeads up Zulkifli! On 19 Apr (Fri) at 1PM, we’ll be having a maintenance in your area. It will take 4 hours to restore power. We will do our absolute best to perform this operation as fast as possible. We’re doing this for a brighter future! Sorry for any inconvenience caused.");
        notification7.setEnergyTip("");


        notifications.add(notification1);
        notifications.add(notification2);
        notifications.add(notification3);
        notifications.add(notification4);
        notifications.add(notification6);
        notifications.add(notification7);
        notifications.add(notification4);
        notifications.add(notification6);
        notifications.add(notification3);
        notifications.add(notification4);

        for (Notification notification : notifications) {
            DatabaseClient.getInstance(context).addNotification(Notification.REQUEST_CODE_ADD_NOTIFICATIONS, notification, null);
        }
    }

    @Override
    public void onInsert(Object object, int requestCode, int responseCode) {
        if (requestCode == Notification.REQUEST_CODE_ADD_NOTIFICATIONS) {

        }
    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {

    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        if (requestCode == REQUEST_CODE_GET_ALL_NOTIFICATIONS) {
            setNotifications((ArrayList<Notification>) object);
        }
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }
}
