package com.ey.dgs.webservice;

import com.ey.dgs.api_response.LoginRequest;
import com.ey.dgs.api_response.LoginResponse;
import com.ey.dgs.api_response.UserDetailResponse;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("Authenticate")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("UserDetails")
    Call<String> updateUser(@Body User user);

    @GET("UserDetails")
    Call<UserDetailResponse> getUserDetails(@Query("UserName") String UserName);

    @GET("AccountDetails")
    Call<AccountSettings> getAccountSettings(@Query("AccountNumber") String AccountNumber);

}
