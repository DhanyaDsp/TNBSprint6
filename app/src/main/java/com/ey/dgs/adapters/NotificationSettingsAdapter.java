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


    final int TYPE_SETTINGS = 0;
    final int TYPE_SET_THRESHOLD = 1;
    final int TYPE_HEADER = 2;
    private final RecyclerView rvNotificationSettings;
    public ArrayList<NotificationSetting> notificationSettings;
    private Activity context;
    private Fragment fragment;
    private NotificationSetting averageThresholdItem, userThresholdItem;
    AccountSettings accountSettings;
    EnergyConsumptions energyConsumptions;
    private boolean isExpanded;
    public boolean isThresholdChanged;
    public boolean isUpdated;

    public void setAccountSettings(AccountSettings accountSettings) {
        this.accountSettings = accountSettings;
    }

    public void setNotificationSettings(ArrayList<NotificationSetting> notificationSettings) {
        this.notificationSettings.clear();
        this.notificationSettings.addAll(notificationSettings);
        notifyDataSetChanged();
    }

    public void setEnergyConsumptions(EnergyConsumptions energyConsumptions) {
        this.energyConsumptions = energyConsumptions;
        if (energyConsumptions.getEnergyConsumptionFlag()) {
            this.notificationSettings.add(4, averageThresholdItem);
            this.notificationSettings.add(5, userThresholdItem);
            isExpanded = true;
            notifyDataSetChanged();
        }
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
                settingsHolder.scTurn.setOnCheckedChangeListener(null);
                settingsHolder.tvName.setText(notificationSetting.getName());
                settingsHolder.scTurn.setChecked(notificationSetting.isTurnedOn());

                if (accountSettings != null) {
                    if (isExpanded) {
                        if (position == 6) {
                            settingsHolder.scTurn.setChecked(accountSettings.isServiceAvailabilityFlag());
                        } else if (position == 9) {
                            settingsHolder.scTurn.setChecked(accountSettings.isPushNotificationFlag());
                        } else if (position == 10) {
                            settingsHolder.scTurn.setChecked(accountSettings.isSmsNotificationFlag());
                        }
                    } else {
                        if (position == 4) {
                            settingsHolder.scTurn.setChecked(accountSettings.isServiceAvailabilityFlag());
                        } else if (position == 7) {
                            settingsHolder.scTurn.setChecked(accountSettings.isPushNotificationFlag());
                        } else if (position == 8) {
                            settingsHolder.scTurn.setChecked(accountSettings.isSmsNotificationFlag());
                        }
                    }
                }

                if (energyConsumptions != null) {
                    if (isExpanded) {
                        if (position == 3) {
                            settingsHolder.scTurn.setChecked(energyConsumptions.getEnergyConsumptionFlag());
                        }
                    } else {
                        if (position == 3) {
                            settingsHolder.scTurn.setChecked(energyConsumptions.getEnergyConsumptionFlag());
                        }
                    }
                }

                settingsHolder.scTurn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isUpdated) {
                            if (position == 3) {
                                if (isChecked) {
                                    isExpanded = true;
                                    notificationSettings.add(4, averageThresholdItem);
                                    notificationSettings.add(5, userThresholdItem);
                                    notifyItemInserted(4);
                                    notifyItemInserted(5);
                                } else {
                                    isExpanded = false;
                                    notificationSettings.remove(averageThresholdItem);
                                    notificationSettings.remove(userThresholdItem);
                                    notifyItemRemoved(4);
                                    notifyItemRemoved(5);
                                }
                                if (energyConsumptions != null) {
                                    energyConsumptions.setEnergyConsumptionFlag(isChecked);
                                }
                            } else if (position == 4) {
                                if (!isExpanded) {
                                    if (accountSettings != null) {
                                        accountSettings.setServiceAvailabilityFlag(isChecked);
                                    }
                                }
                            } else if (position == 6) {
                                if (isExpanded) {
                                    if (accountSettings != null) {
                                        accountSettings.setServiceAvailabilityFlag(isChecked);
                                    }
                                }
                            } else if (position == 7) {
                                if (!isExpanded) {
                                    if (accountSettings != null) {
                                        accountSettings.setPushNotificationFlag(isChecked);
                                    }
                                }
                            } else if (position == 8) {
                                if (!isExpanded) {
                                    if (accountSettings != null) {
                                        accountSettings.setSmsNotificationFlag(isChecked);
                                    }
                                }
                            } else if (position == 9) {
                                if (isExpanded) {
                                    if (accountSettings != null) {
                                        accountSettings.setPushNotificationFlag(isChecked);
                                    }
                                }
                            } else if (position == 10) {
                                if (isExpanded) {
                                    if (accountSettings != null) {
                                        accountSettings.setSmsNotificationFlag(isChecked);
                                    }
                                }
                            }
                        }
                    }
                });
                break;

            case TYPE_SET_THRESHOLD:
                ThresholdHolder thresholdHolder = (ThresholdHolder) holder;
                thresholdHolder.tvName.setText(notificationSetting.getName());
                thresholdHolder.etThreshold.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    Utils.hideKeyBoard(context);
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });
                if (position == 4) {
                    thresholdHolder.etThreshold.setFocusable(false);
                    thresholdHolder.etThreshold.setBackground(null);
                }
                if (energyConsumptions != null) {
                    if (isExpanded) {
                        if (position == 4) {
                            thresholdHolder.etThreshold.setText(energyConsumptions.getAverageConsumption());
                        } else if (position == 5) {
                            if (((AccountNotificationSettingsFragment) fragment).isThresholdOrSubscribe()) {
                                thresholdHolder.etThreshold.requestFocus();
                                Utils.showKeyBoard(context);
                            }
                            thresholdHolder.etThreshold.setText(energyConsumptions.getUserThreshold());
                            thresholdHolder.etThreshold.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    String threshold = s.toString().trim();
                                    if (!TextUtils.isEmpty(threshold) && Integer.parseInt(threshold) > 0) {
                                        isThresholdChanged = true;
                                        energyConsumptions.setUserThreshold(s.toString());
                                    } else {
                                        Utils.showToast(context, "Please Enter a Threshold Value Great than Zero and Non Empty");
                                    }
                                }
                            });
                        }
                    } else {

                    }
                }
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

    public EnergyConsumptions getEnergyConsumptions() {
        return energyConsumptions;
    }

    public boolean isThresholdChanged() {
        return isThresholdChanged;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
