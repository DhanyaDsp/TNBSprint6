package com.ey.dgs.model;

public class EnergyConsumptions {

    private boolean energyConsumptionFlag;
    private boolean averageConsumption;
    private boolean userThreshold;

    public boolean isEnergyConsumptionFlag() {
        return energyConsumptionFlag;
    }

    public void setEnergyConsumptionFlag(boolean energyConsumptionFlag) {
        this.energyConsumptionFlag = energyConsumptionFlag;
    }

    public boolean isAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(boolean averageConsumption) {
        this.averageConsumption = averageConsumption;
    }

    public boolean isUserThreshold() {
        return userThreshold;
    }

    public void setUserThreshold(boolean userThreshold) {
        this.userThreshold = userThreshold;
    }
}
