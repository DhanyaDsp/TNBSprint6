package com.ey.dgs.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;

public class DialogHelper {

    public void DialogHelper() {

    }

    public void showPrimaryAccountDialog(Account selectedAccount, Context context, View.OnClickListener listener) {
        if (context != null) {
            AppCompatDialog dialog = new AppCompatDialog(context);
            dialog.setContentView(R.layout.primary_account_popup);
            AppCompatTextView tvAccountName = dialog.getWindow().findViewById(R.id.tvAccountName);
            AppCompatButton btnGoToAccount= dialog.getWindow().findViewById(R.id.btnGoToAccount);
            btnGoToAccount.setOnClickListener(listener);
            tvAccountName.setText(selectedAccount.getNickName());
            dialog.show();
        }
    }
}
