package com.quotehunters.quotecollection.model.dto;

public class QuoteDTO {
    private int quoteId;
    private String quoteContent;

    private String personName;
    private String themeName;
    private String countryName;
    private String periodName;
    private String fieldName;

    private int themeId;
    private int personId;


    @Override
    public String toString() {
        return "QuoteDTO{" +
                "quoteId=" + quoteId +
                ", quoteContent='" + quoteContent + '\'' +
                ", personName='" + personName + '\'' +
                ", themeName='" + themeName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", periodName='" + periodName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", themeId=" + themeId +
                ", personId=" + personId +
                '}';
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public QuoteDTO() {}

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuoteContent() {
        return quoteContent;
    }

    public void setQuoteContent(String quoteContent) {
        this.quoteContent = quoteContent;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}
