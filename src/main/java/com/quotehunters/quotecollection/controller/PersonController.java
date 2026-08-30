package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.PersonDTO;
import com.quotehunters.quotecollection.model.service.PersonService;
import com.quotehunters.quotecollection.view.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Scanner;

public class PersonController {

    private PersonService personService = new PersonService();
    private PersonView personView = new PersonView();
    private final CountryView countryView = new CountryView();
    private final PeriodView periodView = new PeriodView();
    private final FieldView fieldView = new FieldView();

    /* 인물 조회*/
    // 1. 전체 인물 조회
    public List<PersonDTO> selectAllPerson() {

        List<PersonDTO> personList = personService.selectAllPerson();

        // 조회 결과 유무에 따라 안내 메시지 또는 인물 목록 출력
        // 인물 리스트에 값이 존재하지 않을 때
        if (personList.isEmpty()) {
            personView.printMessage("등록된 인물이 없습니다.");
        } else {
            // View에 전체 인물의 정보를 전달
            personView.displayAllPerson(personList);
        }
        return personList;

    }

    // 2. 국가별 인물 조회
    public void selectPersonByCountry(ScannerView scannerView, Scanner scanner) {
        int countryId = countryView.selectCountry(scannerView, scanner);
        if (countryId == 0) return;
        List<PersonDTO> personList = personService.selectPersonByCountry(countryId);

        if (personList.isEmpty()) {
            personView.printMessage("해당 국가에 등록된 인물이 없습니다.");
        } else {
            personView.selectPersonByCountry(personList);
        }
    }

    // 3. 시대별 인물 조회
    public void selectPersonByPeriod(ScannerView scannerView, Scanner scanner) {
        int periodId = periodView.selectPeriod(scannerView, scanner);
        if (periodId == 0) return;
        List<PersonDTO> personList = personService.selectPersonByPeriod(periodId);

        if (personList.isEmpty()) {
            personView.printMessage("해당 시대에 등록된 인물이 없습니다.");
        } else {
            personView.selectPersonByPeriod(personList);
        }

    }

    // 4. 분야별 인물 조회
    public void selectPersonByField(ScannerView scannerView, Scanner scanner) {
        int fieldId = fieldView.selectFields(scannerView, scanner);
        if (fieldId == 0) return;

        List<PersonDTO> personList = personService.selectPersonByField(fieldId);

        if (personList.isEmpty()) {
            personView.printMessage("해당 분야에 등록된 인물이 없습니다.");
        } else {
            personView.selectPersonByField(personList);
        }
    }

    // 5. 인물 이름 조회
    public void selectPersonByName(String personName) {
        List<PersonDTO> personList = personService.selectPersonByName(personName);

        if (personList.isEmpty()) {
            personView.printMessage("'" + personName + "'에 해당하는 인물이 존재하지 않습니다.");
        } else {
            personView.selectPersonByName(personList);
        }
    }

    // 6. 명언 키워드 검색에 따른 인물 조회
    public void selectPersonByQuoteKeyword(String quoteKeyword) {
        List<PersonDTO> personList = personService.selectPersonByQuoteKeyword(quoteKeyword);

        if (personList.isEmpty()) {
            personView.printMessage("'" + quoteKeyword + "' 키워드가 포함된 명언을 남긴 인물이 없습니다.");
        } else {
            personView.selectPersonByQuoteKeyword(personList, quoteKeyword);
        }
    }

    /* 인물 정보 수정 */
    // 1. 인물 국가 수정
    public int updatePersonCountry(int personId, int newCountryId) {
       // 여기서는 View에게 중복 여부와 등록 결과를 그대로 돌려줌
        return personService.updatePersonCountry(personId, newCountryId);
    }

    // 2. 인물 시대 수정
    public int updatePersonPeriod(int personId, int periodId) {
        return personService.updatePersonPeriod(personId, periodId);
    }

    // 3. 인물 분야 수정
    public int updatePersonField(int personId, int fieldId) {
        return personService.updatePersonField(personId, fieldId);
    }

    // 4. 인물 이름 수정
    public int updatePersonName(int personId, String personName) {
        return personService.updatePersonName(personId, personName);
    }

    /* 인물 등록 */
    // 1. 인물의 이름 중복 확인
    public boolean existsPersonName(String personName) {
        return  personService.existsPersonName(personName);
    }

    // 2. 인물 등록
    public int insertPerson(PersonDTO personDTO) {
        return personService.insertPerson(personDTO);
    }

    /* 인물 삭제 */
    public int deletePerson(int personId) {
        return personService.deletePerson(personId);
    }

