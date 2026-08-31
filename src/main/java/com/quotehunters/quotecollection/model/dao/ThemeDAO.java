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
            throw new IllegalStateException("주제 쿼리 설정을 불러오는 중 오류가 발생했습니다.", e);
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
            System.out.println("주제 등록 중 오류가 발생했습니다.");
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
            System.out.println("주제명 중복 확인 중 오류가 발생했습니다.");
            return false;
        } finally {
            JDBC.close(rs);
            JDBC.close(pstmt);
        }
    }

    public boolean existsThemeName(Connection connection, int id, String fieldName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String query = prop.getProperty("existsThemeNameExceptId");

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
            System.out.println("주제명 중복 확인 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return false;
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
            System.out.println("주제 목록 조회 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(rs);
            JDBC.close(pstmt);
        }

        return themes;
    }

    public int updateTheme(Connection con, int id, String themeName) {
        PreparedStatement pstmt = null;
        String query = prop.getProperty("updateTheme");
        int result = 0;

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, themeName);
            pstmt.setInt(2, id);
            result = pstmt.executeUpdate();
            return result;
        } catch (SQLException e) {
            System.out.println("주제 수정 중 오류가 발생했습니다.");
            return result;
        } finally {
            JDBC.close(pstmt);
        }
    }

    public int deleteTheme(Connection con, int id) {
        PreparedStatement pstmt = null;
        String query = prop.getProperty("deleteTheme");
        int result = 0;

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("주제 삭제 중 오류가 발생했습니다.");
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }
}
