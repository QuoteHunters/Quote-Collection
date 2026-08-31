package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.MemberDTO;
import com.quotehunters.quotecollection.model.dto.PersonDTO;
import com.quotehunters.quotecollection.model.dto.QuoteDTO;
import com.quotehunters.quotecollection.model.service.QuoteService;
import com.quotehunters.quotecollection.view.BookmarkView;
import com.quotehunters.quotecollection.view.QuoteView;
import com.quotehunters.quotecollection.view.ResultView;

import java.util.List;
import java.util.Scanner;

public class QuoteController {

    private QuoteService quoteService = new QuoteService();
    private QuoteView resultView = new QuoteView();
    private BookmarkView bookmarkView = new BookmarkView();
    private ResultView messageView = new ResultView();

    private static final int NO_DATA = -1;
    private static final int BACK = 0;
    private static final int SELECTED = 1;

    private void bookmarkQuote(Scanner scanner, MemberDTO member, List<QuoteDTO> quoteList) {
        while (true) {
            QuoteDTO quote = bookmarkView.selectQuoteForFavorite(scanner, quoteList);

            if (quote == null) {
                return;
            }

            bookmarkView.showQuoteDetailForFavorite(scanner, member, quote);
        }
    }

    private boolean canUseBookmark(MemberDTO member) {
        return member != null && member.getUserAuth() == 1;
    }

    private void browseQuoteDetails(
            Scanner scanner,
            List<QuoteDTO> quoteList
    ) {

        while (true) {
            QuoteDTO quote = bookmarkView.selectQuoteForFavorite(
                    scanner,
                    quoteList
            );

            if (quote == null) {
                return;
            }

            resultView.printQuoteDetail(quote);
            resultView.inputQuoteDetailBack(scanner);
        }
    }

    // [Quote-003] 전체 명언을 조회하고 사용자는 상세에서 즐겨찾기를 선택할 수 있다.
    public void selectAllQuotes(Scanner scanner, MemberDTO member) {

        try {
            List<QuoteDTO> quoteList = quoteService.selectAllQuotes();

            if (quoteList.isEmpty()) {
                messageView.errorMessage("등록된 명언이 없습니다.");
            } else if (canUseBookmark(member)) {
                bookmarkQuote(scanner, member, quoteList);
            } else {
                resultView.printQuoteList(quoteList);
            }

        } catch (RuntimeException e) {
            messageView.errorMessage("전체 명언 조회 중 오류가 발생했습니다.");
        }
    }

    // [Quote-004] 현재 날짜를 기준으로 오늘의 명언 한 건을 조회한다.
    public void selectTodayQuote(Scanner scanner, MemberDTO member) {

        try {
            QuoteDTO quote = quoteService.selectTodayQuote();

            if (quote == null) {
                messageView.errorMessage("등록된 명언이 없습니다.");
            } else if (canUseBookmark(member)) {
                bookmarkView.showQuoteDetailForFavorite(scanner, member, quote);
            } else {
                resultView.printTodayQuote(quote);
            }

        } catch (RuntimeException e) {
            messageView.errorMessage("오늘의 명언 조회 중 오류가 발생했습니다.");
        }
    }

    // [Quote-002] 사용자는 검색된 명언의 상세로 이동할 수 있다.
    // 키워드 검색 결과의 존재 여부에 따라 목록 또는 안내 메시지를 출력한다.
    public void searchQuotesByKeyword(Scanner scanner, MemberDTO member, String keyword) {

        String searchKeyword = keyword == null ? "" : keyword.trim();

        if (searchKeyword.isEmpty()) {
            messageView.errorMessage("검색할 명언 키워드를 입력해주세요.");
            return;
        }

        try {
            List<QuoteDTO> quoteList =
                    quoteService.searchQuotesByKeyword(searchKeyword);

            if (quoteList.isEmpty()) {
                messageView.errorMessage(
                        "'" + searchKeyword + "'(으)로 조회된 명언이 없습니다."
                );
            } else if (canUseBookmark(member)) {
                bookmarkQuote(scanner, member, quoteList);
            } else {
                resultView.printSearchedQuoteList(quoteList);
            }

        } catch (RuntimeException e) {
            messageView.errorMessage("명언 검색 중 오류가 발생했습니다.");
        }
    }

