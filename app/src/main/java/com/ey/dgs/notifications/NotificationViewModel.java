package com.ey.dgs.notifications;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.Notification;

import java.util.ArrayList;
import java.util.Date;

import static com.ey.dgs.model.Notification.REQUEST_CODE_GET_ALL_NOTIFICATIONS;

public class NotificationViewModel extends ViewModel implements DatabaseCallback {

    private MutableLiveData<ArrayList<Notification>> notifications = new MutableLiveData<>();
    Context context;

    public NotificationViewModel() {
    }

    public void loadNotificationsFromLocalDB(int userId, int accountId) {
        DatabaseClient.getInstance(context).getNotifications(REQUEST_CODE_GET_ALL_NOTIFICATIONS, userId, accountId, this);
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

    public void addDummyNotifications(int accountId, int userId) {
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
