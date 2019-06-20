package com.ey.dgs.dashboard.billing;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.api_response.BillingDetailsResponse;
import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.BillingDetails;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class BillingHistoryViewModel extends ViewModel implements DatabaseCallback, APICallback {

    private MutableLiveData<ArrayList<BillingHistory>> billingHistories = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> daysList = new MutableLiveData<>();
    private MutableLiveData<BillingHistory> billingHistory = new MutableLiveData<>();
    private MutableLiveData<Boolean> loaderData = new MutableLiveData<>();
    private Context context;
    private AppPreferences appPreferences;

    public LiveData<ArrayList<BillingHistory>> getBillingHistories() {
        return billingHistories;
    }

    public LiveData<BillingHistory> getBillingHistory() {
        return billingHistory;
    }

    private void setBillingHistory(BillingHistory billingHistory) {
        this.billingHistory.postValue(billingHistory);
    }

    public MutableLiveData<Boolean> getLoaderData() {
        return loaderData;
    }

    public void setLoaderData(Boolean show) {
        this.loaderData.postValue(show);
    }

    private void setDays(ArrayList<String> days) {
        this.daysList.postValue(days);
    }

    public MutableLiveData<ArrayList<String>> getDays() {
        return daysList;
    }

    private void setBillingHistories(ArrayList<BillingHistory> billingHistories) {
        this.billingHistories.postValue(billingHistories);
    }

    public void setContext(Context context) {
        this.context = context;
        appPreferences = new AppPreferences(context);
        ArrayList<String> days = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            days.add("Apr " + i);
        }
        this.daysList.postValue(days);
    }

    public Context getContext() {
        return context;
    }

    public void getBillingHistoryFromServer(User user, String period, Account account) {
        setLoaderData(true);
        new ApiClient().getBillingHistoryFromServer(appPreferences.getAuthToken(), account, period, user.getEmail(), this);
    }

    /*public void addBillingHistoriesToLocalDB(List<BillingHistory> billingHistories) {
        DatabaseClient.getInstance(context).addBillingHistories(BillingHistory.REQUEST_CODE_ADD_BILLING_HISTORY, billingHistories, this);
    }*/

    public void addBillingHistoryToLocalDB(BillingHistory billingHistory) {
        DatabaseClient.getInstance(context).addBillingHistory(BillingHistory.REQUEST_CODE_ADD_BILLING_HISTORY, billingHistory, this);
    }

    public void loadBillingHistoryFromLocalDB(String accountNumber) {
        DatabaseClient.getInstance(context).getBillingHistory(accountNumber, BillingHistory.REQUEST_CODE_GET_BILLING_HISTORY, this);
    }

    @Override
    public void onInsert(Object object, int requestCode, int responseCode) {
        if (requestCode == BillingHistory.REQUEST_CODE_ADD_BILLING_HISTORY) {
            if (object != null) {
                if (object instanceof BillingHistory) {
                    setBillingHistory((BillingHistory) object);
                }
            }
        }
    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {

    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        if (requestCode == BillingHistory.REQUEST_CODE_GET_BILLING_HISTORY) {
            BillingHistory billingHistory = (BillingHistory) object;
            setBillingHistory(billingHistory);
        }
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        setLoaderData(false);
        if (requestCode == ApiClient.REQUEST_CODE_GET_BILLING_HISTORY) {
            BillingDetailsResponse billingDetailsResponse = (BillingDetailsResponse) obj;
            BillingDetails[] billingDetails = billingDetailsResponse.getResult().getBillingDetails();
            BillingHistory billingHistory = new BillingHistory();
            billingHistory.setAccountNumber(billingDetailsResponse.getResult().getAccountNumber());
            JsonArray billingDetailsJsonArray = new JsonArray();
            for (BillingDetails billingDetail : billingDetails) {
                JsonObject object = new JsonObject();
                object.addProperty("billedValue", billingDetail.getBilledValue());
                object.addProperty("billedDate", billingDetail.getBilledDate());
                billingDetailsJsonArray.add(object);
            }
            billingHistory.setBillingDetails(billingDetailsJsonArray.toString());
            addBillingHistoryToLocalDB((billingHistory));
        }
    }


    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        setLoaderData(false);
        Utils.showToast(context, (String) obj);
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }
}
