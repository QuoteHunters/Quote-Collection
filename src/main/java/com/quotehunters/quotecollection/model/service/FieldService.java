package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.model.dto.FieldDTO;
import com.quotehunters.quotecollection.model.dao.FieldDAO;

import java.sql.Connection;
import java.sql.SQLException;
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

    public boolean existsFieldName(String fieldName) {
        Connection con = getConnection();

        try {
            if (fieldDAO.existsFieldName(con, fieldName)) {
                con.close();
                return true;
            }

            con.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsFieldName(int id, String fieldName) {
        Connection con = getConnection();

        try {
            if (fieldDAO.existsFieldName(con, id, fieldName)) {
                con.close();
                return true;
            }

            con.close();

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int updateField(int id, String fieldName) {
        Connection con = getConnection();
        int result = fieldDAO.updateField(con, id, fieldName);

        try {
            if (result > 0) {
                con.commit();
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con);
        }

        return result;
    }

    public int insertField(String fieldName) {
        Connection con = getConnection();

        int result = fieldDAO.insertField(con, fieldName);

        try {
            if (result > 0) {
                con.commit();
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con);
        }

        return result;
    }

    public int deleteField(int id) {
        Connection con = getConnection();

        int result = fieldDAO.deleteField(con, id);

        try {
            if (result > 0) {
                con.commit();
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con);
        }

        return result;
    }
}
