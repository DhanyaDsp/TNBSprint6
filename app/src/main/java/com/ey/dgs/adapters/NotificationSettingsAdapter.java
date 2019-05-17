package com.ey.dgs.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ey.dgs.R;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.NotificationSetting;
import com.ey.dgs.notifications.settings.AccountNotificationSettingsFragment;

import java.util.ArrayList;


public class NotificationSettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    final int TYPE_SETTINGS = 0;
    final int TYPE_SET_THRESHOLD = 1;
    final int TYPE_HEADER = 2;
    private final RecyclerView rvNotificationSettings;
    public ArrayList<NotificationSetting> notificationSettings;
    private Activity context;
    private Fragment fragment;
    NotificationSetting averageThresholdItem, userThresholdItem;
    AccountSettings accountSettings;
    private boolean isExpanded;

    public void setAccountSettings(AccountSettings accountSettings) {
        this.accountSettings = accountSettings;
    }

    public NotificationSettingsAdapter(RecyclerView rvNotificationSettings, Fragment fragment, Activity context, ArrayList<NotificationSetting> notificationSettings) {
        this.notificationSettings = notificationSettings;
        this.context = context;
        this.rvNotificationSettings = rvNotificationSettings;
        this.fragment = fragment;
        averageThresholdItem = new NotificationSetting("My average consumption(RM)", true, false, true);
        userThresholdItem = new NotificationSetting("Notify me when my consumption reaches(RM)", true, false, true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_SETTINGS) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_setting_item, parent, false);
            return new SettingsHolder(itemView);
        } else if (viewType == TYPE_SET_THRESHOLD) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_setting_threshold_item, parent, false);
            return new ThresholdHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_setting_header_tem, parent, false);
            return new HeaderHolder(itemView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        NotificationSetting notificationSetting = this.notificationSettings.get(position);
        if (notificationSetting.isHeader()) {
            return TYPE_HEADER;
        } else if (notificationSetting.isThreshold()) {
            return TYPE_SET_THRESHOLD;
        } else {
            return TYPE_SETTINGS;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NotificationSetting notificationSetting = this.notificationSettings.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_SETTINGS:
                SettingsHolder settingsHolder = (SettingsHolder) holder;
                settingsHolder.tvName.setText(notificationSetting.getName());
                settingsHolder.scTurn.setChecked(notificationSetting.isTurnedOn());

                if (accountSettings != null) {
                    settingsHolder.scTurn.setOnCheckedChangeListener(null);
                    if (isExpanded) {
                        if (position == 6) {
                            settingsHolder.scTurn.setChecked(accountSettings.isServiceAvailability());
                        } else if (position == 9) {
                            settingsHolder.scTurn.setChecked(accountSettings.isPushNotificationFlag());
                        } else if (position == 10) {
                            settingsHolder.scTurn.setChecked(accountSettings.isSmsNotificationFlag());
                        }
                    } else {
                        if (position == 4) {
                            settingsHolder.scTurn.setChecked(accountSettings.isServiceAvailability());
                        } else if (position == 7) {
                            settingsHolder.scTurn.setChecked(accountSettings.isPushNotificationFlag());
                        } else if (position == 8) {
                            settingsHolder.scTurn.setChecked(accountSettings.isSmsNotificationFlag());
                        }
                    }
                }
                settingsHolder.scTurn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (position == 3) {
                            if (isChecked) {
                                isExpanded = true;
                                notificationSettings.add(4, averageThresholdItem);
                                notificationSettings.add(5, userThresholdItem);
                                notifyItemInserted(4);
                                notifyItemInserted(4);
                            } else {
                                isExpanded = false;
                                notificationSettings.remove(averageThresholdItem);
                                notificationSettings.remove(userThresholdItem);
                                notifyItemRemoved(4);
                                notifyItemRemoved(5);
                            }
                            notificationSetting.setTurnedOn(isChecked);

                        }
                    }
                });
                break;

            case TYPE_SET_THRESHOLD:
                ThresholdHolder thresholdHolder = (ThresholdHolder) holder;
                thresholdHolder.tvName.setText(notificationSetting.getName());
                if (position == 4) {
                    thresholdHolder.etThreshold.setFocusable(false);
                    thresholdHolder.etThreshold.setBackground(null);
                }
                thresholdHolder.etThreshold.setText("45");
                break;

            case TYPE_HEADER:
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.tvHeader.setText(notificationSetting.getName());
                break;

        }
    }

    @Override
    public int getItemCount() {
        return this.notificationSettings.size();
    }


    public class SettingsHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvName;
        SwitchCompat scTurn;

        private SettingsHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            scTurn = itemView.findViewById(R.id.scTurn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvHeader;

        private HeaderHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
        }

    }

    public class ThresholdHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvName;
        AppCompatEditText etThreshold;

        private ThresholdHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            etThreshold = itemView.findViewById(R.id.etThreshold);
        }
    }

}
