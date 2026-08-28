package com.quotehunters.quotecollection.model.dto;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.FieldDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FieldDAO {
    private Properties prop = new Properties();

    // 기본 생성자로 FieldDAO를 사용하면 제일 먼저 실행되게 함.
    public FieldDAO() {
        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/quotehunters/quotecollection/mapper/field-query.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FieldDTO> allFields(Connection connection) {
        Statement stmt = null;
        ResultSet rset = null;
        List<FieldDTO> fields = new ArrayList<>();

        String query = prop.getProperty("allField");

        try {
            stmt = connection.createStatement();
            rset = stmt.executeQuery(query);

            while (rset.next()) {
                FieldDTO fieldDTO = new FieldDTO();
                fieldDTO.setFieldId(rset.getInt("field_id"));
                fieldDTO.setFieldName(rset.getString("field_name"));

                fields.add(fieldDTO);
            }
        } catch (SQLException e) {
            System.out.println("All Fields SQL Exception");
        } finally {
            JDBC.close(rset);
            JDBC.close(stmt);
        }

        return fields;
    }
}
