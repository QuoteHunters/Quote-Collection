package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.model.dao.FieldDTO;
import com.quotehunters.quotecollection.model.dto.FieldDAO;

import java.sql.Connection;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.close;
import static com.quotehunters.quotecollection.common.JDBC.getConnection;

public class FieldService {
    private FieldDAO fieldDAO = new FieldDAO();

    public List<FieldDTO> allFields() {
        Connection con = getConnection();
        List<FieldDTO> fields = fieldDAO.allFields(con);

        close(con);

        return fields;
    }
}
