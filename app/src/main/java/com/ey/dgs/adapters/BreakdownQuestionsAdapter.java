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
import com.ey.dgs.model.BreakQuestionItem;
import com.ey.dgs.model.Notification;
import com.ey.dgs.notifications.NotificationDetailPage;
import com.ey.dgs.notifications.NotificationListActivity;

import java.util.ArrayList;


public class BreakdownQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BreakQuestionItem> breakQuestionItems;
    private Activity context;

    public BreakdownQuestionsAdapter(Activity context, ArrayList<BreakQuestionItem> breakQuestionItems) {
        this.breakQuestionItems = breakQuestionItems;
        this.context = context;
    }

    @Override
    public BreakQuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakdown_item, parent, false);
        return new BreakQuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        final BreakQuestionItem breakQuestionItem = this.breakQuestionItems.get(i);
        BreakQuestionHolder breakQuestionHolder = (BreakQuestionHolder) holder;
        breakQuestionHolder.tvName.setText(breakQuestionItem.getName());
    }

    @Override
    public int getItemCount() {
        return this.breakQuestionItems.size();
    }

    public class BreakQuestionHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;

        private BreakQuestionHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

}
