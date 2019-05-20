package com.ey.dgs.notifications;

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

public class NotificationDetailPage extends AppCompatActivity {

    private Notification notification;
    AppCompatTextView tvEnergyTips, tvHeader, tvMessage;
    LinearLayout llEnergyTips;
    AppCompatImageView ivBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail_page);
        notification = (Notification) getIntent().getSerializableExtra("notification");
        initViews();
        setData();
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
        getSupportActionBar().setTitle("Consumption");
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

}
