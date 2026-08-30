package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.CountryDTO;
import com.quotehunters.quotecollection.model.dto.FieldDTO;
import com.quotehunters.quotecollection.model.dto.PeriodDTO;
import com.quotehunters.quotecollection.model.dto.PersonDTO;
import com.quotehunters.quotecollection.model.service.PersonService;
import com.quotehunters.quotecollection.view.*;

import java.util.List;
import java.util.Scanner;

public class PersonController {

    private PersonService personService = new PersonService();
    private PersonView personView = new PersonView();
    private final CountryView countryView = new CountryView();
    private final PeriodView periodView = new PeriodView();
    private final FieldView fieldView = new FieldView();
    private final CountryController countryController = new CountryController();
    private final PeriodController periodController = new PeriodController();
    private final FieldController fieldController = new FieldController();
    private final ScannerView scannerView = new ScannerView();

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

    public void registerPerson(ScannerView scannerView, Scanner scanner, ResultView resultView) {
        PersonDTO newPerson = new PersonDTO();
        int step = 1;

        while (step <= 4) {
            switch (step) {
                case 1:
                    if (!selectCountryForRegistration(scannerView, scanner, newPerson)) return;
                    step = 2;
                    break;
                case 2:
                    step = selectPeriodForRegistration(scannerView, scanner, newPerson) ? 3 : 1;
                    break;
                case 3:
                    step = selectFieldForRegistration(scannerView, scanner, newPerson) ? 4 : 2;
                    break;
                case 4:
                    String personName = personView.inputPersonName(scannerView, scanner);
                    if (personName == null) {
                        step = 3;
                    } else if (existsPersonName(personName)) {
                        resultView.errorMessage("이미 등록된 인물 이름입니다.");
                    } else {
                        newPerson.setPersonName(personName);
                        step = 5;
                    }
                    break;
                default:
                    break;
            }
        }

        registrationConfirmLoop:
        while (true) {
            personView.displayPersonForInsert(newPerson);
            String choice = personView.confirmPersonInsert(scannerView, scanner);

            if ("등록".equals(choice)) {
                int result = insertPerson(newPerson);
                if (result > 0) {
                    resultView.successMessage("인물이 등록되었습니다.");
                } else {
                    resultView.errorMessage("인물 등록에 실패했습니다.");
                }
                return;
            }

            String cancelAction = personView.selectPersonInsertCancelAction(scannerView, scanner);
            if ("완전 취소".equals(cancelAction)) {
                personView.printMessage("인물 등록을 취소했습니다.");
                return;
            }

            int section = personView.selectPersonInsertSection(scannerView, scanner);
            if (section == 0) continue;

            boolean changed = false;
            switch (section) {
                case 1:
                    changed = selectCountryForRegistration(scannerView, scanner, newPerson);
                    break;
                case 2:
                    changed = selectPeriodForRegistration(scannerView, scanner, newPerson);
                    break;
                case 3:
                    changed = selectFieldForRegistration(scannerView, scanner, newPerson);
                    break;
                case 4:
                    while (true) {
                        String changedName = personView.inputPersonName(scannerView, scanner);
                        if (changedName == null) break;
                        if (existsPersonName(changedName)) {
                            resultView.errorMessage("이미 등록된 인물 이름입니다.");
                            continue;
                        }
                        newPerson.setPersonName(changedName);
                        changed = true;
                        break;
                    }
                    break;
                default:
                    break;
            }
            if (changed) continue registrationConfirmLoop;
        }
    }

    private boolean selectCountryForRegistration(ScannerView scannerView, Scanner scanner, PersonDTO person) {
        int countryId = countryView.selectCountry(scannerView, scanner);
        if (countryId == 0) return false;
        for (CountryDTO country : countryController.allCountries()) {
            if (country.getCountryId() == countryId) {
                person.setCountryId(countryId);
                person.setCountryName(country.getCountryName());
                return true;
            }
        }
        return false;
    }

    private boolean selectPeriodForRegistration(ScannerView scannerView, Scanner scanner, PersonDTO person) {
        int periodId = periodView.selectPeriod(scannerView, scanner);
        if (periodId == 0) return false;
        for (PeriodDTO period : periodController.allPeriods()) {
            if (period.getPeriodId() == periodId) {
                person.setPeriodId(periodId);
                person.setPeriodName(period.getPeriodName());
                return true;
            }
        }
        return false;
    }

    private boolean selectFieldForRegistration(ScannerView scannerView, Scanner scanner, PersonDTO person) {
        int fieldId = fieldView.selectFields(scannerView, scanner);
        if (fieldId == 0) return false;
        for (FieldDTO field : fieldController.allFields()) {
            if (field.getFieldId() == fieldId) {
                person.setFieldId(fieldId);
                person.setFieldName(field.getFieldName());
                return true;
            }
        }
        return false;
    }