    // [Quote-005, Quote-006]
    // 명언 보유 인물을 선택한 뒤 해당 인물의 명언 목록과 상세를 조회한다.
    public void searchQuotesByPerson(Scanner scanner, MemberDTO member) {

        while (true) {
            List<QuoteDTO> personList;

            try {
                personList = quoteService.selectPersonsWithQuotes();
            } catch (RuntimeException e) {
                messageView.errorMessage("명언 보유 인물 목록 조회 중 오류가 발생했습니다.");

                if (resultView.inputBrowseRetry(scanner)) {
                    continue;
                }

                return;
            }

            if (personList.isEmpty()) {
                messageView.errorMessage("명언을 보유한 인물이 없습니다.");
                return;
            }

            resultView.printPersonsWithQuotes(personList);

            int selectedNumber = resultView.inputListNumber(
                    scanner,
                    personList.size(),
                    "인물 번호 선택"
            );

            if (selectedNumber == 0) {
                return;
            }

            QuoteDTO selectedPerson = personList.get(selectedNumber - 1);

            searchQuotesByPersonId(
                    scanner,
                    member,
                    selectedPerson.getPersonId()
            );
        }
    }

    // 인물 탐색에서 이미 선택한 인물의 명언을 이어서 조회한다.
    public void searchQuotesByPerson(
            Scanner scanner,
            MemberDTO member,
            PersonDTO selectedPerson
    ) {

        if (selectedPerson == null) {
            return;
        }

        searchQuotesByPersonId(
                scanner,
                member,
                selectedPerson.getPersonId()
        );
    }

    private void searchQuotesByPersonId(
            Scanner scanner,
            MemberDTO member,
            int personId
    ) {

        while (true) {
            try {
                List<QuoteDTO> quoteList =
                        quoteService.selectQuotesByPersonIdForUpdate(
                                personId
                        );

                if (quoteList.isEmpty()) {
                    messageView.errorMessage("선택한 인물의 명언이 없습니다.");
                } else if (canUseBookmark(member)) {
                    bookmarkQuote(scanner, member, quoteList);
                } else {
                    browseQuoteDetails(scanner, quoteList);
                }

                return;

            } catch (RuntimeException e) {
                messageView.errorMessage("인물별 명언 목록 조회 중 오류가 발생했습니다.");

                if (!resultView.inputBrowseRetry(scanner)) {
                    return;
                }
            }
        }
    }

    // [Quote-001]
    // 명언 등록 전체 흐름을 진행한다.
    public boolean registerQuote(Scanner scanner) {

        QuoteDTO quote = new QuoteDTO();

        registrationInput:
        while (true) {

            if (!selectPersonForRegistration(scanner, quote)) {
                return false;
            }

            while (true) {
                int themeResult =
                        selectThemeForRegistration(scanner, quote);

                if (themeResult == NO_DATA) {
                    return false;
                }

                if (themeResult == BACK) {
                    continue registrationInput;
                }

                String quoteContent =
                        resultView.inputQuoteContent(scanner);

                if (quoteContent == null) {
                    continue;
                }

                quote.setQuoteContent(quoteContent);
                break registrationInput;
            }
        }

        while (true) {
            resultView.printQuoteRegistrationSummary(quote);
            String decision = resultView.inputRegistrationDecision(scanner);

            if ("취소".equals(decision)) {
                resultView.printMessage("명언 등록을 취소했습니다.");
                return true;
            }

            if ("수정".equals(decision)) {
                modifyQuoteRegistration(scanner, quote);
                continue;
            }

            if (executeQuoteInsert(quote)) {
                return false;
            }
        }
    }

