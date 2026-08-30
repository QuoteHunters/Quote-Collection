package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.MemberDTO;
import com.quotehunters.quotecollection.model.dto.QuoteDTO;
import com.quotehunters.quotecollection.model.service.QuoteService;
import com.quotehunters.quotecollection.view.BookmarkView;
import com.quotehunters.quotecollection.view.QuoteView;

import java.util.List;
import java.util.Scanner;

public class QuoteController {

    private QuoteService quoteService = new QuoteService();
    private QuoteView resultView = new QuoteView();
    private BookmarkView bookmarkView = new BookmarkView();

    private static final int NO_DATA = -1;
    private static final int BACK = 0;
    private static final int SELECTED = 1;

    private void bookmarkQuote(Scanner scanner, MemberDTO member, List<QuoteDTO> quoteList) {
        QuoteDTO quote = bookmarkView.selectQuoteForFavorite(scanner, quoteList);
        bookmarkView.showQuoteDetailForFavorite(scanner, member, quote);
    }

    public void selectAllQuotes(Scanner scanner, MemberDTO member) {

        List<QuoteDTO> quoteList = quoteService.selectAllQuotes();

        if (quoteList.isEmpty()) {
            resultView.printMessage("등록된 명언이 없습니다.");
        } else {
            resultView.printQuoteList(quoteList);
            bookmarkQuote(scanner, member, quoteList);
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
    public void searchQuotesByKeyword(Scanner scanner, MemberDTO member, String keyword) {

        try {
            List<QuoteDTO> quoteList =
                    quoteService.searchQuotesByKeyword(keyword);

            if (quoteList.isEmpty()) {
                resultView.printMessage(
                        "'" + keyword + "'(으)로 조회된 명언이 없습니다."
                );
            } else {
                resultView.printSearchedQuoteList(quoteList);
                bookmarkQuote(scanner, member, quoteList);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            resultView.printMessage("명언 검색 중 오류가 발생했습니다.");
        }
    }
    // 인물 후보를 선택한 뒤 해당 인물의 명언만 조회한다.
    public void searchQuotesByPerson(Scanner scanner, MemberDTO member) {

        QuoteDTO selectedPerson = selectPersonFromAll(scanner);

        if (selectedPerson == null) {
            return;
        }

        try {
            List<QuoteDTO> quoteList =
                    quoteService.selectQuotesByPersonIdForUpdate(
                            selectedPerson.getPersonId()
                    );

            if (quoteList.isEmpty()) {
                resultView.printMessage("등록된 명언이 없습니다.");
            } else {
                resultView.printSearchedQuoteList(quoteList);
                bookmarkQuote(scanner, member, quoteList);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            resultView.printMessage("인물별 명언 검색 중 오류가 발생했습니다.");
        }
    }

    // 명언 등록 전체 흐름을 진행한다.
    public void registerQuote(Scanner scanner) {

        QuoteDTO quote = new QuoteDTO();

        while (true) {

            if (!selectPersonForRegistration(scanner, quote)) {
                return;
            }

            int themeResult =
                    selectThemeForRegistration(scanner, quote);

            if (themeResult == NO_DATA) {
                return;
            }

            if (themeResult == BACK) {
                continue;
            }

            break;
        }

        quote.setQuoteContent(
                resultView.inputQuoteContent(scanner)
        );

        executeQuoteInsert(quote);
    }

    // 전체 주제에서 하나를 선택하고 해당 주제의 명언을 조회한다.
    public void selectQuotesByTheme(Scanner scanner, MemberDTO member) {

        try {
            List<QuoteDTO> themeList =
                    quoteService.selectThemesForQuoteRegistration();

            if (themeList.isEmpty()) {
                resultView.printMessage(
                        "등록된 주제가 없습니다."
                );
                return;
            }

            // 전체 주제를 화면 순번과 함께 출력한다.
            resultView.printThemeCandidates(themeList);

            int selectedNumber =
                    resultView.inputListNumber(
                            scanner,
                            themeList.size(),
                            "조회할 주제 번호를 입력해주세요"
                    );

            if (selectedNumber == 0) {
                return;
            }

            QuoteDTO selectedTheme =
                    themeList.get(selectedNumber - 1);

            // 선택된 DTO의 실제 themeId로 명언을 조회한다.
            List<QuoteDTO> quoteList =
                    quoteService.selectQuotesByThemeId(
                            selectedTheme.getThemeId()
                    );

            if (quoteList.isEmpty()) {
                resultView.printMessage(
                        "등록된 명언이 없습니다."
                );
                return;
            }

            resultView.printQuotesByTheme(
                    selectedTheme,
                    quoteList
            );
            bookmarkQuote(scanner, member, quoteList);

        } catch (RuntimeException e) {
            e.printStackTrace();

            resultView.printMessage(
                    "주제별 명언 조회 중 오류가 발생했습니다."
            );
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

            if (personName == null) {
                return false;
            }

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

            if (selectedNumber == 0) {
                continue;
            }

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

    // 주제를 선택하고 선택·뒤로가기·데이터 없음 상태를 반환한다.
    private int selectThemeForRegistration(
            Scanner scanner,
            QuoteDTO quote
    ) {

        List<QuoteDTO> themeList =
                quoteService.selectThemesForQuoteRegistration();

        if (themeList.isEmpty()) {
            resultView.printMessage("등록된 주제가 없습니다.");
            return NO_DATA;
        }

        resultView.printThemeCandidates(themeList);

        int selectedNumber = resultView.inputListNumber(
                scanner,
                themeList.size(),
                "등록할 주제 번호를 입력해주세요"
        );

        if (selectedNumber == 0) {
            return BACK;
        }

        QuoteDTO selectedTheme =
                themeList.get(selectedNumber - 1);

        quote.setThemeId(selectedTheme.getThemeId());
        quote.setThemeName(selectedTheme.getThemeName());

        return SELECTED;
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

    // 인물과 명언을 선택하고 명언 내용만 수정하는 전체 흐름을 처리한다.
    public void updateQuote(Scanner scanner) {

        /*
         * UPDATE 결과가 0이거나 예외가 발생하면
         * 이 지점부터 다시 시작한다.
         */
        updateFlow:
        while (true) {
            QuoteDTO selectedPerson = selectPerson(scanner);

            if (selectedPerson == null) {
                return;
            }

            List<QuoteDTO> quoteList =
                    quoteService.selectQuotesByPersonIdForUpdate(
                            selectedPerson.getPersonId()
                    );

            /*
             * 선택한 인물에게 등록된 명언이 없으면
             * 인물 검색 단계로 돌아간다.
             */
            if (quoteList.isEmpty()) {
                resultView.printMessage("등록된 명언이 없습니다.");
                continue;
            }

            resultView.printQuotesForUpdate(quoteList);

            int selectedNumber = resultView.inputListNumber(
                    scanner,
                    quoteList.size(),
                    "수정할 명언 번호를 입력해주세요"
            );

            if (selectedNumber == 0) {
                continue updateFlow;
            }


            /*
             * 사용자가 입력한 값은 화면 순번이다.
             * 실제 UPDATE에는 DTO의 quoteId가 사용된다.
             */
            QuoteDTO selectedQuote =
                    quoteList.get(selectedNumber - 1);

            resultView.printCurrentQuoteForUpdate(selectedQuote);

            /*
             * 재수정을 선택하면 인물과 명언 선택을 유지한 채
             * 새 명언 내용만 다시 입력한다.
             */
            while (true) {
                String originalContent =
                        selectedQuote.getQuoteContent();

                String newContent =
                        resultView.inputQuoteContent(scanner);

                String decision =
                        resultView.inputUpdateDecision(scanner);

                if (decision.equals("N")) {
                    return;
                }

                if (decision.equals("재수정")) {
                    continue;
                }

                /*
                 * 같은 내용을 입력한 경우도 허용한다.
                 * 불필요한 UPDATE를 실행하지 않고 정상 수정으로 처리한다.
                 */
                if (originalContent.equals(newContent)) {
                    resultView.printMessage("수정되었습니다.");
                    return;
                }

                selectedQuote.setQuoteContent(newContent);

                try {
                    boolean success =
                            quoteService.updateQuoteContent(
                                    selectedQuote
                            );

                    if (success) {
                        resultView.printMessage("수정되었습니다.");
                        return;
                    }

                    /*
                     * UPDATE 결과가 0이면 수정 흐름의 처음으로 돌아가
                     * 인물부터 다시 선택한다.
                     */
                    resultView.printMessage(
                            "명언 수정에 실패했습니다. 다시 시도해주세요."
                    );

                    continue updateFlow;

                } catch (RuntimeException e) {
                    e.printStackTrace();

                    resultView.printMessage(
                            "명언 수정에 실패했습니다. 다시 시도해주세요."
                    );

                    continue updateFlow;
                }
            }
        }
    }


    // 이름으로 인물을 검색하고 화면 번호로 선택한 인물 DTO를 반환한다.
    private QuoteDTO selectPerson(Scanner scanner) {

        while (true) {
            String personName =
                    resultView.inputPersonName(scanner);

            if (personName == null) {
                return null;
            }

            List<QuoteDTO> personList =
                    quoteService.searchPersonsForQuoteRegistration(
                            personName
                    );

            if (personList.isEmpty()) {
                resultView.printMessage("조회된 인물이 없습니다.");
                continue;
            }

            resultView.printPersonCandidates(personList);

            int selectedNumber = resultView.inputListNumber(
                    scanner,
                    personList.size(),
                    "인물 번호를 입력해주세요"
            );

            if (selectedNumber == 0) {
                continue;
            }

            /*
             * 사용자는 화면 순번을 입력한다.
             * 실제 personId는 반환되는 DTO 안에 보관되어 있다.
             */
            return personList.get(selectedNumber - 1);
        }
    }



    // 검색한 명언을 선택하고 해당 명언의 주제만 수정한다.
    public void updateQuoteTheme(Scanner scanner) {

        searchFlow:
        while (true) {
            int searchType =
                    resultView.inputQuoteSearchType(scanner);

            if (searchType == 0) {
                return;
            }

            List<QuoteDTO> quoteList =
                    searchQuotesByCondition(
                            scanner,
                            searchType
                    );

            if (quoteList == null) {
                continue;
            }

            if (quoteList.isEmpty()) {
                resultView.printMessage(
                        "조회된 명언이 없습니다."
                );
                continue;
            }

            quoteSelection:
            while (true) {
                resultView.printQuoteSearchResults(quoteList);

                int selectedQuoteNumber =
                        resultView.inputListNumber(
                                scanner,
                                quoteList.size(),
                                "수정할 명언 번호를 입력해주세요"
                        );

                if (selectedQuoteNumber == 0) {
                    continue searchFlow;
                }

                QuoteDTO selectedQuote =
                        quoteList.get(selectedQuoteNumber - 1);

                resultView.printCurrentQuoteForUpdate(
                        selectedQuote
                );

                themeSelection:
                while (true) {
                    List<QuoteDTO> themeList =
                            quoteService
                                    .selectThemesForQuoteRegistration();

                    if (themeList.isEmpty()) {
                        resultView.printMessage(
                                "등록된 주제가 없습니다."
                        );
                        return;
                    }

                    resultView.printThemeCandidates(themeList);

                    int selectedThemeNumber =
                            resultView.inputListNumber(
                                    scanner,
                                    themeList.size(),
                                    "새로운 주제 번호를 입력해주세요"
                            );

                    if (selectedThemeNumber == 0) {
                        continue quoteSelection;
                    }

                    QuoteDTO selectedTheme =
                            themeList.get(
                                    selectedThemeNumber - 1
                            );

                    resultView.printThemeUpdateSummary(
                            selectedQuote,
                            selectedTheme
                    );

                    String decision =
                            resultView.inputUpdateDecision(scanner);

                    if (decision.equals("N")) {
                        return;
                    }

                    if (decision.equals("재수정")) {
                        continue themeSelection;
                    }

                    try {
                        boolean success =
                                quoteService.updateQuoteTheme(
                                        selectedQuote.getQuoteId(),
                                        selectedTheme.getThemeId()
                                );

                        if (success) {
                            resultView.printMessage(
                                    "수정되었습니다."
                            );
                            return;
                        }

                        resultView.printMessage(
                                "명언 주제 수정에 실패했습니다. " +
                                        "다시 시도해주세요."
                        );

                        continue searchFlow;

                    } catch (RuntimeException e) {
                        e.printStackTrace();

                        resultView.printMessage(
                                "명언 주제 수정 중 " +
                                        "오류가 발생했습니다."
                        );

                        return;
                    }
                }
            }
        }
    }

    // 검색 방식에 따라 주제 또는 인물을 먼저 선택한 뒤 명언을 조회한다.
    private List<QuoteDTO> searchQuotesByCondition(
            Scanner scanner,
            int searchType
    ) {

        switch (searchType) {
            case 1:
                QuoteDTO selectedTheme =
                        selectThemeFromAll(scanner);

                if (selectedTheme == null) {
                    return null;
                }

                return quoteService.selectQuotesByThemeId(
                        selectedTheme.getThemeId()
                );

            case 2:
                String keyword = resultView.inputSearchWord(
                        scanner,
                        "명언 내용"
                );

                if (keyword == null) {
                    return null;
                }

                return quoteService.searchQuotesByKeyword(keyword);

            case 3:
                QuoteDTO selectedPerson =
                        selectPersonFromAll(scanner);

                if (selectedPerson == null) {
                    return null;
                }

                return quoteService
                        .selectQuotesByPersonIdForUpdate(
                                selectedPerson.getPersonId()
                        );

            default:
                return null;
        }
    }

    // 검색 결과에서 명언을 선택하고 최종 확인 후 삭제한다.
    public void deleteQuote(Scanner scanner) {

        searchFlow:
        while (true) {
            int searchType =
                    resultView.inputQuoteSearchType(scanner);

            if (searchType == 0) {
                return;
            }

            List<QuoteDTO> quoteList =
                    searchQuotesByCondition(
                            scanner,
                            searchType
                    );

            if (quoteList == null) {
                continue;
            }

            if (quoteList.isEmpty()) {
                resultView.printMessage(
                        "조회된 명언이 없습니다."
                );
                continue;
            }

            resultView.printQuoteSearchResults(quoteList);

            int selectedNumber =
                    resultView.inputListNumber(
                            scanner,
                            quoteList.size(),
                            "삭제할 명언 번호를 입력해주세요"
                    );

            if (selectedNumber == 0) {
                continue;
            }

            QuoteDTO selectedQuote =
                    quoteList.get(selectedNumber - 1);

            resultView.printQuoteDeleteSummary(selectedQuote);

            String decision =
                    resultView.inputDeleteDecision(scanner);

            if (decision.equals("N")) {
                return;
            }

            try {
                boolean success =
                        quoteService.deleteQuote(
                                selectedQuote.getQuoteId()
                        );

                if (success) {
                    resultView.printMessage(
                            "명언이 삭제되었습니다."
                    );
                    return;
                }

                resultView.printMessage(
                        "명언 삭제에 실패했습니다. " +
                                "다시 시도해주세요."
                );

                continue searchFlow;

            } catch (RuntimeException e) {
                e.printStackTrace();

                resultView.printMessage(
                        "명언 삭제 중 오류가 발생했습니다."
                );

                return;
            }
        }
    }

    // 전체 주제 목록을 출력하고 화면 번호로 하나를 선택한다.
    private QuoteDTO selectThemeFromAll(Scanner scanner) {

        List<QuoteDTO> themeList =
                quoteService.selectThemesForQuoteRegistration();

        if (themeList.isEmpty()) {
            resultView.printMessage("등록된 주제가 없습니다.");
            return null;
        }

        resultView.printThemeCandidates(themeList);

        int selectedNumber = resultView.inputListNumber(
                scanner,
                themeList.size(),
                "주제 번호를 입력해주세요"
        );

        if (selectedNumber == 0) {
            return null;
        }

        return themeList.get(selectedNumber - 1);
    }

    // 전체 인물 목록을 출력하고 화면 번호로 하나를 선택한다.
    private QuoteDTO selectPersonFromAll(Scanner scanner) {

        List<QuoteDTO> personList =
                quoteService.searchPersonsForQuoteRegistration("");

        if (personList.isEmpty()) {
            resultView.printMessage("등록된 인물이 없습니다.");
            return null;
        }

        resultView.printPersonCandidates(personList);

        int selectedNumber = resultView.inputListNumber(
                scanner,
                personList.size(),
                "인물 번호를 입력해주세요"
        );

        if (selectedNumber == 0) {
            return null;
        }

        return personList.get(selectedNumber - 1);
    }
}
