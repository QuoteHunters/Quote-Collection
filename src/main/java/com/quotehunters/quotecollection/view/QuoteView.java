package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.model.dto.QuoteDTO;

import java.util.List;
import java.util.Scanner;

public class QuoteView {

    private static final String HEADER = "=".repeat(10);
    private static final String LINE = "-".repeat(30);
    private final ResultView resultView = new ResultView();

    private void printHeader(String title) {
        System.out.println(HEADER + " " + title + " " + HEADER);
    }

    public void printQuoteList(List<QuoteDTO> quoteList) {

        System.out.println();
        printHeader("명언 목록");

        for (QuoteDTO quote : quoteList) {
            System.out.println("명언 : " + quote.getQuoteContent());
            System.out.println("인물 : " + quote.getPersonName());
            System.out.println("주제 : " + quote.getThemeName());
            System.out.println(LINE);
        }
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printTodayQuote(QuoteDTO quote) {

        System.out.println();
        printHeader("오늘의 명언");
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("주제 : " + quote.getThemeName());
        System.out.println(LINE);
    }

    // 키워드로 검색된 명언 목록을 선택 가능한 번호와 함께 출력한다.
    public void printSearchedQuoteList(List<QuoteDTO> quoteList) {

        System.out.println();
        printHeader("검색 결과");

        for (int i = 0; i < quoteList.size(); i++) {
            QuoteDTO quote = quoteList.get(i);

            System.out.println("번호 : " + (i + 1));
            System.out.println("명언 : " + quote.getQuoteContent());
            System.out.println("인물 : " + quote.getPersonName());
            System.out.println("주제 : " + quote.getThemeName());
            System.out.println(LINE);
        }
    }

    // 인물 이름을 입력받고 0이면 뒤로가기를 반환한다.
    public String inputPersonName(Scanner scanner) {

        while (true) {
            System.out.print(
                    "인물 이름 입력 (0: 뒤로가기) : "
            );

            String personName = scanner.nextLine().trim();

            if (personName.equals("0")) {
                return null;
            }

            if (personName.isEmpty()) {
                resultView.errorMessage("인물 이름을 입력해주세요.");
                continue;
            }

            return personName;
        }
    }

    // 검색된 인물 후보를 화면 선택 번호와 함께 출력한다.
    public void printPersonCandidates(List<QuoteDTO> personList) {

        System.out.println();
        printHeader("인물 검색 결과");

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

        System.out.println(LINE);
    }

    // 명언을 보유한 인물을 선택 번호와 함께 출력한다.
    public void printPersonsWithQuotes(List<QuoteDTO> personList) {

        System.out.println();
        printHeader("명언 보유 인물 목록");

        for (int i = 0; i < personList.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + personList.get(i).getPersonName()
            );
        }

        System.out.println("0. 명언 탐색으로");
        System.out.println(LINE);
    }

    // 전체 주제 목록을 화면 선택 번호와 함께 출력한다.
    public void printThemeCandidates(List<QuoteDTO> themeList) {

        System.out.println();
        printHeader("전체 주제 목록");

        for (int i = 0; i < themeList.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + themeList.get(i).getThemeName()
            );
        }

