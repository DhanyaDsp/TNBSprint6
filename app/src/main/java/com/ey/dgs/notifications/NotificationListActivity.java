package com.ey.dgs.notifications;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ey.dgs.R;
import com.ey.dgs.adapters.NotificationListAdapter;
import com.ey.dgs.model.Notification;
import com.ey.dgs.notifications.settings.SettingsMenuFragment;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.DialogHelper;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationListActivity extends AppCompatActivity implements SettingsMenuFragment.OnFragmentInteractionListener {
    RecyclerView rvNotifications;
    NotificationListAdapter notificationListAdapter;
    ArrayList<Notification> notifications = new ArrayList<>();
    private NotificationViewModel notificationViewModel;
    int userId;
    AppPreferences appPreferences;
    boolean allNotifications;
    String accountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        appPreferences = new AppPreferences(this);
        userId = appPreferences.getUser_id();
        allNotifications = getIntent().getBooleanExtra("allNotifications", false);
        if (!allNotifications) {
            accountNumber = getIntent().getStringExtra("accountNumber");
        } else {
            accountNumber = "";
        }
        initViews();
        subscribe();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_delete:
                deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void subscribe() {
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        notificationViewModel.loadNotificationsFromLocalDB(userId, accountNumber);
        notificationViewModel.getNotifications().observe(this, notifications -> {
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

    private void deleteAll() {
        DialogHelper.showDeleteAll(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.hidePopup();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.hidePopup();
                if (!notifications.isEmpty()) {
                    notificationViewModel.deleteNotificationsFromLocalDB();
                    notificationListAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No notifications to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
