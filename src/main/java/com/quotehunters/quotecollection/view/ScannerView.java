package com.quotehunters.quotecollection.view;

import java.util.Scanner;

public class ScannerView {
    public int scannInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " : ");
            String answer = sc.nextLine().trim();

            try {
                return Integer.parseInt(answer);
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요");
            }
        }
    }

    public String scannString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " : ");
            String answer = sc.nextLine().trim();

            if (answer.isEmpty() || answer.equals("")) {
                System.out.println("한 글자 이상 입력해주세요");
                continue;
            }

            return answer;
        }


    }
}
