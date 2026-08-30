package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.ThemeController;
import com.quotehunters.quotecollection.model.dto.ThemeDTO;

import java.util.ArrayList;
import java.util.List;
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
                case 0: {
                    sc.close();
                    return;
                }
                case 1: {
                    selectThemes();
                    break;
                }
                case 2: {
                    insertTheme(scv, sc);
                    break;
                }
                case 3: {
                    updateTheme(scv, sc);
                    break;
                }
                case 4: {
                    deleteTheme(scv, sc);
                    break;
                }
                default: {
                    rv.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                    break;
                }
            }
        }
    }

    public void insertTheme(ScannerView scannerView, Scanner scanner) {
        System.out.println("======= 주제 등록 ========");
        insertLoop:
        while (true) {
            String themeName = scannerView.scannString(scanner, "입력 (0: 뒤로가기)");
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
                String check = scannerView.scannString(scanner, "등록 / 수정 / 취소");
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

    public void selectThemes() {
        List<ThemeDTO> themes = tc.selectThemes();

        System.out.println("----------------------------");
        if (themes.isEmpty()) {
            System.out.println("조회 결과 없음");
            return;
        }

        for (int i = 0; i < themes.size(); i++) {
            System.out.println((i + 1) + ". " + themes.get(i).getTheme_name());
        }
        System.out.println("----------------------------");
    }

    public int selectIdTheme(ScannerView scannerView, Scanner scanner) {
        List<ThemeDTO> themes = tc.selectThemes();

        selectThemes();

        int choice = 0;

        while (true) {
            choice = scannerView.scannInt(scanner, "선택 (0: 뒤로가기)");
            if (choice < 0 || choice > themes.size()) {
                rv.errorMessage("메뉴에 있는 번호를 선택해주세요");
                continue;
            }

            break;
        }

        if (choice == 0) return 0;

        return themes.get(choice - 1).getTheme_id();
    }

    public void updateTheme(ScannerView scannerView, Scanner scanner) {
        selectLoop:
        while (true) {
            int id = selectIdTheme(scannerView, scanner);

            if (id == 0) return;

            updateLoop:
            while (true) {
                String changeName = scannerView.scannString(scanner, "변경할 주제명 입력 (0: 뒤로가기)");
                if (changeName.equals("0")) continue selectLoop;
                if (changeName.length() > 10) {
                    rv.errorMessage("분야명은 10글자 이하로 입력해주세요.");
                    continue;
                }

                if (tc.existsTheme(id, changeName)) {
                    rv.errorMessage("중복되는 분야가 존재합니다.");
                    continue;
                }

                while (true) {
                    String check = scannerView.scannString(scanner, "정말 수정하시겠습니까? 예 / 재수정 / 아니오");

                    if (check.equals("예")) {
                        int result = tc.updateTheme(id, changeName);
                        if (result > 0) {
                            rv.successMessage("수정이 완료되었습니다.");
                            return;
                        }

                        rv.errorMessage("수정에 실패하였습니다. 다시 시도해주세요");
                        return;
                    }

                    if (check.equals("재수정")) continue updateLoop;

                    if (check.equals("아니오")) return;

                    rv.errorMessage("예, 재수정, 아니오 중 하나를 입력해주세요.");
                }
            }
        }
    }

    public void deleteTheme(ScannerView scannerView, Scanner scanner) {
        deleteLoop:
        while (true) {
            int id = selectIdTheme(scannerView, scanner);
            if (id == 0) return;

            while (true) {
                String str = scannerView.scannString(scanner, "정말 삭제하시겠습니까? 완료 / 재선택 / 취소");
                if (str.equals("취소")) return;
                if (str.equals("재선택")) continue deleteLoop;
                if (str.equals("완료")) {
                    int result = tc.deleteTheme(id);
                    if (result > 0) {
                        rv.successMessage("삭제가 완료되었습니다.");
                        return;
                    }
                }

                rv.errorMessage("완료, 재수정, 취소 중 하나를 입력해주세요");
            }
        }
    }
}
