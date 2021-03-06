package com.ey.dgs.notifications.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.authentication.AuthenticationActivity;
import com.ey.dgs.dashboard.myaccount.AccountSettingsViewModel;
import com.ey.dgs.model.Account;
import com.ey.dgs.utils.Constants;
import com.ey.dgs.utils.FragmentUtils;

public class NotificationSettingsActivity extends AppCompatActivity implements SettingsMenuFragment.OnFragmentInteractionListener, NotificationToggleFragment.OnFragmentInteractionListener, AccountNotificationSettingsFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener, View.OnClickListener {

    int from;
    Account account;
    AccountSettingsViewModel accountSettingsViewModel;
    private View loader, sessionExpiredView;
    private boolean isSessionExpired;
    AppCompatButton btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification_settings);
        from = getIntent().getIntExtra("from", 0);
        if (from == Constants.IS_FROM_ACCOUNT) {
            account = (Account) getIntent().getSerializableExtra("account");
        }
        accountSettingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        initViews();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        showSettingsFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onActionBarBackBtnPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingsFragment() {
        if (from == Constants.IS_FROM_ACCOUNT) {
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    replaceFragment(FragmentUtils.INDEX_NOTIFICATION_SETTINGS_FRAGMENT, account, AccountNotificationSettingsFragment.class.getName(), R.id.flNotificationContainer);
        } else if (from == Constants.IS_FROM_DASHBOARD) {
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    replaceFragment(FragmentUtils.INDEX_NOTIFICATION_TOGGLE_FRAGMENT, account, NotificationToggleFragment.class.getName(), R.id.flNotificationContainer);
        } else if (from == Constants.IS_FROM_MORE) {
            FragmentUtils.newInstance(getSupportFragmentManager()).
                    addFragment(FragmentUtils.INDEX_SETTINGS_FRAGMENT, account, SettingsMenuFragment.class.getName(), null, R.id.flNotificationContainer);
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("More");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loader = findViewById(R.id.loader);
        sessionExpiredView = findViewById(R.id.sessionExpiredView);
        btnBackToLogin = sessionExpiredView.findViewById(R.id.btnBackToLogin);
        btnBackToLogin.setOnClickListener(this);
    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void moveToNotificationSettings(Account account) {
        FragmentUtils.newInstance(getSupportFragmentManager())
                .addFragment(FragmentUtils.INDEX_NOTIFICATION_SETTINGS_FRAGMENT, account, "AccountNotificationSettingsFragment", null, R.id.flNotificationContainer);
    }

    public void onActionBarBackBtnPressed() {
        if (!isSessionExpired) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flNotificationContainer);
            if (currentFragment instanceof SettingsMenuFragment) {
                finish();
            } else if (currentFragment instanceof NotificationToggleFragment) {
                onBackPressed();
                setActionBarTitle("More");
            } else if (currentFragment instanceof AccountNotificationSettingsFragment) {
                ((AccountNotificationSettingsFragment) currentFragment).updateAccountDetails();
                setActionBarTitle("Notifications");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!isSessionExpired) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flNotificationContainer);
            if (currentFragment != null) {
                if (currentFragment instanceof SettingsMenuFragment) {
                    HomeActivity.IS_COMING_FROM_MORE = true;
                    finish();
                } else if (currentFragment instanceof NotificationToggleFragment) {
                    super.onBackPressed();
                    setActionBarTitle("More");
                } else if (currentFragment instanceof AccountNotificationSettingsFragment) {
                    super.onBackPressed();
                    setActionBarTitle("Notifications");
                }
            }
        }
    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void moveToNotificationTogglePage() {
        FragmentUtils.newInstance(getSupportFragmentManager()).addFragment(FragmentUtils.INDEX_NOTIFICATION_TOGGLE_FRAGMENT, null, NotificationToggleFragment.class.getName(), null, R.id.flNotificationContainer);
    }

    public void showProgress(boolean show) {
        if (show) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }

    public void showSessionExpiredView(boolean show) {
        isSessionExpired = show;
        if (show) {
            sessionExpiredView.setVisibility(View.VISIBLE);
        } else {
            sessionExpiredView.setVisibility(View.GONE);
        }
    }

    private void logout() {
        finish();
        moveToLoginPage();
    }

    private void moveToLoginPage() {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onBackStackChanged() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flNotificationContainer);
        if (currentFragment instanceof NotificationToggleFragment) {
            ((NotificationToggleFragment) currentFragment).onRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackToLogin:
                logout();
                break;
            default:
                break;
        }
    }
}
