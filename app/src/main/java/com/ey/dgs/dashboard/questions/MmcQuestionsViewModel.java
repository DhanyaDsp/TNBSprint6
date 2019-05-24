package com.ey.dgs.dashboard.questions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.Question;

import java.util.ArrayList;

public class MmcQuestionsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Question>> questionsData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Question>> getQuestionsData() {
        return questionsData;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questionsData.postValue(questions);
    }

    public void loadQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question("How many people are staying in Sooraya", Question.TYPE_TEXT, null));
        questions.add(new Question("How many people are staying in Sooraya", Question.TYPE_MULTI_CHOICE, null));
        questions.add(new Question("How many people are staying in Sooraya", Question.TYPE_DROPDOWN, new String[]{"1", "2", "3"}));
        setQuestions(questions);
    }
}
