package com.ey.dgs.dashboard.manageAccounts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.AccountsQuestionsPagerAdapter;

import java.util.ArrayList;

public class MMCManageAccountsFragment extends Fragment {

    public static final String NICKNAMES = "nicknames";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    ArrayList<String> nicknames = new ArrayList<>();
    ArrayList<String> accountNumber = new ArrayList<>();
    private ViewPager vpQuestions;
    private TabLayout tlDots;
    private AccountsQuestionsPagerAdapter adapter;
    View view;

    public static MMCManageAccountsFragment newInstance(ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        MMCManageAccountsFragment fragment = new MMCManageAccountsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(NICKNAMES, nicknames);
        args.putStringArrayList(ACCOUNT_NUMBER, accountNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nicknames = getArguments().getStringArrayList(NICKNAMES);
            accountNumber = getArguments().getStringArrayList(ACCOUNT_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manage_account_people, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        vpQuestions = view.findViewById(R.id.vpQuestionItems);
        tlDots = view.findViewById(R.id.tlDots);
        adapter = new AccountsQuestionsPagerAdapter(getChildFragmentManager());
        adapter.addFrag(PeopleQuestionFragment.newInstance(nicknames, accountNumber));
        adapter.addFrag(ThresholdQuestionFragment.newInstance(nicknames, accountNumber));
        vpQuestions.setAdapter(adapter);
        tlDots.setupWithViewPager(vpQuestions, true);
    }

}
