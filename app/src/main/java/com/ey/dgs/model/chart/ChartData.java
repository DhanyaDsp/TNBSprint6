package com.ey.dgs.model.chart;

/**
 * Created by Sooraj.HS on 24-05-2019.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartData {

    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("val")
    @Expose
    private float val;
    @SerializedName("isSelected")
    @Expose
    private Boolean isSelected = false;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getVal() {
        return val;
    }

    public void setVal(float val) {
        this.val = val;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

}