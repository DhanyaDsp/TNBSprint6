package com.ey.dgs.database;

public interface DatabaseCallback {

    void onInsert(Object object, int requestCode, int responseCode);

    void onUpdate(Object object, int requestCode, int responseCode);

    void onReceived(Object object, int requestCode, int responseCode);

    void onError(Object object, int requestCode, int responseCode);
}
