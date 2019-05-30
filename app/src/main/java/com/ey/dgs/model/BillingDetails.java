package com.ey.dgs.model;


import java.io.Serializable;

public class BillingDetails implements Serializable {

    private int id;

    private String billedDate;

    private float billedValue;

    public String getBilledDate() {
        return billedDate;
    }

    public void setBilledDate(String billedDate) {
        this.billedDate = billedDate;
    }

    public float getBilledValue() {
        return billedValue;
    }

    public void setBilledValue(float billedValue) {
        this.billedValue = billedValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
