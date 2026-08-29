package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.model.dto.CountryDTO;
import com.quotehunters.quotecollection.model.dto.PersonDTO;

import java.util.List;
import java.util.Map;
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
    // 0. 수정할 인물 선택
    // 1차 조회 결과에서 수정할 인물 선택
    public PersonDTO selectPerson(Scanner sc, List<PersonDTO> personList) {
        if (personList.isEmpty()) { return null; }

        while (true) {
            int personId = scannerView.scannInt(sc, "수정할 인물 번호 선택 (0: 뒤로가기)");

            if (personId == 0) { return null; }

            for (PersonDTO person : personList) {
                if (person.getPersonId() == personId) {
                    return person;
                }
            }

            resultView.errorMessage("조회된 목록에 있는 인물 번호를 선택해주세요.");
        }
    }

    /* 1. 인물의 국가 수정 */
    // 1-1. 인물의 국가를 수정하기 위해 새 국가를 선택
    public int selectCountryForUpdate(Scanner sc, PersonDTO selectedPerson, List<CountryDTO> countryList) {

        // 1차 조회에서 전달받은 인물의 이름과 현재 국가를 출력
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

            // 뒤로 가기 선택시 전 단계로 이동
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

    /* 2. 인물의 시대 수정 */
    // 2-1. 인물의 시대를 수정하기 위해 새로운 시대 선택
    // Map은 나중에 PeriodList 들어오면 변경
    public int selectPeriodForUpdate(Scanner sc, PersonDTO selectedPerson, Map<Integer, String> periodMap) {

        System.out.println("\n========== 인물의 시대 수정 ==========");
        System.out.println("인물 이름 : " + selectedPerson.getPersonName());
        System.out.println("현재 시대 : " + selectedPerson.getPeriodName());

        // 등록된 시대가 없으면 선택할 수 없음
        if (periodMap.isEmpty()) {
            printMessage("등록된 시대가 없습니다.");
            return 0;
        }

        System.out.println("\n========== 시대 목록 ==========");

        // key는 실제 period_id, value는 시대명
        for (Map.Entry<Integer, String> period : periodMap.entrySet()) {
            System.out.println(period.getKey() + ". " + period.getValue());
        }

        while (true) {
            int periodId = scannerView.scannInt(sc, "변경할 시대 선택 (0: 뒤로가기)");

            // 한 단계 위인 수정할 인물 선택으로 이동
            if (periodId == 0) {
                return 0;
            }

            // 목록에 존재하는 실제 period_id인지 확인
            if (!periodMap.containsKey(periodId)) {
                resultView.errorMessage("목록에 있는 시대 번호를 선택해주세요.");
                continue;
            }

            // 현재 시대와 동일한 시대인지 확인
            if (selectedPerson.getPeriodId() == periodId) {
                resultView.errorMessage("현재 시대와 동일한 시대입니다.");
                continue;
            }

            System.out.println("새로 변경할 시대명 : " + periodMap.get(periodId));

            return periodId;
        }
    }

    // 2-2. 인물 시대 수정 최종 확인
    public String confirmPeriodUpdate(Scanner sc) {

        while (true) {
            String choice = scannerView.scannString(sc, "작업 선택 (완료 / 재수정 / 취소)");

            if ("완료".equals(choice) || "재수정".equals(choice) || "취소".equals(choice)) {
                return choice;
            }

            resultView.errorMessage("완료, 재수정, 취소 중 하나를 입력해주세요.");
        }
    }


    /* 인물 등록 */
    // 1. 등록할 인물 이름 입력 및 길이 검증
    // 중복 확인은 Application에서
    // PersonView가 PersonController를 직접 만들면 순환 생성 문제가 생길 수 있음
    public String inputPersonName(Scanner sc) {

        while (true) {
            String personName = scannerView.scannString(sc, "등록할 인물 이름 입력 (0: 뒤로가기)");

            // 이름 입력 단계에서 뒤로가기
            if ("0".equals(personName)) { return null;}

            // person_name VARCHAR(50) 길이 검증
            if (personName.length() > 50) {
                resultView.errorMessage("인물 이름은 50자 이하로 입력해주세요.");
                continue;
            }

            return personName;
        }
    }

    // 2. 등록할 인물의 정보 미리보기
    public void displayPersonForInsert(PersonDTO person) {

        System.out.println("\n========== 인물 등록 정보 ==========");
        System.out.println("인물 이름 : " + person.getPersonName());
        System.out.println("국가 : " + person.getCountryName());
        System.out.println("시대 : " + person.getPeriodName());
        System.out.println("분야 : " + person.getFieldName());
        System.out.println("==================================");
    }

    // 3. 인물 등록 여부 확인 (1차)
    public String confirmPersonInsert(Scanner sc) {

        while (true) {
            String choice = scannerView.scannString(sc, "작업 선택 (등록 / 취소)");

            if ("등록".equals(choice) || "취소".equals(choice)) {
                return choice;
            }

            resultView.errorMessage("등록 또는 취소를 입력해주세요.");
        }
    }

    // 4. 등록 취소 선택 시 완전 취소 또는 수정 구간 선택
    public String selectPersonInsertCancelAction(Scanner sc) {
        while (true) {
            String choice = scannerView.scannString(sc, "작업 선택 (완전 취소 / 수정 구간 선택)");

            if ("완전 취소".equals(choice) || "수정 구간 선택".equals(choice)) {
                return choice;
            }

            resultView.errorMessage("완전 취소 또는 수정 구간 선택을 입력해주세요.");
        }
    }

    // 5. 다시 입력할 등록 정보 구간 선택
    public int selectPersonInsertSection(Scanner sc) {

        System.out.println("\n========== 수정 구간 선택 ==========");
        System.out.println("1. 국가");
        System.out.println("2. 시대");
        System.out.println("3. 분야");
        System.out.println("4. 인물 이름");
        System.out.println("0. 뒤로가기"); // 취소 or 완전취소 선택 구간으로 돌아감

        while (true) {
            int choice = scannerView.scannInt(sc, "수정할 구간 선택");

            if (choice >= 0 && choice <= 4) { return choice; }

            resultView.errorMessage("목록에 있는 번호를 선택해주세요.");
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
