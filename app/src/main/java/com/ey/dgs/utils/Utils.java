package com.ey.dgs.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

import com.ey.dgs.notifications.pushnotifications.FireBaseMessagingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

public class Utils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if ((email.matches(emailPattern))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPlayServices(Context context) {
        int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }

    public static boolean isAppIsInBackground(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals("com.ey.dgs")) {
                    return true;
                }
            }
        }
        return false;

    }
}
