package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.PersonDAO;
import com.quotehunters.quotecollection.model.dto.PersonDTO;

import java.sql.Connection;
import java.util.List;

public class PersonService {
    private PersonDAO personDAO = new PersonDAO();


    /* 인물 조회*/
    // 1. 전체 인물 조회
    public List<PersonDTO> selectAllPerson() {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectAllPerson(con);

        JDBC.close(con);

        return personList;
    }

    // 2. 국가별 인물 조회
    public List<PersonDTO> selectPersonByCountry(int countryId) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByCountry(con, countryId);

        JDBC.close(con);

        return personList;

    }

    // 3. 시대별 인물 조회
    public List<PersonDTO> selectPersonByPeriod(int periodId) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByPeriod(con, periodId);

        JDBC.close(con);

        return personList;

    }

    // 4. 분야별 인물 조회
    public List<PersonDTO> selectPersonByField(int fieldId) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByField(con, fieldId);

        JDBC.close(con);

        return personList;

    }

    // 5. 인물 이름 조회
    public List<PersonDTO> selectPersonByName(String personName) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByName(con, personName);

        JDBC.close(con);

        return personList;

    }

    // 6. 명언 키워드 검색에 따른 인물 조회
    public List<PersonDTO> selectPersonByQuoteKeyword(String quoteKeyword) {

        Connection con = JDBC.getConnection();

        List<PersonDTO> personList = personDAO.selectPersonByQuoteKeyword(con, quoteKeyword);

        JDBC.close(con);

        return personList;
    }

    /* 인물 정보 수정*/
    // 1. 인물의 국가 수정
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

    // 2. 인물의 시대 수정
    public int updatePersonPeriod(int personId, int periodId) {

        Connection con = JDBC.getConnection();

        int result = 0;

        try {
            result = personDAO.updatePersonPeriod(con, personId, periodId);

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

    // 3. 인물의 분야 수정
    public int updatePersonField(int personId, int fieldId) {

        Connection con = JDBC.getConnection();

        int result = 0;

        try {
            result = personDAO.updatePersonField(con, personId, fieldId);

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

    // 4. 인물의 이름 수정
    public int updatePersonName(int personId, String personName) {
        Connection con = JDBC.getConnection();

        int result = 0;

        try {
            result = personDAO.updatePersonName(con, personId, personName);
            if (result > 0) {
                JDBC.commit(con);
            } else {
                JDBC.rollback(con);
            }
        } finally{
                JDBC.close(con);
            }

        return result;
    }


    /* 인물 등록 */
    // 인물의 이름 중복 확인
    public boolean existsPersonName(String personName) {
        Connection con = JDBC.getConnection();

        boolean exists;

        try {
            exists = personDAO.existsPersonName(con, personName);
        } finally {
            JDBC.close(con);
        }

        return exists;
    }

    // 인물 등록
    public int insertPerson(PersonDTO person) {
        Connection con = JDBC.getConnection();

        int result = 0;

        try {
            result = personDAO.insertPerson(con, person);

            if (result > 0) { // 인물 등록에 성공하면
                JDBC.commit(con);
            } else {
                JDBC.rollback(con);
            }

        } finally {
            JDBC.close(con);
        }

        return result;
    }

    /* 인물 삭제 */
    // 선택한 인물의 명언과 인물 정보 삭제
    public int deletePerson(int personId) {
        // 하나의 트랜잭션으로 처리함
        Connection con = JDBC.getConnection();

        int result = 0;

        try {
            // 1. 인물이 보유한 명언 먼저 삭제
            // 명언은 없는데 인물만 존재하는 경우가 있을 수도 있기 떄문에 결과가 0이어도 정상
            personDAO.deleteQuoteByPerson(con, personId);

            // 2. 인물 정보도 삭제
            result = personDAO.deletePerson(con, personId);

            if (result > 0) {
                JDBC.commit(con);
            } else {
                JDBC.rollback(con);
            }
        }
        catch (RuntimeException e) {
            // 두 DELETE 중 SQL 오류가 발생하면 전체 취소
            // ex. 인물 A의 명언은 삭제했는데 인물 정보는 삭제를 실패했을 경우 rollback으로 명언도 복구
            JDBC.rollback(con);
            e.printStackTrace();
            result = 0;

        } finally {
            JDBC.close(con);
        }

        return result;
    }
}
