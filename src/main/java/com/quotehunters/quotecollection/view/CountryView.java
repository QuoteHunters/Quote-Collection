package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.CountryController;
import com.quotehunters.quotecollection.model.dto.CountryDTO;

import java.util.List;      // 목록 타입
import java.util.Scanner;   // 키보드 입력 도구

public class CountryView {

    public static final int SELECT_CANCEL = -1;
    private static final String HEADER = "=".repeat(10);
    private static final String LINE = "-".repeat(30);

    private final CountryController countryController = new CountryController();
    private final ResultView rv = new ResultView();

    // [국가 목록 출력]
    public void allCountries() {
        printHeader("국가 전체 조회");

        try {
            List<CountryDTO> countries = countryController.allCountries();
            printCountries(countries);
        } catch (RuntimeException e) {
            rv.errorMessage("국가 목록 조회에 실패했습니다. 다시 시도해주세요.");
        }
    }

    // [국가 선택]
    public int selectCountry(ScannerView scannerView, Scanner scanner) {
        while (true) {
            List<CountryDTO> countries;

            try {
                countries = countryController.allCountries();
            } catch (RuntimeException e) {
                rv.errorMessage("국가 목록 조회에 실패했습니다. 다시 시도해주세요.");

                if (retryCountryList(scannerView, scanner)) {
                    continue;
                }

                return SELECT_CANCEL;
            }

            if (!printCountries(countries)) {               // 화면에 목록 출력
                return 0;                                   // 목록이 없으면 선택 불가 → 뒤로가기 취급
            }

            while (true) {
                int choice = scannerView.scannInt(scanner, "선택 (0: 뒤로가기)");

                if (choice == 0) {
                    return 0;
                }

                if (choice < 0 || countries.size() < choice) {
                    rv.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
                    continue;
                }

                return countries.get(choice - 1).getCountryId();
            }
        }
    }

