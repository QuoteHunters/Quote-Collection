package com.quotehunters.quotecollection.view;

public class ResultView {
    public void successMessage(String message) {
        System.out.println("[완료] " + message);
    }

    public void errorMessage(String message) {
        System.out.println("[오류] " + message);
    }
}
