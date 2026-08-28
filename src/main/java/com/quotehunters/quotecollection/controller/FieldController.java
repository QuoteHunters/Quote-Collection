package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.FieldDTO;
import com.quotehunters.quotecollection.model.service.FieldService;

import java.util.List;

public class FieldController {
    private FieldService fieldService = new FieldService();

    public List<FieldDTO> allFields() {
        return fieldService.allFields();
    }

    public int selectFieldId(int index) {
        return fieldService.allFields().get(index).getFieldId();
    }

    public boolean existsField(String fieldName) { return fieldService.existsFieldName(fieldName); }

    public boolean existsField(int id, String fieldName) { return fieldService.existsFieldName(id, fieldName); }

    public int updateField(int id, String fieldName) { return fieldService.updateField(id, fieldName); }

    public int insertField(String fieldName) { return fieldService.insertField(fieldName); }
}
