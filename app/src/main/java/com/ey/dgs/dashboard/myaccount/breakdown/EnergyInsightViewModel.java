package com.ey.dgs.dashboard.myaccount.breakdown;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.model.BreakQuestionItem;

import java.util.ArrayList;

public class EnergyInsightViewModel extends ViewModel {

    MutableLiveData<ArrayList<BreakQuestionItem>> breakdownItems = new MutableLiveData<>();
    Context context;


    public void loadQuestions() {
        ArrayList<BreakQuestionItem> breakQuestionItems = new ArrayList<>();
        breakQuestionItems.add(new BreakQuestionItem("Air-Con", false, ""));
        breakQuestionItems.add(new BreakQuestionItem("Refrigerator", false, ""));
        breakQuestionItems.add(new BreakQuestionItem("Dryer", false, ""));
        breakQuestionItems.add(new BreakQuestionItem("Washing Machine", false, ""));
        breakQuestionItems.add(new BreakQuestionItem("Television", false, ""));
        breakQuestionItems.add(new BreakQuestionItem("Oven", false, ""));
        breakQuestionItems.add(new BreakQuestionItem("Water Heater", false, ""));
        breakQuestionItems.add(new BreakQuestionItem("Water Pump", false, ""));
        this.breakdownItems.postValue(breakQuestionItems);
    }

    public MutableLiveData<ArrayList<BreakQuestionItem>> getBreakdownItems() {
        return breakdownItems;
    }

    public void setBreakdownItems(ArrayList<BreakQuestionItem> breakdownItems) {
        this.breakdownItems.postValue(breakdownItems);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
