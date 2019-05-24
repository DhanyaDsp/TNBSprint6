package com.ey.dgs.model;

import java.io.Serializable;

public class Setting implements Serializable {
    private boolean smsNotificationFlag;

    private boolean pushNotificationFlag;

    private boolean serviceAvailabilityFlag;

    private EnergyConsumptions energyConsumptions;

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

    public EnergyConsumptions getEnergyConsumptions() {
        return energyConsumptions;
    }

    public void setEnergyConsumptions(EnergyConsumptions energyConsumptions) {
        this.energyConsumptions = energyConsumptions;
    }

    @Override
    public String toString() {
        return "ClassPojo [smsNotificationFlag = " + smsNotificationFlag + ", pushNotificationFlag = " + pushNotificationFlag + ", serviceAvailabilityFlag = " + serviceAvailabilityFlag + ", energyConsumptions = " + energyConsumptions + "]";
    }
}
