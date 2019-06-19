package com.ey.dgs.dashboard.questions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.database.DatabaseCallback;
import com.ey.dgs.database.DatabaseClient;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountDetails;
import com.ey.dgs.model.AccountDetailsRequest;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import static com.ey.dgs.webservice.ApiClient.REQUEST_CODE_ANSWER_QUESTIONS;

public class QuestionsViewModel extends ViewModel implements APICallback, DatabaseCallback {

    private Context context;
    AppPreferences appPreferences;
    boolean isSuccess = false;
    private MutableLiveData<Boolean> loaderData = new MutableLiveData<>();
    private MutableLiveData<AccountDetails[]> accountData = new MutableLiveData<>();
    private MutableLiveData<Account> account = new MutableLiveData<>();
    AccountDetails[] accountDetails;

    public void setContext(Context context) {
        this.context = context;
        appPreferences = new AppPreferences(context);
    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        if (requestCode == REQUEST_CODE_ANSWER_QUESTIONS) {
            setLoader(false);
            setSuccess(true);
            updateAccountDetailsToLocalDB(getAccountDetails());
        }
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        Utils.showToast(context, (String) obj);
        setLoader(false);
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }

    public void getAccountFromLocalDB(Account account) {
        DatabaseClient.getInstance(context).getAccount(Account.REQUEST_CODE_GET_ACCOUNT,
                account, this);
    }

    public void updateAccountDetailsToLocalDB(AccountDetails[] accountDetails) {
        DatabaseClient.getInstance(context).updateSingleAccountThreshold(Account.REQUEST_CODE_UPDATE_ACCOUNTS,
                accountDetails, this);
    }

    public void updateAccountDetailsInServer(AccountDetails[] accountDetailsArray) {
        AccountDetailsRequest accountDetailsRequest = new AccountDetailsRequest(appPreferences.getUser_name(), accountDetailsArray);
        new ApiClient().updateAccountDetails(appPreferences.getAuthToken(), accountDetailsRequest, this);
        setAccountDetails(accountDetailsRequest.getAccountDetails());
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public MutableLiveData<Boolean> getLoaderData() {
        return loaderData;
    }

    public void setLoader(boolean showLoader) {
        loaderData.postValue(showLoader);
    }

    private void setAccountDetails(AccountDetails[] accountDetails) {
        this.accountDetails = accountDetails;
    }

    public AccountDetails[] getAccountDetails() {
        return accountDetails;
    }

    @Override
    public void onInsert(Object object, int requestCode, int responseCode) {

    }

    @Override
    public void onUpdate(Object object, int requestCode, int responseCode) {
        if(requestCode == Account.REQUEST_CODE_UPDATE_ACCOUNTS) {
            this.accountData.postValue((AccountDetails[]) object);
        } else if (requestCode == Account.REQUEST_CODE_GET_ACCOUNT) {
            this.account.postValue((Account) object);
        }
    }

    @Override
    public void onReceived(Object object, int requestCode, int responseCode) {
        if(requestCode == Account.REQUEST_CODE_UPDATE_ACCOUNTS) {
            this.accountData.postValue((AccountDetails[]) object);
        } else if (requestCode == Account.REQUEST_CODE_GET_ACCOUNT) {
            this.account.postValue((Account) object);
        }
    }

    @Override
    public void onError(Object object, int requestCode, int responseCode) {

    }

    public MutableLiveData<AccountDetails[]> getAccountData() {
        return accountData;
    }

    public void setAccountData(MutableLiveData<AccountDetails[]> accountData) {
        this.accountData = accountData;
    }

    public MutableLiveData<Account> getAccount() {
        return account;
    }

    public void setAccount(MutableLiveData<Account> account) {
        this.account = account;
    }
}
