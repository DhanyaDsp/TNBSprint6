package com.ey.dgs.dashboard.myaccount.breakdown;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.BreakdownQuestionsAdapter;
import com.ey.dgs.database.AccountDao;
import com.ey.dgs.model.BreakQuestionItem;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;


public class BreakdownQuestionsFragment extends Fragment {

    User user;
    private View view;
    EnergyInsightViewModel energyInsightViewModel;
    private ArrayList<BreakQuestionItem> breakQuestionItems = new ArrayList<>();
    RecyclerView rvQuestions;
    BreakdownQuestionsAdapter questionsAdapter;

    public BreakdownQuestionsFragment() {
    }

    public static BreakdownQuestionsFragment newInstance(User user) {
        BreakdownQuestionsFragment fragment = new BreakdownQuestionsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.USER, (Serializable) user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(Constants.USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_breakdown_questions, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        rvQuestions = view.findViewById(R.id.rvQuestions);
        rvQuestions.setHasFixedSize(true);
        GridLayoutManager rvLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvQuestions.setLayoutManager(rvLayoutManager);
        rvQuestions.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        energyInsightViewModel = ViewModelProviders.of(this).get(EnergyInsightViewModel.class);
        energyInsightViewModel.setContext(getActivity());
        energyInsightViewModel.loadQuestions();
        energyInsightViewModel.getBreakdownItems().observe(getViewLifecycleOwner(), breakQuestionItems -> {
            this.breakQuestionItems.clear();
            if (breakQuestionItems != null) {
                this.breakQuestionItems.addAll(breakQuestionItems);
                questionsAdapter = new BreakdownQuestionsAdapter(getActivity(), breakQuestionItems);
                rvQuestions.setAdapter(questionsAdapter);
            }
        });
    }
}