    // [국가 등록] 국가명 입력 → 등록 Validation → [등록 / 수정 / 취소]
    public boolean registCountry(ScannerView scannerView, Scanner scanner) {
        printHeader("국가 등록");

        while (true) {
            String countryName = scannerView.scannString(scanner, "등록할 국가명 입력 (0: 뒤로가기)");

            if (countryName.equals("0")) {
                return false;
            }

            while (true) {
                String check = scannerView.scannString(scanner,
                        "[" + countryName + "] (으)로 등록하시겠습니까? (등록 / 수정 / 취소)");

                if (check.equals("등록")) {
                    String message;

                    try {
                        message = countryController.registCountry(countryName);
                    } catch (RuntimeException e) {
                        rv.errorMessage("국가 등록 중 오류가 발생했습니다. 다시 시도해주세요.");
                        continue;
                    }

                    if (message.equals("국가가 등록되었습니다.")) {
                        rv.successMessage(message);
                        return false;
                    }

                    rv.errorMessage(message);

                    if (message.contains("실패했습니다.")) {
                        continue;
                    }

                    break;
                }

                if (check.equals("수정")) {
                    break;
                }

                if (check.equals("취소")) {
                    return true;
                }

                rv.errorMessage("등록, 수정, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    // [국가 수정]  리스트 추출 → 선택 → 입력 → 수정 Validation → [완료 / 재수정 / 취소]
    public boolean modifyCountry(ScannerView scannerView, Scanner scanner) {
        printHeader("국가 수정");

        selectLoop:
        while (true) {
            int countryId = selectCountry(scannerView, scanner);

            if (countryId == SELECT_CANCEL) {
                return true;
            }

            if (countryId == 0) {
                return false;
            }

            // '재수정' 시 여기(국가명 입력)로 돌아옴
            nameLoop:
            while (true) {
                String newName = scannerView.scannString(scanner, "변경할 국가명 입력 (0: 뒤로가기)");

                if (newName.equals("0")) {
                    continue selectLoop;                         // 0이면 대상 선택부터 다시
                }

                while (true) {
                    String check = scannerView.scannString(scanner,
                            "[" + newName + "] (으)로 수정하시겠습니까? (완료 / 재수정 / 취소)");

                    if (check.equals("완료")) {
                        String message;

                        try {
                            message = countryController.modifyCountry(countryId, newName);
                        } catch (RuntimeException e) {
                            rv.errorMessage("국가 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
                            continue;
                        }

                        if (message.equals("국가가 수정되었습니다.")) {
                            rv.successMessage(message);
                            return false;
                        }

                        rv.errorMessage(message);

                        if (message.contains("실패했습니다.")) {
                            continue;
                        }

                        continue nameLoop;
                    }

                    if (check.equals("재수정")) {
                        continue nameLoop;
                    }

                    if (check.equals("취소")) {
                        return true;
                    }

                    rv.errorMessage("완료, 재수정, 취소 중 하나를 입력해주세요.");
                }
            }
        }
    }

    // [국가 삭제] 명세 Country-004: 목록 → 선택 → 1차 경고 → 2차 경고(연쇄 삭제) → [완료/재선택/취소]
    public boolean removeCountry(ScannerView scannerView, Scanner scanner) {
        printHeader("국가 삭제");

        selectLoop:
        while (true) {
            int countryId = selectCountry(scannerView, scanner);

            if (countryId == SELECT_CANCEL) {
                return true;
            }

            if (countryId == 0) {
                return false;                                   // 뒤로가기면 이전 메뉴로
            }

            // --- 1차 경고 (명세 S4~S5-1) ---
            while (true) {
                String first = scannerView.scannString(scanner,
                        "선택한 국가를 삭제하시겠습니까? (예 / 아니오)");

                if (first.equals("예")) {
                    break;                                      // 다음 경고로 진행
                }

                if (first.equals("아니오")) {
                    return true;                                // DELETE 없이 이전 메뉴로
                }

                rv.errorMessage("예, 아니오 중 하나를 입력해주세요.");   // 그 외 입력 시
            }

            // --- 2차 경고: 연쇄 삭제 안내 (명세 S6) ---
            System.out.println("해당 국가를 삭제할 경우 해당 국가에 속한 모든 인물의 정보(명언 포함)가 삭제됩니다.");

            while (true) {
                String second = scannerView.scannString(scanner,
                        "그래도 삭제하시겠습니까? (완료 / 재선택 / 취소)");

                if (second.equals("완료")) {                    // [완료] 삭제 실행
                    String message;

                    try {
                        message = countryController.removeCountry(countryId);
                    } catch (RuntimeException e) {
                        rv.errorMessage("국가 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
                        continue;
                    }

                    if (message.equals("국가가 삭제되었습니다.")) {
                        rv.successMessage(message);              // 명세 S8-1: 완료 메시지 출력
                        return false;                            // 메뉴로 복귀
                    } else {
                        rv.errorMessage(message);                // 실패 시 사유 출력
                    }

                    continue;
                }

                if (second.equals("재선택")) {
                    continue selectLoop;                         // [재선택] 대상 선택부터 다시
                }

                if (second.equals("취소")) {
                    return true;                                // [취소] DELETE 없이 이전 메뉴로
                }

                rv.errorMessage("완료, 재선택, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    private boolean printCountries(List<CountryDTO> countries) {
        System.out.println(LINE);

        if (countries.isEmpty()) {                        // 워크플로우 예외: 조회 결과 없으면 안내
            System.out.println("등록된 국가가 없습니다.");
            System.out.println(LINE);
            return false;
        }

        for (int i = 0; i < countries.size(); i++) {
            System.out.println((i + 1) + ". " + countries.get(i).getCountryName());
        }

        System.out.println(LINE);
        return true;
    }

    private boolean retryCountryList(ScannerView scannerView, Scanner scanner) {
        while (true) {
            String choice = scannerView.scannString(scanner, "국가 목록을 다시 조회하시겠습니까? (재시도 / 취소)");

            if (choice.equals("재시도")) {
                return true;
            }

            if (choice.equals("취소")) {
                return false;
            }

            rv.errorMessage("재시도, 취소 중 하나를 입력해주세요.");
        }
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println(HEADER + " " + title + " " + HEADER);
    }
}
