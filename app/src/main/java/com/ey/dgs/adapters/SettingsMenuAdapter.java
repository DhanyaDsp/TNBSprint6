package com.ey.dgs.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.model.SettingMenuItem;
import com.ey.dgs.notifications.settings.SettingsMenuFragment;

import java.util.ArrayList;


public class SettingsMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SettingMenuItem> settingMenuItems;
    private Activity context;
    Fragment fragment;

    public SettingsMenuAdapter(Activity context, Fragment fragment, ArrayList<SettingMenuItem> settingMenuItems) {
        this.settingMenuItems = settingMenuItems;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public SettingsMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_menu_item, parent, false);
        return new SettingsMenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        final SettingMenuItem settingMenuItem = this.settingMenuItems.get(i);
        SettingsMenuViewHolder settingsMenuViewHolder = (SettingsMenuViewHolder) holder;
        settingsMenuViewHolder.tvSettingName.setText(settingMenuItem.getName());
    }

    @Override
    public int getItemCount() {
        return this.settingMenuItems.size();
    }


    public class SettingsMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView tvSettingName;

        private SettingsMenuViewHolder(View itemView) {
            super(itemView);
            tvSettingName = itemView.findViewById(R.id.tvSettingMenuName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof SettingsMenuFragment) {
                        if (getAdapterPosition() == 1) {
                            ((SettingsMenuFragment) fragment).moveToNotificationTogglePage(settingMenuItems.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

}
