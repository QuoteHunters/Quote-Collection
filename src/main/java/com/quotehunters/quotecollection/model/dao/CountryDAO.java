package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.CountryDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CountryDAO {

    private final Properties prop = new Properties();

    public CountryDAO() {
        try {
            prop.loadFromXML(new FileInputStream(
                    "src/main/java/com/quotehunters/quotecollection/mapper/country-query.xml"));
        } catch (IOException e) {
            throw new IllegalStateException("국가 SQL을 불러오지 못했습니다.", e);
        }
    }

    public List<CountryDTO> allCountries(Connection connection) {
        Statement stmt = null;
        ResultSet rset = null;
        List<CountryDTO> countries = new ArrayList<>();

        try {
            stmt = connection.createStatement();
            rset = stmt.executeQuery(prop.getProperty("selectAllCountry"));

            while (rset.next()) {
                CountryDTO country = new CountryDTO();
                country.setCountryId(rset.getInt("country_id"));
                country.setCountryName(rset.getString("country_name"));
                countries.add(country);
            }

            return countries;
        } catch (SQLException e) {
            throw new IllegalStateException("국가 목록 조회 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(rset);
            JDBC.close(stmt);
        }
    }

    public boolean existsCountryName(Connection connection, String countryName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            pstmt = connection.prepareStatement(prop.getProperty("selectCountryByName"));
            pstmt.setString(1, countryName);
            rset = pstmt.executeQuery();

            return rset.next() && rset.getInt(1) > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("국가명 확인 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
    }

    public int insertCountry(Connection connection, String countryName) {
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(prop.getProperty("insertCountry"));
            pstmt.setString(1, countryName);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("국가 등록 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(pstmt);
        }
    }

    public int updateCountry(Connection connection, int countryId, String countryName) {
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(prop.getProperty("updateCountry"));
            pstmt.setString(1, countryName);
            pstmt.setInt(2, countryId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("국가 수정 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(pstmt);
        }
    }

    // 연쇄 삭제 1단계: 해당 국가 소속 인물들의 명언에 달린 즐겨찾기 삭제 (처리 행 수 반환, 0도 정상)
    public int deleteBookmarkByCountry(Connection connection, int countryId) {
        return executeDelete(connection, "deleteBookmarkByCountry", countryId);
    }

    // 연쇄 삭제 2단계: 해당 국가 소속 인물들의 명언 삭제
    public int deleteQuoteByCountry(Connection connection, int countryId) {
        return executeDelete(connection, "deleteQuoteByCountry", countryId);
    }

    // 연쇄 삭제 3단계: 해당 국가 소속 인물 삭제
    public int deletePersonByCountry(Connection connection, int countryId) {
        return executeDelete(connection, "deletePersonByCountry", countryId);
    }

    public int deleteCountry(Connection connection, int countryId) {
        return executeDelete(connection, "deleteCountry", countryId);
    }

    private int executeDelete(Connection connection, String queryKey, int countryId) {
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(prop.getProperty(queryKey));   // XML에서 SQL 꺼내기
            pstmt.setInt(1, countryId);          // ? 에 국가 번호 채우기
            return pstmt.executeUpdate();        // 실행, 지워진 행 수 받기
        } catch (SQLException e) {
            throw new IllegalStateException("국가 연쇄 삭제 중 오류가 발생했습니다.", e);
        } finally {
            JDBC.close(pstmt);
        }
    }
}