    public void testInsertPerson(ScannerView scannerView, Scanner scanner, ResultView resultView) {

        PersonDTO newPerson = new PersonDTO();


        int countryId = countryView.selectCountry(scannerView, scanner);
        newPerson.setCountryId(countryId);
        newPerson.setCountryName("대한민국");


        /*
         * TODO [국가 목록 조회 연동]
         * 국가 담당자의 전체 국가 조회 기능이 병합되면
         * 임시값을 사용자가 선택한 CountryDTO 값으로 교체
         */

        /*
         * TODO [시대 목록 조회 연동]
         * 시대 담당자의 전체 시대 조회 기능이 병합되면
         * 임시값을 사용자가 선택한 PeriodDTO 값으로 교체
         */
        newPerson.setPeriodId(1);
        newPerson.setPeriodName("AD 16세기");

        /*
         * TODO [분야 목록 조회 연동]
         * 분야 담당자의 전체 분야 조회 기능이 병합되면
         * 임시값을 사용자가 선택한 FieldDTO 값으로 교체
         */
        int fieldId = fieldView.selectFields(scannerView, scanner);
        newPerson.setFieldId(fieldId);
        newPerson.setFieldName("철학");

        // 인물 이름 입력 및 중복 검증
        while (true) {
            String personName = personView.inputPersonName(scannerView, scanner);

            if (personName == null) {
                /*
                 * TODO [분야 목록 조회 연동]
                 * 실제 등록 흐름에서는 한 단계 위인
                 * 분야 선택 화면으로 이동
                 *
                 * 임시 테스트에서는 분야 선택 화면이 없으므로
                 * 인물 기능 테스트 메뉴로 이동
                 */
                return;
            }

            if (existsPersonName(personName)) {
                resultView.errorMessage("이미 등록된 인물 이름입니다.");
                continue;
            }
            newPerson.setPersonName(personName);
            break;
        }

        // 등록 정보 확인 및 수정 반복
        registrationConfirmLoop:
        while (true) {

            // INSERT 전 등록할 정보 출력
            personView.displayPersonForInsert(newPerson);

            String choice = personView.confirmPersonInsert(scannerView, scanner);

            // 등록 선택 시 INSERT 실행
            if ("등록".equals(choice)) {

                int result = insertPerson(newPerson);

                if (result > 0) {
                    resultView.successMessage("인물이 등록되었습니다.");
                } else {
                    resultView.errorMessage("인물 등록에 실패했습니다.");
                }

                return;
            }

            // 취소 선택 시:
            // 완전 취소 또는 수정 구간 선택
            cancelActionLoop:
            while (true) {

                String cancelAction = personView.selectPersonInsertCancelAction(scannerView, scanner);

                // INSERT를 실행하지 않고 등록 완전 취소
                if ("완전 취소".equals(cancelAction)) {
                    personView.printMessage("인물 등록을 취소했습니다.");
                    return;
                }

                // 수정할 등록 정보 구간 선택
                sectionSelectLoop:
                while (true) {
                    int section = personView.selectPersonInsertSection(scannerView, scanner);

                    // 한 단계 위인 취소 후 작업 선택으로 이동
                    if (section == 0) {
                        continue cancelActionLoop;
                    }

                    switch (section) {
                        case 1:
                            /*
                             * TODO [국가 목록 조회 연동]
                             * 국가 담당자의 전체 국가 조회 기능이 병합되면
                             * 국가 목록을 출력하고 CountryDTO를 재선택
                             *
                             * 선택한 countryId와 countryName을
                             * newPerson에 다시 저장한 뒤
                             * registrationConfirmLoop로 이동
                             */
                            resultView.errorMessage("국가 목록 조회 기능 연동 후 사용할 수 있습니다.");
                            continue sectionSelectLoop;

                        case 2:
                            /*
                             * TODO [시대 목록 조회 연동]
                             * 시대 담당자의 전체 시대 조회 기능이 병합되면
                             * 시대 목록을 출력하고 PeriodDTO를 재선택
                             *
                             * 선택한 periodId와 periodName을
                             * newPerson에 다시 저장한 뒤
                             * registrationConfirmLoop로 이동
                             */
                            resultView.errorMessage("시대 목록 조회 기능 연동 후 사용할 수 있습니다.");
                            continue sectionSelectLoop;

                        case 3:
                            /*
                             * TODO [분야 목록 조회 연동]
                             * 분야 담당자의 전체 분야 조회 기능이 병합되면
                             * 분야 목록을 출력하고 FieldDTO를 재선택
                             *
                             * 선택한 fieldId와 fieldName을
                             * newPerson에 다시 저장한 뒤
                             * registrationConfirmLoop로 이동
                             */
                            resultView.errorMessage("분야 목록 조회 기능 연동 후 사용할 수 있습니다.");
                            continue sectionSelectLoop;

                        case 4:
                            // 인물 이름 재입력 및 검증
                            while (true) {
                                String changedName =
                                        personView.inputPersonName(scannerView, scanner);

                                // 이름 재입력에서 뒤로가기
                                if (changedName == null) {
                                    continue sectionSelectLoop;
                                }

                                if (existsPersonName(changedName)) {
                                    resultView.errorMessage(
                                            "이미 등록된 인물 이름입니다."
                                    );
                                    continue;
                                }

                                newPerson.setPersonName(changedName);

                                // 수정된 전체 등록 정보를 다시 확인
                                continue registrationConfirmLoop;
                            }
                    }
                }
            }
        }

    } //testInsertPerson()
}
