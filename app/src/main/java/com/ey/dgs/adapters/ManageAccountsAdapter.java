package com.ey.dgs.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ey.dgs.R;
import com.ey.dgs.dashboard.manageAccounts.ManageAccountsFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.utils.FragmentUtils;

import java.util.ArrayList;

public class ManageAccountsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    final int TYPE_ACCOUNT = 2;
    final int TYPE_ADD_ACCOUNT = 3;
    private Fragment fragment;
    private Activity context;
    private ArrayList<Account> accounts;
    ArrayList<String> nicknames = new ArrayList<>();
    ArrayList<String> accountNumber = new ArrayList<>();

    public ManageAccountsAdapter(Fragment fragment, Activity context, ArrayList<Account> accounts) {
        this.fragment = fragment;
        this.accounts = accounts;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_ACCOUNT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_accounts_item, parent, false);
            return new ManageAccountsAdapter.ManageAccountsHolder(itemView);
        } else if (viewType == TYPE_ADD_ACCOUNT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_account_item, parent, false);
            return new ManageAccountsAdapter.ManageAccountButtonHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        final Account account = accounts.get(position);
        switch (holder1.getItemViewType()) {
            case TYPE_ACCOUNT:
                ManageAccountsHolder holder = (ManageAccountsHolder) holder1;
                holder.tvNickname.setText(account.getNickName());
                holder.tvAccountNo.setText(account.getAccountNumber());
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        account.setSelected(!account.isSelected());

                    }
                });
                break;

            case TYPE_ADD_ACCOUNT:
                ManageAccountsAdapter.ManageAccountButtonHolder buttonHolder = (ManageAccountsAdapter.ManageAccountButtonHolder) holder1;

                break;
        }
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
    public int getItemCount() {
        return this.accounts.size();
    }

    public ArrayList<Account> getAccounts() {
        ArrayList<Account> selectedAccounts = new ArrayList<>();
        for (Account account: accounts){
            if(account.isSelected()) {
                selectedAccounts.add(account);
            }
        }
        return selectedAccounts;
    }

    public class ManageAccountsHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvNickname, tvAccountNo;
        AppCompatCheckBox checkBox;

        private ManageAccountsHolder(View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNickname);
            tvAccountNo = itemView.findViewById(R.id.tvAccountNo);
            checkBox = itemView.findViewById(R.id.checkbox_accounts);
        }
    }

    public class ManageAccountButtonHolder extends RecyclerView.ViewHolder{

        AppCompatButton btnManageConsumption;
        private ManageAccountButtonHolder(View itemView) {
            super(itemView);
            btnManageConsumption = itemView.findViewById(R.id.btnManageConsumption);
            btnManageConsumption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Account> selectedAccounts = getAccounts();
                    if(!selectedAccounts.isEmpty()) {
                        for(int i=0; i< selectedAccounts.size(); i++) {
                            nicknames.add(selectedAccounts.get(i).getNickName());
                            accountNumber.add(selectedAccounts.get(i).getAccountNumber());
                        }
                        ((ManageAccountsFragment) fragment).openManageAccountsFragment(FragmentUtils.INDEX_MANAGE_ACCOUNTS_QUESTIONS,
                                nicknames, accountNumber);
                    }
                }
            });
        }
    }
}
