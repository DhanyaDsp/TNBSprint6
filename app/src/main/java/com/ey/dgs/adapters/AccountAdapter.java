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

import java.util.ArrayList;


public class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    final int TYPE_ACCOUNT = 0;
    final int TYPE_ADD_ACCOUNT = 1;
    private ArrayList<Account> accounts;
    private Activity context;
    private Fragment fragment;

    public AccountAdapter(Fragment fragment, Activity context, ArrayList<Account> accounts) {
        this.accounts = accounts;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_ACCOUNT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
            return new AccountHolder(itemView);
        } else if (viewType == TYPE_ADD_ACCOUNT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_account_item, parent, false);
            return new AddAccountHolder(itemView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Account account = this.accounts.get(position);
        if (account.isAccount()) {
            return TYPE_ADD_ACCOUNT;
        } else {
            return TYPE_ACCOUNT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Account account = this.accounts.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_ACCOUNT:
                AccountHolder accountHolder = (AccountHolder) holder;
                accountHolder.tvAcountID.setText(account.getAccountNumber());
                accountHolder.tvAccountName.setText(account.getNickName());
                accountHolder.tvLastBilledAmount.setText(account.getLastBilledAmount());
                accountHolder.tvDate.setText(account.getLastBilledDate());
                break;

            case TYPE_ADD_ACCOUNT:
                AddAccountHolder chartHolder = (AddAccountHolder) holder;

                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.accounts.size();
    }


    public class AccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView tvAcountID, tvAccountName, tvLastBilledAmount, tvDate;

        private AccountHolder(View itemView) {
            super(itemView);
            tvAcountID = itemView.findViewById(R.id.tvAccountID);
            tvAccountName = itemView.findViewById(R.id.tvAccountName);
            tvLastBilledAmount = itemView.findViewById(R.id.tvLastBilledAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashboardFragment) fragment).openFragment(4, accounts.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

    public class AddAccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AddAccountHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
