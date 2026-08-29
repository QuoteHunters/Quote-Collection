package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.ThemeDTO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

public class ThemeDAO {
    Properties prop = new Properties();

    public ThemeDAO() {
        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/quotehunters/quotecollection/mapper/theme-query.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int insertTheme(Connection con, String themeName) {
        PreparedStatement pstmt = null;
        String query = prop.getProperty("insertTheme");
        int result = 0;

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, themeName);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert theme failed " + e.getMessage());
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }

    public boolean existsThemeName(Connection con, String themeName) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = prop.getProperty("existsThemeName");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, themeName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }

            return false;
        } catch (SQLException e) {
            System.out.println("Exists theme name failed " + e.getMessage());
            return false;
        } finally {
            JDBC.close(rs);
            JDBC.close(pstmt);
        }
    }

    public List<ThemeDTO> selectThemes(Connection con) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = prop.getProperty("selectThemes");
        List<ThemeDTO> themes = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ThemeDTO theme = new ThemeDTO();
                theme.setTheme_id(rs.getInt("theme_id"));
                theme.setTheme_name(rs.getString("theme_name"));

                themes.add(theme);
            }
        } catch (SQLException e) {
            System.out.println("Select themes failed " + e.getMessage());
        } finally {
            JDBC.close(rs);
            JDBC.close(pstmt);
        }

        return themes;
    }
}
