package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.ThemeController;

import java.util.Scanner;

public class ThemeView {
    private final ScannerView scv = new ScannerView();
    private final Scanner sc = new Scanner(System.in);
    private final ResultView rv = new ResultView();
    private final ThemeController tc = new ThemeController();

    public void themeMainView() {
        while (true) {
            System.out.println("1. 조회");
            System.out.println("2. 등록");
            System.out.println("3. 수정");
            System.out.println("4. 삭제");
            System.out.println("0. 메인화면으로");

            int choice = scv.scannInt(sc, "선택");
            switch (choice) {
                case 1: {
                    System.out.println("조회");
                    break;
                }
                case 2: {
                    insertTheme();
                    break;
                }
                case 3: {
                    System.out.println("수정");
                    break;
                }
                case 4: {
                    System.out.println("삭제");
                    break;
                }
                default: {
                    rv.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    public void insertTheme() {
        System.out.println("======= 주제 등록 ========");
        insertLoop:
        while (true) {
            String themeName = scv.scannString(sc, "입력 (0: 뒤로가기)");
            if (themeName.equals("0")) return;
            if (themeName.length() > 10) {
                rv.errorMessage("주제명은 10글자 이하로 입력해주세요.");
                continue;
            }
            if (tc.existsTheme(themeName)) {
                rv.errorMessage("중복되는 주제가 존재합니다.");
                continue;
            }

            while (true) {
                String check = scv.scannString(sc, "등록 / 수정 / 취소");
                if (check.equals("취소")) return;
                if (check.equals("수정")) continue insertLoop;
                if (check.equals("등록")) {
                    int result = tc.insertTheme(themeName);

                    if (result > 0) {
                        rv.successMessage("등록이 완료되었습니다.");
                        return;
                    }

                    rv.errorMessage("등록에 실패하였습니다.");
                    return;
                }

                rv.errorMessage("등록, 수정, 취소 중 하나를 입력해주세요");
                continue;
            }
        }
    }
}
