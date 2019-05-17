package com.ey.dgs.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.notifications.settings.NotificationToggleFragment;

import java.util.ArrayList;


public class NotificationAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Account> accounts;
    private Activity context;
    private Fragment fragment;

    public NotificationAccountAdapter(Fragment fragment, Activity context, ArrayList<Account> accounts) {
        this.accounts = accounts;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_account_item, parent, false);
        return new AccountHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Account account = this.accounts.get(position);
        AccountHolder accountHolder = (AccountHolder) holder;
        accountHolder.tvAccountName.setText(account.getNickName());
        accountHolder.tvAccountID.setText(account.getAccountNumber());
    }

    @Override
    public int getItemCount() {
        return this.accounts.size();
    }

    public class AccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView tvAccountName, tvAccountID;

        private AccountHolder(View itemView) {
            super(itemView);
            tvAccountName = itemView.findViewById(R.id.tvAccountName);
            tvAccountID = itemView.findViewById(R.id.tvAccountID);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof NotificationToggleFragment) {
                        ((NotificationToggleFragment) fragment).moveToNotificationSettings(accounts.get(getAdapterPosition()));
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

}
