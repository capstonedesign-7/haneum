package com.example.haneum;

public class StepThreeMessage {
    private String text;
    private boolean isUser;

    public StepThreeMessage(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
    }

    public String getText() {
        return text;
    }

    public boolean isUser() {
        return isUser;
    }
}