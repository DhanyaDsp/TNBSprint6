package com.ey.dgs.notifications;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ey.dgs.R;
import com.ey.dgs.adapters.NotificationListAdapter;
import com.ey.dgs.model.Notification;
import com.ey.dgs.notifications.settings.SettingsMenuFragment;
import com.ey.dgs.utils.AppPreferences;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationListActivity extends AppCompatActivity implements SettingsMenuFragment.OnFragmentInteractionListener {
    RecyclerView rvNotifications;
    NotificationListAdapter notificationListAdapter;
    ArrayList<Notification> notifications = new ArrayList<>();
    private NotificationViewModel notificationViewModel;
    int userId, accountId;
    AppPreferences appPreferences;
    boolean allNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        appPreferences = new AppPreferences(this);
        userId = appPreferences.getUser_id();
        allNotifications = getIntent().getBooleanExtra("allNotifications", false);
        if (!allNotifications) {
            accountId = getIntent().getIntExtra("accountId", -1);
        } else {
            accountId = -1;
        }
        initViews();
        subscribe();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void subscribe() {
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        notificationViewModel.loadNotificationsFromLocalDB(userId, accountId);
        notificationViewModel.getNotifications().observeForever(notifications -> {
            this.notifications.clear();
            this.notifications.addAll(notifications);
            Collections.reverse(this.notifications);
            notificationListAdapter.notifyDataSetChanged();
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.notifications));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setHasFixedSize(true);
        LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
        rvNotifications.setLayoutManager(rvLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvNotifications.addItemDecoration(itemDecorator);
        rvNotifications.setItemAnimator(new DefaultItemAnimator());
        notificationListAdapter = new NotificationListAdapter(this, notifications);
        rvNotifications.setAdapter(notificationListAdapter);
    }

    @Override
    public void onFragmentInteraction(String title) {

    }
}
