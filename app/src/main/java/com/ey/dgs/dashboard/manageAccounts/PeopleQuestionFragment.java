package com.ey.dgs.dashboard.manageAccounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.PeopleQuestionAdapter;
import com.ey.dgs.utils.Utils;

import java.util.ArrayList;

public class PeopleQuestionFragment extends Fragment {

    ArrayList<String> nicknames = new ArrayList<>();
    ArrayList<String> accountNumber = new ArrayList<>();
    ArrayList<String> peopleInProperty = new ArrayList<>();
    PeopleQuestionAdapter adapter;
    private LinearLayoutManager rvLayoutManager;
    RecyclerView rvQuestions;
    View view;

    public static PeopleQuestionFragment newInstance(ArrayList<String> nicknames, ArrayList<String> accountNumber,
                                                     ArrayList<String> peopleInProperty) {
        PeopleQuestionFragment fragment = new PeopleQuestionFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(MMCManageAccountsFragment.NICKNAMES, nicknames);
        args.putStringArrayList(MMCManageAccountsFragment.ACCOUNT_NUMBER, accountNumber);
        args.putStringArrayList(MMCManageAccountsFragment.PEOPLEINPROPERTY, peopleInProperty);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nicknames = getArguments().getStringArrayList(MMCManageAccountsFragment.NICKNAMES);
        accountNumber = getArguments().getStringArrayList(MMCManageAccountsFragment.ACCOUNT_NUMBER);
        peopleInProperty = getArguments().getStringArrayList(MMCManageAccountsFragment.PEOPLEINPROPERTY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.people_question, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        rvQuestions = view.findViewById(R.id.rvPeople);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvQuestions.setLayoutManager(rvLayoutManager);
        adapter = new PeopleQuestionAdapter(this, nicknames, accountNumber, peopleInProperty);
        rvQuestions.setAdapter(adapter);
    }

    public void setPeopleInProperty(ArrayList<String> accountNumber, ArrayList<Integer> peopleInProperty) {
        ThresholdQuestionFragment.setValues(accountNumber, peopleInProperty);
    }

    public void moveNextSlide(){
        MMCManageAccountsFragment.moveNext();
    }
}
