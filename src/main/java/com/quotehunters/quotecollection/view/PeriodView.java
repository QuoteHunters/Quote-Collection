package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.PeriodController;
import com.quotehunters.quotecollection.model.dto.PeriodDTO;

import java.util.List;
import java.util.Scanner;

public class PeriodView {

    private static final String HEADER = "=".repeat(10);
    private static final String LINE = "-".repeat(30);

    private final PeriodController periodController = new PeriodController();
    private final ResultView rv = new ResultView();

    // [시대 목록 출력] (Period-002)
    public void allPeriods() {
        printHeader("시대 조회");

        try {
            printPeriods(periodController.allPeriods());
        } catch (RuntimeException e) {
            rv.errorMessage("시대 목록 조회에 실패했습니다.");
        }
    }

    // [시대 선택]
    public int selectPeriod(ScannerView scannerView, Scanner scanner) {
        while (true) {
            List<PeriodDTO> periods;

            try {
                periods = periodController.allPeriods();
            } catch (RuntimeException e) {
                rv.errorMessage("시대 목록 조회에 실패했습니다.");

                while (true) {
                    String retry = scannerView.scannString(scanner,
                            "시대 목록을 다시 조회하시겠습니까? (재시도 / 뒤로가기)");

                    if (retry.equals("재시도")) {
                        break;
                    }

                    if (retry.equals("뒤로가기")) {
                        return 0;
                    }

                    rv.errorMessage("재시도, 뒤로가기 중 하나를 입력해주세요.");
                }

                continue;
            }

            printPeriods(periods);

            if (periods.isEmpty()) {
                return 0;                                   // 뒤로가기
            }

            while (true) {
                int choice = scannerView.scannInt(scanner, "선택 (0: 뒤로가기)");

                if (choice == 0) {
                    return 0;
                }

                if (choice < 0 || periods.size() < choice) {
                    rv.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
                    continue;                                 // 다시 입력받음
                }

                return periods.get(choice - 1).getPeriodId();
            }
        }
    }

