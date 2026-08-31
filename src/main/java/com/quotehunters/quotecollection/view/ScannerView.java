package com.quotehunters.quotecollection.view;

import java.util.Scanner;

public class ScannerView {
    private final ResultView resultView = new ResultView();

    public int scannInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " : ");
            String answer = sc.nextLine().trim();

            try {
                int number = Integer.parseInt(answer);

                if (!answer.equals(String.valueOf(number))) {
                    resultView.errorMessage("숫자를 올바른 형식으로 입력해주세요.");
                    continue;
                }

                return number;
            } catch (NumberFormatException e) {
                resultView.errorMessage("숫자를 입력해주세요.");
            }
        }
    }

    public String scannString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " : ");
            String answer = sc.nextLine().trim();

            if (answer.isEmpty()) {
                resultView.errorMessage("한 글자 이상 입력해주세요.");
                continue;
            }

            return answer;
        }
    }
}
