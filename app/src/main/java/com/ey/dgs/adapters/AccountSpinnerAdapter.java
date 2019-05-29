package com.ey.dgs.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;

import java.util.ArrayList;

public class AccountSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    ArrayList<Account> accounts;
    Context context;

    public AccountSpinnerAdapter(Context context, ArrayList<Account> accounts) {
        this.accounts = accounts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Account getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.spinner_account_item, null);
        TextView textView = (TextView) view.findViewById(R.id.main);
        textView.setText(accounts.get(position).getNickName());
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view;
        view = View.inflate(context, R.layout.spinner_account_item, null);
        final TextView textView = (TextView) view.findViewById(R.id.main);
        textView.setText(accounts.get(position).getNickName());
        return view;
    }
}
