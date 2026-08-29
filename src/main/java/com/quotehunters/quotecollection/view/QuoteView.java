package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.model.dto.QuoteDTO;

import java.util.List;
import java.util.Scanner;

public class QuoteView {

    public void printQuoteList(List<QuoteDTO> quoteList) {

        System.out.println();
        System.out.println("========== 명언 목록 ==========");

        for (QuoteDTO quote : quoteList) {
            System.out.println("명언 : " + quote.getQuoteContent());
            System.out.println("인물 : " + quote.getPersonName());
            System.out.println("주제 : " + quote.getThemeName());
            System.out.println("------------------------------");
        }
    }

    public void printMessage(String message) {
        System.out.println(">> " + message);
    }

    public void printTodayQuote(QuoteDTO quote) {

        System.out.println();
        System.out.println("========== 오늘의 명언 ==========");
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("주제 : " + quote.getThemeName());
        System.out.println("================================");
    }

    // 키워드로 검색된 명언 목록을 선택 가능한 번호와 함께 출력한다.
    public void printSearchedQuoteList(List<QuoteDTO> quoteList) {

        System.out.println();
        System.out.println("========== 검색 결과 ==========");

        for (QuoteDTO quote : quoteList) {
            System.out.println("번호 : " + quote.getQuoteId());
            System.out.println("명언 : " + quote.getQuoteContent());
            System.out.println("인물 : " + quote.getPersonName());
            System.out.println("주제 : " + quote.getThemeName());
            System.out.println("------------------------------");
        }
    }

    // 등록할 인물을 검색하기 위한 이름을 입력받는다.
    public String inputPersonName(Scanner scanner) {

        while (true) {
            System.out.print("검색할 인물 이름을 입력해주세요 : ");

            String personName = scanner.nextLine().trim();

            if (personName.isEmpty()) {
                printMessage("인물 이름을 입력해주세요.");
                continue;
            }

            return personName;
        }
    }

    // 검색된 인물 후보를 화면 선택 번호와 함께 출력한다.
    public void printPersonCandidates(List<QuoteDTO> personList) {

        System.out.println();
        System.out.println("========== 인물 검색 결과 ==========");

        for (int i = 0; i < personList.size(); i++) {
            QuoteDTO person = personList.get(i);

            System.out.println(
                    (i + 1) + ". " +
                            person.getPersonName() +
                            " | 국가: " + person.getCountryName() +
                            " | 시대: " + person.getPeriodName() +
                            " | 분야: " + person.getFieldName()
            );
        }

        System.out.println("==================================");
    }

    // 전체 주제 목록을 화면 선택 번호와 함께 출력한다.
    public void printThemeCandidates(List<QuoteDTO> themeList) {

        System.out.println();
        System.out.println("========== 전체 주제 목록 ==========");

        for (int i = 0; i < themeList.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + themeList.get(i).getThemeName()
            );
        }

        System.out.println("==================================");
    }

    // 목록 크기를 기준으로 유효한 화면 선택 번호를 입력받는다.
    public int inputListNumber(
            Scanner scanner,
            int listSize,
            String prompt
    ) {

        while (true) {
            System.out.print(prompt + " : ");

            String input = scanner.nextLine().trim();

            try {
                int selectedNumber = Integer.parseInt(input);

                if (selectedNumber < 1 || selectedNumber > listSize) {
                    printMessage("리스트에 존재하는 번호를 입력해주세요.");
                    continue;
                }

                return selectedNumber;

            } catch (NumberFormatException e) {
                printMessage("숫자를 입력해주세요.");
            }
        }
    }

    // 빈 값과 DB의 VARCHAR(255) 길이를 검사하며 명언 내용을 입력받는다.
    public String inputQuoteContent(Scanner scanner) {

        while (true) {
            System.out.print("명언 내용을 입력해주세요 : ");

            String quoteContent = scanner.nextLine().trim();

            if (quoteContent.isEmpty()) {
                printMessage("명언 내용을 입력해주세요.");
                continue;
            }

            int characterCount = quoteContent.codePointCount(
                    0,
                    quoteContent.length()
            );

            if (characterCount > 255) {
                printMessage("명언은 255자 이하로 입력해주세요.");
                continue;
            }

            return quoteContent;
        }
    }

    // 현재 선택한 인물, 주제 및 명언 내용을 최종 확인용으로 출력한다.
    public void printQuoteRegistrationSummary(QuoteDTO quote) {

        System.out.println();
        System.out.println("========== 명언 등록 내용 ==========");
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("국가 : " + quote.getCountryName());
        System.out.println("시대 : " + quote.getPeriodName());
        System.out.println("분야 : " + quote.getFieldName());
        System.out.println("주제 : " + quote.getThemeName());
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println("==================================");
    }

    // 등록, 취소 또는 수정을 선택받는다.
    public String inputRegistrationDecision(Scanner scanner) {

        while (true) {
            System.out.print("등록하시겠습니까? [Y/N/수정] : ");

            String decision = scanner.nextLine().trim();

            if (decision.equalsIgnoreCase("Y")) {
                return "Y";
            }

            if (decision.equalsIgnoreCase("N")) {
                return "N";
            }

            if (decision.equals("수정")) {
                return "수정";
            }

            printMessage("Y, N, 수정 중 하나를 입력해주세요.");
        }
    }

    // 수정할 항목을 인물, 주제, 명언 내용 중에서 선택받는다.
    public int inputModificationTarget(Scanner scanner) {

        System.out.println();
        System.out.println("1. 인물");
        System.out.println("2. 주제");
        System.out.println("3. 명언 내용");

        return inputListNumber(
                scanner,
                3,
                "수정할 항목 번호를 입력해주세요"
        );
    }
}