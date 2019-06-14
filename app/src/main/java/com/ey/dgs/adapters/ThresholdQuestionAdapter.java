package com.ey.dgs.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;

import java.util.ArrayList;

public class ThresholdQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> nicknames;
    ArrayList<String> accontNumber;

    public ThresholdQuestionAdapter(ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        this.nicknames = nicknames;
        this.accontNumber = accountNumber;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.threshold_question_item, viewGroup, false);
        return new ThresholdQuestionAdapter.ThresholdQuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ThresholdQuestionHolder holder = (ThresholdQuestionHolder) viewHolder;
        holder.tvNickname.setText(nicknames.get(i));
        holder.tvAccountNo.setText(accontNumber.get(i));
    }

    @Override
    public int getItemCount() {
        return this.nicknames.size();
    }

    public class ThresholdQuestionHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvNickname, tvAccountNo;
        AppCompatEditText thresholdAnswer;

        private ThresholdQuestionHolder(View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNickname);
            tvAccountNo = itemView.findViewById(R.id.tvAccountNo);
            thresholdAnswer = itemView.findViewById(R.id.etAnswer);
        }
    }
}
