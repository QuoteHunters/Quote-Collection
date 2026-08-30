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
}
