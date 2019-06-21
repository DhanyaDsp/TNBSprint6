package com.ey.dgs.webservice;

import com.ey.dgs.api_response.APIResponse;
import com.ey.dgs.api_response.AccountSettingsResponse;
import com.ey.dgs.api_response.BillingDetailsResponse;
import com.ey.dgs.api_response.GetQuestionsResponse;
import com.ey.dgs.api_response.LoginRequest;
import com.ey.dgs.api_response.LoginResponse;
import com.ey.dgs.api_response.PrimaryAccountResponse;
import com.ey.dgs.api_response.UserDetailResponse;
import com.ey.dgs.api_response.UserSettingsResponse;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountDetailsRequest;
import com.ey.dgs.model.AnswerRequest;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.BillingPeriodReqest;
import com.ey.dgs.model.NotificationSettingsRequest;
import com.ey.dgs.model.SetPrimaryAccountRequest;
import com.ey.dgs.model.User;
import com.ey.dgs.model.UserSettings;
import com.ey.dgs.utils.HttpClientService;
import com.ey.dgs.utils.MockResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://apscadvdgsapm01.azure-api.net/Login/v1.3/";
    private static final String LOGIN_BASE_URL = "https://apscadvdgsapm01.azure-api.net/UserDetails/v1.3/";
    private static final String BILLING_BASE_URL = "https://apscadvdgsapm01.azure-api.net/BillingHistory/v1.3/";
    private static final String ACCOUNT_DETAILS_URL = "https://apscadvdgsapm01.azure-api.net/UserDetails/v1.3/";
    private static Retrofit retrofit = null;

    public static int REQUEST_CODE_UPDATE_USER = 100;
    private static int REQUEST_CODE_LOGIN_USER = 101;
    public static int REQUEST_CODE_GET_USER = 102;
    public static int REQUEST_CODE_UPDATE_ACCOUNT_DETAILS = 103;
    public static int REQUEST_CODE_GET_ACCOUNT_DETAILS = 104;
    private static int REQUEST_CODE_SET_PRIMARY_ACCOUNT = 105;
    public static int REQUEST_CODE_GET_QUESTIONS = 106;
    public static int REQUEST_CODE_GET_BILLING_HISTORY = 107;
    public static int REQUEST_CODE_ANSWER_QUESTIONS = 108;
    public static int REQUEST_CODE_GET_USER_SETTINGS = 109;
    public static int REQUEST_CODE_UPDATE_USER_SETTINGS = 110;

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static Retrofit getUserDetailsClient() {
        return retrofit = new Retrofit.Builder()
                .baseUrl(LOGIN_BASE_URL).client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getBillingHistoryClient() {
        return retrofit = new Retrofit.Builder()
                .baseUrl(BILLING_BASE_URL).client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getAccountDetailsClient() {
        return retrofit = new Retrofit.Builder()
                .baseUrl(ACCOUNT_DETAILS_URL).client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRawTextClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(LOGIN_BASE_URL)
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public void login(User user, APICallback callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse != null) {
                    if (loginResponse.isSuccess()) {
                        callback.onSuccess(REQUEST_CODE_LOGIN_USER, loginResponse, response.code());
                    } else {
                        loginResponse.setMessage("Failed to login");
                        callback.onFailure(REQUEST_CODE_LOGIN_USER, loginResponse, response.code());
                    }
                } else {
                    loginResponse = new LoginResponse();
                    loginResponse.setMessage("Failed to login!");
                    callback.onFailure(REQUEST_CODE_LOGIN_USER, loginResponse, response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setMessage("Failed to login!");
                callback.onFailure(REQUEST_CODE_LOGIN_USER, loginResponse, 0);
            }
        });
    }

    public void getUserDetail(String token, User user, APICallback callback) {
        ApiInterface apiService = ApiClient.getUserDetailsClient().create(ApiInterface.class);
        Call<UserDetailResponse> call = apiService.getUserDetails(token, user.getEmail());
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                UserDetailResponse userDetailResponse = response.body();
                if (userDetailResponse != null) {
                    if (userDetailResponse.getSuccess()) {
                        //setting local user data
                        userDetailResponse.getUser().setRememberMe(user.isRememberMe());
                        userDetailResponse.getUser().setUserId(user.getUserId());
                        userDetailResponse.getUser().setSplashScreenSeen(user.isSplashScreenSeen());
                        callback.onSuccess(REQUEST_CODE_GET_USER, userDetailResponse, response.code());
                    } else {
                        userDetailResponse.setMessage("Failed to load User Details!");
                        callback.onFailure(REQUEST_CODE_GET_USER, userDetailResponse.getMessage(), response.code());
                    }
                } else {
                    userDetailResponse = new UserDetailResponse();
                    userDetailResponse.setMessage("Failed to load User Details!");
                    callback.onFailure(REQUEST_CODE_GET_USER, userDetailResponse.getMessage(), response.code());
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                UserDetailResponse userDetailResponse = new UserDetailResponse();
                userDetailResponse.setMessage("Failed to load User Details!");
                callback.onFailure(REQUEST_CODE_GET_USER, userDetailResponse.getMessage(), 0);
            }
        });
    }

    public void updateUser(String token, User user, APICallback callback) {
        ApiInterface apiService = ApiClient.getRawTextClient().create(ApiInterface.class);

        Call<PrimaryAccountResponse> call = apiService.updateUser(token, user);
        call.enqueue(new Callback<PrimaryAccountResponse>() {
            @Override
            public void onResponse(Call<PrimaryAccountResponse> call, Response<PrimaryAccountResponse> response) {
                PrimaryAccountResponse accountResponse = response.body();
                if (accountResponse != null && accountResponse.isSuccess()) {
                    callback.onSuccess(REQUEST_CODE_UPDATE_USER, user, response.code());
                } else {
                    callback.onFailure(REQUEST_CODE_UPDATE_USER, "Failed to update User", response.code());
                }
            }

            @Override
            public void onFailure(Call<PrimaryAccountResponse> call, Throwable t) {
                callback.onFailure(REQUEST_CODE_UPDATE_USER, "Failed to update User", 0);
            }
        });
    }

    public void getUserSettings(String token, User user, APICallback callback) {
        ApiInterface apiService = ApiClient.getUserDetailsClient().create(ApiInterface.class);

        Call<UserSettingsResponse> call = apiService.getUserSettings(token, user.getEmail());
        call.enqueue(new Callback<UserSettingsResponse>() {
            @Override
            public void onResponse(Call<UserSettingsResponse> call, Response<UserSettingsResponse> response) {
                UserSettingsResponse userSettingsResponse = response.body();
                /*MOCK RESPONSE*/
                //Gson gson = new Gson();
                //UserSettingsResponse userSettingsResponse = gson.fromJson(MockResponse.USER_SETTINGS_RESPONSE, UserSettingsResponse.class);
                /*MOCK RESPONSE*/
                if (userSettingsResponse != null) {
                    if (userSettingsResponse.isSuccess()) {
                        userSettingsResponse.getUserSettings().setUserId(user.getUserId());
                        userSettingsResponse.getUserSettings().setUserName(user.getEmail());
                        callback.onSuccess(REQUEST_CODE_GET_USER_SETTINGS, userSettingsResponse, response.code());
                    } else {
                        userSettingsResponse.setMessage("Failed to load User Settings!");
                        callback.onFailure(REQUEST_CODE_GET_USER_SETTINGS, userSettingsResponse.getMessage(), response.code());
                    }
                } else {
                    userSettingsResponse = new UserSettingsResponse();
                    userSettingsResponse.setMessage("Failed to load User Settings!");
                    callback.onFailure(REQUEST_CODE_GET_USER_SETTINGS, userSettingsResponse.getMessage(), response.code());
                }
            }

            @Override
            public void onFailure(Call<UserSettingsResponse> call, Throwable t) {
                UserSettingsResponse userSettingsResponse = new UserSettingsResponse();
                userSettingsResponse.setMessage("Failed to load User Settings!");
                callback.onFailure(REQUEST_CODE_GET_USER_SETTINGS, userSettingsResponse.getMessage(), 0);
            }
        });
    }

    public void getAccountSettingsFromServer(String token, String userName, String accountNumber, APICallback callback) {
        ApiInterface apiService = ApiClient.getUserDetailsClient().create(ApiInterface.class);

        Call<AccountSettingsResponse> call = apiService.getAccountSettings(token, userName, accountNumber);
        call.enqueue(new Callback<AccountSettingsResponse>() {
            @Override
            public void onResponse(Call<AccountSettingsResponse> call, Response<AccountSettingsResponse> response) {
                AccountSettingsResponse accountSettingsResponse = response.body();
                if (accountSettingsResponse != null) {
                    accountSettingsResponse.getResult().setAccountNumber(accountNumber);
                    accountSettingsResponse.getResult().setAccountId(Integer.parseInt(accountNumber));
                    callback.onSuccess(REQUEST_CODE_GET_ACCOUNT_DETAILS, accountSettingsResponse.getResult(), response.code());
                } else {
                    accountSettingsResponse = new AccountSettingsResponse();
                    accountSettingsResponse.setMessage("Failed to load Account Settings!");
                    callback.onFailure(REQUEST_CODE_GET_ACCOUNT_DETAILS, accountSettingsResponse.getMessage(), response.code());
                }

            }

            @Override
            public void onFailure(Call<AccountSettingsResponse> call, Throwable t) {
                AccountSettingsResponse accountSettingsResponse = new AccountSettingsResponse();
                accountSettingsResponse.setMessage("Failed to load Account Settings!");
                callback.onFailure(REQUEST_CODE_GET_ACCOUNT_DETAILS, accountSettingsResponse.getMessage(), 0);
            }
        });
    }

    public void updateAccountSettingsInServer(String token, NotificationSettingsRequest notificationSettingsRequest, APICallback callback) {
        ApiInterface apiService = ApiClient.getRawTextClient().create(ApiInterface.class);
        Call<APIResponse> call = apiService.updateAccountSettings(token, notificationSettingsRequest);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse = response.body();
                if (apiResponse != null) {
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(REQUEST_CODE_UPDATE_ACCOUNT_DETAILS, notificationSettingsRequest, response.code());
                    } else {
                        callback.onFailure(REQUEST_CODE_UPDATE_ACCOUNT_DETAILS, "Failed to update Account Settings", response.code());
                    }
                } else {
                    callback.onFailure(REQUEST_CODE_UPDATE_ACCOUNT_DETAILS, "Failed to update Account Settings", response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                callback.onFailure(REQUEST_CODE_UPDATE_ACCOUNT_DETAILS, "Failed to update Account Settings", 0);
            }
        });
    }

    public void updateUserSettingsInServer(String token, UserSettings userSettings, APICallback callback) {
        ApiInterface apiService = ApiClient.getRawTextClient().create(ApiInterface.class);
        Call<APIResponse> call = apiService.updateUserSettings(token, userSettings);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse = response.body();
                if (apiResponse != null) {
                    if (apiResponse.isSuccess()) {
                        if (callback != null)
                            callback.onSuccess(REQUEST_CODE_UPDATE_USER_SETTINGS, userSettings, response.code());
                    } else {
                        apiResponse.setMessage("Failed to update User Settings");
                        if (callback != null)
                            callback.onFailure(REQUEST_CODE_UPDATE_USER_SETTINGS, apiResponse.getMessage(), response.code());
                    }
                } else {
                    apiResponse = new APIResponse();
                    apiResponse.setMessage("Failed to update User Settings");
                    if (callback != null)
                        callback.onFailure(REQUEST_CODE_UPDATE_USER_SETTINGS, apiResponse.getMessage(), response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if (callback != null)
                    callback.onFailure(REQUEST_CODE_UPDATE_USER_SETTINGS, "Failed to update User Settings", 0);
            }
        });
    }

    public void getQuestionsFromServer(String token, String accountNumber, String userName, APICallback callback) {
        ApiInterface apiService = ApiClient.getUserDetailsClient().create(ApiInterface.class);

        Call<GetQuestionsResponse> call = apiService.getQuestions(token, userName, accountNumber);
        call.enqueue(new Callback<GetQuestionsResponse>() {
            @Override
            public void onResponse(Call<GetQuestionsResponse> call, Response<GetQuestionsResponse> response) {
                GetQuestionsResponse getQuestionsResponse = response.body();
             /*   Gson gson = new Gson();
                getQuestionsResponse = gson.fromJson(MockResponse.MOCK_QUESTIONS_RESPONSE, GetQuestionsResponse.class);*/
                if (getQuestionsResponse != null) {
                    if (getQuestionsResponse.isSuccess()) {
                        callback.onSuccess(REQUEST_CODE_GET_QUESTIONS, getQuestionsResponse, response.code());
                    } else {
                        getQuestionsResponse = new GetQuestionsResponse();
                        callback.onFailure(REQUEST_CODE_GET_QUESTIONS, getQuestionsResponse.getMessage(), response.code());
                    }
                } else {
                    getQuestionsResponse = new GetQuestionsResponse();
                    callback.onFailure(REQUEST_CODE_GET_QUESTIONS, "Failed to load questions", response.code());
                }
            }

            @Override
            public void onFailure(Call<GetQuestionsResponse> call, Throwable t) {
                GetQuestionsResponse getQuestionsResponse = new GetQuestionsResponse();
                callback.onFailure(REQUEST_CODE_GET_QUESTIONS, "Failed to load questions", 0);
            }
        });
    }

    public void getBillingHistoryFromServer(String token, Account account, String period, String
            userName, APICallback callback) {
        ApiInterface apiService = ApiClient.getBillingHistoryClient().create(ApiInterface.class);
        BillingPeriodReqest billingPeriodReqest = new BillingPeriodReqest(account.getAccountNumber(), period);
        Call<BillingDetailsResponse> call = apiService.getBillingHistory(token, billingPeriodReqest);
        call.enqueue(new Callback<BillingDetailsResponse>() {
            @Override
            public void onResponse(Call<BillingDetailsResponse> call, Response<BillingDetailsResponse> response) {
                BillingDetailsResponse billingDetailsResponse = response.body();
               /* BillingDetailsResponse billingDetailsResponse = new BillingDetailsResponse();
                Gson gson = new Gson();
                if (period.equalsIgnoreCase(BillingHistory.DAILY)) {
                    billingDetailsResponse = gson.fromJson(MockResponse.MOCK_BILLING_RESPONSE_DAILY,
                            BillingDetailsResponse.class);
                } else if (period.equalsIgnoreCase(BillingHistory.WEEKLY)) {
                    billingDetailsResponse = gson.fromJson(MockResponse.MOCK_BILLING_RESPONSE_WEEKLY,
                            BillingDetailsResponse.class);
                } else if (period.equalsIgnoreCase(BillingHistory.MONTHLY)) {
                    billingDetailsResponse = gson.fromJson(MockResponse.MOCK_BILLING_RESPONSE_MONTHLY,
                            BillingDetailsResponse.class);
                }*/

                if (billingDetailsResponse != null) {
                    if (billingDetailsResponse.isSuccess()) {
                        account.setBillingDetails(billingDetailsResponse.getResult().getBillingDetails());
                        callback.onSuccess(REQUEST_CODE_GET_BILLING_HISTORY, billingDetailsResponse, response.code());
                    } else {
                        billingDetailsResponse = new BillingDetailsResponse();
                        billingDetailsResponse.setMessage("Failed to load Billing History!");
                        callback.onFailure(REQUEST_CODE_GET_BILLING_HISTORY, billingDetailsResponse.getMessage(), response.code());
                    }
                } else {
                    billingDetailsResponse = new BillingDetailsResponse();
                    billingDetailsResponse.setMessage("Failed to load Billing History!");
                    callback.onFailure(REQUEST_CODE_GET_BILLING_HISTORY, billingDetailsResponse.getMessage(), response.code());
                }
            }

            @Override
            public void onFailure(Call<BillingDetailsResponse> call, Throwable t) {
                BillingDetailsResponse billingDetailsResponse = new BillingDetailsResponse();
                billingDetailsResponse.setMessage("Failed to load Billing History!");
                callback.onFailure(REQUEST_CODE_GET_BILLING_HISTORY, billingDetailsResponse.getMessage(), 0);
            }
        });
    }

    public void answerQuestion(String token, AnswerRequest answerRequest, APICallback callback) {
        ApiInterface apiService = ApiClient.getRawTextClient().create(ApiInterface.class);
        Call<APIResponse> call = apiService.answerQuestions(token, answerRequest);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse = response.body();
                if (apiResponse != null) {
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(REQUEST_CODE_ANSWER_QUESTIONS, answerRequest.getQuestionId(), response.code());
                    } else {
                        callback.onFailure(REQUEST_CODE_ANSWER_QUESTIONS, "Failed to answer this question", response.code());
                    }
                } else {
                    callback.onFailure(REQUEST_CODE_ANSWER_QUESTIONS, "Failed to answer this question", response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                callback.onFailure(REQUEST_CODE_ANSWER_QUESTIONS, "Failed to answer this question", 0);
            }
        });
    }

    public void updateAccountDetails(String token, AccountDetailsRequest accountDetailsRequest, APICallback callback) {
        ApiInterface apiInterface = ApiClient.getAccountDetailsClient().create(ApiInterface.class);
        Call<APIResponse> call = apiInterface.updateAccountDetails(token, accountDetailsRequest);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse = response.body();
                if (apiResponse != null) {
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(REQUEST_CODE_ANSWER_QUESTIONS, apiResponse.getStatusCode(), response.code());
                    } else {
                        callback.onFailure(REQUEST_CODE_ANSWER_QUESTIONS, "Failed to answer this question", response.code());
                    }
                } else {
                    callback.onFailure(REQUEST_CODE_ANSWER_QUESTIONS, "Failed to answer this question", response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }
}
