package com.ey.dgs.api_response;

import com.google.gson.Gson;

public class LoginRequest {

    private String UserName;

    private String Password;


    public LoginRequest(String userName, String password) {
        UserName = userName;
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
