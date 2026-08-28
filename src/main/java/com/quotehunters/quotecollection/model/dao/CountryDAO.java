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
    private Properties prop = new Properties();

    public CountryDAO() {
        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/quotehunters/quotecollection/mapper/country-query.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CountryDTO> allCountries(Connection connection) {
        Statement stmt = null;
        ResultSet rset = null;
        List<CountryDTO> countries = new ArrayList<>();

        String query = prop.getProperty("selectAllCountry");

        try {
            stmt = connection.createStatement();
            rset = stmt.executeQuery(query);

            while (rset.next()) {
                CountryDTO countryDTO = new CountryDTO();
                countryDTO.setCountryId(rset.getInt("country_id"));
                countryDTO.setCountryName(rset.getString("country_name"));

                countries.add(countryDTO);
            }
        } catch (SQLException e) {
            System.out.println("All Countries SQL Exception");
        } finally {
            JDBC.close(rset);
            JDBC.close(stmt);
        }

        return countries;
    }

    public boolean existsCountryName(Connection connection, String countryName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        String query = prop.getProperty("selectCountryByName");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, countryName);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                if (rset.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Exists Country Name SQL Exception");
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return false;
    }

    public int insertCountry(Connection connection, String countryName) {
        PreparedStatement pstmt = null;
        int result = 0;

        String query = prop.getProperty("insertCountry");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, countryName);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert Country SQL Exception");
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }

    public int updateCountry(Connection connection, int countryId, String countryName) {
        PreparedStatement pstmt = null;
        int result = 0;

        String query = prop.getProperty("updateCountry");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, countryName);
            pstmt.setInt(2, countryId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update Country SQL Exception");
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }

    public int deleteCountry(Connection connection, int countryId) {
        PreparedStatement pstmt = null;
        int result = 0;

        String query = prop.getProperty("deleteCountry");

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, countryId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Delete Country SQL Exception");
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }

}