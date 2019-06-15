package com.ey.dgs.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ey.dgs.R;
import com.ey.dgs.dashboard.manageAccounts.PeopleQuestionFragment;

import java.util.ArrayList;

public class PeopleQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Integer> displayCounts = new ArrayList<>();
    private ArrayList<String> nicknames;
    private ArrayList<String> accountNumber;
    private Fragment fragment;

    public PeopleQuestionAdapter(Fragment fragment, ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        this.fragment = fragment;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PeopleQuestionHolder holder = (PeopleQuestionHolder) viewHolder;
        holder.tvNickname.setText(nicknames.get(position));
        holder.tvAccountNo.setText(accountNumber.get(position));
        displayCounts.add(position, 1);
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displayCounts.get(position) > 1) {
                    int number = displayCounts.get(position);
                     number= number - 1;
                    holder.numberDisplay.setText("" + number);
                    displayCounts.set(position, number);
                    setPeopleInProperty(displayCounts);
                }
            }
        });
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displayCounts.get(position) <  10) {
                    int number = displayCounts.get(position);
                    number= number + 1;
                    holder.numberDisplay.setText("" + number);
                    displayCounts.set(position, number);
                    setPeopleInProperty(displayCounts);
                }
            }
        });
        setPeopleInProperty(displayCounts);
    }

    private void setPeopleInProperty(ArrayList<Integer> peopleInProperty) {
        ((PeopleQuestionFragment) fragment).setPeopleInProperty(accountNumber, peopleInProperty);
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
}
