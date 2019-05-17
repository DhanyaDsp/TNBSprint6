package com.ey.dgs.model;

public class NotificationSetting {

    private String name;
    private boolean isTurnedOn, isHeader, isThreshold;

    public NotificationSetting(String name, boolean isTurnedOn, boolean isHeader, boolean isThreshold) {
        this.name = name;
        this.isTurnedOn = isTurnedOn;
        this.isHeader = isHeader;
        this.isThreshold = isThreshold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTurnedOn() {
        return isTurnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        isTurnedOn = turnedOn;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isThreshold() {
        return isThreshold;
    }

    public void setThreshold(boolean threshold) {
        isThreshold = threshold;
    }
}
