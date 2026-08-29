package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.model.dto.CountryDTO;
import com.quotehunters.quotecollection.model.dto.PersonDTO;

import java.util.List;
import java.util.Scanner;

public class PersonView {

    private final ScannerView scannerView = new ScannerView();
    private final ResultView resultView = new ResultView();

    // 전체 인물 목록 출력
    // 인물 식별 번호는 출력하되 국가·시대·분야 ID는 사용자에게 노출하지 않음
    public void displayAllPerson(List<PersonDTO> personList) {

        System.out.println("\n========== 전체 인물 목록 ==========");

        for (PersonDTO person : personList) {
            System.out.println("----------------------------------");
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    // 국가별 인물 목록 출력
    public void selectPersonByCountry(List<PersonDTO> personList) {

        // 국가별 조회는 모두 같은 국가에 속한 인물들이므로 제목에 한 번만 출력
        String countryName = personList.get(0).getCountryName();
        System.out.println("\n========== " + countryName + "의 인물 목록 ==========");

        for (PersonDTO person : personList) {
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    // 시대별 인물 목록
    public void selectPersonByPeriod(List<PersonDTO> personList) {

        // 시대별 조회 또한 모두 같은 시대에 속한 인물들이므로 제목에 한 번만 출력
        String periodName = personList.get(0).getPeriodName();
        System.out.println("\n========== " + periodName + "의 인물 목록 ==========");

        for (PersonDTO person : personList) {
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    // 분야별 인물 조회
    public void selectPersonByField(List<PersonDTO> personList) {

        // 분야별 조회 또한 모두 같은 시대에 속한 인물들이므로 제목에 한 번만 출력
        String fieldName = personList.get(0).getFieldName();

        System.out.println("\n========== " + fieldName + "분야의 인물 목록 ==========");

        for (PersonDTO person : personList) {
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    // 인물 이름 검색 결과 출력
    public void selectPersonByName(List<PersonDTO> personList) {

        System.out.println("\n========== 인물 이름 검색 결과 ==========");

        for (PersonDTO person : personList) {
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    public void selectPersonByQuoteKeyword(List<PersonDTO> personList, String quoteKeyword) {

        System.out.println("\n========== 명언 키워드에 따른 검색 결과 ==========");

        System.out.println("입력한 키워드 : " + quoteKeyword);
        System.out.println("----------------------------------");

        for (PersonDTO person : personList) {
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    /* 인물 수정 */

    /* 1. 인물의 국가 수정 */
    // 1-1. 인물의 국가를 수정하기 위해 새 국가를 선택
    public int selectCountryForUpdate(Scanner sc, PersonDTO selectedPerson, List<CountryDTO> countryList) {

        // 입럭받은 인물의 이름과 현재 국가를 출력
        System.out.println("\n========== 인물의 국가 수정 ==========");
        System.out.println("인물 이름 : " + selectedPerson.getPersonName());
        System.out.println("현재 국가 : " + selectedPerson.getCountryName());

        // 등록된 국가가 없을 때
        if (countryList.isEmpty()) {
            printMessage("등록된 국가가 없습니다.");
            return 0;
        }

        // 존재하는 국가를 선택할 수 있도록 목록을 보여줌
        // 사용자가 보는 목록 번호는 1번부터 시작 (i + 1)
        System.out.println("\n========== 국가 목록 ==========");
        for (int i = 0; i < countryList.size(); i++) {
            System.out.println((i + 1) + ". " + countryList.get(i).getCountryName());
        }

        // 변경할 국가의 목록 번호를 입력 받음
        while (true) {
            int choice = scannerView.scannInt(sc, "변경할 국가 선택 (0: 뒤로가기)");

            // 뒤로 가기 선택시 정확히 한 Depth 위로 전달
            // 호출한 곳에 0을 반환하고 무한루프문을 빠져나감
            if (choice == 0) {
                return 0;
            }

            // 출력한 국가 목록의 선택 범위를 검사
            // 범위를 벗어날 경우 재선택 (범위: 1 ~ 국가목록 개수)
            if (choice < 1 || choice > countryList.size()) {
                resultView.errorMessage("목록에 있는 국가 번호를 선택해주세요.");
                continue;
            }

            // 선택 번호를 DTO로 변환
            // 실제 리스트의 인덱스는 0부터 시작 (choice - 1)
            CountryDTO selectedCountry = countryList.get(choice - 1);

            // 현재 국가와 동일한 국가를 선택한 경우
            if (selectedPerson.getCountryId() == selectedCountry.getCountryId()) {
                resultView.errorMessage("현재 국가와 동일한 국가입니다.");
                continue;
            }

            // 선택한 국가의 실제 countryId를 반환
            /* 입력받은 choice값과 countryId가 다른 상황이 있을 수 있으므로 (삭제 등의 이슈로 인해)
            *  choice값이 아니라 countryId 자체를 반환
            */
            System.out.println("새로 변경할 국가명 : " + selectedCountry.getCountryName());
            return selectedCountry.getCountryId();
        }
    }

    // 1-2. 인물 국가 수정 최종 확인
    public String confirmCountryUpdate(Scanner sc) {

        while (true) {
            String choice = scannerView.scannString(sc, "작업 선택 (완료 / 재수정 / 취소)");

            // 셋 중 일치하는 값을 입력하면 호출한 곳에 반환
            if ("완료".equals(choice) || "재수정".equals(choice) || "취소".equals(choice)) {
                return choice;
            }
            resultView.errorMessage("완료, 재수정, 취소 중 하나를 입력해주세요.");
        }
    }


    // 성공·실패·안내 메세지 출력
    // View : 받은 문장을 보여주기만 하는 역할
    // Controller : 실행 결과에 따라 어떤 문장을 보여줄지 결정
    public void printMessage(String message) {
        System.out.println();
        System.out.println(message);
    }
}