    public void updatePerson(Scanner scanner) {
        while (true) {
            int searchType = personView.selectPersonUpdateSearchType(scanner);
            if (searchType == 0) return;

            List<PersonDTO> personList = searchPersonsByCondition(scanner, searchType);
            if (personList == null) continue;

            if (personList.isEmpty()) {
                personView.printMessage("조회된 인물이 없습니다. 검색 방식을 다시 선택해주세요.");
                continue;
            }

            personView.displayAllPerson(personList);
            PersonDTO selectedPerson = personView.selectPerson(scanner, personList);
            if (selectedPerson == null) continue;

            int target = personView.selectPersonUpdateSection(scanner);
            if (target == 0) continue;

            switch (target) {
                case 1:
                    updatePersonCountryFlow(scanner, selectedPerson);
                    return;
                case 2:
                    updatePersonPeriodFlow(scanner, selectedPerson);
                    return;
                case 3:
                    updatePersonFieldFlow(scanner, selectedPerson);
                    return;
                case 4:
                    updatePersonNameFlow(scanner, selectedPerson);
                    return;
                default:
                    break;
            }
        }
    }

    private void updatePersonCountryFlow(Scanner scanner, PersonDTO person) {
        while (true) {
            int countryId = personView.selectCountryForUpdate(scanner, person, countryController.allCountries());
            if (countryId == 0) return;
            String decision = personView.confirmCountryUpdate(scanner);
            if ("취소".equals(decision)) return;
            if ("재수정".equals(decision)) continue;
            printUpdateResult(updatePersonCountry(person.getPersonId(), countryId));
            return;
        }
    }

    private void updatePersonPeriodFlow(Scanner scanner, PersonDTO person) {
        while (true) {
            int periodId = personView.selectPeriodForUpdate(scanner, person, periodController.allPeriods());
            if (periodId == 0) return;
            String decision = personView.confirmPeriodUpdate(scanner);
            if ("취소".equals(decision)) return;
            if ("재수정".equals(decision)) continue;
            printUpdateResult(updatePersonPeriod(person.getPersonId(), periodId));
            return;
        }
    }

    private void updatePersonFieldFlow(Scanner scanner, PersonDTO person) {
        while (true) {
            int fieldId = personView.selectFieldForUpdate(scanner, person, fieldController.allFields());
            if (fieldId == 0) return;
            String decision = personView.confirmFieldUpdate(scanner);
            if ("취소".equals(decision)) return;
            if ("재수정".equals(decision)) continue;
            printUpdateResult(updatePersonField(person.getPersonId(), fieldId));
            return;
        }
    }

    private void updatePersonNameFlow(Scanner scanner, PersonDTO person) {
        while (true) {
            String personName = personView.inputPersonNameForUpdate(scanner, person);
            if (personName == null) return;
            if (existsPersonName(personName)) {
                personView.printMessage("이미 등록된 인물 이름입니다.");
                continue;
            }
            String decision = personView.confirmPersonNameUpdate(scanner);
            if ("취소".equals(decision)) return;
            if ("재수정".equals(decision)) continue;
            printUpdateResult(updatePersonName(person.getPersonId(), personName));
            return;
        }
    }

    private void printUpdateResult(int result) {
        personView.printMessage(result > 0 ? "인물 정보가 수정되었습니다." : "인물 정보 수정에 실패했습니다.");
    }

    public void deletePerson(Scanner scanner) {
        while (true) {
            int searchType = personView.selectPersonDeleteSearchType(scanner);
            if (searchType == 0) return;

            List<PersonDTO> personList = searchPersonsByCondition(scanner, searchType);
            if (personList == null) continue;

            if (personList.isEmpty()) {
                personView.printMessage("조회된 인물이 없습니다. 검색 방식을 다시 선택해주세요.");
                continue;
            }

            personView.displayAllPerson(personList);
            PersonDTO selectedPerson = personView.selectPersonForDelete(scanner, personList);
            if (selectedPerson == null) continue;

            personView.displayPersonForDelete(selectedPerson);
            if ("취소".equals(personView.confirmPersonForDelete(scanner))) return;

            int result = deletePerson(selectedPerson.getPersonId());
            personView.printMessage(result > 0 ? "인물이 삭제되었습니다." : "인물 삭제에 실패했습니다.");
            return;
        }
    }

    private List<PersonDTO> searchPersonsByCondition(Scanner scanner, int searchType) {
        switch (searchType) {
            case 1:
                int countryId = countryView.selectCountry(scannerView, scanner);
                return countryId == 0 ? null : personService.selectPersonByCountry(countryId);
            case 2:
                int periodId = periodView.selectPeriod(scannerView, scanner);
                return periodId == 0 ? null : personService.selectPersonByPeriod(periodId);
            case 3:
                int fieldId = fieldView.selectFields(scannerView, scanner);
                return fieldId == 0 ? null : personService.selectPersonByField(fieldId);
            case 4:
                String personName = personView.inputPersonNameForSearch(scanner);
                return personName == null ? null : personService.selectPersonByName(personName);
            default:
                return null;
        }
    }
}
