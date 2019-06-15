package com.ey.dgs.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ey.dgs.R;
import com.ey.dgs.dashboard.manageAccounts.ThresholdQuestionFragment;

import java.util.ArrayList;

public class ThresholdQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> nicknames;
    private ArrayList<String> accountNumber;
    private Fragment fragment;
    final int TYPE_ACCOUNT = 2;
    final int TYPE_NEXT = 3;

    private String[] thresholdValues;

    public ThresholdQuestionAdapter(Fragment fragment, ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        this.fragment = fragment;
        this.nicknames = nicknames;
        this.accountNumber = accountNumber;
        thresholdValues = new String[nicknames.size()];
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ACCOUNT) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.threshold_question_item, viewGroup, false);
            return new ThresholdQuestionAdapter.ThresholdQuestionHolder(itemView, new MyCustomEditTextListener());
        } else if (viewType == TYPE_NEXT) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.next_layout, viewGroup, false);
            return new ThresholdQuestionAdapter.NextHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        String[] texts = new String[nicknames.size()];
        if (viewHolder.getItemViewType() == TYPE_ACCOUNT) {
            ThresholdQuestionHolder holder = (ThresholdQuestionHolder) viewHolder;
            holder.tvNickname.setText(nicknames.get(i));
            holder.tvAccountNo.setText(accountNumber.get(i));
            holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
            holder.thresholdAnswer.setText(thresholdValues[holder.getAdapterPosition()]);
        } else if (viewHolder.getItemViewType() == TYPE_NEXT) {
            NextHolder holder = (NextHolder) viewHolder;
        }
    }

    @Override
    public int getItemCount() {
        return this.nicknames.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == nicknames.size()) ? TYPE_NEXT : TYPE_ACCOUNT;
    }

    public class ThresholdQuestionHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvNickname, tvAccountNo;
        AppCompatEditText thresholdAnswer;
        public MyCustomEditTextListener myCustomEditTextListener;

        private ThresholdQuestionHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNickname);
            tvAccountNo = itemView.findViewById(R.id.tvAccountNo);
            thresholdAnswer = itemView.findViewById(R.id.etAnswer);
            this.myCustomEditTextListener = myCustomEditTextListener;
            thresholdAnswer.addTextChangedListener(myCustomEditTextListener);
        }
    }

    public class NextHolder extends RecyclerView.ViewHolder {

        Button btnNext;

        private NextHolder(View itemView) {
            super(itemView);
            btnNext = itemView.findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ThresholdQuestionFragment) fragment).setThresholdValues(accountNumber, thresholdValues);
                }
            });
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!TextUtils.isEmpty(charSequence.toString()) && Integer.valueOf(charSequence.toString()) > 0)
                thresholdValues[position] = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
