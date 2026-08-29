package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.PersonDAO;
import com.quotehunters.quotecollection.model.dto.PersonDTO;

import java.sql.Connection;
import java.util.List;

public class PersonService {
    private PersonDAO personDAO = new PersonDAO();


    // 전체 인물 조회
    public List<PersonDTO> selectAllPerson() {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectAllPerson(con);

        JDBC.close(con);

        return personList;
    }

    // 국가별 인물 조회
    public List<PersonDTO> selectPersonByCountry(int countryId) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByCountry(con, countryId);

        JDBC.close(con);

        return personList;

    }

    // 시대별 인물 조회
    public List<PersonDTO> selectPersonByPeriod(int periodId) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByPeriod(con, periodId);

        JDBC.close(con);

        return personList;

    }

    // 분야별 인물 조회
    public List<PersonDTO> selectPersonByField(int fieldId) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByField(con, fieldId);

        JDBC.close(con);

        return personList;

    }

    // 인물 이름 조회
    public List<PersonDTO> selectPersonByName(String personName) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByName(con, personName);

        JDBC.close(con);

        return personList;

    }

    // 명언 키워드 검색에 따른 인물 조회
    public List<PersonDTO> selectPersonByQuoteKeyword(String quoteKeyword) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByQuoteKeyword(con, quoteKeyword);

        JDBC.close(con);

        return personList;
    }

    // 인물의 국가 수정
    public int updatePersonCountry(int personId, int countryId) {

        Connection con = JDBC.getConnection();

        int result = 0;

        try {
            result = personDAO.updatePersonCountry(con, personId, countryId);

            if (result > 0) {
                JDBC.commit(con);
            } else {
                JDBC.rollback(con);
            }
        } finally {
            JDBC.close(con);
        }
        return result;
    }
}
