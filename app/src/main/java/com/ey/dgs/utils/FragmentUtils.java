package com.ey.dgs.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ey.dgs.R;
import com.ey.dgs.authentication.LoginFragment;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.dashboard.MyDashboardFragment;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.dashboard.questions.MMCQuestionsFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.NotificationSetting;
import com.ey.dgs.notifications.settings.AccountNotificationSettingsFragment;
import com.ey.dgs.notifications.settings.NotificationToggleFragment;
import com.ey.dgs.notifications.settings.SettingsMenuFragment;

import java.util.HashMap;

public class FragmentUtils {

    FragmentManager fragmentManager;
    private static FragmentUtils fragmentUtils;
    private HashMap<String, Fragment> fragments = new HashMap<>();
    public static int INDEX_LOGIN = 1;
    public static int INDEX_MY_ACCOUNT = 4;
    public static int INDEX_SETTINGS_FRAGMENT = 5;
    public static int INDEX_NOTIFICATION_TOGGLE_FRAGMENT = 6;
    public static int INDEX_NOTIFICATION_SETTINGS_FRAGMENT = 7;
    public static int INDEX_QUESTIONS_FRAGMENT = 8;
    public static int INDEX_MY_DASHBOARD_FRAGMENT = 9;

    public static FragmentUtils newInstance(FragmentManager fragmentManager) {
        fragmentUtils = new FragmentUtils(fragmentManager);
        return fragmentUtils;
    }

    private FragmentUtils(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void replaceFragment(int index, Object object, String fragmentTag, int viewId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment newFragment = null;
        switch (index) {
            case 7:
                newFragment = AccountNotificationSettingsFragment.newInstance((Account) object);
                fragmentTransaction.replace(viewId, newFragment, fragmentTag).commitAllowingStateLoss();
                break;
            case 9:
                newFragment = MyDashboardFragment.newInstance((Account) object);
                fragmentTransaction.replace(viewId, newFragment, fragmentTag).commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    public void addFragment(int index, Object object, String fragmentTag, int viewId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment newFragment = null;
        switch (index) {
            case 1:
                newFragment = LoginFragment.newInstance();
                fragmentTransaction.replace(viewId, newFragment, fragmentTag).commitAllowingStateLoss();
                break;
            case 3:
                newFragment = DashboardFragment.newInstance();
                fragmentTransaction.replace(viewId, newFragment, fragmentTag).commitAllowingStateLoss();
                break;
            case 4:
                newFragment = MyAccountFragment.newInstance((Account) object);
                fragmentTransaction.add(viewId, newFragment, fragmentTag).addToBackStack(fragmentTag).commit();
                break;
            case 5:
                newFragment = SettingsMenuFragment.newInstance();
                fragmentTransaction.add(viewId, newFragment, fragmentTag).addToBackStack(fragmentTag).commit();
                break;
            case 6:
                newFragment = NotificationToggleFragment.newInstance();
                fragmentTransaction.add(viewId, newFragment, fragmentTag).addToBackStack(fragmentTag).commit();
                break;
            case 7:
                newFragment = AccountNotificationSettingsFragment.newInstance((Account) object);
                fragmentTransaction.add(viewId, newFragment, fragmentTag).addToBackStack(fragmentTag).commit();
                break;
            case 8:
                newFragment = MMCQuestionsFragment.newInstance((Account) object);
                fragmentTransaction.add(viewId, newFragment, fragmentTag).addToBackStack(fragmentTag).commit();
                break;
            case 9:
                newFragment = MyDashboardFragment.newInstance((Account) object);
                fragmentTransaction.add(viewId, newFragment, fragmentTag).addToBackStack(fragmentTag).commit();
                break;
            default:
                break;
        }
    }
}
