package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.FieldController;
import com.quotehunters.quotecollection.model.dao.FieldDTO;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FieldView {
    private FieldController fieldController = new FieldController();

    public void allFields() {
        List<FieldDTO> fields = fieldController.allFields();

        System.out.println("----------------------------");
        if (fields.isEmpty()) {
            System.out.println("조회 결과 없음");
            return;
        }

        for (int i = 1; i < fields.size(); i++) {
            System.out.println(i + ". " + fields.get(i).getFieldName());
        }
        System.out.println("----------------------------");
    }
}
