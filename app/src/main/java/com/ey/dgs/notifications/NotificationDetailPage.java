package com.ey.dgs.notifications;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ey.dgs.R;
import com.ey.dgs.model.Notification;

import java.io.Serializable;

public class NotificationDetailPage extends AppCompatActivity {

    private Notification notification;
    AppCompatTextView tvEnergyTips, tvHeader, tvMessage;
    LinearLayout llEnergyTips;
    AppCompatImageView ivBanner;
    NotificationViewModel notificationViewModel;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail_page);
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        if (getIntent().getSerializableExtra("notification") != null) {
            notification = (Notification) getIntent().getSerializableExtra("notification");
            position = getIntent().getIntExtra("position", -1);
            if (notification != null) {
                initViews();
                setData();
                if (!notification.isRead()) {
                    notification.setRead(true);
                    notificationViewModel.updateNotificationInLocalDB(notification);
                }
            }
        }
    }

    private void setData() {
        tvHeader.setText(notification.getHeader());
        tvMessage.setText(notification.getMessage());
        if (!TextUtils.isEmpty(notification.getEnergyTip())) {
            llEnergyTips.setVisibility(View.VISIBLE);
            tvEnergyTips.setText(notification.getEnergyTip());
        }
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        notification = (Notification) getIntent().getSerializableExtra("notification");
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (notification.getNotificationType().equalsIgnoreCase(Notification.MMC) ||
                notification.getNotificationType().equalsIgnoreCase(Notification.ADVANCED)) {
            getSupportActionBar().setTitle("Consumption");
        } else {
            getSupportActionBar().setTitle("Service Availability");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvEnergyTips = findViewById(R.id.tvEnergyTips);
        tvHeader = findViewById(R.id.tvHeader);
        tvMessage = findViewById(R.id.tvMessage);
        llEnergyTips = findViewById(R.id.llEnergyTips);
        ivBanner = findViewById(R.id.ivBanner);
        if (notification.getNotificationType().equalsIgnoreCase(Notification.MMC)) {
            ivBanner.setImageResource(R.drawable.notification_detail_bg);
        } else {
            ivBanner.setImageResource(R.drawable.service_banner);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("notification", (Serializable) notification);
        intent.putExtra("position", position);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}
