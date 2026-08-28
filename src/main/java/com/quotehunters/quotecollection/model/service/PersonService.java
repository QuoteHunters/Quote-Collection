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
}
