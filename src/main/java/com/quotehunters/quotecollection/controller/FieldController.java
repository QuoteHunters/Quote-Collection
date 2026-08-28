package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dao.FieldDTO;
import com.quotehunters.quotecollection.model.service.FieldService;

import java.util.List;

public class FieldController {
    private FieldService fieldService = new FieldService();

    public List<FieldDTO> allFields() {
        return fieldService.allFields();
    }
}
