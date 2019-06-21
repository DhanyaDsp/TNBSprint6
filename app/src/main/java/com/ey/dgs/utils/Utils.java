package com.ey.dgs.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.ParseException;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ey.dgs.notifications.pushnotifications.FireBaseMessagingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
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

    public static void showKeyBoard(Activity context) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void hideKeyBoard(Activity context) {
        if (context != null) {
            View view = context.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

    public static String formatAccountDate(String serverDate) {
        if (!TextUtils.isEmpty(serverDate)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date sourceDate = null;
            try {
                sourceDate = dateFormat.parse(serverDate);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM");
            String outputDate = targetFormat.format(sourceDate);
            return outputDate;
        }
        return "";
    }

    public static String formatCurrentDate(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM");
        String outputDate = targetFormat.format(sourceDate);
        return outputDate;
    }

    public static String formatNotificationDate() {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date());
    }

    public static String formatNotificationDate(Date date) {
        return new SimpleDateFormat("dd MMM yyyy").format(date);
    }

    public static String formatAccountDetailDate(String serverDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(serverDate);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy");
        String outputDate = targetFormat.format(sourceDate);
        return outputDate;
    }

    public static String formatThreshold(float value) {
        return String.format("%.2f", value);
    }

}
