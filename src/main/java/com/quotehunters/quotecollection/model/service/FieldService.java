package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.model.dto.FieldDTO;
import com.quotehunters.quotecollection.model.dao.FieldDAO;

import java.sql.Connection;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.*;

public class FieldService {
    private FieldDAO fieldDAO = new FieldDAO();

    public List<FieldDTO> allFields() {
        Connection con = getConnection();

        try {
            return fieldDAO.allFields(con);
        } finally {
            close(con);
        }
    }

    public boolean existsFieldName(String fieldName) {
        Connection con = getConnection();
        boolean exists;

        try {
            exists = fieldDAO.existsFieldName(con, fieldName);
        } finally {
            close(con);
        }

        return exists;
    }

    public boolean existsFieldName(int id, String fieldName) {
        Connection con = getConnection();
        boolean exists;

        try {
            exists = fieldDAO.existsFieldName(con, id, fieldName);
        } finally {
            close(con);
        }

        return exists;
    }

    public int updateField(int id, String fieldName) {
        Connection con = getConnection();

        try {
            int result;
            try {
                result = fieldDAO.updateField(con, id, fieldName);
            } catch (RuntimeException e) {
                rollbackAfterFailure(con, e);
                throw e;
            }

            finishTransaction(con, result);
            return result;
        } finally {
            close(con);
        }
    }

    public int insertField(String fieldName) {
        Connection con = getConnection();

        try {
            int result;
            try {
                result = fieldDAO.insertField(con, fieldName);
            } catch (RuntimeException e) {
                rollbackAfterFailure(con, e);
                throw e;
            }

            finishTransaction(con, result);
            return result;
        } finally {
            close(con);
        }
    }

    public int deleteField(int id) {
        Connection con = getConnection();

        try {
            int result;
            try {
                result = fieldDAO.deleteField(con, id);
            } catch (RuntimeException e) {
                rollbackAfterFailure(con, e);
                throw e;
            }

            finishTransaction(con, result);
            return result;
        } finally {
            close(con);
        }
    }

    private void finishTransaction(Connection con, int result) {
        if (result <= 0) {
            rollback(con);
            return;
        }

        try {
            commit(con);
        } catch (RuntimeException e) {
            rollbackAfterFailure(con, e);
            throw e;
        }
    }

    private void rollbackAfterFailure(Connection con, RuntimeException failure) {
        try {
            rollback(con);
        } catch (RuntimeException rollbackFailure) {
            failure.addSuppressed(rollbackFailure);
        }
    }
}
