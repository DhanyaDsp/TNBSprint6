package com.ey.dgs.model;

public class BreakQuestionItem {

    private String name;

    private boolean isSelected;

    private String answer;

    public BreakQuestionItem(String name, boolean isSelected, String answer) {
        this.name = name;
        this.isSelected = isSelected;
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
