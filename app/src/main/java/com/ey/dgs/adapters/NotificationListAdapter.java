package com.ey.dgs.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.model.Notification;
import com.ey.dgs.notifications.NotificationDetailPage;

import java.util.ArrayList;


public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Notification> notifications;
    private Activity context;

    public NotificationListAdapter(Activity context, ArrayList<Notification> notifications) {
        this.notifications = notifications;
        this.context = context;
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        final Notification notification = this.notifications.get(i);
        NotificationHolder notificationHolder = (NotificationHolder) holder;
        notificationHolder.tvMessage.setText(notification.getMessage());
    }

    @Override
    public int getItemCount() {
        return this.notifications.size();
    }


    public class NotificationHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvMessage, tvDetailMessage;

        private NotificationHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDetailMessage = itemView.findViewById(R.id.tvDetailMessage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NotificationDetailPage.class);
                    context.startActivity(intent);
                }
            });
        }
    }

}
