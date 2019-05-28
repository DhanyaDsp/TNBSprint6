package com.ey.dgs.webservice;

import com.ey.dgs.api_response.APIResponse;
import com.ey.dgs.api_response.AccountSettingsResponse;
import com.ey.dgs.api_response.BillingDetailsResponse;
import com.ey.dgs.api_response.GetQuestionsResponse;
import com.ey.dgs.api_response.LoginRequest;
import com.ey.dgs.api_response.LoginResponse;
import com.ey.dgs.api_response.PrimaryAccountResponse;
import com.ey.dgs.api_response.UserDetailResponse;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.AnswerRequest;
import com.ey.dgs.model.BillingPeriodReqest;
import com.ey.dgs.model.NotificationSettingsRequest;
import com.ey.dgs.model.SetPrimaryAccountRequest;
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
    Call<PrimaryAccountResponse> updateUser(@Header("Authorization") String token, @Body User user);

    @POST("PrimaryAccount")
    Call<PrimaryAccountResponse> setPrimaryAccount(@Header("Authorization") String token, @Body SetPrimaryAccountRequest setPrimaryAccountRequest);

    @GET("UserDetails")
    Call<UserDetailResponse> getUserDetails(@Header("Authorization") String token, @Query("UserName") String UserName);

    @GET("AccountDetails")
    Call<AccountSettingsResponse> getAccountSettings(@Header("Authorization") String token, @Query("AccountNumber") String AccountNumber, @Query("UserName") String userName);

    @POST("AccountDetails")
    Call<APIResponse> updateAccountSettings(@Header("Authorization") String token, @Body NotificationSettingsRequest notificationSettingsRequest);

    @POST("Questionaire")
    Call<APIResponse> answerQuestions(@Header("Authorization") String token,@Body AnswerRequest answerRequest);

    @GET("Questionaire")
    Call<GetQuestionsResponse> getQuestions(@Header("Authorization") String token, @Query("UserName") String UserName, @Query("AccountNumber") String AccountNumber);

    @POST("BillingDetails")
    Call<BillingDetailsResponse> getBillingHistory(@Header("Authorization") String token, @Body BillingPeriodReqest billingPeriodReqest);
}
