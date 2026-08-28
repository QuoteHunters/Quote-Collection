package com.quotehunters.quotecollection.model.dto;

public class FieldDTO {
    private int fieldId;
    private String fieldName;

    public FieldDTO() {}

    public FieldDTO(String fieldName, int fieldId) {
        this.fieldName = fieldName;
        this.fieldId = fieldId;
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
        return "FieldDTO{" +
                "fieldId=" + fieldId +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }
}
