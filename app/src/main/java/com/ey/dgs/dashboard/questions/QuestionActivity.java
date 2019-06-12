package com.ey.dgs.dashboard.questions;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ey.dgs.R;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.MyDashboardFragment;
import com.ey.dgs.dashboard.myaccount.AccountSettingsViewModel;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.FragmentUtils;

import static com.ey.dgs.utils.FragmentUtils.INDEX_QUESTIONS_FRAGMENT;

public class QuestionActivity extends AppCompatActivity implements MyDashboardFragment.OnFragmentInteractionListener {

    Account account;
    private AccountSettingsViewModel accountSettingsViewModel;
    private LoginViewModel loginViewModel;
    AppPreferences appPreferences;
    private User user;
    private BillingHistory billingHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        billingHistory = (BillingHistory) getIntent().getSerializableExtra("billingHistory");
        account = billingHistory.getAccount();
        appPreferences = new AppPreferences(this);
        showQuestionsFragment();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onActionBarBackBtnPressed();
                return true;
        }
        return false;
    }

    private void showQuestionsFragment() {
        FragmentUtils.newInstance(getSupportFragmentManager())
                .addFragment(INDEX_QUESTIONS_FRAGMENT, billingHistory, MMCQuestionsFragment.class.getName(), R.id.flQuestions);
    }

    @Override
    public void onFragmentInteraction(String title) {

    }

    public void onActionBarBackBtnPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flQuestions);
        if (currentFragment instanceof MMCQuestionsFragment) {
            finish();
        } /*else if (currentFragment instanceof NotificationToggleFragment) {
            super.onBackPressed();
            setActionBarTitle("More");
        } else if (currentFragment instanceof AccountNotificationSettingsFragment) {
            ((AccountNotificationSettingsFragment) currentFragment).updateAccountDetails();
            setActionBarTitle("Notifications");
        }*/
    }
}
