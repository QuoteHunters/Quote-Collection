package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.ThemeDAO;
import com.quotehunters.quotecollection.model.dto.ThemeDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.close;

public class ThemeService {
    private final ThemeDAO themeDAO = new ThemeDAO();

    public int insertTheme(String themeName) {
        Connection con = null;

        try {
            con = JDBC.getConnection();
            int result = themeDAO.insertTheme(con, themeName);

            if (result > 0) {
                con.commit();
            } else {
                con.rollback();
            }

            return result;
        } catch (SQLException e) {
            IllegalStateException failure =
                    new IllegalStateException("주제 등록 중 오류가 발생했습니다.", e);
            rollback(con, failure);
            throw failure;
        } catch (RuntimeException e) {
            rollback(con, e);
            throw e;
        } finally {
            close(con);
        }
    }

    public boolean existsTheme(String themeName) {
        Connection con = null;

        try {
            con = JDBC.getConnection();
            return themeDAO.existsThemeName(con, themeName);
        } finally {
            close(con);
        }
    }

    public boolean existsTheme(int id, String themeName) {
        Connection con = null;

        try {
            con = JDBC.getConnection();
            return themeDAO.existsThemeName(con, id, themeName);
        } finally {
            close(con);
        }
    }

    public List<ThemeDTO> selectThemes() {
        Connection con = null;

        try {
            con = JDBC.getConnection();
            return themeDAO.selectThemes(con);
        } finally {
            close(con);
        }
    }

    public int updateTheme(int id, String themeName) {
        Connection con = null;

        try {
            con = JDBC.getConnection();
            int result = themeDAO.updateTheme(con, id, themeName);

            if (result > 0) {
                con.commit();
            } else {
                con.rollback();
            }

            return result;
        } catch (SQLException e) {
            IllegalStateException failure =
                    new IllegalStateException("주제 수정 중 오류가 발생했습니다.", e);
            rollback(con, failure);
            throw failure;
        } catch (RuntimeException e) {
            rollback(con, e);
            throw e;
        } finally {
            close(con);
        }
    }

    public int deleteTheme(int id) {
        Connection con = null;

        try {
            con = JDBC.getConnection();
            int result = themeDAO.deleteTheme(con, id);

            if (result > 0) {
                con.commit();
            } else {
                con.rollback();
            }

            return result;
        } catch (SQLException e) {
            IllegalStateException failure =
                    new IllegalStateException("주제 삭제 중 오류가 발생했습니다.", e);
            rollback(con, failure);
            throw failure;
        } catch (RuntimeException e) {
            rollback(con, e);
            throw e;
        } finally {
            close(con);
        }
    }

    private void rollback(Connection con, RuntimeException failure) {
        if (con == null) {
            return;
        }

        try {
            con.rollback();
        } catch (SQLException e) {
            failure.addSuppressed(e);
        }
    }
}
