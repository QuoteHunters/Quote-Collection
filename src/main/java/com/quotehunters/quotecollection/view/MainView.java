package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.PersonController;
import com.quotehunters.quotecollection.controller.QuoteController;
import com.quotehunters.quotecollection.model.dto.MemberDTO;
import com.quotehunters.quotecollection.model.dto.PersonDTO;

import java.util.Scanner;

public class MainView {
    private static final String HEADER = "=".repeat(10);
    private static final String LINE = "-".repeat(30);

    private final ResultView resultView = new ResultView();
    private final MemberView memberView = new MemberView();
    private final BookmarkView bookmarkView = new BookmarkView();
    private final CountryView countryView = new CountryView();
    private final PeriodView periodView = new PeriodView();
    private final FieldView fieldView = new FieldView();
    private final ThemeView themeView = new ThemeView();

    private final QuoteController quoteController = new QuoteController();
    private final PersonController personController = new PersonController();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        ScannerView scannerView = new ScannerView();
        MemberDTO member = null;

        printHeader("명언 도감 시스템");
        while (true) {
            System.out.println();
            printHeader("회원 관리");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 프로그램 종료");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: {
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    return;
                }
                case 1: {
                    member = memberView.login(scanner);
                    if (member == null) break;
                    switch (member.getUserAuth()) {
                        case 0: {
                            adminMainView(scannerView, scanner, member, resultView);
                            break;
                        }
                        case 1: {
                            userMainMenu(scannerView, scanner, member);
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    memberView.signUp(scanner);
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private void userMainMenu(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            printHeader("사용자 메인");
            System.out.println("1. 명언 탐색");
            System.out.println("2. 인물 탐색");
            System.out.println("3. My Page");
            System.out.println("0. 로그아웃");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: {
                    System.out.println("로그아웃 되었습니다.");
                    return;
                }
                case 1: {
                    quoteSearchView(scannerView, scanner, member);
                    break;
                }
                case 2: {
                    personSearchView(scannerView, scanner, member);
                    break;
                }
                case 3: {
                    if (myPageView(scannerView, scanner, member)) return;
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private void quoteSearchView(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            printHeader("명언 탐색");
            System.out.println("1. 오늘의 명언");
            System.out.println("2. 전체 명언 조회");
            System.out.println("3. 주제별 명언 조회");
            System.out.println("4. 인물별 명언 조회");
            System.out.println("5. 명언 키워드로 명언 조회");
            System.out.println("0. 뒤로가기");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    quoteController.selectTodayQuote(scanner, member);
                    break;
                }
                case 2: {
                    quoteController.selectAllQuotes(scanner, member);
                    break;
                }
                case 3: {
                    quoteController.selectQuotesByTheme(scanner, member);
                    break;
                }
                case 4: {
                    quoteController.searchQuotesByPerson(scanner, member);
                    break;
                }
                case 5: {
                    String keyword = scannerView.scannString(scanner, "키워드 입력 (0: 뒤로가기)");
                    if (keyword.equals("0")) break;
                    quoteController.searchQuotesByKeyword(scanner, member, keyword);
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private void personSearchView(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            printHeader("인물 탐색");
            System.out.println("1. 전체 인물 조회");
            System.out.println("2. 국가별 인물 조회");
            System.out.println("3. 시대별 인물 조회");
            System.out.println("4. 분야별 인물 조회");
            System.out.println("5. 인물 이름 검색");
            System.out.println("6. 명언 키워드로 인물 검색");
            System.out.println("0. 뒤로가기");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    openPersonQuotes(1, scannerView, scanner, member);
                    break;
                }
                case 2: {
                    openPersonQuotes(2, scannerView, scanner, member);
                    break;
                }
                case 3: {
                    openPersonQuotes(3, scannerView, scanner, member);
                    break;
                }
                case 4: {
                    openPersonQuotes(4, scannerView, scanner, member);
                    break;
                }
                case 5: {
                    openPersonQuotes(5, scannerView, scanner, member);
                    break;
                }
                case 6: {
                    openPersonQuotes(6, scannerView, scanner, member);
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private boolean myPageView(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            printHeader("My Page");
            System.out.println("1. 내 즐겨찾기");
            System.out.println("2. 비밀번호 변경");
            System.out.println("3. 회원탈퇴");
            System.out.println("0. 뒤로가기");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return false;
                case 1: {
                    bookmarkView.showFavoriteList(scanner, member);
                    break;
                }
                case 2: {
                    memberView.changePassword(scanner, member);
                    break;
                }
                case 3: {
                    if (memberView.withdraw(scanner, member)) return true;
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private void adminMainView(ScannerView scannerView, Scanner scanner, MemberDTO member, ResultView resultView) {
        while (true) {
            System.out.println();
            printHeader("관리자 메인");
            System.out.println("1. 탐색");
            System.out.println("2. 등록");
            System.out.println("3. 수정");
            System.out.println("4. 삭제");
            System.out.println("0. 로그아웃");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    searchMainView(scannerView, scanner, member);
                    break;
                }
                case 2: {
                    insertMainView(scannerView, scanner, resultView);
                    break;
                }
                case 3: {
                    updateMainView(scannerView, scanner);
                    break;
                }
                case 4: {
                    deleteMainView(scannerView, scanner);
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private void searchMainView(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            printHeader("탐색");
            System.out.println("1. 인물 탐색");
            System.out.println("2. 명언 탐색");
            System.out.println("3. 카테고리 탐색");
            System.out.println("0. 뒤로가기");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    personSearchView(scannerView, scanner, member);
                    break;
                }
                case 2: {
                    quoteSearchView(scannerView, scanner, member);
                    break;
                }
                case 3: {
                    categorySearchView(scannerView, scanner);
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private void categorySearchView(ScannerView scannerView, Scanner scanner) {
        while (true) {
            System.out.println();
            printHeader("카테고리 탐색");
            System.out.println("1. 국가 조회");
            System.out.println("2. 시대 조회");
            System.out.println("3. 분야 조회");
            System.out.println("4. 주제 조회");
            System.out.println("0. 뒤로가기");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    try {
                        countryView.allCountries();
                    } catch (RuntimeException e) {
                        resultView.errorMessage("국가 조회 중 오류가 발생했습니다.");
                    }
                    break;
                }
                case 2: {
                    periodView.allPeriods();
                    break;
                }
                case 3: {
                    try {
                        fieldView.allFields();
                    } catch (RuntimeException e) {
                        resultView.errorMessage("분야 조회 중 오류가 발생했습니다.");
                    }
                    break;
                }
                case 4: {
                    try {
                        themeView.selectThemes();
                    } catch (RuntimeException e) {
                        resultView.errorMessage("주제 조회 중 오류가 발생했습니다.");
                    }
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private void insertMainView(ScannerView scannerView, Scanner scanner, ResultView resultView) {
        while (true) {
            System.out.println();
            printHeader("등록");
            System.out.println("1. 인물 등록");
            System.out.println("2. 명언 등록");
            System.out.println("3. 국가 등록");
            System.out.println("4. 시대 등록");
            System.out.println("5. 분야 등록");
            System.out.println("6. 주제 등록");
            System.out.println("0. 뒤로가기");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    if (personController.registerPerson(scannerView, scanner, resultView)) return;
                    break;
                }
                case 2: {
                    if (quoteController.registerQuote(scanner)) return;
                    break;
                }
                case 3: {
                    try {
                        if (countryView.registCountry(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("국가 등록 중 오류가 발생했습니다.");
                    }
                    break;
                }
                case 4: {
                    if (periodView.registPeriod(scannerView, scanner)) return;
                    break;
                }
                case 5: {
                    try {
                        if (fieldView.insertField(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("분야 등록 중 오류가 발생했습니다.");
                    }
                    break;
                }
                case 6: {
                    try {
                        if (themeView.insertTheme(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("주제 등록 중 오류가 발생했습니다.");
                    }
                    break;
                }
                default: {
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    private void updateMainView(ScannerView scannerView, Scanner scanner) {
        while (true) {
            System.out.println();
            printHeader("수정");
            System.out.println("1. 인물 수정");
            System.out.println("2. 명언 문장 수정");
            System.out.println("3. 명언 주제 수정");
            System.out.println("4. 국가 수정");
            System.out.println("5. 시대 수정");
            System.out.println("6. 분야 수정");
            System.out.println("7. 주제 수정");
            System.out.println("0. 뒤로가기");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: if (personController.updatePerson(scanner)) return; break;
                case 2: if (quoteController.updateQuote(scanner)) return; break;
                case 3: if (quoteController.updateQuoteTheme(scanner)) return; break;
                case 4:
                    try {
                        if (countryView.modifyCountry(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("국가 수정 중 오류가 발생했습니다.");
                    }
                    break;
                case 5: if (periodView.modifyPeriod(scannerView, scanner)) return; break;
                case 6:
                    try {
                        if (fieldView.updateField(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("분야 수정 중 오류가 발생했습니다.");
                    }
                    break;
                case 7:
                    try {
                        if (themeView.updateTheme(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("주제 수정 중 오류가 발생했습니다.");
                    }
                    break;
                default: resultView.errorMessage("메뉴에 있는 번호를 선택해주세요."); break;
            }
        }
    }

    private void openPersonQuotes(
            int searchType,
            ScannerView scannerView,
            Scanner scanner,
            MemberDTO member
    ) {
        PersonDTO selectedPerson = personController.selectPersonForQuoteBrowse(searchType, scannerView, scanner);

        if (selectedPerson != null) {
            quoteController.searchQuotesByPerson(scanner, member, selectedPerson);
        }
    }

    private void deleteMainView(ScannerView scannerView, Scanner scanner) {
        while (true) {
            System.out.println();
            printHeader("삭제");
            System.out.println("1. 인물 삭제");
            System.out.println("2. 명언 삭제");
            System.out.println("3. 국가 삭제");
            System.out.println("4. 시대 삭제");
            System.out.println("5. 분야 삭제");
            System.out.println("6. 주제 삭제");
            System.out.println("0. 뒤로가기");
            System.out.println(LINE);

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: if (personController.deletePerson(scanner)) return; break;
                case 2: if (quoteController.deleteQuote(scanner)) return; break;
                case 3:
                    try {
                        if (countryView.removeCountry(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("국가 삭제 중 오류가 발생했습니다.");
                    }
                    break;
                case 4: if (periodView.removePeriod(scannerView, scanner)) return; break;
                case 5:
                    try {
                        if (fieldView.deleteField(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("분야 삭제 중 오류가 발생했습니다.");
                    }
                    break;
                case 6:
                    try {
                        if (themeView.deleteTheme(scannerView, scanner)) return;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("주제 삭제 중 오류가 발생했습니다.");
                    }
                    break;
                default: resultView.errorMessage("메뉴에 있는 번호를 선택해주세요."); break;
            }
        }
    }

    private void printHeader(String title) {
        System.out.println(HEADER + " " + title + " " + HEADER);
    }
}
