package com.ey.dgs.model;

public class Question {

    public static int TYPE_TEXT = 1;
    public static int TYPE_DROPDOWN = 2;
    public static int TYPE_MULTI_CHOICE = 3;

    public Question(String question, int type, String[] options) {
        this.question = question;
        this.type = type;
        this.options = options;
    }

    private String question;

    private int type;

    private String[] options;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
