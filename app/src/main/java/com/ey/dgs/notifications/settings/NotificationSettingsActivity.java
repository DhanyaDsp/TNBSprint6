package com.ey.dgs.notifications.settings;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.SettingMenuItem;
import com.ey.dgs.utils.FragmentUtils;

public class NotificationSettingsActivity extends AppCompatActivity implements SettingsMenuFragment.OnFragmentInteractionListener, NotificationToggleFragment.OnFragmentInteractionListener, AccountNotificationSettingsFragment.OnFragmentInteractionListener {

    boolean isComingFromPopup, isAddThreshold;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        isComingFromPopup = getIntent().getBooleanExtra("isComingFromPopup", false);
        isAddThreshold = getIntent().getBooleanExtra("isAddThreshold", false);
        account = (Account) getIntent().getSerializableExtra("account");
        initViews();
        showSettingsFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingsFragment() {
        if (isAddThreshold) {
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    setFragment(FragmentUtils.INDEX_SETTINGS_FRAGMENT, account, SettingsMenuFragment.class.getName(), R.id.flNotificationContainer);
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    setFragment(FragmentUtils.INDEX_NOTIFICATION_TOGGLE_FRAGMENT, account, SettingsMenuFragment.class.getName(), R.id.flNotificationContainer);
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    setFragment(FragmentUtils.INDEX_NOTIFICATION_SETTINGS_FRAGMENT, account, SettingsMenuFragment.class.getName(), R.id.flNotificationContainer);
        } else if (isComingFromPopup) {
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    setFragment(FragmentUtils.INDEX_SETTINGS_FRAGMENT, null, SettingsMenuFragment.class.getName(), R.id.flNotificationContainer);
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    setFragment(FragmentUtils.INDEX_NOTIFICATION_TOGGLE_FRAGMENT, null, SettingsMenuFragment.class.getName(), R.id.flNotificationContainer);

        } else {
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    setFragment(FragmentUtils.INDEX_SETTINGS_FRAGMENT, null, SettingsMenuFragment.class.getName(), R.id.flNotificationContainer);
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("More");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void moveToNotificationSettings(Account account) {
        FragmentUtils.newInstance(getSupportFragmentManager())
                .setFragment(FragmentUtils.INDEX_NOTIFICATION_SETTINGS_FRAGMENT, account, "AccountNotificationSettingsFragment", R.id.flNotificationContainer);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flNotificationContainer);
        if (currentFragment instanceof SettingsMenuFragment) {
            finish();
        } else if (currentFragment instanceof NotificationToggleFragment) {
            getSupportFragmentManager().popBackStack();
            setActionBarTitle("More");
        } else if (currentFragment instanceof AccountNotificationSettingsFragment) {
            getSupportFragmentManager().popBackStack();
            setActionBarTitle("Notifications");
        }

    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void moveToNotificationTogglePage() {
        FragmentUtils.newInstance(getSupportFragmentManager()).setFragment(FragmentUtils.INDEX_NOTIFICATION_TOGGLE_FRAGMENT, null, NotificationToggleFragment.class.getName(), R.id.flNotificationContainer);
    }
}
