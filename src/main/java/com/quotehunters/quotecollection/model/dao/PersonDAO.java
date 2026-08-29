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

    // 분야별 인물 조회
    public List<PersonDTO> selectPersonByField(Connection con, int fieldId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<PersonDTO> personList = new ArrayList<>();

        String query = prop.getProperty("selectPersonByField");

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, fieldId);

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

    // 인물 이름 조회
    public List<PersonDTO> selectPersonByName(Connection con, String personName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<PersonDTO> personList = new ArrayList<>();

        String query = prop.getProperty("selectPersonByName");

        try {
            pstmt = con.prepareStatement(query);

            // 입력된 문자열이 이름의 일부에 포함된 인물을 모두 검색
            pstmt.setString(1, "%"+ personName + "%");

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

    // 입력된 명언의 키워드가 포함된 명언을 말한 인물 조회
    public List<PersonDTO> selectPersonByQuoteKeyword(Connection con, String quoteKeyword) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<PersonDTO> personList = new ArrayList<>();

        // 중복인물 발생 시 XML 쿼리에서 Distinct로 제거해서 뽑아옴
        String query = prop.getProperty("selectPersonByQuoteKeyword");

        try {
            pstmt = con.prepareStatement(query);

            // 명언 내용에 입력된 키워드가 포함되어 있는지 검색
            pstmt.setString(1, "%" + quoteKeyword + "%");

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

    // 인물의 국가 수정
    // 변경에 성공하면 1 아니면 0을 반환
    public int updatePersonCountry(Connection con, int personId, int countryId) {

        PreparedStatement pstmt = null;
        int result = 0;

        String query = prop.getProperty("updatePersonCountry");

        try {
            pstmt = con.prepareStatement(query);

            // 새로운 국가의 번호
            pstmt.setInt(1, countryId);
            // 수정할 인물의 번호
            pstmt.setInt(2, personId);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }

    // 인물의 시대 수정
    public int updatePersonPeriod(Connection con, int personId, int periodId) {

        PreparedStatement pstmt = null;
        int result = 0;

        String query = prop.getProperty("updatePersonPeriod");

        try {
            pstmt = con.prepareStatement(query);

            // 새로운 시대의 번호
            pstmt.setInt(1, periodId);
            // 수정할 인물의 번호
            pstmt.setInt(2, personId);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }


    /* 인물 등록 */
    // 1. 입력된 이름의 이름 존재 유무 확인
    public boolean existsPersonName(Connection con, String personName) {

        PreparedStatement pstmt = null;

        ResultSet rset = null;

        boolean exists = false;

        String query = prop.getProperty("existsPersonName");

        try {
            pstmt = con.prepareStatement(query);

            // 중복 여부를 확인할 인물 이름 전달
            pstmt.setString(1, personName);

            rset = pstmt.executeQuery();

            // COUNT(*)는 항상 행 1개를 반환함
            // 조회된 개수가 0보다 크면 동일한 이름이 존재하는거임 (등록 불가)
            if (rset.next()) {
                exists = rset.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return exists;
    }

    // 2. 인물 등록
    public int insertPerson(Connection con, PersonDTO person) {
        PreparedStatement pstmt = null;

        int result = 0;

        String query = prop.getProperty("insertPerson");

        try {
            pstmt = con.prepareStatement(query);

            // 등록할 인물의 정보를 전달
            pstmt.setInt(1, person.getCountryId());
            pstmt.setInt(2, person.getPeriodId());
            pstmt.setInt(3, person.getFieldId());
            pstmt.setString(4, person.getPersonName());

            // result 값
            // 1: 인물 등록 성공(1명)
            // 0 : 등록 실패
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }
}
