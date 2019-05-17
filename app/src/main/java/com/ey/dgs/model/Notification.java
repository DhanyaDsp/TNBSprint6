package com.ey.dgs.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Notification {

    public static int REQUEST_CODE_ADD_NOTIFICATIONS = 5;
    public static int REQUEST_CODE_GET_ALL_NOTIFICATIONS = 6;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String header;
    private String notificationType;
    private String message;
    private String energyTip;

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
}
