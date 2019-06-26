package com.ey.dgs.dashboard.questions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.api_response.GetQuestionsResponse;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.AnswerRequest;
import com.ey.dgs.model.Question;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ey.dgs.webservice.ApiClient.REQUEST_CODE_ANSWER_QUESTIONS;

public class MmcQuestionsViewModel extends ViewModel implements APICallback {

    Context context;
    AppPreferences appPreferences;

    private MutableLiveData<ArrayList<Question>> questionsData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Question>> getQuestionsData() {
        return questionsData;
    }

    private MutableLiveData<Boolean> loaderData = new MutableLiveData<>();

    public Context getContext() {
        return context;
    }

    private MutableLiveData<Integer> questionsAnswered = new MutableLiveData<>();

    public void setContext(Context context) {
        this.context = context;
        appPreferences = new AppPreferences(context);
    }

    public MutableLiveData<Boolean> getLoaderData() {
        return loaderData;
    }

    public void setLoader(boolean showLoader) {
        loaderData.postValue(showLoader);
    }

    public MutableLiveData<Integer> getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered.postValue(questionsAnswered);
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questionsData.postValue(questions);
    }

    public void loadQuestionsFromServer(String accountNumber, String userName) {
        setLoader(true);
        new ApiClient().getQuestionsFromServer(appPreferences.getAuthToken(), accountNumber, userName, this);
    }

    public void answerQuestion(AnswerRequest answerRequest) {
        setLoader(true);
        new ApiClient().answerQuestion(appPreferences.getAuthToken(), answerRequest, this);
    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        setLoader(false);
        if (requestCode == ApiClient.REQUEST_CODE_GET_QUESTIONS) {
            GetQuestionsResponse getQuestionsResponse = (GetQuestionsResponse) obj;
            setQuestions(new ArrayList<Question>(Arrays.asList(getQuestionsResponse.getResult())));
        } else if (requestCode == REQUEST_CODE_ANSWER_QUESTIONS) {
            setQuestionsAnswered((Integer) obj);
        }
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        setLoader(false);
        if (requestCode == REQUEST_CODE_ANSWER_QUESTIONS) {
            //setQuestionsAnswered(0);
        }
        Utils.showToast(context, (String) obj);

    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {

    }

    @Override
    public void onOffline(int requestCode, boolean isLoading) {

    }
}
