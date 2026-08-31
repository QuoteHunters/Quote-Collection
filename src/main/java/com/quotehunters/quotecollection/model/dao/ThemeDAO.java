package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.ThemeDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ThemeDAO {
    private final Properties prop = new Properties();
    private final IllegalStateException mapperFailure;

    public ThemeDAO() {
        IllegalStateException failure = null;

        try (FileInputStream inputStream = new FileInputStream(
                "src/main/java/com/quotehunters/quotecollection/mapper/theme-query.xml")) {
            prop.loadFromXML(inputStream);
        } catch (IOException e) {
            failure = new IllegalStateException("주제 쿼리 설정을 불러오는 중 오류가 발생했습니다.", e);
        }

        mapperFailure = failure;
    }

    public int insertTheme(Connection con, String themeName) {
        checkMapper();

        PreparedStatement pstmt = null;
        String query = prop.getProperty("insertTheme");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, themeName);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("주제 등록 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(pstmt);
        }
    }

    public boolean existsThemeName(Connection con, String themeName) {
        checkMapper();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = prop.getProperty("existsThemeName");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, themeName);
            rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("주제명 중복 확인 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(rs);
            JDBC.close(pstmt);
        }
    }

    public boolean existsThemeName(Connection con, int id, String themeName) {
        checkMapper();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = prop.getProperty("existsThemeNameExceptId");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, themeName);
            pstmt.setInt(2, id);
            rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("주제명 중복 확인 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(rs);
            JDBC.close(pstmt);
        }
    }

    public List<ThemeDTO> selectThemes(Connection con) {
        checkMapper();

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

            return themes;
        } catch (SQLException e) {
            throw new IllegalStateException("주제 목록 조회 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(rs);
            JDBC.close(pstmt);
        }
    }

    public int updateTheme(Connection con, int id, String themeName) {
        checkMapper();

        PreparedStatement pstmt = null;
        String query = prop.getProperty("updateTheme");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, themeName);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("주제 수정 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(pstmt);
        }
    }

    public int deleteTheme(Connection con, int id) {
        checkMapper();

        PreparedStatement pstmt = null;
        String query = prop.getProperty("deleteTheme");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("주제 삭제 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(pstmt);
        }
    }

    private void checkMapper() {
        if (mapperFailure != null) {
            throw mapperFailure;
        }
    }
}
