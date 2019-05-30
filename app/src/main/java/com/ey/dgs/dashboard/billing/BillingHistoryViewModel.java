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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillingHistoryViewModel extends ViewModel implements DatabaseCallback, APICallback {

    private MutableLiveData<ArrayList<BillingHistory>> billingHistoryData = new MutableLiveData<>();
    private Context context;
    private AppPreferences appPreferences;

    public LiveData<ArrayList<BillingHistory>> getBillingHistory() {
        return billingHistoryData;
    }

    private void setBillingHistory(ArrayList<BillingHistory> billingHistories) {
        this.billingHistoryData.postValue(billingHistories);
    }

    public void setContext(Context context) {
        this.context = context;
        appPreferences = new AppPreferences(context);
    }

    public Context getContext() {
        return context;
    }

    public void getBillingHistoryFromServer(User user, Account account) {
        new ApiClient().getBillingHistoryFromServer(appPreferences.getAuthToken(), account, user.getEmail(), this);
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
                ArrayList<BillingHistory> billingHistories = new ArrayList((List<BillingHistory>) object);
                setBillingHistory(billingHistories);
            }
        }
    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {
        if (requestCode == Account.REQUEST_CODE_SET_PRIMARY_ACCOUNT) {

        }
    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        if (requestCode == BillingHistory.REQUEST_CODE_GET_BILLING_HISTORY) {
            BillingHistory billingHistory = (BillingHistory) object;
            billingHistoryData.getValue().add(billingHistory);
        }
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        if (requestCode == ApiClient.REQUEST_CODE_GET_BILLING_HISTORY) {
            BillingDetailsResponse billingDetailsResponse = (BillingDetailsResponse) obj;
            BillingDetails[] billingDetails = billingDetailsResponse.getResult().getBillingDetails();
            BillingHistory billingHistory = new BillingHistory();
            billingHistory.setAccountNumber(billingDetailsResponse.getResult().getAccountNumber());
            billingHistory.setBillingDetails(Arrays.toString(billingDetails));
            addBillingHistoryToLocalDB((billingHistory));
        }
    }


    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        Utils.showToast(context, (String) obj);
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }
}
