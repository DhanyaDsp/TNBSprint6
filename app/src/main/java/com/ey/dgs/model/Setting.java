package com.ey.dgs.model;

import java.io.Serializable;

public class Setting implements Serializable {

    private boolean smsNotificationFlag;

    private boolean pushNotificationFlag;

    private boolean serviceAvailabilityFlag;

    private boolean energyConsumptionFlag;


    public boolean getSmsNotificationFlag() {
        return smsNotificationFlag;
    }

    public void setSmsNotificationFlag(boolean smsNotificationFlag) {
        this.smsNotificationFlag = smsNotificationFlag;
    }

    public boolean getPushNotificationFlag() {
        return pushNotificationFlag;
    }

    public void setPushNotificationFlag(boolean pushNotificationFlag) {
        this.pushNotificationFlag = pushNotificationFlag;
    }

    public boolean getServiceAvailabilityFlag() {
        return serviceAvailabilityFlag;
    }

    public void setServiceAvailabilityFlag(boolean serviceAvailabilityFlag) {
        this.serviceAvailabilityFlag = serviceAvailabilityFlag;
    }

    public boolean isEnergyConsumptionFlag() {
        return energyConsumptionFlag;
    }

    public void setEnergyConsumptionFlag(boolean energyConsumptionFlag) {
        this.energyConsumptionFlag = energyConsumptionFlag;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "smsNotificationFlag=" + smsNotificationFlag +
                ", pushNotificationFlag=" + pushNotificationFlag +
                ", serviceAvailabilityFlag=" + serviceAvailabilityFlag +
                ", energyConsumptionFlag=" + energyConsumptionFlag +
                '}';
    }
}
