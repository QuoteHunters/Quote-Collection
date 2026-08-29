package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.ThemeDAO;

import java.sql.Connection;
import java.sql.SQLException;

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

        } finally {
            JDBC.close(con);
        }

        return result;
    }

    public boolean existsTheme(String themeName) {
        Connection con = JDBC.getConnection();

        try {
            if (themeDAO.existsThemeName(con, themeName)) {
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
}
