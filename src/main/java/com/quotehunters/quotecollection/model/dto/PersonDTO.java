package com.quotehunters.quotecollection.model.dto;

public class PersonDTO {
    private int personId; // 인물 고유 번호
    private String personName; // 인물 이름
    private int countryId; // 국가 고유 번호
    private String countryName; // 국가명
    private int periodId; // 시대 고유 번호
    private String periodName; // 시대명
    private int fieldId; // 분야 고유 번호
    private String fieldName; // 분야명

    public PersonDTO() {
    }

    public PersonDTO(int personId, String personName, int countryId, String countryName, int periodId,
                     String periodName, int fieldId, String fieldName) {
        this.personId = personId;
        this.personName = personName;
        this.countryId = countryId;
        this.countryName = countryName;
        this.periodId = periodId;
        this.periodName = periodName;
        this.fieldId = fieldId;
        this.fieldName = fieldName;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "personId=" + personId +
                ", personName='" + personName + '\'' +
                ", countryId=" + countryId +
                ", countryName='" + countryName + '\'' +
                ", periodId=" + periodId +
                ", periodName='" + periodName + '\'' +
                ", fieldId=" + fieldId +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }
}
