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

    private static final int UPDATE_BACK = 0;
    private static final int UPDATE_COMPLETE = 1;
    private static final int UPDATE_CANCEL = 2;
    private static final int REGISTRATION_BACK = 0;
    private static final int REGISTRATION_COMPLETE = 1;
    private static final int REGISTRATION_CANCEL = 2;
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
        try {
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
        } catch (RuntimeException e) {
            personView.printMessage("데이터베이스 오류로 인물 조회에 실패했습니다.");
            return null;
        }
    }

    // 2. 국가별 인물 조회
    public void selectPersonByCountry(ScannerView scannerView, Scanner scanner) {
        try {
            int countryId = countryView.selectCountry(scannerView, scanner);
            if (countryId <= 0) return;
            List<PersonDTO> personList = personService.selectPersonByCountry(countryId);

            if (personList.isEmpty()) {
                personView.printMessage("해당 국가에 등록된 인물이 없습니다.");
            } else {
                personView.selectPersonByCountry(personList);
            }
        } catch (RuntimeException e) {
            personView.printMessage("데이터베이스 오류로 국가별 인물 조회에 실패했습니다.");
        }
    }

    // 3. 시대별 인물 조회
    public void selectPersonByPeriod(ScannerView scannerView, Scanner scanner) {
        try {
            int periodId = periodView.selectPeriod(scannerView, scanner);
            if (periodId == 0) return;
            List<PersonDTO> personList = personService.selectPersonByPeriod(periodId);

            if (personList.isEmpty()) {
                personView.printMessage("해당 시대에 등록된 인물이 없습니다.");
            } else {
                personView.selectPersonByPeriod(personList);
            }
        } catch (RuntimeException e) {
            personView.printMessage("데이터베이스 오류로 시대별 인물 조회에 실패했습니다.");
        }
    }

    // 4. 분야별 인물 조회
    public void selectPersonByField(ScannerView scannerView, Scanner scanner) {
        try {
            int fieldId = fieldView.selectFields(scannerView, scanner);
            if (fieldId == 0) return;

            List<PersonDTO> personList = personService.selectPersonByField(fieldId);

            if (personList.isEmpty()) {
                personView.printMessage("해당 분야에 등록된 인물이 없습니다.");
            } else {
                personView.selectPersonByField(personList);
            }
        } catch (RuntimeException e) {
            personView.printMessage("데이터베이스 오류로 분야별 인물 조회에 실패했습니다.");
        }
    }

    // 5. 인물 이름 조회
    public void selectPersonByName(Scanner scanner) {
        String personName = personView.inputPersonNameForSearch(scanner);
        if (personName == null) return;
        selectPersonByName(personName);
    }

    public void selectPersonByName(String personName) {
        try {
            List<PersonDTO> personList = personService.selectPersonByName(personName);

            if (personList.isEmpty()) {
                personView.printMessage("'" + personName + "'(으)로 조회된 인물이 없습니다.");
            } else {
                personView.selectPersonByName(personList);
            }
        } catch (RuntimeException e) {
            personView.printMessage("데이터베이스 오류로 인물 이름 조회에 실패했습니다.");
        }
    }

    // 6. 명언 키워드 검색에 따른 인물 조회
    public void selectPersonByQuoteKeyword(Scanner scanner) {
        String quoteKeyword = personView.inputQuoteKeywordForSearch(scanner);
        if (quoteKeyword == null) return;
        selectPersonByQuoteKeyword(quoteKeyword);
    }

    public void selectPersonByQuoteKeyword(String quoteKeyword) {
        try {
            List<PersonDTO> personList = personService.selectPersonByQuoteKeyword(quoteKeyword);

            if (personList.isEmpty()) {
                personView.printMessage("'" + quoteKeyword + "' 키워드가 포함된 명언을 남긴 인물이 없습니다.");
            } else {
                personView.selectPersonByQuoteKeyword(personList, quoteKeyword);
            }
        } catch (RuntimeException e) {
            personView.printMessage("데이터베이스 오류로 명언 키워드 인물 조회에 실패했습니다.");
        }
    }

    public PersonDTO selectPersonForQuoteBrowse(int searchType, ScannerView scannerView, Scanner scanner) {
        int categoryId = 0;
        String searchText = null;

        switch (searchType) {
            case 1:
                break;
            case 2:
                categoryId = countryView.selectCountry(scannerView, scanner);
                if (categoryId <= 0) return null;
                break;
            case 3:
                categoryId = periodView.selectPeriod(scannerView, scanner);
                if (categoryId == 0) return null;
                break;
            case 4:
                categoryId = fieldView.selectFields(scannerView, scanner);
                if (categoryId == 0) return null;
                break;
            case 5:
                searchText = personView.inputPersonNameForSearch(scanner);
                if (searchText == null) return null;
                break;
            case 6:
                searchText = personView.inputQuoteKeywordForSearch(scanner);
                if (searchText == null) return null;
                break;
            default:
                return null;
        }

        while (true) {
            try {
                List<PersonDTO> personList = selectPersonsForQuoteBrowse(searchType, categoryId, searchText);

                if (personList.isEmpty()) {
                    printEmptyPersonBrowseResult(searchType, searchText);
                    return null;
                }

                displayPersonBrowseResult(searchType, personList, searchText);
                return personView.selectPersonForQuoteBrowse(scanner, personList);
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 인물을 조회하지 못했습니다.");
                if (!personView.retryPersonBrowse(scanner)) return null;
            }
        }
    }

    private List<PersonDTO> selectPersonsForQuoteBrowse(int searchType, int categoryId, String searchText) {
        switch (searchType) {
            case 1:
                return personService.selectAllPerson();
            case 2:
                return personService.selectPersonByCountry(categoryId);
            case 3:
                return personService.selectPersonByPeriod(categoryId);
            case 4:
                return personService.selectPersonByField(categoryId);
            case 5:
                return personService.selectPersonByName(searchText);
            case 6:
                return personService.selectPersonByQuoteKeyword(searchText);
            default:
                throw new IllegalArgumentException("지원하지 않는 인물 검색 방식입니다.");
        }
    }

    private void displayPersonBrowseResult(int searchType, List<PersonDTO> personList, String searchText) {
        switch (searchType) {
            case 1:
                personView.displayAllPerson(personList);
                break;
            case 2:
                personView.selectPersonByCountry(personList);
                break;
            case 3:
                personView.selectPersonByPeriod(personList);
                break;
            case 4:
                personView.selectPersonByField(personList);
                break;
            case 5:
                personView.selectPersonByName(personList);
                break;
            case 6:
                personView.selectPersonByQuoteKeyword(personList, searchText);
                break;
            default:
                break;
        }
    }

    private void printEmptyPersonBrowseResult(int searchType, String searchText) {
        switch (searchType) {
            case 1:
                personView.printMessage("등록된 인물이 없습니다.");
                break;
            case 2:
                personView.printMessage("해당 국가에 등록된 인물이 없습니다.");
                break;
            case 3:
                personView.printMessage("해당 시대에 등록된 인물이 없습니다.");
                break;
            case 4:
                personView.printMessage("해당 분야에 등록된 인물이 없습니다.");
                break;
            case 5:
                personView.printMessage("'" + searchText + "'(으)로 조회된 인물이 없습니다.");
                break;
            case 6:
                personView.printMessage("'" + searchText + "' 키워드가 포함된 명언을 남긴 인물이 없습니다.");
                break;
            default:
                break;
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

    public boolean registerPerson(ScannerView scannerView, Scanner scanner, ResultView resultView) {
        return registerPersonFlow(scannerView, scanner, resultView);
    }

    private boolean registerPersonFlow(ScannerView scannerView, Scanner scanner, ResultView resultView) {
        PersonDTO newPerson = new PersonDTO();
        int step = 1;

        while (step <= 4) {
            switch (step) {
                case 1:
                    int countryResult = selectCountryForRegistration(scannerView, scanner, newPerson);
                    if (countryResult == REGISTRATION_CANCEL) return true;
                    if (countryResult == REGISTRATION_BACK) return false;
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
                    } else {
                        try {
                            if (existsPersonName(personName)) {
                                resultView.errorMessage("이미 등록된 인물 이름입니다.");
                            } else {
                                newPerson.setPersonName(personName);
                                step = 5;
                            }
                        } catch (RuntimeException e) {
                            resultView.errorMessage("데이터베이스 오류로 인물 이름을 확인하지 못했습니다. 다시 입력해주세요.");
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        while (true) {
            personView.displayPersonForInsert(newPerson);
            String choice = personView.confirmPersonInsert(scannerView, scanner);

            if ("취소".equals(choice)) {
                personView.printMessage("인물 등록을 취소했습니다.");
                return true;
            }

            if ("수정".equals(choice)) {
                if (modifyPersonRegistration(scannerView, scanner, resultView, newPerson)) {
                    return true;
                }
                continue;
            }

            try {
                int result = insertPerson(newPerson);
                if (result > 0) {
                    resultView.successMessage("인물이 등록되었습니다.");
                    return false;
                }
                resultView.errorMessage("인물 등록에 실패했습니다. 다시 선택해주세요.");
            } catch (RuntimeException e) {
                resultView.errorMessage("데이터베이스 오류로 인물 등록에 실패했습니다. 다시 선택해주세요.");
            }
        }
    }

    private boolean modifyPersonRegistration(
            ScannerView scannerView,
            Scanner scanner,
            ResultView resultView,
            PersonDTO person
    ) {
        while (true) {
            int section = personView.selectPersonInsertSection(scanner);
            if (section == 0) return false;

            switch (section) {
                case 1:
                    int countryResult = selectCountryForRegistration(scannerView, scanner, person);
                    if (countryResult == REGISTRATION_CANCEL) return true;
                    if (countryResult == REGISTRATION_COMPLETE) return false;
                    break;
                case 2:
                    if (selectPeriodForRegistration(scannerView, scanner, person)) return false;
                    break;
                case 3:
                    if (selectFieldForRegistration(scannerView, scanner, person)) return false;
                    break;
                case 4:
                    if (changePersonNameForRegistration(scannerView, scanner, resultView, person)) return false;
                    break;
                default:
                    break;
            }
        }
    }

    private boolean changePersonNameForRegistration(
            ScannerView scannerView,
            Scanner scanner,
            ResultView resultView,
            PersonDTO person
    ) {
        while (true) {
            String personName = personView.inputPersonName(scannerView, scanner);
            if (personName == null) return false;

            try {
                if (existsPersonName(personName)) {
                    resultView.errorMessage("이미 등록된 인물 이름입니다.");
                    continue;
                }
                person.setPersonName(personName);
                return true;
            } catch (RuntimeException e) {
                resultView.errorMessage("데이터베이스 오류로 인물 이름을 확인하지 못했습니다. 다시 입력해주세요.");
            }
        }
    }

    private int selectCountryForRegistration(ScannerView scannerView, Scanner scanner, PersonDTO person) {
        while (true) {
            int countryId = countryView.selectCountry(scannerView, scanner);
            if (countryId == CountryView.SELECT_CANCEL) return REGISTRATION_CANCEL;
            if (countryId == 0) return REGISTRATION_BACK;

            try {
                for (CountryDTO country : countryController.allCountries()) {
                    if (country.getCountryId() == countryId) {
                        person.setCountryId(countryId);
                        person.setCountryName(country.getCountryName());
                        return REGISTRATION_COMPLETE;
                    }
                }
                personView.printMessage("선택한 국가 정보를 확인할 수 없습니다.");
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 국가 목록을 조회하지 못했습니다.");
            }

            if (!personView.retryCategoryList(scanner, "국가")) return REGISTRATION_CANCEL;
        }
    }

    private boolean selectPeriodForRegistration(ScannerView scannerView, Scanner scanner, PersonDTO person) {
        while (true) {
            int periodId = periodView.selectPeriod(scannerView, scanner);
            if (periodId == 0) return false;

            try {
                for (PeriodDTO period : periodController.allPeriods()) {
                    if (period.getPeriodId() == periodId) {
                        person.setPeriodId(periodId);
                        person.setPeriodName(period.getPeriodName());
                        return true;
                    }
                }
                personView.printMessage("선택한 시대 정보를 확인할 수 없습니다.");
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 시대 목록을 조회하지 못했습니다.");
            }

            if (!personView.retryCategoryList(scanner, "시대")) return false;
        }
    }

    private boolean selectFieldForRegistration(ScannerView scannerView, Scanner scanner, PersonDTO person) {
        while (true) {
            int fieldId = fieldView.selectFields(scannerView, scanner);
            if (fieldId == 0) return false;

            try {
                for (FieldDTO field : fieldController.allFields()) {
                    if (field.getFieldId() == fieldId) {
                        person.setFieldId(fieldId);
                        person.setFieldName(field.getFieldName());
                        return true;
                    }
                }
                personView.printMessage("선택한 분야 정보를 확인할 수 없습니다.");
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 분야 목록을 조회하지 못했습니다.");
            }

            if (!personView.retryCategoryList(scanner, "분야")) return false;
        }
    }

    public boolean updatePerson(Scanner scanner) {
        return updatePersonFlow(scanner);
    }

    private boolean updatePersonFlow(Scanner scanner) {
        while (true) {
            int searchType = personView.selectPersonUpdateSearchType(scanner);
            if (searchType == 0) return false;

            List<PersonDTO> personList;
            try {
                personList = searchPersonsByCondition(scanner, searchType);
            } catch (AdminMenuCancelException e) {
                return true;
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 인물을 조회하지 못했습니다. 다시 선택해주세요.");
                continue;
            }
            if (personList == null) continue;

            if (personList.isEmpty()) {
                personView.printMessage("조회된 인물이 없습니다. 검색 방식을 다시 선택해주세요.");
                continue;
            }

            while (true) {
                personView.displayAllPerson(personList);
                PersonDTO selectedPerson = personView.selectPerson(scanner, personList);
                if (selectedPerson == null) break;

                while (true) {
                    int target = personView.selectPersonUpdateSection(scanner);
                    if (target == 0) break;

                    int updateResult = UPDATE_BACK;
                    switch (target) {
                        case 1:
                            updateResult = updatePersonCountryFlow(scanner, selectedPerson);
                            break;
                        case 2:
                            updateResult = updatePersonPeriodFlow(scanner, selectedPerson);
                            break;
                        case 3:
                            updateResult = updatePersonFieldFlow(scanner, selectedPerson);
                            break;
                        case 4:
                            updateResult = updatePersonNameFlow(scanner, selectedPerson);
                            break;
                        default:
                            break;
                    }

                    if (updateResult == UPDATE_CANCEL) return true;
                    if (updateResult == UPDATE_COMPLETE) return false;
                }
            }
        }
    }

    private int updatePersonCountryFlow(Scanner scanner, PersonDTO person) {
        while (true) {
            List<CountryDTO> countryList;
            try {
                countryList = countryController.allCountries();
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 국가 목록을 조회하지 못했습니다.");
                if (personView.retryCategoryList(scanner, "국가")) continue;
                return UPDATE_CANCEL;
            }

            int countryId = personView.selectCountryForUpdate(scanner, person, countryList);
            if (countryId == 0) return UPDATE_BACK;

            while (true) {
                String decision = personView.confirmCountryUpdate(scanner);
                if ("취소".equals(decision)) return UPDATE_CANCEL;
                if ("재수정".equals(decision)) break;
                try {
                    int result = updatePersonCountry(person.getPersonId(), countryId);
                    if (result > 0) {
                        personView.printMessage("인물 정보가 수정되었습니다.");
                        return UPDATE_COMPLETE;
                    }
                    personView.printMessage("인물 정보 수정에 실패했습니다. 다시 선택해주세요.");
                } catch (RuntimeException e) {
                    personView.printMessage("데이터베이스 오류로 인물 국가 수정에 실패했습니다. 다시 선택해주세요.");
                }
            }
        }
    }

    private int updatePersonPeriodFlow(Scanner scanner, PersonDTO person) {
        while (true) {
            List<PeriodDTO> periodList;
            try {
                periodList = periodController.allPeriods();
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 시대 목록을 조회하지 못했습니다.");
                if (personView.retryCategoryList(scanner, "시대")) continue;
                return UPDATE_CANCEL;
            }

            int periodId = personView.selectPeriodForUpdate(scanner, person, periodList);
            if (periodId == 0) return UPDATE_BACK;

            while (true) {
                String decision = personView.confirmPeriodUpdate(scanner);
                if ("취소".equals(decision)) return UPDATE_CANCEL;
                if ("재수정".equals(decision)) break;
                try {
                    int result = updatePersonPeriod(person.getPersonId(), periodId);
                    if (result > 0) {
                        personView.printMessage("인물 정보가 수정되었습니다.");
                        return UPDATE_COMPLETE;
                    }
                    personView.printMessage("인물 정보 수정에 실패했습니다. 다시 선택해주세요.");
                } catch (RuntimeException e) {
                    personView.printMessage("데이터베이스 오류로 인물 시대 수정에 실패했습니다. 다시 선택해주세요.");
                }
            }
        }
    }

    private int updatePersonFieldFlow(Scanner scanner, PersonDTO person) {
        while (true) {
            List<FieldDTO> fieldList;
            try {
                fieldList = fieldController.allFields();
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 분야 목록을 조회하지 못했습니다.");
                if (personView.retryCategoryList(scanner, "분야")) continue;
                return UPDATE_CANCEL;
            }

            int fieldId = personView.selectFieldForUpdate(scanner, person, fieldList);
            if (fieldId == 0) return UPDATE_BACK;

            while (true) {
                String decision = personView.confirmFieldUpdate(scanner);
                if ("취소".equals(decision)) return UPDATE_CANCEL;
                if ("재수정".equals(decision)) break;
                try {
                    int result = updatePersonField(person.getPersonId(), fieldId);
                    if (result > 0) {
                        personView.printMessage("인물 정보가 수정되었습니다.");
                        return UPDATE_COMPLETE;
                    }
                    personView.printMessage("인물 정보 수정에 실패했습니다. 다시 선택해주세요.");
                } catch (RuntimeException e) {
                    personView.printMessage("데이터베이스 오류로 인물 분야 수정에 실패했습니다. 다시 선택해주세요.");
                }
            }
        }
    }

    private int updatePersonNameFlow(Scanner scanner, PersonDTO person) {
        while (true) {
            String personName = personView.inputPersonNameForUpdate(scanner, person);
            if (personName == null) return UPDATE_BACK;
            try {
                if (existsPersonName(personName)) {
                    personView.printMessage("이미 등록된 인물 이름입니다.");
                    continue;
                }
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 인물 이름을 확인하지 못했습니다. 다시 입력해주세요.");
                continue;
            }

            while (true) {
                String decision = personView.confirmPersonNameUpdate(scanner);
                if ("취소".equals(decision)) return UPDATE_CANCEL;
                if ("재수정".equals(decision)) break;
                try {
                    int result = updatePersonName(person.getPersonId(), personName);
                    if (result > 0) {
                        personView.printMessage("인물 정보가 수정되었습니다.");
                        return UPDATE_COMPLETE;
                    }
                    personView.printMessage("인물 정보 수정에 실패했습니다. 다시 선택해주세요.");
                } catch (RuntimeException e) {
                    personView.printMessage("데이터베이스 오류로 인물 이름 수정에 실패했습니다. 다시 선택해주세요.");
                }
            }
        }
    }

    public boolean deletePerson(Scanner scanner) {
        return deletePersonFlow(scanner);
    }

    private boolean deletePersonFlow(Scanner scanner) {
        while (true) {
            int searchType = personView.selectPersonDeleteSearchType(scanner);
            if (searchType == 0) return false;

            List<PersonDTO> personList;
            try {
                personList = searchPersonsByCondition(scanner, searchType);
            } catch (AdminMenuCancelException e) {
                return true;
            } catch (RuntimeException e) {
                personView.printMessage("데이터베이스 오류로 인물을 조회하지 못했습니다. 다시 선택해주세요.");
                continue;
            }
            if (personList == null) continue;

            if (personList.isEmpty()) {
                personView.printMessage("조회된 인물이 없습니다. 검색 방식을 다시 선택해주세요.");
                continue;
            }

            personView.displayAllPerson(personList);
            PersonDTO selectedPerson = personView.selectPersonForDelete(scanner, personList);
            if (selectedPerson == null) continue;

            personView.displayPersonForDelete(selectedPerson);
            while (true) {
                if ("아니오".equals(personView.confirmPersonForDelete(scanner))) {
                    personView.printMessage("인물 삭제를 취소했습니다.");
                    return true;
                }

                try {
                    int result = deletePerson(selectedPerson.getPersonId());
                    if (result > 0) {
                        personView.printMessage("인물이 삭제되었습니다.");
                        return false;
                    }
                    personView.printMessage("인물 삭제에 실패했습니다. 다시 선택해주세요.");
                } catch (RuntimeException e) {
                    personView.printMessage("데이터베이스 오류로 인물 삭제에 실패했습니다. 다시 선택해주세요.");
                }
            }
        }
    }

    private List<PersonDTO> searchPersonsByCondition(Scanner scanner, int searchType) {
        switch (searchType) {
            case 1:
                int countryId = countryView.selectCountry(scannerView, scanner);
                if (countryId == CountryView.SELECT_CANCEL) {
                    throw new AdminMenuCancelException();
                }
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
            case 5:
                String quoteKeyword = personView.inputQuoteKeywordForSearch(scanner);
                return quoteKeyword == null ? null : personService.selectPersonByQuoteKeyword(quoteKeyword);
            default:
                return null;
        }
    }

    private static class AdminMenuCancelException extends RuntimeException {
    }
}