    // [시대 등록] 시대명 입력 → 등록 Validation → [등록 / 수정 / 취소] (Period-001)
    public boolean registPeriod(ScannerView scannerView, Scanner scanner) {
        printHeader("시대 등록");

        while (true) {
            String periodName = scannerView.scannString(scanner, "등록할 시대명 입력 (0: 뒤로가기)");

            if (periodName.equals("0")) {
                return false;
            }

            while (true) {
                String check = scannerView.scannString(scanner,
                        "[" + periodName + "](으)로 등록하시겠습니까? (등록 / 수정 / 취소)");

                if (check.equals("등록")) {
                    String message;

                    try {
                        message = periodController.registPeriod(periodName);
                    } catch (RuntimeException e) {
                        rv.errorMessage("시대 등록에 실패했습니다.");
                        continue;
                    }

                    if (message.equals("시대가 등록되었습니다.")) {
                        rv.successMessage(message);
                        return false;                           // 성공 → 메뉴로 복귀
                    }

                    rv.errorMessage(message);                  // 에러 메세지 출력
                    if (message.equals("시대 등록에 실패했습니다.")) {
                        continue;
                    }

                    break;                                     // 이름 입력부터 다시
                }

                if (check.equals("수정")) {
                    break;                                     // 이름 재입력
                }

                if (check.equals("취소")) {
                    return true;                               // 메뉴로
                }

                rv.errorMessage("등록, 수정, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    // [시대 수정] 리스트 추출 → 선택 → 입력 → 수정 Validation → [완료 / 재수정 / 취소] (Period-003)
    public boolean modifyPeriod(ScannerView scannerView, Scanner scanner) {
        printHeader("시대 수정");

        selectLoop:
        while (true) {
            int id = selectPeriod(scannerView, scanner);

            if (id == 0) {
                return false;
            }

            // '재수정' 시 여기(시대명 입력)로 돌아옴
            while (true) {
                String newName = scannerView.scannString(scanner, "변경할 시대명 입력 (0: 이전 단계)");

                if (newName.equals("0")) {
                    continue selectLoop;                       // 0이면 대상 선택으로 돌아감
                }

                // 수정 여부 확인 ([완료/재수정/취소])
                while (true) {
                    String check = scannerView.scannString(scanner,
                            "[" + newName + "](으)로 수정하시겠습니까? (완료 / 재수정 / 취소)");

                    if (check.equals("완료")) {
                        String message;

                        try {
                            message = periodController.modifyPeriod(id, newName);
                        } catch (RuntimeException e) {
                            rv.errorMessage("시대 수정에 실패했습니다.");
                            continue;
                        }

                        if (message.equals("시대가 수정되었습니다.")) {
                            rv.successMessage(message);
                            return false;
                        }

                        rv.errorMessage(message);              // 실패 사유 메세지 출력
                        if (message.equals("시대 수정에 실패했습니다.")) {
                            continue;
                        }

                        break;                               // 검증 실패면 새 이름 입력부터 다시
                    }

                    if (check.equals("재수정")) {
                        break;
                    }

                    if (check.equals("취소")) {
                        return true;                           // UPDATE 없이 메뉴로
                    }

                    rv.errorMessage("완료, 재수정, 취소 중 하나를 입력해주세요.");
                }
            }
        }
    }

    // [시대 삭제] 명세 Period-004: 목록 → 선택 → 1차 경고 → 2차 경고 → [완료/재선택/취소]
    public boolean removePeriod(ScannerView scannerView, Scanner scanner) {
        printHeader("시대 삭제");

        selectLoop:
        while (true) {
            int id = selectPeriod(scannerView, scanner);

            if (id == 0) {
                return false;                                 // 이전 메뉴로
            }

            // --- 1차 경고 (명세 S4~S5-1) ---
            while (true) {
                String first = scannerView.scannString(scanner, "선택한 시대를 삭제하시겠습니까? (예 / 아니오)");

                if (first.equals("아니오")) {
                    return true;                              // 이전 메뉴로
                }

                if (first.equals("예")) {
                    break;                                    // 다음 경고로 진행
                }

                rv.errorMessage("예, 아니오 중 하나를 입력해주세요.");
            }

            // --- 2차 경고: (명세 S6) ---
            System.out.println("해당 시대를 삭제하면 관련 인물, 명언, 즐겨찾기도 모두 삭제됩니다.");

            while (true) {
                String second = scannerView.scannString(scanner, "삭제하시겠습니까? (완료 / 재선택 / 취소)");

                if (second.equals("완료")) {                  // 삭제 실행
                    String message;

                    try {
                        message = periodController.removePeriod(id);
                    } catch (RuntimeException e) {
                        rv.errorMessage("시대 삭제에 실패했습니다.");
                        continue;
                    }

                    if (message.equals("시대가 삭제되었습니다.")) {
                        rv.successMessage(message);            // 명세 S8-1: 완료 메시지 출력
                        return false;                          // 메뉴로 복귀
                    }

                    rv.errorMessage(message);                  // 실패 시 사유 메세지 출력
                    continue;
                }

                if (second.equals("재선택")) {
                    continue selectLoop;                       // [재선택] 대상 선택부터 다시
                }

                if (second.equals("취소")) {
                    return true;                              // DELETE 없이 이전 메뉴로
                }

                rv.errorMessage("완료, 재선택, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    private void printHeader(String title) {
        System.out.println(HEADER + " " + title + " " + HEADER);
    }

    private void printPeriods(List<PeriodDTO> periods) {
        System.out.println(LINE);

        if (periods.isEmpty()) {                              // 워크플로우 예외: 조회 결과 없으면 안내
            System.out.println("조회된 시대가 없습니다.");
            System.out.println(LINE);
            return;
        }

        for (int i = 0; i < periods.size(); i++) {
            System.out.println((i + 1) + ". " + periods.get(i).getPeriodName());
        }

        System.out.println(LINE);
    }
}
