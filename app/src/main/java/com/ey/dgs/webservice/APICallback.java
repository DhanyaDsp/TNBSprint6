package com.ey.dgs.webservice;

public interface APICallback {

    public void onSuccess(int requestCode, Object obj, int code);

    public void onFailure(int requestCode, Object obj, int code);

    public void onProgress(int requestCode, boolean isLoading);

    public void onOffline(int requestCode, boolean isLoading);
}
