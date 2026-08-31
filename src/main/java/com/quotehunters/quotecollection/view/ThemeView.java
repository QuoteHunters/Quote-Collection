package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.ThemeController;
import com.quotehunters.quotecollection.model.dto.ThemeDTO;

import java.util.List;
import java.util.Scanner;

public class ThemeView {
    private static final int CANCEL_SELECTION = -1;
    private static final String HEADER = "=".repeat(10);
    private static final String LINE = "-".repeat(30);

    private final ResultView resultView = new ResultView();
    private final ThemeController themeController = new ThemeController();

    public void themeMainView(ScannerView scannerView, Scanner scanner) {
        while (true) {
            printHeader("주제 관리");
            System.out.println("1. 조회");
            System.out.println("2. 등록");
            System.out.println("3. 수정");
            System.out.println("4. 삭제");
            System.out.println("0. 메인 화면으로");
            System.out.println(LINE);

            int choice = scannerView.scannInt(scanner, "선택");

            switch (choice) {
                case 0:
                    return;
                case 1:
                    selectThemes();
                    break;
                case 2:
                    if (insertTheme(scannerView, scanner)) {
                        return;
                    }
                    break;
                case 3:
                    if (updateTheme(scannerView, scanner)) {
                        return;
                    }
                    break;
                case 4:
                    if (deleteTheme(scannerView, scanner)) {
                        return;
                    }
                    break;
                default:
                    resultView.errorMessage("메뉴에 있는 번호를 선택해주세요.");
            }
        }
    }

