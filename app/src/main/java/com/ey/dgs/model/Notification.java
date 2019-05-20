package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Notification implements Serializable {

    public static int REQUEST_CODE_ADD_NOTIFICATIONS = 5;
    public static int REQUEST_CODE_GET_ALL_NOTIFICATIONS = 6;

    public static String MMC = "MMCNotification";
    public static String ADVANCED = "AdvancedNotification";
    public static String SA_ACK = "SAAcknowledgement";
    public static String SA_RES = "SARestoration";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String header;
    private String notificationType;
    private String message;
    private String energyTip;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
