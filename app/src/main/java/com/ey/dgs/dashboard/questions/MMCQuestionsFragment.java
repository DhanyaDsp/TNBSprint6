package com.ey.dgs.dashboard.questions;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.QuestionsPagerAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.AnswerRequest;
import com.ey.dgs.model.Question;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class MMCQuestionsFragment extends Fragment {

    private MmcQuestionsViewModel mViewModel;
    ViewPager vpQuestions;
    private View rootView;
    QuestionsPagerAdapter questionsPagerAdapter;
    ArrayList<Question> questions = new ArrayList<>();
    Account account;
    AppCompatButton btnNext;
    View loader;
    AppCompatTextView tvAccountName;
    LoginViewModel loginViewModel;
    AppPreferences appPreferences;
    private User user;


    public static MMCQuestionsFragment newInstance(Account account) {
        MMCQuestionsFragment fragment = new MMCQuestionsFragment();
        Bundle args = new Bundle();
        args.putSerializable("account", (Serializable) account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = (Account) getArguments().getSerializable("account");
        appPreferences = new AppPreferences(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mmcquestions_fragment, container, false);
        initViews();
        setData();
        return rootView;
    }

    private void setData() {
        tvAccountName.setText(account.getNickName());
    }

    private void initViews() {
        vpQuestions = rootView.findViewById(R.id.vpQuestions);
        loader = rootView.findViewById(R.id.loader);
        tvAccountName = rootView.findViewById(R.id.tvAccountName);
        btnNext = rootView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vpQuestions.getCurrentItem();
                View currentQuestionView = vpQuestions.getChildAt(position);
                AppCompatEditText etAnswer = currentQuestionView.findViewById(R.id.etAnswer);
                String answer = etAnswer.getText().toString();
                AnswerRequest answerRequest = new AnswerRequest();
                answerRequest.setUserName(account.getAccountNumber());
                answerRequest.setUserName(user.getEmail());
                answerRequest.setResponse(answer);
                answerRequest.setQuestionId(questions.get(position).getQuestionId());
                if (!TextUtils.isEmpty(answer)) {
                    mViewModel.answerQuestion(answerRequest);
                } else {
                    Utils.showToast(getActivity(), "Please fill the answer");
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MmcQuestionsViewModel.class);
        mViewModel.setContext(getActivity());
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setContext(getActivity());
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
        });
        mViewModel.loadQuestionsFromServer(account.getAccountNumber(), new User().getEmail());
        mViewModel.getLoaderData().observe(getViewLifecycleOwner(), loading -> {
            showProgress(loading);
        });
        mViewModel.getQuestionsAnswered().observe(getViewLifecycleOwner(), index -> {
            vpQuestions.setCurrentItem(vpQuestions.getCurrentItem() + 1);
        });
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

    public void showProgress(boolean isProgress) {
        if (isProgress) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }
}
