package com.quotehunters.quotecollection.model.dto;


public class CountryDTO {


    private int countryId;        // country_id 컬럼 (국가 고유 번호)
    private String countryName;   // country_name 컬럼 (국가명)


    public CountryDTO() {}


    public CountryDTO(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }


    public int getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }


    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    @Override
    public String toString() {
        return countryId + ". " + countryName;   // 예: "1. 한국"
    }
}