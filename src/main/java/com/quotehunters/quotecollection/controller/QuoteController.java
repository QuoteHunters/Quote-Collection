package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.QuoteDTO;
import com.quotehunters.quotecollection.model.service.QuoteService;
import com.quotehunters.quotecollection.view.QuoteView;

import java.util.List;
import java.util.Scanner;

public class QuoteController {

    private QuoteService quoteService = new QuoteService();
    private QuoteView resultView = new QuoteView();

    public void selectAllQuotes() {

        List<QuoteDTO> quoteList = quoteService.selectAllQuotes();

        if (quoteList.isEmpty()) {
            resultView.printMessage("등록된 명언이 없습니다.");
        } else {
            resultView.printQuoteList(quoteList);
        }
    }

    public void selectTodayQuote() {

        QuoteDTO quote = quoteService.selectTodayQuote();

        if (quote == null) {
            resultView.printMessage("등록된 명언이 없습니다.");
        } else {
            resultView.printTodayQuote(quote);
        }
    }

    // 키워드 검색 결과의 존재 여부에 따라 목록 또는 안내 메시지를 출력한다.
    public void searchQuotesByKeyword(String keyword) {

        try {
            List<QuoteDTO> quoteList =
                    quoteService.searchQuotesByKeyword(keyword);

            if (quoteList.isEmpty()) {
                resultView.printMessage(
                        "'" + keyword + "'(으)로 조회된 명언이 없습니다."
                );
            } else {
                resultView.printSearchedQuoteList(quoteList);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            resultView.printMessage("명언 검색 중 오류가 발생했습니다.");
        }
    }
    // 인물별 검색 결과의 존재 여부에 따라 목록 또는 안내 문장을 출력한다.
    public void searchQuotesByPerson(String personName) {

        try {
            List<QuoteDTO> quoteList =
                    quoteService.searchQuotesByPerson(personName);

            if (quoteList.isEmpty()) {
                resultView.printMessage(
                        "'" + personName + "'(으)로 조회된 명언이 없습니다."
                );
            } else {
                resultView.printSearchedQuoteList(quoteList);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            resultView.printMessage("인물별 명언 검색 중 오류가 발생했습니다.");
        }
    }

    // 명언 등록 전체 흐름을 진행한다.
    public void registerQuote(Scanner scanner) {

        QuoteDTO quote = new QuoteDTO();

        if (!selectPersonForRegistration(scanner, quote)) {
            return;
        }

        if (!selectThemeForRegistration(scanner, quote)) {
            return;
        }

        quote.setQuoteContent(resultView.inputQuoteContent(scanner));

        while (true) {
            resultView.printQuoteRegistrationSummary(quote);

            String decision =
                    resultView.inputRegistrationDecision(scanner);

            if (decision.equals("Y")) {
                executeQuoteInsert(quote);
                return;
            }

            if (decision.equals("N")) {
                return;
            }

            modifyQuoteRegistration(scanner, quote);
        }
    }

    // 인물을 검색하고 화면 순번으로 등록 대상 인물을 선택한다.
    private boolean selectPersonForRegistration(
            Scanner scanner,
            QuoteDTO quote
    ) {

        while (true) {
            String personName =
                    resultView.inputPersonName(scanner);

            List<QuoteDTO> personList =
                    quoteService.searchPersonsForQuoteRegistration(personName);

            if (personList.isEmpty()) {
                resultView.printMessage("조회된 인물이 없습니다.");
                continue;
            }

            resultView.printPersonCandidates(personList);

            int selectedNumber = resultView.inputListNumber(
                    scanner,
                    personList.size(),
                    "등록할 인물 번호를 입력해주세요"
            );

            QuoteDTO selectedPerson =
                    personList.get(selectedNumber - 1);

            quote.setPersonId(selectedPerson.getPersonId());
            quote.setPersonName(selectedPerson.getPersonName());
            quote.setCountryName(selectedPerson.getCountryName());
            quote.setPeriodName(selectedPerson.getPeriodName());
            quote.setFieldName(selectedPerson.getFieldName());

            return true;
        }
    }

    // 전체 주제를 조회하고 화면 순번으로 등록 대상 주제를 선택한다.
    private boolean selectThemeForRegistration(
            Scanner scanner,
            QuoteDTO quote
    ) {

        List<QuoteDTO> themeList =
                quoteService.selectThemesForQuoteRegistration();

        if (themeList.isEmpty()) {
            resultView.printMessage("등록된 주제가 없습니다.");
            return false;
        }

        resultView.printThemeCandidates(themeList);

        int selectedNumber = resultView.inputListNumber(
                scanner,
                themeList.size(),
                "등록할 주제 번호를 입력해주세요"
        );

        QuoteDTO selectedTheme =
                themeList.get(selectedNumber - 1);

        quote.setThemeId(selectedTheme.getThemeId());
        quote.setThemeName(selectedTheme.getThemeName());

        return true;
    }

    // 사용자가 고른 항목만 다시 입력받고 나머지 등록 정보는 유지한다.
    private void modifyQuoteRegistration(
            Scanner scanner,
            QuoteDTO quote
    ) {

        int target = resultView.inputModificationTarget(scanner);

        switch (target) {
            case 1:
                selectPersonForRegistration(scanner, quote);
                break;

            case 2:
                selectThemeForRegistration(scanner, quote);
                break;

            case 3:
                quote.setQuoteContent(
                        resultView.inputQuoteContent(scanner)
                );
                break;

            default:
                // inputModificationTarget에서 검증하므로 도달하지 않는다.
                break;
        }
    }

    // 명언 INSERT를 실행하고 성공 또는 실패 메시지를 출력한다.
    private void executeQuoteInsert(QuoteDTO quote) {

        try {
            boolean success = quoteService.insertQuote(quote);

            if (success) {
                resultView.printMessage("명언 등록이 완료되었습니다.");
            } else {
                resultView.printMessage(
                        "명언 등록에 실패했습니다. 다시 시도해주세요."
                );
            }

        } catch (RuntimeException e) {
            e.printStackTrace();

            resultView.printMessage(
                    "명언 등록에 실패했습니다. 다시 시도해주세요."
            );
        }
    }
}