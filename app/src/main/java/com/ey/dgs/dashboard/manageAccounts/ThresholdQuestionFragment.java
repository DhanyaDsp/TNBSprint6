package com.ey.dgs.dashboard.manageAccounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.ThresholdQuestionAdapter;

import java.util.ArrayList;

public class ThresholdQuestionFragment extends Fragment {

    ArrayList<String> nicknames = new ArrayList<>();
    ArrayList<String> accountNumber = new ArrayList<>();
    ThresholdQuestionAdapter adapter;
    private LinearLayoutManager rvLayoutManager;
    RecyclerView rvQuestions;
    View view;

    public static ThresholdQuestionFragment newInstance(ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        ThresholdQuestionFragment fragment = new ThresholdQuestionFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(MMCManageAccountsFragment.NICKNAMES, nicknames);
        args.putStringArrayList(MMCManageAccountsFragment.ACCOUNT_NUMBER, accountNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nicknames = getArguments().getStringArrayList(MMCManageAccountsFragment.NICKNAMES);
            accountNumber = getArguments().getStringArrayList(MMCManageAccountsFragment.ACCOUNT_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.threshold_question, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        rvQuestions = view.findViewById(R.id.rvThreshold);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvQuestions.setLayoutManager(rvLayoutManager);
        adapter = new ThresholdQuestionAdapter(nicknames, accountNumber);
        rvQuestions.setAdapter(adapter);
    }
}
