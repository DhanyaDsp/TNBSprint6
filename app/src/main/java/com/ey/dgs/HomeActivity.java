package com.ey.dgs;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ey.dgs.authentication.AuthenticationActivity;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.dashboard.DashboardViewModel;
import com.ey.dgs.dashboard.MyDashboardFragment;
import com.ey.dgs.dashboard.manageAccounts.MMCManageAccountsFragment;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.dashboard.questions.MMCQuestionsFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.User;
import com.ey.dgs.notifications.NotificationListActivity;
import com.ey.dgs.notifications.pushnotifications.AzureRegistrationIntentService;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Constants;
import com.ey.dgs.utils.FragmentUtils;
import com.ey.dgs.utils.Utils;

import java.io.Serializable;

public class HomeActivity extends AppCompatActivity implements MyAccountFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener, MyDashboardFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener, View.OnClickListener {

    public static boolean IS_THRESHOLD = false;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static boolean isVisible;
    public static int INDEX_DASHBOARD = 3;
    public static boolean IS_COMING_FROM_MORE;
    private AppCompatTextView tvTitle;
    private Toolbar toolbar;
    LoginViewModel loginViewModel;
    private Account selectedAccount;
    public User user;
    AppPreferences appPreferences;
    String userName;
    private String TAG = "HomeActivity";
    private boolean isUserDetailsServiceCalled;
    private boolean isServerAccountUpdated;
    private BottomNavigationView navigation;
    private boolean dashboardShown;
    private boolean notificationRegistered;
    View loader, offlineView;
    private Fragment currentFragment;
    AppCompatButton btnRefresh;
    View sessionExpiredView;
    AppCompatButton btnBackToLogin;
    private boolean isSessionExpired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appPreferences = new AppPreferences(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        user = (User) getIntent().getSerializableExtra("user");
        userName = user.getEmail();
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setContext(this);
        initViews();
        subscribe();
        notificationsSetup();
    }

    private void notificationsSetup() {
        if (!notificationRegistered) {
            registerWithNotificationHubs(this, user.getEmail());
            notificationRegistered = true;
        }
    }

    public static void registerWithNotificationHubs(Context context, String emailID) {
        if (Utils.checkPlayServices(context)) {
            Intent intent = new Intent(context, AzureRegistrationIntentService.class);
            intent.putExtra("emailTag", emailID);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else
                context.startService(intent);
        }
    }


    private void subscribe() {
        loginViewModel.getOfflineData().observe(this, isOffline -> {
            showOfflineView(isOffline);
        });
        loginViewModel.getUserDetailFromServer(user);
        loginViewModel.getUserDetail().observe(this, user -> {
            if (user != null) {
                navigation.setSelectedItemId(R.id.navigation_dashboard);
            }
        });
        loginViewModel.getShowProgress().observe(this, showProgress -> {
            showProgress(showProgress);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                if (!isSessionExpired) {
                    moveToNotificationListPage();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    private void moveToNotificationListPage() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeFlContainer);
        Intent intent = new Intent(this, NotificationListActivity.class);
        if (currentFragment instanceof MyAccountFragment) {
            selectedAccount = ((MyAccountFragment) currentFragment).getAccount();
            if (selectedAccount != null) {
                intent.putExtra("allNotifications", false);
                intent.putExtra("account", (Serializable) selectedAccount);
                intent.putExtra("accountNumber", selectedAccount.getAccountNumber());
            }
        } else {
            intent.putExtra("allNotifications", true);
        }
        startActivity(intent);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("");

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loader = findViewById(R.id.loader);
        offlineView = findViewById(R.id.offlineView);
        btnRefresh = offlineView.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);
        sessionExpiredView = findViewById(R.id.sessionExpiredView);
        btnBackToLogin = sessionExpiredView.findViewById(R.id.btnBackToLogin);
        btnBackToLogin.setOnClickListener(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    FragmentUtils.newInstance(getSupportFragmentManager())
                            .addFragment(INDEX_DASHBOARD, null, DashboardFragment.class.getName(), null, R.id.homeFlContainer);


                    return true;
               /* case R.id.navigation_bills:
                    FragmentUtils.newInstance(getSupportFragmentManager())
                            .addFragment(INDEX_DASHBOARD, null, DashboardFragment.class.getName(), R.id.homeFlContainer);
                    return true;
                case R.id.navigation_promotions:
                    FragmentUtils.newInstance(getSupportFragmentManager())
                            .addFragment(INDEX_DASHBOARD, null, DashboardFragment.class.getName(), R.id.homeFlContainer);
                    return true;
                case R.id.navigation_feedback:
                    FragmentUtils.newInstance(getSupportFragmentManager())
                            .addFragment(INDEX_DASHBOARD, null, DashboardFragment.class.getName(), R.id.homeFlContainer);
                    return true;
                    */
                case R.id.navigation_more:
                    if (!isSessionExpired) {
                        moveToSettingsPage();
                    }
                    return true;
            }
            return false;
        }
    };

    private void moveToSettingsPage() {
        Intent intent = new Intent(HomeActivity.this, NotificationSettingsActivity.class);
        intent.putExtra("from", Constants.IS_FROM_MORE);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(String title) {
        tvTitle.setText(title);
    }

    public void onBackPressed() {
        if (!isSessionExpired) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.homeFlContainer);
                if (fragment instanceof MyAccountFragment) {
                    setToolbarTitle("");
                }
                if (fragment instanceof MMCManageAccountsFragment || fragment instanceof MMCQuestionsFragment) {
                    Utils.hideKeyBoard(this);
                }
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void setToolbarTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void onBackStackChanged() {
        currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeFlContainer);
        if (currentFragment != null) {
            if (currentFragment instanceof DashboardFragment) {
                ((DashboardFragment) currentFragment).onResume();
                getSupportActionBar().setHomeButtonEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else if (currentFragment instanceof MyDashboardFragment) {
                ((MyDashboardFragment) currentFragment).refresh();
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else if (currentFragment instanceof MMCQuestionsFragment) {
                ((MMCQuestionsFragment) currentFragment).refresh();
            } else {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.homeFlContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeFlContainer);
        if (IS_COMING_FROM_MORE) {
            navigation.setOnNavigationItemSelectedListener(null);
            navigation.setSelectedItemId(R.id.navigation_dashboard);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            IS_COMING_FROM_MORE = false;
        }
        if (currentFragment != null) {
            if (currentFragment instanceof MyDashboardFragment) {
                ((MyDashboardFragment) currentFragment).refresh();
            }
        }
    }

    public void showProgress(boolean show) {
        if (show) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }

    public void showOfflineView(boolean show) {
        if (show) {
            offlineView.setVisibility(View.VISIBLE);
        } else {
            offlineView.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                refreshPage();
                break;
            case R.id.btnBackToLogin:
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {
        finish();
        moveToLoginPage();
    }

    private void moveToLoginPage() {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
    }

    private void refreshPage() {
        loginViewModel.setOfflineData(false);
        loginViewModel.setShowProgress(true);
        loginViewModel.getUserDetailFromServer(user);
    }
}
