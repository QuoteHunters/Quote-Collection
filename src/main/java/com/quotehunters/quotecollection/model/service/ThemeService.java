package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.ThemeDAO;
import com.quotehunters.quotecollection.model.dto.ThemeDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.close;

public class ThemeService {
    private final ThemeDAO themeDAO = new ThemeDAO();

    public int insertTheme(String themeName) {
        Connection con = JDBC.getConnection();
        int result = themeDAO.insertTheme(con, themeName);

        try {
            if(result > 0) {
                con.commit();
            } else  {
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con);
        }

        return result;
    }

    public boolean existsTheme(String themeName) {
        Connection con = JDBC.getConnection();

        if (themeDAO.existsThemeName(con, themeName)) {
            close(con);
            return true;
        }

        close(con);
        return false;
    }

    public boolean existsTheme(int id, String themeName) {
        Connection con = JDBC.getConnection();

        if (themeDAO.existsThemeName(con, id, themeName)) {
            close(con);
            return true;
        }

        close(con);
        return false;
    }

    public List<ThemeDTO> selectThemes() {
        Connection con = JDBC.getConnection();
        List<ThemeDTO> themes = themeDAO.selectThemes(con);

        close(con);

        return themes;
    }

    public int updateTheme(int id, String themeName) {
        Connection con = JDBC.getConnection();
        int result = themeDAO.updateTheme(con, id, themeName);

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

    public int deleteTheme(int id) {
        Connection con = JDBC.getConnection();
        int result = themeDAO.deleteTheme(con, id);
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