    // [Quote-008]
    // 전체 주제에서 하나를 선택하고 해당 주제의 명언을 조회한다.
    public void selectQuotesByTheme(Scanner scanner, MemberDTO member) {

        try {
            List<QuoteDTO> themeList =
                    quoteService.selectThemesForQuoteRegistration();

            if (themeList.isEmpty()) {
                messageView.errorMessage(
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
                            "조회할 주제 번호 입력"
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
                messageView.errorMessage(
                        "선택한 주제의 명언이 없습니다."
                );
                return;
            }

            if (canUseBookmark(member)) {
                bookmarkQuote(scanner, member, quoteList);
            } else {
                resultView.printQuotesByTheme(
                        selectedTheme,
                        quoteList
                );
            }

        } catch (RuntimeException e) {
            messageView.errorMessage(
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

            List<QuoteDTO> personList;

            try {
                personList = quoteService.searchPersonsForQuoteRegistration(personName);
            } catch (RuntimeException e) {
                messageView.errorMessage("인물 검색 중 오류가 발생했습니다.");
                continue;
            }

            if (personList.isEmpty()) {
                messageView.errorMessage("조회된 인물이 없습니다.");
                continue;
            }

            resultView.printPersonCandidates(personList);

            int selectedNumber = resultView.inputListNumber(
                    scanner,
                    personList.size(),
                    "등록할 인물 번호 입력"
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

        while (true) {
            List<QuoteDTO> themeList;

            try {
                themeList = quoteService.selectThemesForQuoteRegistration();
            } catch (RuntimeException e) {
                messageView.errorMessage("주제 목록 조회 중 오류가 발생했습니다.");

                if (resultView.inputThemeListRetry(scanner)) {
                    continue;
                }

                return BACK;
            }

            if (themeList.isEmpty()) {
                messageView.errorMessage("등록된 주제가 없습니다.");
                return NO_DATA;
            }

            resultView.printThemeCandidates(themeList);

            int selectedNumber = resultView.inputListNumber(
                    scanner,
                    themeList.size(),
                    "등록할 주제 번호 입력"
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
                String quoteContent = resultView.inputQuoteContent(scanner);

                if (quoteContent != null) {
                    quote.setQuoteContent(quoteContent);
                }
                break;

            default:
                // inputModificationTarget에서 검증하므로 도달하지 않는다.
                break;
        }
    }

    // 명언 INSERT를 실행하고 성공 또는 실패 메시지를 출력한다.
    private boolean executeQuoteInsert(QuoteDTO quote) {

        try {
            boolean success = quoteService.insertQuote(quote);

            if (success) {
                messageView.successMessage("명언이 등록되었습니다.");
                return true;
            } else {
                messageView.errorMessage(
                        "명언 등록에 실패했습니다. 다시 시도해주세요."
                );
            }

        } catch (RuntimeException e) {
            messageView.errorMessage(
                    "명언 등록에 실패했습니다. 다시 시도해주세요."
            );
        }

        return false;
    }

    // [Quote-006]
    // 인물과 명언을 선택하고 명언 내용만 수정하는 전체 흐름을 처리한다.
    public boolean updateQuote(Scanner scanner) {

        /*
         * 인물 또는 명언 선택을 다시 시작해야 하면
         * 이 지점부터 다시 시작한다.
         */
        updateFlow:
        while (true) {
            QuoteDTO selectedPerson = selectPerson(scanner);

            if (selectedPerson == null) {
                return false;
            }

            List<QuoteDTO> quoteList;

            while (true) {
                try {
                    quoteList = quoteService.selectQuotesByPersonIdForUpdate(
                            selectedPerson.getPersonId()
                    );
                    break;
                } catch (RuntimeException e) {
                    messageView.errorMessage("명언 목록 조회 중 오류가 발생했습니다.");

                    if (resultView.inputDatabaseRetry(scanner, "명언 목록")) {
                        continue;
                    }

                    return true;
                }
            }

            /*
             * 선택한 인물에게 등록된 명언이 없으면
             * 인물 검색 단계로 돌아간다.
             */
            if (quoteList.isEmpty()) {
                messageView.errorMessage("선택한 인물의 명언이 없습니다.");
                continue;
            }

            quoteSelection:
            while (true) {
                resultView.printQuotesForUpdate(quoteList);

                int selectedNumber = resultView.inputListNumber(
                        scanner,
                        quoteList.size(),
                        "수정할 명언 번호 입력"
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

                /*
                 * 재수정을 선택하면 인물과 명언 선택을 유지한 채
                 * 새 명언 내용만 다시 입력한다.
                 */
                while (true) {
                    resultView.printCurrentQuoteForUpdate(selectedQuote);

                    String originalContent = selectedQuote.getQuoteContent();
                    String newContent = resultView.inputQuoteContent(scanner);

                    if (newContent == null) {
                        continue quoteSelection;
                    }

                    resultView.printQuoteContentUpdateSummary(
                            selectedQuote,
                            newContent
                    );

                    String decision = resultView.inputUpdateDecision(scanner);

                    if (decision.equals("취소")) {
                        return true;
                    }

                    if (decision.equals("재수정")) {
                        continue;
                    }

                    /*
                     * 같은 내용을 입력한 경우도 허용한다.
                     * 불필요한 UPDATE를 실행하지 않고 정상 수정으로 처리한다.
                     */
                    if (originalContent.equals(newContent)) {
                        messageView.successMessage("명언이 수정되었습니다.");
                        return false;
                    }

                    selectedQuote.setQuoteContent(newContent);

                    /*
                     * UPDATE 결과가 0이거나 예외가 발생하면
                     * 기존 내용을 복원하고 같은 명언을 다시 수정한다.
                     */
                    try {
                        boolean success = quoteService.updateQuoteContent(selectedQuote);

                        if (success) {
                            messageView.successMessage("명언이 수정되었습니다.");
                            return false;
                        }

                        messageView.errorMessage(
                                "명언 수정에 실패했습니다. 다시 시도해주세요."
                        );

                    } catch (RuntimeException e) {
                        messageView.errorMessage(
                                "명언 수정 중 오류가 발생했습니다."
                        );
                    }

                    selectedQuote.setQuoteContent(originalContent);
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

            List<QuoteDTO> personList;

            try {
                personList = quoteService.searchPersonsForQuoteRegistration(
                        personName
                );
            } catch (RuntimeException e) {
                messageView.errorMessage("인물 검색 중 오류가 발생했습니다.");
                continue;
            }

            if (personList.isEmpty()) {
                messageView.errorMessage("조회된 인물이 없습니다.");
                continue;
            }

            resultView.printPersonCandidates(personList);

            int selectedNumber = resultView.inputListNumber(
                    scanner,
                    personList.size(),
                    "인물 번호 입력"
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



    // [QuoteCategory-001]
    // 검색한 명언을 선택하고 해당 명언의 주제만 수정한다.
    public boolean updateQuoteTheme(Scanner scanner) {

        searchFlow:
        while (true) {
            int searchType =
                    resultView.inputQuoteSearchType(scanner);

            if (searchType == 0) {
                return false;
            }

            List<QuoteDTO> quoteList;

            try {
                quoteList = searchQuotesByCondition(
                            scanner,
                            searchType
                    );
            } catch (RuntimeException e) {
                messageView.errorMessage("명언 검색 중 오류가 발생했습니다.");
                continue;
            }

            if (quoteList == null) {
                continue;
            }

            if (quoteList.isEmpty()) {
                messageView.errorMessage(
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
                                "수정할 명언 번호 입력"
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
                    List<QuoteDTO> themeList;

                    try {
                        themeList = quoteService.selectThemesForQuoteRegistration();
                    } catch (RuntimeException e) {
                        messageView.errorMessage("주제 목록 조회 중 오류가 발생했습니다.");

                        if (resultView.inputDatabaseRetry(scanner, "주제 목록")) {
                            continue themeSelection;
                        }

                        return true;
                    }

                    if (themeList.isEmpty()) {
                        messageView.errorMessage(
                                "등록된 주제가 없습니다."
                        );
                        return false;
                    }

                    resultView.printThemeCandidates(themeList);

                    int selectedThemeNumber =
                            resultView.inputListNumber(
                                    scanner,
                                    themeList.size(),
                                    "새 주제 번호 입력"
                            );

                    if (selectedThemeNumber == 0) {
                        continue quoteSelection;
                    }

                    QuoteDTO selectedTheme =
                            themeList.get(
                                    selectedThemeNumber - 1
                            );

                    if (selectedQuote.getThemeId() == selectedTheme.getThemeId()) {
                        messageView.errorMessage(
                                "현재 주제와 다른 주제를 선택해주세요."
                        );
                        continue;
                    }

                    while (true) {
                        resultView.printThemeUpdateSummary(
                                selectedQuote,
                                selectedTheme
                        );

                        String decision = resultView.inputUpdateDecision(scanner);

                        if (decision.equals("취소")) {
                            return true;
                        }

                        if (decision.equals("재수정")) {
                            continue themeSelection;
                        }

                        try {
                            boolean success = quoteService.updateQuoteTheme(
                                    selectedQuote.getQuoteId(),
                                    selectedTheme.getThemeId()
                            );

                            if (success) {
                                messageView.successMessage(
                                        "명언 주제가 수정되었습니다."
                                );
                                return false;
                            }

                            messageView.errorMessage(
                                    "명언 주제 수정에 실패했습니다. 다시 시도해주세요."
                            );

                        } catch (RuntimeException e) {
                            messageView.errorMessage(
                                    "명언 주제 수정 중 오류가 발생했습니다."
                            );
                        }
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
                        selectPerson(scanner);

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

    // [Quote-007]
    // 검색 결과에서 명언을 선택하고 최종 확인 후 삭제한다.
    public boolean deleteQuote(Scanner scanner) {

        searchFlow:
        while (true) {
            int searchType =
                    resultView.inputQuoteSearchType(scanner);

            if (searchType == 0) {
                return false;
            }

            List<QuoteDTO> quoteList;

            try {
                quoteList = searchQuotesByCondition(
                            scanner,
                            searchType
                    );
            } catch (RuntimeException e) {
                messageView.errorMessage("명언 검색 중 오류가 발생했습니다.");
                continue;
            }

            if (quoteList == null) {
                continue;
            }

            if (quoteList.isEmpty()) {
                messageView.errorMessage(
                        "조회된 명언이 없습니다."
                );
                continue;
            }

            resultView.printQuoteSearchResults(quoteList);

            int selectedNumber =
                    resultView.inputListNumber(
                            scanner,
                            quoteList.size(),
                            "삭제할 명언 번호 입력"
                    );

            if (selectedNumber == 0) {
                continue;
            }

            QuoteDTO selectedQuote =
                    quoteList.get(selectedNumber - 1);

            while (true) {
                resultView.printQuoteDeleteSummary(selectedQuote);

                String decision = resultView.inputDeleteDecision(scanner);

                if (decision.equals("아니오")) {
                    return true;
                }

                try {
                    boolean success = quoteService.deleteQuote(
                            selectedQuote.getQuoteId()
                    );

                    if (success) {
                        messageView.successMessage(
                                "명언이 삭제되었습니다."
                        );
                        return false;
                    }

                    messageView.errorMessage(
                            "명언 삭제에 실패했습니다. 다시 시도해주세요."
                    );

                } catch (RuntimeException e) {
                    messageView.errorMessage(
                            "명언 삭제 중 오류가 발생했습니다."
                    );
                }
            }
        }
    }

    // 전체 주제 목록을 출력하고 화면 번호로 하나를 선택한다.
    private QuoteDTO selectThemeFromAll(Scanner scanner) {

        List<QuoteDTO> themeList =
                quoteService.selectThemesForQuoteRegistration();

        if (themeList.isEmpty()) {
            messageView.errorMessage("등록된 주제가 없습니다.");
            return null;
        }

        resultView.printThemeCandidates(themeList);

        int selectedNumber = resultView.inputListNumber(
                scanner,
                themeList.size(),
                "주제 번호 입력"
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
            messageView.errorMessage("등록된 인물이 없습니다.");
            return null;
        }

        resultView.printPersonCandidates(personList);

        int selectedNumber = resultView.inputListNumber(
                scanner,
                personList.size(),
                "인물 번호 입력"
        );

        if (selectedNumber == 0) {
            return null;
        }

        return personList.get(selectedNumber - 1);
    }
}