        System.out.println(LINE);
    }

    // 목록 크기를 기준으로 유효한 화면 선택 번호를 입력받는다.
    // 목록 번호를 입력받고 0이면 뒤로가기를 반환한다.
    public int inputListNumber(
            Scanner scanner,
            int listSize,
            String prompt
    ) {

        while (true) {
            System.out.print(prompt + " (0: 뒤로가기) : ");

            String input = scanner.nextLine().trim();

            try {
                int selectedNumber = Integer.parseInt(input);

                if (!input.equals(String.valueOf(selectedNumber))) {
                    resultView.errorMessage(
                            "숫자를 올바른 형식으로 입력해주세요."
                    );
                    continue;
                }

                if (selectedNumber == 0) {
                    return 0;
                }

                if (selectedNumber < 1 || selectedNumber > listSize) {
                    resultView.errorMessage(
                            "리스트에 존재하는 번호를 입력해주세요."
                    );
                    continue;
                }

                return selectedNumber;

            } catch (NumberFormatException e) {
                resultView.errorMessage("숫자를 입력해주세요.");
            }
        }
    }

    // 주제 목록 조회 실패 후 재시도 또는 이전 단계 이동을 선택받는다.
    public boolean inputThemeListRetry(Scanner scanner) {

        while (true) {
            System.out.print(
                    "주제 목록을 다시 조회하시겠습니까? " +
                            "(재시도 / 뒤로가기) : "
            );

            String decision = scanner.nextLine().trim();

            if (decision.equals("재시도")) {
                return true;
            }

            if (decision.equals("뒤로가기")) {
                return false;
            }

            resultView.errorMessage(
                    "재시도, 뒤로가기 중 하나를 입력해주세요."
            );
        }
    }

    public boolean inputDatabaseRetry(Scanner scanner, String target) {

        while (true) {
            System.out.print(
                    target + "을(를) 다시 조회하시겠습니까? " +
                            "(재시도 / 취소) : "
            );

            String decision = scanner.nextLine().trim();

            if (decision.equals("재시도")) {
                return true;
            }

            if (decision.equals("취소")) {
                return false;
            }

            resultView.errorMessage(
                    "재시도, 취소 중 하나를 입력해주세요."
            );
        }
    }

    // 탐색 조회 실패 후 재시도 또는 이전 단계 이동을 선택받는다.
    public boolean inputBrowseRetry(Scanner scanner) {

        System.out.println("1. 재시도");
        System.out.println("0. 이전 단계로");

        return inputListNumber(
                scanner,
                1,
                "번호 선택"
        ) == 1;
    }

    public void printQuoteDetail(QuoteDTO quote) {

        System.out.println();
        printHeader("명언 상세");
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("주제 : " + quote.getThemeName());
        System.out.println(LINE);
    }

    public void inputQuoteDetailBack(Scanner scanner) {

        System.out.println("0. 이전 화면으로");
        inputListNumber(scanner, 0, "번호 선택");
    }

    // 빈 값과 DB의 VARCHAR(255) 길이를 검사하며 명언 내용을 입력받는다.
    public String inputQuoteContent(Scanner scanner) {

        while (true) {
            System.out.print("명언 내용 입력 (0: 뒤로가기) : ");

            String quoteContent = scanner.nextLine().trim();

            if (quoteContent.equals("0")) {
                return null;
            }

            if (quoteContent.isEmpty()) {
                resultView.errorMessage("명언 내용을 입력해주세요.");
                continue;
            }

            int characterCount = quoteContent.codePointCount(
                    0,
                    quoteContent.length()
            );

            if (characterCount > 255) {
                resultView.errorMessage("명언은 255자 이하로 입력해주세요.");
                continue;
            }

            return quoteContent;
        }
    }

    // 현재 선택한 인물, 주제 및 명언 내용을 최종 확인용으로 출력한다.
    public void printQuoteRegistrationSummary(QuoteDTO quote) {

        System.out.println();
        printHeader("명언 등록 내용");
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("국가 : " + quote.getCountryName());
        System.out.println("시대 : " + quote.getPeriodName());
        System.out.println("분야 : " + quote.getFieldName());
        System.out.println("주제 : " + quote.getThemeName());
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println(LINE);
    }

    // 등록, 취소 또는 수정을 선택받는다.
    public String inputRegistrationDecision(Scanner scanner) {

        while (true) {
            System.out.print("작업 선택 (등록 / 수정 / 취소) : ");

            String decision = scanner.nextLine().trim();

            if (decision.equals("등록") ||
                    decision.equals("수정") ||
                    decision.equals("취소")) {
                return decision;
            }

            resultView.errorMessage(
                    "등록, 수정, 취소 중 하나를 입력해주세요."
            );
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
                "수정 항목 번호 입력"
        );
    }

    // 선택한 인물의 명언을 DB ID가 아닌 화면 순번과 함께 출력한다.
    public void printQuotesForUpdate(List<QuoteDTO> quoteList) {

        System.out.println();
        printHeader("수정할 명언 목록");

        for (int i = 0; i < quoteList.size(); i++) {
            QuoteDTO quote = quoteList.get(i);

            System.out.println(
                    (i + 1) + ". " +
                            quote.getQuoteContent() +
                            " | 주제: " + quote.getThemeName()
            );
        }

        System.out.println(LINE);
    }

    // 수정 대상으로 선택한 명언의 현재 정보를 출력한다.
    public void printCurrentQuoteForUpdate(QuoteDTO quote) {

        System.out.println();
        printHeader("현재 명언 정보");
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println("주제 : " + quote.getThemeName());
        System.out.println(LINE);
    }

    public void printQuoteContentUpdateSummary(
            QuoteDTO quote,
            String newContent
    ) {

        System.out.println();
        printHeader("명언 수정");
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("현재 명언 : " + quote.getQuoteContent());
        System.out.println("변경할 명언 : " + newContent);
        System.out.println(LINE);
    }

    // 수정 완료, 재수정 또는 취소를 선택받는다.
    public String inputUpdateDecision(Scanner scanner) {

        while (true) {
            System.out.print("작업 선택 (완료 / 재수정 / 취소) : ");

            String decision = scanner.nextLine().trim();

            if (decision.equals("완료") ||
                    decision.equals("재수정") ||
                    decision.equals("취소")) {
                return decision;
            }

            resultView.errorMessage(
                    "완료, 재수정, 취소 중 하나를 입력해주세요."
            );
        }
    }

    // 명언 주제 수정에 사용할 검색 방식을 선택받는다.
    public int inputQuoteSearchType(Scanner scanner) {

        System.out.println();
        printHeader("명언 검색 방식");
        System.out.println("1. 주제 검색");
        System.out.println("2. 명언 내용 검색");
        System.out.println("3. 인물 검색");
        System.out.println(LINE);

        return inputListNumber(
                scanner,
                3,
                "검색 방식 번호 입력"
        );
    }

    // 검색어를 입력받고 0이면 이전 화면으로 돌아간다.
    public String inputSearchWord(
            Scanner scanner,
            String target
    ) {

        while (true) {
            System.out.print(
                    target + " 입력 (0: 뒤로가기) : "
            );

            String keyword = scanner.nextLine().trim();

            if (keyword.equals("0")) {
                return null;
            }

            if (keyword.isEmpty()) {
                resultView.errorMessage(target + "을(를) 입력해주세요.");
                continue;
            }

            return keyword;
        }
    }

    // 주제 수정 대상 명언을 화면 순번과 함께 출력한다.
    public void printQuoteSearchResults(
            List<QuoteDTO> quoteList
    ) {

        System.out.println();
        printHeader("명언 검색 결과");

        for (int i = 0; i < quoteList.size(); i++) {
            QuoteDTO quote = quoteList.get(i);

            System.out.println(
                    (i + 1) + ". " +
                            quote.getQuoteContent() +
                            " | 인물: " + quote.getPersonName() +
                            " | 현재 주제: " + quote.getThemeName()
            );
        }

        System.out.println(LINE);
    }

    // 현재 주제와 새로 선택한 주제를 최종 확인용으로 출력한다.
    public void printThemeUpdateSummary(
            QuoteDTO quote,
            QuoteDTO newTheme
    ) {

        System.out.println();
        printHeader("명언 주제 수정");
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("현재 주제 : " + quote.getThemeName());
        System.out.println("변경할 주제 : " + newTheme.getThemeName());
        System.out.println(LINE);
    }

    // 삭제할 명언 정보와 즐겨찾기 삭제 안내를 출력한다.
    public void printQuoteDeleteSummary(QuoteDTO quote) {

        System.out.println();
        printHeader("삭제할 명언");
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("주제 : " + quote.getThemeName());
        System.out.println(LINE);
        System.out.println(
                "※ 해당 명언의 즐겨찾기도 함께 삭제됩니다."
        );
        System.out.println(LINE);
    }

    // 명언 삭제 여부를 예 또는 아니오로 입력받는다.
    public String inputDeleteDecision(Scanner scanner) {

        while (true) {
            System.out.print("삭제 여부 (예 / 아니오) : ");

            String decision = scanner.nextLine().trim();

            if (decision.equals("예") || decision.equals("아니오")) {
                return decision;
            }

            resultView.errorMessage("예 또는 아니오를 입력해주세요.");
        }
    }

    // 선택한 주제의 명언을 화면 순번과 함께 출력한다.
    public void printQuotesByTheme(
            QuoteDTO selectedTheme,
            List<QuoteDTO> quoteList
    ) {

        System.out.println();
        printHeader(selectedTheme.getThemeName() + " 주제 명언");

        for (int i = 0; i < quoteList.size(); i++) {
            QuoteDTO quote = quoteList.get(i);

            System.out.println("번호 : " + (i + 1));
            System.out.println(
                    "명언 : " + quote.getQuoteContent()
            );
            System.out.println(
                    "인물 : " + quote.getPersonName()
            );
            System.out.println(LINE);
        }
    }

}
