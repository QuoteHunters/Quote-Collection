package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.PersonController;
import com.quotehunters.quotecollection.controller.QuoteController;
import com.quotehunters.quotecollection.model.dto.MemberDTO;

import java.util.Scanner;

public class MainView {
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

        System.out.println("Quote Collection - 명언 도감 시스템");
        while (true) {
            System.out.println();
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 프로그램 종료");

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
                            adminMainView(scannerView, scanner, member);
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
                    System.out.println("잘못 입력하셨습니다.");
                    break;
                }
            }
        }
    }

    private void userMainMenu(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("1. 오늘의 명언");
            System.out.println("2. 명언 탐색");
            System.out.println("3. 인물 탐색");
            System.out.println("4. My Page");
            System.out.println("0. 로그아웃");

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: {
                    System.out.println("로그아웃 되었습니다.");
                    return;
                }
                case 1: {
                    quoteController.selectTodayQuote();
                    break;
                }
                case 2: {
                    quoteSearchView(scannerView, scanner, member);
                    break;
                }
                case 3: {
                    personSearchView(scannerView, scanner);
                    break;
                }
                case 4: {
                    myPageView(scannerView, scanner, member);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void quoteSearchView(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            System.out.println("========== 명언 탐색 ==========");
            System.out.println("1. 전체 명언 조회");
            System.out.println("2. 주제별 명언 조회");
            System.out.println("3. 인물별 명언 조회");
            System.out.println("4. 명언 키워드로 명언 조회");
            System.out.println("0. 뒤로가기");
            System.out.println("===============================");

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    quoteController.selectAllQuotes(scanner, member);
                    break;
                }
                case 2: {
                    quoteController.selectQuotesByTheme(scanner, member);
                    break;
                }
                case 3: {
                    quoteController.searchQuotesByPerson(scanner, member);
                    break;
                }
                case 4: {
                    String keyword = scannerView.scannString(scanner, "키워드 입력 (0: 뒤로가기)");
                    if (keyword.equals("0")) break;
                    quoteController.searchQuotesByKeyword(scanner, member, keyword);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void personSearchView(ScannerView scannerView, Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("========== 인물 탐색 ==========");
            System.out.println("1. 전체 인물 조회");
            System.out.println("2. 국가별 명언 조회");
            System.out.println("3. 시대별 명언 조회");
            System.out.println("4. 분야별 명언 조회");
            System.out.println("0. 뒤로가기");
            System.out.println("===============================");

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    personController.selectAllPerson();
                    break;
                }
                case 2: {
                    personController.selectPersonByCountry(scannerView, scanner);
                    break;
                }
                case 3: {
                    personController.selectPersonByPeriod(scannerView, scanner);
                    break;
                }
                case 4: {
                    personController.selectPersonByField(scannerView, scanner);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void myPageView(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            System.out.println("========== My Page ==========");
            System.out.println("1. 내 즐겨찾기");
            System.out.println("2. 비밀번호 변경");
            System.out.println("0. 뒤로가기");
            System.out.println("===============================");

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    bookmarkView.showFavoriteList(scanner, member);
                    break;
                }
                case 2: {
                    memberView.changePassword(scanner, member);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void adminMainView(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("1. 탐색");
            System.out.println("2. 등록");
            System.out.println("3. 수정");
            System.out.println("4. 삭제");
            System.out.println("0. 로그아웃");

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    searchMainView(scannerView, scanner, member);
                    break;
                }
                case 2: {

                    break;
                }
                case 3: {

                    break;
                }
                case 4: {

                    break;
                }
                default: {

                    break;
                }
            }
        }
    }

    private void searchMainView(ScannerView scannerView, Scanner scanner, MemberDTO member) {
        while (true) {
            System.out.println("========== 탐색 ==========");
            System.out.println("1. 인물 탐색");
            System.out.println("2. 명언 탐색");
            System.out.println("3. 카테고리 탐색");
            System.out.println("0. 뒤로가기");

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    personController.selectAllPerson();
                    break;
                }
                case 2: {
                    quoteController.selectAllQuotes(scanner, member);
                    break;
                }
                case 3: {
                    categorySearchView(scannerView, scanner);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void categorySearchView(ScannerView scannerView, Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("========== 카테고리 탐색 ==========");
            System.out.println("1. 국가 조회");
            System.out.println("2. 시대 조회");
            System.out.println("3. 분야 조회");
            System.out.println("4. 주제 조회");
            System.out.println("0. 뒤로가기");

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {
                    countryView.allCountries();
                    break;
                }
                case 2: {
                    periodView.allPeriods();
                    break;
                }
                case 3: {
                    fieldView.allFields();
                    break;
                }
                case 4: {
                    themeView.selectThemes();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void insertMainView(ScannerView scannerView, Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("========== 등록 ==========");
            System.out.println("1. 인물 등록");
            System.out.println("2. 명언 등록");
            System.out.println("3. 국가 등록");
            System.out.println("4. 시대 등록");
            System.out.println("5. 분야 등록");
            System.out.println("6. 주제 등록");
            System.out.println("0. 뒤로가기");

            switch (scannerView.scannInt(scanner, "선택")) {
                case 0: return;
                case 1: {

                    break;
                }
                case 2: {
                    break;
                }
                case 3: {
                    break;
                }
                case 4: {
                    break;
                }
                case 5: {
                    break;
                }
                case 6: {
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
