package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.PersonController;
import com.quotehunters.quotecollection.controller.QuoteController;
import com.quotehunters.quotecollection.model.dto.MemberDTO;

import java.util.Scanner;

public class MainView {
    private final Scanner scanner = new Scanner(System.in);

    private final ScannerView scannerView = new ScannerView();
    private final MemberView memberView = new MemberView();
    private final BookmarkView bookmarkView = new BookmarkView();

    private final QuoteController quoteController = new QuoteController();
    private final PersonController personController = new PersonController();

    private MemberDTO member = null;

    public void start() {
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
                        case 1: {
                            userMainMenu(member);
                            break;
                        }
                        case 2: {
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

    private void userMainMenu(MemberDTO member) {
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
            }
        }
    }
}
