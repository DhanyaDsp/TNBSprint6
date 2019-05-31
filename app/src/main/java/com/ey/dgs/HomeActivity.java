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
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.dashboard.DashboardViewModel;
import com.ey.dgs.dashboard.MyDashboardFragment;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.User;
import com.ey.dgs.notifications.NotificationListActivity;
import com.ey.dgs.notifications.pushnotifications.AzureRegistrationIntentService;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.FragmentUtils;
import com.ey.dgs.utils.Utils;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements MyAccountFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener, MyDashboardFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener {

    public static boolean IS_THRESHOLD = false;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static boolean isVisible;
    private int INDEX_DASHBOARD = 3;
    private AppCompatTextView tvTitle;
    private Toolbar toolbar;
    DashboardViewModel dashboardViewModel;
    LoginViewModel loginViewModel;
    private Account selectedAccount;
    public User user;
    AppPreferences appPreferences;
    String userName;
    private String TAG = "HomeActivity";
    private boolean isUserDetailsServiceCalled;
    public static boolean isQuestionsShown;
    private boolean isServerAccountUpdated;
    private BottomNavigationView navigation;
    private boolean dashboardShown;
    private boolean notificationRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appPreferences = new AppPreferences(this);
        userName = getIntent().getStringExtra("UserName");
        user = new User();
        user.setEmail(userName);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.setContext(this);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setContext(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        subscribe();
        initViews();
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
            // Start IntentService to register this application with FCM.
            Intent intent = new Intent(context, AzureRegistrationIntentService.class);
            intent.putExtra("emailTag", emailID);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else
                context.startService(intent);
        }
    }


    private void subscribe() {
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        loginViewModel.getUserDetail().observe(this, user -> {
            if (user != null) {
                this.user = user;
                notificationsSetup();
                if (!isUserDetailsServiceCalled) {
                    dashboardViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
                    loginViewModel.getUserDetailFromServer(this.user);
                    isUserDetailsServiceCalled = true;
                } else {
                    dashboardViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
                    dashboardViewModel.getAccounts().observeForever(accounts -> {
                        if (!isServerAccountUpdated) {
                            if (accounts == null || accounts.size() <= 0) {
                                if (this.user.getAccountDetails() != null && this.user.getAccountDetails().length > 0) {
                                    dashboardViewModel.addAccountsToLocalDB(Arrays.asList(this.user.getAccountDetails()));
                                }
                            } else {
                                for (Account account : accounts) {
                                    if (account.isPrimaryAccount()) {
                                        selectedAccount = account;
                                        /*if (!user.isPrimaryAccountSet()) {
                                            user.setPrimaryAccountSet(true);
                                            loginViewModel.update(user);
                                        }*/
                                    }
                                }
                                if (!dashboardShown) {
                                    navigation.setSelectedItemId(R.id.navigation_dashboard);
                                    dashboardShown = true;
                                }
                                //Data from server
                                /*if (this.user.getAccountDetails() != null && this.user.getAccountDetails().length > 0) {
                                    dashboardViewModel.addAccountsToLocalDB(Arrays.asList(this.user.getAccountDetails()));
                                    isServerAccountUpdated = true;
                                }*/
                            }
                        }
                    });
                }

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                moveToNotificationListPage();
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
        if (!user.isPrimaryAccountSet()) {
            intent.putExtra("allNotifications", true);
        } else {
            if (selectedAccount != null) {
                intent.putExtra("allNotifications", false);
                intent.putExtra("accountNumber", selectedAccount.getAccountNumber());
            }
        }
        startActivity(intent);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvTitle.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    FragmentUtils.newInstance(getSupportFragmentManager())
                            .addFragment(INDEX_DASHBOARD, null, DashboardFragment.class.getName(), R.id.homeFlContainer);

                  /*  if (!user.isPrimaryAccountSet()) {
                        FragmentUtils.newInstance(getSupportFragmentManager())
                                .addFragment(INDEX_DASHBOARD, null, DashboardFragment.class.getName(), R.id.homeFlContainer);
                    } else {
                        dashboardViewModel.getPrimaryAccountFromLocalDB();
                        dashboardViewModel.getPrimaryAccount().observe(HomeActivity.this, account -> {
                            selectedAccount = account;
                            if (selectedAccount != null) {
                                FragmentUtils.newInstance(getSupportFragmentManager())
                                        .replaceFragment(FragmentUtils.INDEX_MY_DASHBOARD_FRAGMENT, selectedAccount, MyDashboardFragment.class.getName(), R.id.homeFlContainer);
                            }
                        });
                    }*/
                    return true;
                case R.id.navigation_bills:
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
                case R.id.navigation_more:
                    moveToSettingsPage();
                    return true;
            }
            return false;
        }
    };

    private void moveToSettingsPage() {
        Intent intent = new Intent(HomeActivity.this, NotificationSettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(String title) {
        tvTitle.setText(title);
    }

    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.homeFlContainer);
            if (fragment instanceof MyAccountFragment) {
                setToolbarTitle("");
            }
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void setToolbarTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void onBackStackChanged() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeFlContainer);
        if (currentFragment != null) {
            if (currentFragment instanceof DashboardFragment) {
                ((DashboardFragment) currentFragment).onResume();
            }
        }
    }
}
