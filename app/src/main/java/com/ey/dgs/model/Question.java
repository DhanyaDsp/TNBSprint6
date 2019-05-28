package com.ey.dgs.model;

public class Question {

    public static int TYPE_TEXT = 1;
    public static int TYPE_DROPDOWN = 2;
    public static int TYPE_MULTI_CHOICE = 3;

    private int questionId;

    private String question;

    private String response;

    private String type;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClassPojo [questionId = " + questionId + ", question = " + question + ", response = " + response + "]";
    }
}
