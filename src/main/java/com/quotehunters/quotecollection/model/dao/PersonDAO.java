package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.PersonDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PersonDAO {
    private Properties prop = new Properties();

    public PersonDAO() {

        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/quotehunters/quotecollection/mapper/person-query.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 전체 인물 조회
    public List<PersonDTO> selectAllPerson(Connection con) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<PersonDTO> personList = new ArrayList<>();

        String query = prop.getProperty("selectAllPerson");

        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            // JOIN 조회 결과 한 행을 PersonDTO 하나로 변환
            while (rset.next()) {
                PersonDTO person = new PersonDTO();

                // DTO에 받아온 값을 저장
                person.setPersonId(rset.getInt("person_id"));
                person.setPersonName(rset.getString("person_name"));
                person.setCountryId(rset.getInt("country_id"));
                person.setCountryName(rset.getString("country_name"));
                person.setPeriodId(rset.getInt("period_id"));
                person.setPeriodName(rset.getString("period_name"));
                person.setFieldId(rset.getInt("field_id"));
                person.setFieldName(rset.getString("field_name"));

                personList.add(person);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
        return personList;
    }

    // 국가별 인물 조회
    public List<PersonDTO> selectPersonByCountry(Connection con, int countryId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<PersonDTO> personList = new ArrayList<>();

        String query = prop.getProperty("selectPersonByCountry");

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, countryId);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                PersonDTO person = new PersonDTO();

                person.setPersonId(rset.getInt("person_id"));
                person.setPersonName(rset.getString("person_name"));
                person.setCountryId(rset.getInt("country_id"));
                person.setCountryName(rset.getString("country_name"));
                person.setPeriodId(rset.getInt("period_id"));
                person.setPeriodName(rset.getString("period_name"));
                person.setFieldId(rset.getInt("field_id"));
                person.setFieldName(rset.getString("field_name"));

                personList.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
        return personList;
    }

    // 시대별 인물 조회
    public List<PersonDTO> selectPersonByPeriod(Connection con, int periodId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<PersonDTO> personList = new ArrayList<>();

        String query = prop.getProperty("selectPersonByPeriod");

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, periodId);

            rset = pstmt.executeQuery();
            while (rset.next()) {
                PersonDTO person = new PersonDTO();

                person.setPersonId(rset.getInt("person_id"));
                person.setPersonName(rset.getString("person_name"));
                person.setCountryId(rset.getInt("country_id"));
                person.setCountryName(rset.getString("country_name"));
                person.setPeriodId(rset.getInt("period_id"));
                person.setPeriodName(rset.getString("period_name"));
                person.setFieldId(rset.getInt("field_id"));
                person.setFieldName(rset.getString("field_name"));

                personList.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return personList;
    }
}
