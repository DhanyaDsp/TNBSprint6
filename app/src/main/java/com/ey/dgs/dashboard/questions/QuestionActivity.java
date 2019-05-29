package com.ey.dgs.dashboard.questions;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.dashboard.MyDashboardFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.utils.FragmentUtils;

import static com.ey.dgs.utils.FragmentUtils.INDEX_MY_DASHBOARD_FRAGMENT;
import static com.ey.dgs.utils.FragmentUtils.INDEX_QUESTIONS_FRAGMENT;

public class QuestionActivity extends AppCompatActivity implements MyDashboardFragment.OnFragmentInteractionListener {

    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        account = (Account) getIntent().getSerializableExtra("account");
        showQuestionsFragment();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    private void showQuestionsFragment() {
        FragmentUtils.newInstance(getSupportFragmentManager())
                .replaceFragment(INDEX_MY_DASHBOARD_FRAGMENT, account, MyDashboardFragment.class.getName(), R.id.flQuestions);
    }

    @Override
    public void onFragmentInteraction(String title) {

    }
}
