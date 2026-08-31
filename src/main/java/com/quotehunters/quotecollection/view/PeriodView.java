package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.PeriodController;
import com.quotehunters.quotecollection.model.dto.PeriodDTO;

import java.util.List;
import java.util.Scanner;

public class PeriodView {

    private final PeriodController periodController = new PeriodController();
    private final ResultView rv = new ResultView();

    // [시대 목록 출력] (Period-002)
    public void allPeriods() {
        List<PeriodDTO> periods = periodController.allPeriods();

        System.out.println("----------------------------");
        if (periods.isEmpty()) {                          // 워크플로우 예외: 조회 결과 없으면 안내
            System.out.println("조회 결과 없음");
            return;
        }

        for (int i = 0; i < periods.size(); i++) {
            System.out.println((i + 1) + ". " + periods.get(i).getPeriodName());
        }
        System.out.println("----------------------------");
    }

    // [시대 선택]
    public int selectPeriod(ScannerView scannerView, Scanner scanner) {
        List<PeriodDTO> periods = periodController.allPeriods();

        allPeriods();

        if (periods.isEmpty()) return 0;                  // 뒤로가기

        int choice = 0;

        while (true) {
            choice = scannerView.scannInt(scanner, "선택 (0: 뒤로가기)");
            if (choice == 0) break;

            if (choice < 0 || periods.size() < choice) {
                rv.errorMessage("메뉴에 있는 번호를 선택해주세요");
                continue;                                 // 다시 입력받음
            }

            break;
        }

        if (choice == 0) return 0;

        return periods.get(choice - 1).getPeriodId();
    }

    // [시대 등록] 시대명 입력 → 등록 Validation → [등록 / 수정 / 취소] (Period-001)
    public void registPeriod(ScannerView scannerView, Scanner scanner) {
        while (true) {

            String periodName = scannerView.scannString(scanner, "등록할 시대명 입력 (0: 뒤로가기)");
            if (periodName.equals("0")) return;

            while (true) {
                String check = scannerView.scannString(scanner, "[" + periodName + "] (으)로 등록하시겠습니까? 예 / 수정 / 취소");

                if (check.equals("예")) {
                    String message = periodController.registPeriod(periodName);

                    if (message.equals("등록되었습니다.")) {
                        rv.successMessage(message);
                        return;                            // 성공 → 메뉴로 복귀
                    }

                    rv.errorMessage(message);              // 에러 메세지 출력
                    break;                                 // 이름 입력부터 다시
                }

                if (check.equals("수정")) break;           // 이름 재입력

                if (check.equals("취소")) return;          // 메뉴로

                rv.errorMessage("예, 수정, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    // [시대 수정] 리스트 추출 → 선택 → 입력 → 수정 Validation → [완료 / 재수정 / 취소] (Period-003)
    public void modifyPeriod(ScannerView scannerView, Scanner scanner) {
        selectLoop:
        // '재수정' 시 여기(대상 선택)로 돌아옴
        while (true) {

            int id = selectPeriod(scannerView, scanner);
            if (id == 0) return;

            while (true) {
                String newName = scannerView.scannString(scanner, "변경할 시대명 입력 (0: 뒤로가기)");
                if (newName.equals("0")) continue selectLoop;   // 0이면 대상 선택으로 돌아감

                // 수정 여부 확인 ([완료/재수정/취소])
                while (true) {
                    String check = scannerView.scannString(scanner, "[" + newName + "] (으)로 수정하시겠습니까? 예 / 재수정 / 아니오");

                    if (check.equals("예")) {
                        String message = periodController.modifyPeriod(id, newName);

                        if (message.equals("수정되었습니다.")) {
                            rv.successMessage(message);
                            return;
                        }

                        rv.errorMessage(message);          // 실패 사유 메세지 출력
                        continue selectLoop;               // 명세 S6-2: 대상 선택부터 다시
                    }

                    if (check.equals("재수정")) continue selectLoop;

                    if (check.equals("아니오")) return;    // UPDATE 없이 메뉴로

                    rv.errorMessage("예, 재수정, 아니오 중 하나를 입력해주세요.");
                }
            }
        }
    }

    // [시대 삭제] 명세 Period-004: 목록 → 선택 → 1차 경고 → 2차 경고 → [완료/재선택/취소]
    public void removePeriod(ScannerView scannerView, Scanner scanner) {
        selectLoop:
        while (true) {

            int id = selectPeriod(scannerView, scanner);
            if (id == 0) return;                           // 이전 메뉴로

            // --- 1차 경고 (명세 S4~S5-1) ---
            while (true) {
                String first = scannerView.scannString(scanner, "선택한 시대를 삭제하시겠습니까? 예 / 아니오");

                if (first.equals("아니오")) return;        // 이전 메뉴로
                if (first.equals("예")) break;             // 다음 경고로 진행

                rv.errorMessage("예, 아니오 중 하나를 입력해주세요.");
            }

            // --- 2차 경고: (명세 S6) ---
            System.out.println("해당 시대를 삭제할 경우 해당 시대에 속한 모든 인물의 정보(명언 포함)가 삭제됩니다.");

            while (true) {
                String second = scannerView.scannString(scanner, "그래도 삭제하시겠습니까? 예 / 재선택 / 아니오");

                if (second.equals("예")) {                 // 삭제 실행
                    String message = periodController.removePeriod(id);

                    if (message.equals("삭제 되었습니다.")) {
                        rv.successMessage(message);        // 명세 S8-1: 완료 메시지 출력
                        return;                            // 메뉴로 복귀
                    }

                    rv.errorMessage(message);              // 실패 시 사유 메세지 출력
                    continue selectLoop;
                }

                if (second.equals("재선택")) continue selectLoop;   // [재선택] 대상 선택부터 다시

                if (second.equals("아니오")) return;       // DELETE 없이 이전 메뉴로

                rv.errorMessage("예, 재선택, 아니오 중 하나를 입력해주세요.");
            }
        }
    }
}
