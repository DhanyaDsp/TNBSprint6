package com.ey.dgs.webservice;

import android.util.Log;

import com.ey.dgs.api_response.LoginRequest;
import com.ey.dgs.api_response.LoginResponse;
import com.ey.dgs.api_response.UserDetailResponse;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.User;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://apscadvdgsapm01.azure-api.net/Login/v1/";
    public static final String LOGIN_BASE_URL = "https://apscadvdgsapm01.azure-api.net/UserDetails/v1/";
    private static Retrofit retrofit = null;

    public static int REQUEST_CODE_UPDATE_USER = 100;
    public static int REQUEST_CODE_LOGIN_USER = 101;
    public static int REQUEST_CODE_GET_USER = 102;

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static Retrofit getUserDetailsClient() {
        return retrofit = new Retrofit.Builder()
                .baseUrl(LOGIN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
                        loginResponse.setMessage("Invalid Login");
                        callback.onFailure(REQUEST_CODE_LOGIN_USER, loginResponse, response.code());
                    }
                } else {
                    loginResponse = new LoginResponse();
                    loginResponse.setMessage("Please try again!");
                    callback.onFailure(REQUEST_CODE_LOGIN_USER, loginResponse, response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setMessage("Please try again!");
                callback.onFailure(REQUEST_CODE_LOGIN_USER, loginResponse, 0);
            }
        });
    }

    public void getUser(String userName, APICallback callback) {
        ApiInterface apiService = ApiClient.getUserDetailsClient().create(ApiInterface.class);

        Call<UserDetailResponse> call = apiService.getUserDetails(userName);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                UserDetailResponse userDetailResponse = response.body();
                if (userDetailResponse != null) {
                    if (userDetailResponse.getSuccess()) {
                        callback.onSuccess(REQUEST_CODE_GET_USER, userDetailResponse, response.code());
                    } else {
                        userDetailResponse.setMessage("Invalid Login");
                        callback.onFailure(REQUEST_CODE_GET_USER, userDetailResponse, response.code());
                    }
                } else {
                    userDetailResponse = new UserDetailResponse();
                    userDetailResponse.setMessage("Please try again!");
                    callback.onFailure(REQUEST_CODE_GET_USER, userDetailResponse, response.code());
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                UserDetailResponse userDetailResponse = new UserDetailResponse();
                userDetailResponse.setMessage("Please try again!");
                callback.onFailure(REQUEST_CODE_GET_USER, userDetailResponse, 0);
            }
        });
    }

    public void updateUser(User user, APICallback callback) {
        ApiInterface apiService = ApiClient.getUserDetailsClient().create(ApiInterface.class);

        Call<String> call = apiService.updateUser(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.equalsIgnoreCase("Updated")) {
                    callback.onSuccess(REQUEST_CODE_UPDATE_USER, user, response.code());
                } else {
                    callback.onFailure(REQUEST_CODE_UPDATE_USER, "Failed to update User", response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(REQUEST_CODE_UPDATE_USER, "Failed to update User", 0);
            }
        });
    }

    public void getAccountSettingsFromServer(String accountNumber, APICallback callback) {
        ApiInterface apiService = ApiClient.getUserDetailsClient().create(ApiInterface.class);

        Call<AccountSettings> call = apiService.getAccountSettings(accountNumber);
        call.enqueue(new Callback<AccountSettings>() {
            @Override
            public void onResponse(Call<AccountSettings> call, Response<AccountSettings> response) {
                AccountSettings accountSettings = response.body();
                if (accountSettings != null) {
                    accountSettings.setAccountNumber(accountNumber);
                    callback.onSuccess(REQUEST_CODE_UPDATE_USER, accountSettings, response.code());
                } else {
                    accountSettings = new AccountSettings();
                    accountSettings.setMessage("Please try again!");
                    callback.onFailure(REQUEST_CODE_UPDATE_USER, accountSettings, response.code());
                }

            }

            @Override
            public void onFailure(Call<AccountSettings> call, Throwable t) {
                UserDetailResponse userDetailResponse = new UserDetailResponse();
                userDetailResponse.setMessage("Please try again!");
                callback.onFailure(REQUEST_CODE_UPDATE_USER, userDetailResponse, 0);
            }
        });
    }
}
