package com.ey.dgs.notifications.pushnotifications;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Sooraj.HS on 27-09-2018.
 */

public class FireBaseInstanceIDService extends InstanceIDListenerService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//Displaying token on logcat

       /* Intent intent = new Intent(this, AzureRegistrationIntentService.class);
        startService(intent);*/


        Intent intent = new Intent(this, AzureRegistrationIntentService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else
            startService(intent);
    }
}
