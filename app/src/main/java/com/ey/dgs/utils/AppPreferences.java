package com.ey.dgs.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    Context context;
    SharedPreferences preferences;
    public static String USER_ID = "user_id";
    public static String FCM_TOKEN = "fcm_token";
    public static String AZURE_REG_ID = "azure_reg_id";
    int user_id;
    String fcm_token, azure_reg_id;
    boolean isLoginned;

    public AppPreferences(Context context) {

        this.context = context;
        preferences = context.getSharedPreferences("prefs", 0);
    }


    public boolean isLoginned() {
        isLoginned = preferences.getBoolean("isLoginned", false);
        return isLoginned;
    }

    public void setLoginned(boolean isLoginned) {
        preferences.edit().putBoolean("isLoginned", isLoginned).apply();
        this.isLoginned = isLoginned;
    }

    public int getUser_id() {
        user_id = preferences.getInt(USER_ID, -1);
        return user_id;
    }

    public void setUser_id(int user_id) {
        preferences.edit().putInt(USER_ID, user_id).apply();
    }

    public String getFcm_token() {
        fcm_token = preferences.getString(FCM_TOKEN, null);
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        preferences.edit().putString(FCM_TOKEN, fcm_token).apply();
    }

    public String getAzureRegId() {
        azure_reg_id = preferences.getString(AZURE_REG_ID, null);
        return azure_reg_id;
    }

    public void setAzureRegId(String azureRegId) {
        preferences.edit().putString(AZURE_REG_ID, azureRegId).apply();
    }

    public void clearAll() {
        preferences.edit().clear().apply();
    }
}
