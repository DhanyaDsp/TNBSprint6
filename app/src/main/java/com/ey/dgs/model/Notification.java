package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Notification implements Serializable {

    public static int REQUEST_CODE_ADD_NOTIFICATIONS = 5;
    public static int REQUEST_CODE_GET_ALL_NOTIFICATIONS = 6;
    public static int REQUEST_CODE_DELETE_NOTIFICATIONS = 7;
    public static int REQUEST_CODE_DELETE_ONE_NOTIFICATION = 8;

    public static String MMC = "MMC";
    public static String ADVANCED = "AMMC";
    public static String POAN = "POAN";//Outage Acknowledgement Notification
    public static String UPOAM = "UPOAM";//Outage Acknowledgement Notification
    public static String IRT = "IRT";//Inform Restoration Time

    @PrimaryKey
    @NonNull
    private long id;
    private String header;
    private String notificationType;
    private String accountNumber;
    private String message;
    private String energyTip;
    private String date;
    private boolean isRead;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEnergyTip() {
        return energyTip;
    }

    public void setEnergyTip(String energyTip) {
        this.energyTip = energyTip;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
