package com.ey.dgs.dashboard.questions;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.QuestionsPagerAdapter;
import com.ey.dgs.model.Question;

import java.util.ArrayList;

public class MMCQuestionsFragment extends Fragment {

    private MmcQuestionsViewModel mViewModel;
    ViewPager vpQuestions;
    private View rootView;
    QuestionsPagerAdapter questionsPagerAdapter;
    ArrayList<Question> questions = new ArrayList<>();

    public static MMCQuestionsFragment newInstance() {
        return new MMCQuestionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mmcquestions_fragment, container, false);
        initViews();
        return rootView;
    }

    private void initViews() {
        vpQuestions = rootView.findViewById(R.id.vpQuestions);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MmcQuestionsViewModel.class);
        mViewModel.loadQuestions();
        mViewModel.getQuestionsData().observeForever(questions -> {
            if (questions != null && questions.size() > 0) {
                this.questions = questions;
                setAdapter(this.questions);
            }
        });
    }

    private void setAdapter(ArrayList<Question> questions) {
        questionsPagerAdapter = new QuestionsPagerAdapter(getActivity(), questions);
        vpQuestions.setAdapter(questionsPagerAdapter);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        HomeActivity.isQuestionsShown = false;
    }
}
