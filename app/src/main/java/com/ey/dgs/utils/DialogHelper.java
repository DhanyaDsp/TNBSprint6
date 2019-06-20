package com.ey.dgs.utils;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;


public class DialogHelper {

    private static AppCompatDialog dialog;
    private static AppCompatDialog dialog2;

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
            tvAccountName.setText("RM " + threshold);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public static void showDeleteAll(Context context, View.OnClickListener cancelListener, View.OnClickListener deleteListener) {
        if (context != null && dialog == null) {
            dialog = new AppCompatDialog(context);
            dialog.setContentView(R.layout.delete_all_popup);
            AppCompatButton btnCancel = dialog.getWindow().findViewById(R.id.btnCancel);
            AppCompatButton btnDeleteAll = dialog.getWindow().findViewById(R.id.btnDeleteAll);
            btnCancel.setOnClickListener(cancelListener);
            btnDeleteAll.setOnClickListener(deleteListener);
            dialog.setCancelable(false);
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public static void showUserAlert(Context context, String title, String names, View.OnClickListener listener) {
        if ((context != null && dialog == null)){
            dialog = new AppCompatDialog(context);
            dialog.setContentView(R.layout.popup_alert);
            AppCompatTextView tvTitle = dialog.getWindow().findViewById(R.id.popup_title);
            AppCompatTextView message = dialog.getWindow().findViewById(R.id.popup_message);
            Button btnClose = dialog.getWindow().findViewById(R.id.btn_close);
            btnClose.setOnClickListener(listener);
            tvTitle.setText(title);
            if (title == dialog.getContext().getString(R.string.service_restoration)) {
                message.setText(dialog.getContext().getString(R.string.service_restoration_msg, names));
            } else if (title == dialog.getContext().getString(R.string.service_disruption)) {
                message.setText(dialog.getContext().getString(R.string.service_disruption_msg, names));
            }
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.TOP;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            dialog.setCancelable(false);
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public static void showUserAlert2(Context context, String title, String names, View.OnClickListener listener) {
        if ((context != null && dialog2 == null)){
            dialog2 = new AppCompatDialog(context);
            dialog2.setContentView(R.layout.popup_alert);
            AppCompatTextView tvTitle = dialog2.getWindow().findViewById(R.id.popup_title);
            AppCompatTextView message = dialog2.getWindow().findViewById(R.id.popup_message);
            Button btnClose = dialog2.getWindow().findViewById(R.id.btn_close);
            btnClose.setOnClickListener(listener);
            tvTitle.setText(title);
            message.setText(dialog2.getContext().getString(R.string.service_disruption_msg, names));
            Window window = dialog2.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.TOP;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            dialog2.setCancelable(false);
            if (!dialog2.isShowing()) {
                dialog2.show();
            }
        }
    }

    public static void hidePopup() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void hidePopup2() {
        if (dialog2 != null) {
            dialog2.dismiss();
            dialog2 = null;
        }
    }
}
