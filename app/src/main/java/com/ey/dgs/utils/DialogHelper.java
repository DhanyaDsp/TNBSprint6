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

    private static AppCompatDialog dialog;

    public void DialogHelper() {

    }

    public static void showSuccessDialog(Account selectedAccount, String threshold, Context context, View.OnClickListener listener) {
        if (context != null) {
            dialog = new AppCompatDialog(context);
            dialog.setContentView(R.layout.primary_account_popup);
            AppCompatTextView tvAccountName = dialog.getWindow().findViewById(R.id.tvAccountName);
            AppCompatButton btnGoToAccount = dialog.getWindow().findViewById(R.id.btnGoToAccount);
            AppCompatTextView tvMessage = dialog.getWindow().findViewById(R.id.successMessage);
            btnGoToAccount.setOnClickListener(listener);
            tvMessage.setText(dialog.getContext().getString(R.string.success_manage_consumption, selectedAccount.getNickName()));
            tvAccountName.setText("RM "+threshold );
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public static void showDeleteAll(Context context, View.OnClickListener cancelListener, View.OnClickListener deleteListener) {
        if (context != null) {
            dialog = new AppCompatDialog(context);
            dialog.setContentView(R.layout.delete_all_popup);
            AppCompatButton btnCancel= dialog.getWindow().findViewById(R.id.btnCancel);
            AppCompatButton btnDeleteAll = dialog.getWindow().findViewById(R.id.btnDeleteAll);
            btnCancel.setOnClickListener(cancelListener);
            btnDeleteAll.setOnClickListener(deleteListener);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public static void showUserAlert(Context context, View.OnClickListener cancelListener, View.OnClickListener deleteListener) {
        if (context != null) {
            dialog = new AppCompatDialog(context);
            dialog.setContentView(R.layout.delete_all_popup);
            AppCompatButton btnCancel= dialog.getWindow().findViewById(R.id.btnCancel);
            AppCompatButton btnDeleteAll = dialog.getWindow().findViewById(R.id.btnDeleteAll);
            btnCancel.setOnClickListener(cancelListener);
            btnDeleteAll.setOnClickListener(deleteListener);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public static void hidePopup() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
