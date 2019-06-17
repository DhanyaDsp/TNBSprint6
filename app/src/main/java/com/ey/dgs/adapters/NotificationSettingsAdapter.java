package com.ey.dgs.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ey.dgs.R;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.NotificationSetting;
import com.ey.dgs.notifications.settings.AccountNotificationSettingsFragment;
import com.ey.dgs.utils.Utils;

import java.util.ArrayList;


public class NotificationSettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnFocusChangeListener {


    private final int TYPE_SETTINGS = 0;
    private final int TYPE_HEADER = 1;
    private ArrayList<NotificationSetting> notificationSettings;
    private AccountSettings accountSettings;
    private boolean isUpdated;

    public void setAccountSettings(AccountSettings accountSettings) {
        this.accountSettings = accountSettings;
    }

    public void setNotificationSettings(ArrayList<NotificationSetting> notificationSettings) {
        this.notificationSettings.clear();
        this.notificationSettings.addAll(notificationSettings);
        notifyDataSetChanged();
    }

    public NotificationSettingsAdapter(RecyclerView rvNotificationSettings, Fragment fragment, Activity context, ArrayList<NotificationSetting> notificationSettings) {
        this.notificationSettings = notificationSettings;
        Activity context1 = context;
        RecyclerView rvNotificationSettings1 = rvNotificationSettings;
        Fragment fragment1 = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_SETTINGS) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_setting_item, parent, false);
            return new SettingsHolder(itemView);
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
                settingsHolder.scTurn.setOnCheckedChangeListener(null);
                settingsHolder.tvName.setText(notificationSetting.getName());
                settingsHolder.scTurn.setChecked(notificationSetting.isTurnedOn());

                if (accountSettings != null) {
                    if (position == 4) {
                        settingsHolder.scTurn.setChecked(accountSettings.isServiceAvailabilityFlag());
                    } else if (position == 6) {
                        settingsHolder.scTurn.setChecked(accountSettings.isPushNotificationFlag());
                    } else if (position == 7) {
                        settingsHolder.scTurn.setChecked(accountSettings.isSmsNotificationFlag());
                    }

                }

                settingsHolder.scTurn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isUpdated) {
                            if (position == 3) {

                            } else if (position == 4) {

                            } else if (position == 6) {

                            } else if (position == 7) {

                            }
                        }
                    }
                });
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


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }


    public class SettingsHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvName;
        SwitchCompat scTurn;

        private SettingsHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            scTurn = itemView.findViewById(R.id.scTurn);
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

    public AccountSettings getAccountSettings() {
        return accountSettings;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
