package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.CountryController;
import com.quotehunters.quotecollection.model.dto.CountryDTO;

import java.util.List;      // 목록 타입
import java.util.Scanner;   // 키보드 입력 도구

public class CountryView {


    private final CountryController countryController = new CountryController();
    private final ScannerView scv = new ScannerView();
    private final ResultView rv = new ResultView();
    private final Scanner sc = new Scanner(System.in);

    // [국가 목록 출력]
    public void allCountries() {
        List<CountryDTO> countries = countryController.allCountries();

        System.out.println("----------------------------");
        if (countries.isEmpty()) {                        // 워크플로우 예외: 조회 결과 없으면 안내
            System.out.println("조회 결과 없음");
            return;
        }

        for (int i = 0; i < countries.size(); i++) {
            System.out.println((i + 1) + ". " + countries.get(i).getCountryName());
        }
        System.out.println("----------------------------");
    }

    // [국가 선택]
    public int selectCountry(ScannerView scannerView, Scanner scanner) {
        List<CountryDTO> countries = countryController.allCountries();

        allCountries();                                   // 화면에 목록 출력

        if (countries.isEmpty()) return 0;                // 목록이 없으면 선택 불가 → 뒤로가기 취급

        int choice = 0;

        while (true) {
            choice = scannerView.scannInt(scanner, "선택 (0: 뒤로가기)");
            if (choice == 0) break;

            if (choice < 0 || countries.size() < choice) {
                rv.errorMessage("메뉴에 있는 번호를 선택해주세요");
                continue;
            }

            break;
        }

        if (choice == 0) return 0;

        return countries.get(choice - 1).getCountryId();
    }

    // [국가 등록] 국가명 입력 → 등록 Validation → [등록 / 수정 / 취소]
    public void registCountry() {
        while (true) {

            String countryName = scv.scannString(sc, "등록할 국가명 입력 (0: 뒤로가기)");
            if (countryName.equals("0")) return;


            while (true) {
                String check = scv.scannString(sc, "[" + countryName + "] (으)로 등록하시겠습니까? 예 / 수정 / 취소");

                if (check.equals("예")) {
                    String message = countryController.registCountry(countryName);

                    if (message.equals("등록되었습니다.")) {
                        rv.successMessage(message);
                        return;
                    }

                    rv.errorMessage(message);
                    break;
                }

                if (check.equals("수정")) break;

                if (check.equals("취소")) return;

                rv.errorMessage("예, 수정, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    // [국가 수정]  리스트 추출 → 선택 → 입력 → 수정 Validation → [완료 / 재수정 / 취소]
    public void modifyCountry(ScannerView scannerView, Scanner scanner) {
        selectLoop:
        // '재수정' 시 여기(대상 선택)로 돌아옴
        while (true) {

            int id = selectCountry(scannerView, scanner);
            if (id == 0) return;

            while (true) {
                String newName = scv.scannString(sc, "변경할 국가명 입력 (0: 뒤로가기)");
                if (newName.equals("0")) continue selectLoop;   // 0이면 대상 선택부터 다시


                while (true) {
                    String check = scv.scannString(sc, "[" + newName + "] (으)로 수정하시겠습니까? 예 / 재수정 / 아니오");

                    if (check.equals("예")) {
                        String message = countryController.modifyCountry(id, newName);

                        if (message.equals("수정되었습니다.")) {
                            rv.successMessage(message);
                            return;
                        }

                        rv.errorMessage(message);
                        continue selectLoop;
                    }

                    if (check.equals("재수정")) continue selectLoop;

                    if (check.equals("아니오")) return;

                    rv.errorMessage("예, 재수정, 아니오 중 하나를 입력해주세요.");
                }
            }
        }
    }
    // [국가 삭제] 명세 Country-004: 목록 → 선택 → 1차 경고 → 2차 경고(연쇄 삭제) → [완료/재선택/취소]
    public void removeCountry(ScannerView scannerView, Scanner scanner) {
        selectLoop:
        while (true) {

            int id = selectCountry(scannerView, scanner);
            if (id == 0) return;                           // 뒤로가기면 이전 메뉴로

            // --- 1차 경고 (명세 S4~S5-1) ---
            while (true) {
                String first = scv.scannString(sc, "선택한 국가를 삭제하시겠습니까? 예 / 아니오");

                if (first.equals("아니오")) return;        // DELETE 없이 이전 메뉴로
                if (first.equals("예")) break;             // 다음 경고로 진행

                rv.errorMessage("예, 아니오 중 하나를 입력해주세요.");   // 그 외 입력 시
            }

            // --- 2차 경고: 연쇄 삭제 안내 (명세 S6) ---
            System.out.println("해당 국가를 삭제할 경우 해당 국가에 속한 모든 인물의 정보(명언 포함)가 삭제됩니다.");

            while (true) {
                String second = scv.scannString(sc, "그래도 삭제하시겠습니까? 예 / 재선택 / 아니오");

                if (second.equals("예")) {                 // [완료] 삭제 실행
                    String message = countryController.removeCountry(id);

                    if (message.equals("삭제 되었습니다.")) {
                        rv.successMessage(message);        // 명세 S8-1: 완료 메시지 출력
                        return;                            // 메뉴로 복귀
                    }

                    rv.errorMessage(message);              // 실패 시 사유 출력
                    continue selectLoop;
                }

                if (second.equals("재선택")) continue selectLoop;   // [재선택] 대상 선택부터 다시

                if (second.equals("아니오")) return;       // [취소] DELETE 없이 이전 메뉴로

                rv.errorMessage("예, 재선택, 아니오 중 하나를 입력해주세요.");
            }
        }
    }
}

