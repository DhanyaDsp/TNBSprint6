package com.ey.dgs.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ey.dgs.R;

import java.util.ArrayList;

public class PeopleQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int displayCount;
    ArrayList<String> nicknames;
    ArrayList<String> accountNumber;

    public PeopleQuestionAdapter(ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        this.nicknames = nicknames;
        this.accountNumber = accountNumber;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.people_question_item, viewGroup, false);
        return new PeopleQuestionAdapter.PeopleQuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PeopleQuestionHolder holder = (PeopleQuestionHolder) viewHolder;
        holder.tvNickname.setText(nicknames.get(i));
        holder.tvAccountNo.setText(accountNumber.get(i));
    }

    @Override
    public int getItemCount() {
        return this.nicknames.size();
    }

    public class PeopleQuestionHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvNickname, tvAccountNo;
        Button decrease, increase, numberDisplay;

        private PeopleQuestionHolder(View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNickname);
            tvAccountNo = itemView.findViewById(R.id.tvAccountNo);
            numberDisplay = itemView.findViewById(R.id.number_display);
            decrease = itemView.findViewById(R.id.decrease);
            increase = itemView.findViewById(R.id.increase);
        }
    }

    public void increaseInteger() {
        if(displayCount < 10) {
            displayCount = displayCount + 1;
            display(displayCount);
        }
    }

    public void decreaseInteger() {
        if(displayCount > 1) {
            displayCount = displayCount - 1;
            display(displayCount);
        }
    }

    private void display(int number) {
        //numberDisplay.setText("" + number);
    }
}