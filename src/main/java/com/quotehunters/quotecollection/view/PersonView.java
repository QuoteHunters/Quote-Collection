package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.model.dto.CountryDTO;
import com.quotehunters.quotecollection.model.dto.FieldDTO;
import com.quotehunters.quotecollection.model.dto.PeriodDTO;
import com.quotehunters.quotecollection.model.dto.PersonDTO;

import java.util.List;
import java.util.Scanner;

public class PersonView {

    private static final String HEADER = "=".repeat(10);
    private static final String LINE = "-".repeat(30);
    private final ScannerView scannerView = new ScannerView();
    private final ResultView resultView = new ResultView();

    /* 인물 목록 조회 */
    // 전체 인물 목록 출력
    public void displayAllPerson(List<PersonDTO> personList) {

        printHeader("전체 인물 목록");

        // 인물 목록 번호는 출력하되 국가·시대·분야 ID는 사용자에게 노출하지 않음
        for (int i = 0; i < personList.size(); i++) {
            PersonDTO person = personList.get(i);
            System.out.print((i + 1) + ". " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println(LINE);
    }

    // 국가별 인물 목록 출력
    public void selectPersonByCountry(List<PersonDTO> personList) {

        // 국가별 조회는 모두 같은 국가에 속한 인물들이므로 제목에 한 번만 출력
        String countryName = personList.get(0).getCountryName();
        printHeader(countryName + "의 인물 목록");

        for (int i = 0; i < personList.size(); i++) {
            PersonDTO person = personList.get(i);
            System.out.print((i + 1) + ". " + person.getPersonName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println(LINE);
    }

    // 시대별 인물 목록
    public void selectPersonByPeriod(List<PersonDTO> personList) {

        // 시대별 조회 또한 모두 같은 시대에 속한 인물들이므로 제목에 한 번만 출력
        String periodName = personList.get(0).getPeriodName();
        printHeader(periodName + "의 인물 목록");

        for (int i = 0; i < personList.size(); i++) {
            PersonDTO person = personList.get(i);
            System.out.print((i + 1) + ". " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println(LINE);
    }

    // 분야별 인물 조회
    public void selectPersonByField(List<PersonDTO> personList) {

        // 분야별 조회 또한 모두 같은 시대에 속한 인물들이므로 제목에 한 번만 출력
        String fieldName = personList.get(0).getFieldName();

        printHeader(fieldName + " 분야의 인물 목록");

        for (int i = 0; i < personList.size(); i++) {
            PersonDTO person = personList.get(i);
            System.out.print((i + 1) + ". " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.println();
        }

        System.out.println(LINE);
    }

    // 인물 이름 검색 결과 출력
    public void selectPersonByName(List<PersonDTO> personList) {

        printHeader("인물 이름 검색 결과");

        for (int i = 0; i < personList.size(); i++) {
            PersonDTO person = personList.get(i);
            System.out.print((i + 1) + ". " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println(LINE);
    }

    public void selectPersonByQuoteKeyword(List<PersonDTO> personList, String quoteKeyword) {

        printHeader("명언 키워드 인물 검색 결과");

        System.out.println("입력한 키워드 : " + quoteKeyword);
        System.out.println(LINE);

        for (int i = 0; i < personList.size(); i++) {
            PersonDTO person = personList.get(i);
            System.out.print((i + 1) + ". " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println(LINE);
    }

    // 인물 이름 검색어 입력 및 Validation
    public String inputPersonNameForSearch(Scanner sc) {

        while (true) {
            String personName = scannerView.scannString(sc, "검색할 인물 이름 입력 (0: 뒤로가기)");

            if ("0".equals(personName)) {
                return null;
            }

            // person_name VARCHAR(50) 길이 검증
            if (personName.length() > 50) {
                resultView.errorMessage("검색할 인물 이름은 50자 이하로 입력해주세요.");
                continue;
            }

            return personName;
        }
    }

    // 명언 키워드 입력 및 Validation
    public String inputQuoteKeywordForSearch(Scanner sc) {

        while (true) {
            String quoteKeyword = scannerView.scannString(
                    sc,
                    "검색할 명언 키워드 입력 (0: 뒤로가기)"
            );

            if ("0".equals(quoteKeyword)) {
                return null;
            }

            // quote_content VARCHAR(255) 길이 검증
            if (quoteKeyword.length() > 255) {
                resultView.errorMessage("명언 검색 키워드는 255자 이하로 입력해주세요.");
                continue;
            }

            return quoteKeyword;
        }
    }

    /* 인물 수정 */
    public int selectPersonUpdateSearchType(Scanner sc) {
        return selectPersonSearchType(sc, "인물 수정 검색");
    }

    // 0. 수정할 인물 선택
    public PersonDTO selectPerson(Scanner sc, List<PersonDTO> personList) {
        // 1차 조회 결과에서 수정할 인물 선택
        return selectPersonFromList(sc, personList, "수정할 인물 번호 선택 (0: 뒤로가기)");
    }

    public PersonDTO selectPersonForQuoteBrowse(Scanner sc, List<PersonDTO> personList) {
        return selectPersonFromList(sc, personList, "명언을 조회할 인물 번호 선택 (0: 뒤로가기)");
    }

    public int selectPersonUpdateSection(Scanner sc) {
        printHeader("인물 수정 항목");
        System.out.println("1. 국가");
        System.out.println("2. 시대");
        System.out.println("3. 분야");
        System.out.println("4. 이름");
        System.out.println("0. 인물 재선택");
        System.out.println(LINE);

        while (true) {
            int choice = scannerView.scannInt(sc, "수정할 항목 선택");
            if (choice >= 0 && choice <= 4) {
                return choice;
            }
            resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
        }
    }

    /* 1. 인물의 국가 수정 */
    // 1-1. 인물의 국가를 수정하기 위해 새 국가를 선택
    public int selectCountryForUpdate(Scanner sc, PersonDTO selectedPerson, List<CountryDTO> countryList) {

        // 1차 조회에서 전달받은 인물의 이름과 현재 국가를 출력
        printHeader("인물의 국가 수정");
        System.out.println("인물 이름 : " + selectedPerson.getPersonName());
        System.out.println("현재 국가 : " + selectedPerson.getCountryName());

        // 등록된 국가가 없을 때
        if (countryList.isEmpty()) {
            printMessage("등록된 국가가 없습니다.");
            return 0;
        }

        // 존재하는 국가를 선택할 수 있도록 목록을 보여줌
        // 사용자가 보는 목록 번호는 1번부터 시작 (i + 1)
        printHeader("국가 목록");
        for (int i = 0; i < countryList.size(); i++) {
            System.out.println((i + 1) + ". " + countryList.get(i).getCountryName());
        }
        System.out.println(LINE);

        // 변경할 국가의 목록 번호를 입력 받음
        while (true) {
            int choice = scannerView.scannInt(sc, "변경할 국가 선택 (0: 뒤로가기)");

            // 뒤로 가기 선택시 전 단계로 이동
            // 호출한 곳에 0을 반환하고 무한루프문을 빠져나감
            if (choice == 0) {
                return 0;
            }

            // 출력한 국가 목록의 선택 범위인지 검사
            // 범위를 벗어날 경우 재선택 (범위: 1 ~ 국가목록 개수)
            if (choice < 1 || choice > countryList.size()) {
                resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
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
        return confirmPersonUpdate(sc);
    }

    /* 2. 인물의 시대 수정 */
    // 2-1. 인물의 시대를 수정하기 위해 새로운 시대 선택
    public int selectPeriodForUpdate(Scanner sc, PersonDTO selectedPerson, List<PeriodDTO> periodList) {

        printHeader("인물의 시대 수정");
        System.out.println("인물 이름 : " + selectedPerson.getPersonName());
        System.out.println("현재 시대 : " + selectedPerson.getPeriodName());

        // 등록된 시대가 없으면 선택할 수 없음
        if (periodList.isEmpty()) {
            printMessage("등록된 시대가 없습니다.");
            return 0;
        }

        printHeader("시대 목록");

        for (int i = 0; i < periodList.size(); i++) {
            System.out.println((i + 1) + ". " + periodList.get(i).getPeriodName());
        }
        System.out.println(LINE);

        while (true) {
            int choice = scannerView.scannInt(sc, "변경할 시대 선택 (0: 뒤로가기)");

            // 한 단계 위인 수정 항목 선택으로 이동
            if (choice == 0) {
                return 0;
            }

            // 출력한 시대 목록의 선택 범위인지 검사
            if (choice < 1 || choice > periodList.size()) {
                resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
                continue;
            }

            PeriodDTO selectedPeriod = periodList.get(choice - 1);

            // 현재 시대와 동일한 시대인지 확인
            if (selectedPerson.getPeriodId() == selectedPeriod.getPeriodId()) {
                resultView.errorMessage("현재 시대와 동일한 시대입니다.");
                continue;
            }

            System.out.println("새로 변경할 시대명 : " + selectedPeriod.getPeriodName());

            return selectedPeriod.getPeriodId();
        }
    }

    // 2-2. 인물 시대 수정 최종 확인
    public String confirmPeriodUpdate(Scanner sc) {
        return confirmPersonUpdate(sc);
    }

    /* 3. 인물의 분야 수정 */
    // 3-1. 인물의 분야를 수정하기 위해 새로운 분야 선택
    public int selectFieldForUpdate(Scanner sc, PersonDTO selectedPerson, List<FieldDTO> fieldList) {

        printHeader("인물의 분야 수정");
        System.out.println("인물 이름 : " + selectedPerson.getPersonName());
        System.out.println("현재 분야 : " + selectedPerson.getFieldName());

        // 등록된 분야가 없으면 선택할 수 없음
        if (fieldList.isEmpty()) {
            printMessage("등록된 분야가 없습니다.");
            return 0;
        }

        printHeader("분야 목록");

        for (int i = 0; i < fieldList.size(); i++) {
            System.out.println((i + 1) + ". " + fieldList.get(i).getFieldName());
        }
        System.out.println(LINE);

        while (true) {
            int choice = scannerView.scannInt(sc, "변경할 분야 선택 (0: 뒤로가기)");

            // 정확히 한 단계 위인 수정 항목 선택으로 이동
            if (choice == 0) {
                return 0;
            }

            // 출력한 목록의 선택 범위 확인
            if (choice < 1 || choice > fieldList.size()) {
                resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
                continue;
            }

            // 화면 선택 번호를 실제 FieldDTO로 변환
            FieldDTO selectedField = fieldList.get(choice - 1);

            // 현재 분야와 동일한 분야인지 확인
            if (selectedPerson.getFieldId() == selectedField.getFieldId()) {
                resultView.errorMessage("현재 분야와 동일한 분야입니다.");
                continue;
            }

            System.out.println("새로 변경할 분야명 : " + selectedField.getFieldName());

            // 화면 순번이 아니라 실제 DB의 field_id 반환
            return selectedField.getFieldId();
        }
    }

    // 3-2. 인물 분야 수정 최종 확인
    public String confirmFieldUpdate(Scanner sc) {
        return confirmPersonUpdate(sc);
    }

    /* 4. 인물의 이름 수정 */
    // 4-1. 변경할 인물 이름 입력 및 Validation
    public String inputPersonNameForUpdate(Scanner sc, PersonDTO selectedPerson) {

        printHeader("인물의 이름 수정");
        System.out.println("현재 이름 : " + selectedPerson.getPersonName());

        while (true) {
            String newPersonName = scannerView.scannString(sc, "변경할 인물 이름 입력 (0: 뒤로가기)");

            // 정확히 한 단계 위인 수정 항목 선택으로 이동
            if ("0".equals(newPersonName)) {
                return null;
            }

            // person_name VARCHAR(50) 길이 검증
            if (newPersonName.length() > 50) {
                resultView.errorMessage("인물 이름은 50자 이하로 입력해주세요.");
                continue;
            }

            // 현재 이름과 동일한지 검증
            if (selectedPerson.getPersonName().equals(newPersonName)) {
                resultView.errorMessage("현재 이름과 동일한 이름입니다.");
                continue;
            }

            System.out.println("새로 변경할 인물 이름 : " + newPersonName);

            return newPersonName;
        }
    }

    // 4-2. 인물 이름 수정 최종 확인
    public String confirmPersonNameUpdate(Scanner sc) {
        return confirmPersonUpdate(sc);
    }

    private String confirmPersonUpdate(Scanner sc) {
        while (true) {
            String choice = scannerView.scannString(sc, "수정하시겠습니까? (완료 / 재수정 / 취소)");

            // 셋 중 일치하는 값을 입력하면 호출한 곳에 반환
            if ("완료".equals(choice) || "재수정".equals(choice) || "취소".equals(choice)) {
                return choice;
            }

            resultView.errorMessage("완료, 재수정, 취소 중 하나를 입력해주세요.");
        }
    }


    /* 인물 등록 */
    // 1. 등록할 인물 이름 입력 및 길이 검증
    public String inputPersonName(ScannerView scannerView, Scanner scanner) {

        while (true) {
            String personName = scannerView.scannString(scanner, "등록할 인물 이름 입력 (0: 뒤로가기)");

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

        printHeader("인물 등록 정보");
        System.out.println("인물 이름 : " + person.getPersonName());
        System.out.println("국가 : " + person.getCountryName());
        System.out.println("시대 : " + person.getPeriodName());
        System.out.println("분야 : " + person.getFieldName());
        System.out.println(LINE);
    }

    // 3. 인물 등록 여부 확인
    public String confirmPersonInsert(ScannerView scannerView, Scanner sc) {

        while (true) {
            String choice = scannerView.scannString(sc, "등록하시겠습니까? (등록 / 수정 / 취소)");

            if ("등록".equals(choice) || "수정".equals(choice) || "취소".equals(choice)) {
                return choice;
            }

            resultView.errorMessage("등록, 수정, 취소 중 하나를 입력해주세요.");
        }
    }

    // 4. 등록 수정 선택 시 수정 구간 선택
    // 5. 다시 입력할 등록 정보 구간 선택
    public int selectPersonInsertSection(Scanner sc) {
        printHeader("인물 등록 수정 구간");
        System.out.println("1. 국가");
        System.out.println("2. 시대");
        System.out.println("3. 분야");
        System.out.println("4. 이름");
        System.out.println("0. 등록 확인으로"); // 수정하지 않고 등록 확인 화면으로 돌아감
        System.out.println(LINE);

        while (true) {
            int choice = scannerView.scannInt(sc, "수정할 구간 선택");
            if (choice >= 0 && choice <= 4) return choice;
            resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
        }
    }

    /* 인물 삭제 */
    public int selectPersonDeleteSearchType(Scanner sc) {
        return selectPersonSearchType(sc, "인물 삭제 검색");
    }

    private int selectPersonSearchType(Scanner sc, String title) {
        printHeader(title);
        System.out.println("1. 국가 검색");
        System.out.println("2. 시대 검색");
        System.out.println("3. 분야 검색");
        System.out.println("4. 이름 검색");
        System.out.println("5. 명언 키워드 검색");
        System.out.println("0. 뒤로가기");
        System.out.println(LINE);

        while (true) {
            int choice = scannerView.scannInt(sc, "검색 방식 선택");
            if (choice >= 0 && choice <= 5) {
                return choice;
            }
            resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
        }
    }

    public boolean retryCategoryList(Scanner sc, String categoryName) {
        while (true) {
            String choice = scannerView.scannString(sc,
                    categoryName + " 목록을 다시 조회하시겠습니까? (재시도 / 취소)");

            if ("재시도".equals(choice)) return true;
            if ("취소".equals(choice)) return false;
            resultView.errorMessage("재시도 또는 취소를 입력해주세요.");
        }
    }

    public boolean retryPersonBrowse(Scanner sc) {
        while (true) {
            String choice = scannerView.scannString(sc,
                    "인물 목록을 다시 조회하시겠습니까? (재시도 / 이전 화면)");

            if ("재시도".equals(choice)) return true;
            if ("이전 화면".equals(choice)) return false;
            resultView.errorMessage("재시도 또는 이전 화면을 입력해주세요.");
        }
    }

    // 1. 삭제할 인물 선택
    public PersonDTO selectPersonForDelete(Scanner sc, List<PersonDTO> personList) {
        // 삭제할 인물이 아예 없다면(empty) 화면 진행x
        // 아예 인물 리스트가 비어있는 경우 공통 선택 메소드의 while문이 실행되지 않음
        // 빈 목록에서 삭제할 인물을 계속 묻는 입력 반복을 막음
        // 그래서 공통 선택 메소드에서 while문에 들어가기 전에 한 번 검사
        return selectPersonFromList(sc, personList, "삭제할 인물 번호 선택 (0: 뒤로가기)");
    }

    // 2. 삭제할 인물 정보 출력
    public void displayPersonForDelete(PersonDTO person) {
        printHeader("삭제할 인물 정보");
        System.out.println("인물 이름 : " + person.getPersonName());
        System.out.println("국가 : " + person.getCountryName());
        System.out.println("시대 : " + person.getPeriodName());
        System.out.println("분야 : " + person.getFieldName());
        System.out.println(LINE);
        System.out.println("연결된 명언과 해당 명언의 즐겨찾기도 함께 삭제됩니다.");
    }

    // 최종 삭제 여부
    public String confirmPersonForDelete(Scanner sc) {
        while (true) {
            String choice = scannerView.scannString(sc, "삭제하시겠습니까? (예 / 아니오)");

            if ("예".equals(choice) || "아니오".equals(choice)) {
                return choice;
            }

            resultView.errorMessage("예 또는 아니오를 입력해주세요.");
        }
    }

    // 성공·실패·안내 메세지 출력
    public void printMessage(String message) {
        // View : 받은 문장을 보여주기만 하는 역할
        // Controller : 실행 결과에 따라 어떤 문장을 보여줄지 결정
        System.out.println();
        System.out.println(message);
    }

    private PersonDTO selectPersonFromList(Scanner sc, List<PersonDTO> personList, String prompt) {
        if (personList.isEmpty()) return null;

        while (true) {
            int choice = scannerView.scannInt(sc, prompt);
            // 정확히 한 단계 위로 이동
            if (choice == 0) return null;

            // 조회된 인물 중 입력한 목록 번호와 일치하는 인물 반환
            if (choice >= 1 && choice <= personList.size()) {
                return personList.get(choice - 1);
            }
            resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
        }
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println(HEADER + " " + title + " " + HEADER);
    }
}