    public boolean insertTheme(ScannerView scannerView, Scanner scanner) {
        printHeader("주제 등록");

        insertLoop:
        while (true) {
            String themeName = scannerView.scannString(scanner, "등록할 주제명 입력 (0: 뒤로가기)");

            if (themeName.equals("0")) {
                return false;
            }

            if (themeName.length() > 10) {
                resultView.errorMessage("주제명은 10글자 이하로 입력해주세요.");
                continue;
            }

            boolean exists;

            while (true) {
                try {
                    exists = themeController.existsTheme(themeName);
                    break;
                } catch (RuntimeException e) {
                    resultView.errorMessage("주제명 중복 확인 중 오류가 발생했습니다.");

                    if (!retryOrCancel(scannerView, scanner)) {
                        return true;
                    }
                }
            }

            if (exists) {
                resultView.errorMessage("이미 등록된 주제명입니다.");
                continue;
            }

            while (true) {
                String check = scannerView.scannString(scanner,
                        "[" + themeName + "](으)로 등록하시겠습니까? (등록 / 재입력 / 취소)");

                if (check.equals("재입력")) {
                    continue insertLoop;
                }

                if (check.equals("취소")) {
                    return true;
                }

                if (check.equals("등록")) {
                    int result;

                    try {
                        result = themeController.insertTheme(themeName);
                    } catch (RuntimeException e) {
                        resultView.errorMessage("주제 등록 중 오류가 발생했습니다.");
                        continue;
                    }

                    if (result > 0) {
                        resultView.successMessage("주제가 등록되었습니다.");
                        return false;
                    }

                    resultView.errorMessage("주제 등록에 실패했습니다.");
                    continue;
                }

                resultView.errorMessage("등록, 재입력, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    public void selectThemes() {
        printHeader("주제 목록");

        try {
            printThemes(themeController.selectThemes());
        } catch (RuntimeException e) {
            resultView.errorMessage("주제 목록 조회 중 오류가 발생했습니다.");
        }
    }

    public int selectIdTheme(ScannerView scannerView, Scanner scanner) {
        List<ThemeDTO> themes;

        while (true) {
            try {
                themes = themeController.selectThemes();
                break;
            } catch (RuntimeException e) {
                resultView.errorMessage("주제 목록 조회 중 오류가 발생했습니다.");

                if (!retryOrCancel(scannerView, scanner)) {
                    return CANCEL_SELECTION;
                }
            }
        }

        printThemes(themes);

        if (themes.isEmpty()) {
            return 0;
        }

        while (true) {
            int choice = scannerView.scannInt(scanner, "선택 (0: 뒤로가기)");

            if (choice == 0) {
                return 0;
            }

            if (choice < 0 || choice > themes.size()) {
                resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
                continue;
            }

            return themes.get(choice - 1).getTheme_id();
        }
    }

    public boolean updateTheme(ScannerView scannerView, Scanner scanner) {
        printHeader("주제 수정");

        selectLoop:
        while (true) {
            int id = selectIdTheme(scannerView, scanner);

            if (id == CANCEL_SELECTION) {
                return true;
            }

            if (id == 0) {
                return false;
            }

            updateLoop:
            while (true) {
                String changeName = scannerView.scannString(scanner, "변경할 주제명 입력 (0: 뒤로가기)");

                if (changeName.equals("0")) {
                    continue selectLoop;
                }

                if (changeName.length() > 10) {
                    resultView.errorMessage("주제명은 10글자 이하로 입력해주세요.");
                    continue;
                }

                boolean exists;

                while (true) {
                    try {
                        exists = themeController.existsTheme(id, changeName);
                        break;
                    } catch (RuntimeException e) {
                        resultView.errorMessage("주제명 중복 확인 중 오류가 발생했습니다.");

                        if (!retryOrCancel(scannerView, scanner)) {
                            return true;
                        }
                    }
                }

                if (exists) {
                    resultView.errorMessage("이미 등록된 주제명입니다.");
                    continue;
                }

                while (true) {
                    String check = scannerView.scannString(scanner,
                            "[" + changeName + "](으)로 수정하시겠습니까? (완료 / 재수정 / 취소)");

                    if (check.equals("재수정")) {
                        continue updateLoop;
                    }

                    if (check.equals("취소")) {
                        return true;
                    }

                    if (check.equals("완료")) {
                        int result;

                        try {
                            result = themeController.updateTheme(id, changeName);
                        } catch (RuntimeException e) {
                            resultView.errorMessage("주제 수정 중 오류가 발생했습니다.");
                            continue;
                        }

                        if (result > 0) {
                            resultView.successMessage("주제가 수정되었습니다.");
                            return false;
                        }

                        resultView.errorMessage("주제 수정에 실패했습니다.");
                        continue;
                    }

                    resultView.errorMessage("완료, 재수정, 취소 중 하나를 입력해주세요.");
                }
            }
        }
    }

    public boolean deleteTheme(ScannerView scannerView, Scanner scanner) {
        printHeader("주제 삭제");

        selectLoop:
        while (true) {
            int id = selectIdTheme(scannerView, scanner);

            if (id == CANCEL_SELECTION) {
                return true;
            }

            if (id == 0) {
                return false;
            }

            System.out.println("해당 주제를 삭제하면 관련 명언과 즐겨찾기도 모두 삭제됩니다.");

            while (true) {
                String check = scannerView.scannString(scanner,
                        "선택한 주제를 삭제하시겠습니까? (완료 / 재선택 / 취소)");

                if (check.equals("재선택")) {
                    continue selectLoop;
                }

                if (check.equals("취소")) {
                    return true;
                }

                if (check.equals("완료")) {
                    int result;

                    try {
                        result = themeController.deleteTheme(id);
                    } catch (RuntimeException e) {
                        resultView.errorMessage("주제 삭제 중 오류가 발생했습니다.");
                        continue;
                    }

                    if (result > 0) {
                        resultView.successMessage("주제가 삭제되었습니다.");
                        return false;
                    }

                    resultView.errorMessage("주제 삭제에 실패했습니다.");
                    continue;
                }

                resultView.errorMessage("완료, 재선택, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    private void printHeader(String title) {
        System.out.println(HEADER + " " + title + " " + HEADER);
    }

    private void printThemes(List<ThemeDTO> themes) {
        System.out.println(LINE);

        if (themes.isEmpty()) {
            System.out.println("조회된 주제가 없습니다.");
            System.out.println(LINE);
            return;
        }

        for (int i = 0; i < themes.size(); i++) {
            System.out.println((i + 1) + ". " + themes.get(i).getTheme_name());
        }

        System.out.println(LINE);
    }

    private boolean retryOrCancel(ScannerView scannerView, Scanner scanner) {
        while (true) {
            String check = scannerView.scannString(scanner, "다시 시도하시겠습니까? (재시도 / 취소)");

            if (check.equals("재시도")) {
                return true;
            }

            if (check.equals("취소")) {
                return false;
            }

            resultView.errorMessage("재시도, 취소 중 하나를 입력해주세요.");
        }
    }
}
