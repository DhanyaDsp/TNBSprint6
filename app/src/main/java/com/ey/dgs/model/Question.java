package com.ey.dgs.model;

public class Question {

    public static int TYPE_TEXT = 1;
    public static int TYPE_DROPDOWN = 2;
    public static int TYPE_MULTI_CHOICE = 3;

    private String questionId;

    private String question;

    private String response;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ClassPojo [questionId = " + questionId + ", question = " + question + ", response = " + response + "]";
    }
}
