package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.FieldDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
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
            System.out.println("분야 목록 조회 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(rset);
            JDBC.close(stmt);
        }

        return fields;
    }

    public FieldDTO searchFieldById(Connection connection, int id) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        FieldDTO fieldDTO = new FieldDTO();

        String query = prop.getProperty("searchById");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                fieldDTO.setFieldId(rset.getInt("field_id"));
                fieldDTO.setFieldName(rset.getString("field_name"));
            }
        } catch (SQLException e) {
            System.out.println("분야 조회 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return fieldDTO;
    }

    public boolean existsFieldName(Connection connection, String fieldName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String query = prop.getProperty("existsFieldName");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, fieldName);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                if (rset.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("분야명 중복 확인 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return false;
    }

    public boolean existsFieldName(Connection connection, int id, String fieldName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String query = prop.getProperty("existsFieldNameExceptId");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, fieldName);
            pstmt.setInt(2, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                if (rset.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Exists Field Name SQL Exception");
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return false;
    }

    public int updateField(Connection connection, int id, String fieldName) {
        PreparedStatement pstmt = null;
        String query = prop.getProperty("updateField");
        int result = 0;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, fieldName);
            pstmt.setInt(2, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("분야 수정 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }

    public int insertField(Connection connection, String fieldName) {
        PreparedStatement pstmt = null;

        int result = 0;

        String query = prop.getProperty("insertField");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, fieldName);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("분야 등록 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }

    public int deleteField(Connection connection, int id) {
        PreparedStatement pstmt = null;
        int result = 0;
        String query = prop.getProperty("deleteField");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("분야 삭제 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }
}
