package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.CountryDTO;
import com.quotehunters.quotecollection.model.service.CountryService;

import java.util.List;

public class CountryController {


    private CountryService countryService = new CountryService();


    public List<CountryDTO> allCountries() {
        return countryService.allCountries();
    }

    // [등록] 검증 → 통과 시 등록 → 결과 메시지 반환 (명세 Country-001의 Validation)
    public String registCountry(String countryName) {

        // 검증 1: 빈 값 (null이거나, 공백만 친 경우)
        if (countryName == null || countryName.trim().isEmpty()) {
            return "국가명을 입력해주세요.";
        }

        // 검증 2: 글자 수 30자 제한
        if (countryName.length() > 30) {
            return "국가명은 30자 이하로 입력해주세요.";
        }

        // 검증 3: 중복
        if (countryService.existsCountryName(countryName)) {
            return "이미 등록된 국가명입니다.";
        }


        int result = countryService.insertCountry(countryName);


        if (result > 0) {
            return "국가가 등록되었습니다.";
        } else {
            return "국가 등록에 실패했습니다. 다시 시도해주세요.";
        }
    }

    // [수정] 검증 → 통과 시 수정 → 결과 메시지 반환 (명세 Country-003의 S5)
    public String modifyCountry(int countryId, String countryName) {

        // 검증 1: 빈 값
        if (countryName == null || countryName.trim().isEmpty()) {
            return "국가명을 입력해주세요.";
        }

        // 검증 2: 글자 수 (30자)
        if (countryName.length() > 30) {
            return "국가명은 30자 이하로 입력해주세요.";
        }

        // 검증 3: 중복
        if (countryService.existsCountryName(countryName)) {
            return "이미 등록된 국가명입니다.";
        }


        int result = countryService.updateCountry(countryId, countryName);

        if (result > 0) {
            return "국가가 수정되었습니다.";
        } else {
            return "국가 수정에 실패했습니다. 다시 시도해주세요.";
        }
    }
    // [삭제] 연쇄 삭제 실행
    public String removeCountry(int countryId) {
        int result = countryService.deleteCountry(countryId);   // 트랜잭션 삭제 실행

        if (result > 0) {
            return "국가가 삭제되었습니다.";      // 명세 S8-1의 완료 메시지
        } else {
            return "국가 삭제에 실패했습니다. 다시 시도해주세요.";
        }
    }
}
