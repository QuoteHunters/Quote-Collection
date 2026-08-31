package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.model.dao.CountryDAO;
import com.quotehunters.quotecollection.model.dto.CountryDTO;

import java.sql.Connection;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.close;
import static com.quotehunters.quotecollection.common.JDBC.commit;
import static com.quotehunters.quotecollection.common.JDBC.getConnection;
import static com.quotehunters.quotecollection.common.JDBC.rollback;

public class CountryService {

    private final CountryDAO countryDAO = new CountryDAO();

    public List<CountryDTO> allCountries() {
        Connection con = null;

        try {
            con = getConnection();
            return countryDAO.allCountries(con);
        } finally {
            close(con);
        }
    }

    public boolean existsCountryName(String countryName) {
        Connection con = null;

        try {
            con = getConnection();
            return countryDAO.existsCountryName(con, countryName);
        } finally {
            close(con);
        }
    }

    public int insertCountry(String countryName) {
        Connection con = null;

        try {
            con = getConnection();
            int result = countryDAO.insertCountry(con, countryName);
            finishTransaction(con, result);
            return result;
        } catch (RuntimeException e) {
            throw rollbackFailure(con, e);
        } finally {
            close(con);
        }
    }

    public int updateCountry(int countryId, String countryName) {
        Connection con = null;

        try {
            con = getConnection();
            int result = countryDAO.updateCountry(con, countryId, countryName);
            finishTransaction(con, result);
            return result;
        } catch (RuntimeException e) {
            throw rollbackFailure(con, e);
        } finally {
            close(con);
        }
    }

    // [삭제] 연쇄 삭제 4단계를 하나의 트랜잭션으로 처리(bookmark→quote→person→country 순서로 삭제).
    // 국가 행이 삭제되면 1, 대상이 없으면 0을 반환하고 SQL 오류는 예외로 전달
    public int deleteCountry(int countryId) {
        Connection con = null;

        try {
            con = getConnection();
            countryDAO.deleteBookmarkByCountry(con, countryId);   // ① 즐겨찾기 삭제
            countryDAO.deleteQuoteByCountry(con, countryId);      // ② 명언 삭제
            countryDAO.deletePersonByCountry(con, countryId);     // ③ 인물 삭제
            int result = countryDAO.deleteCountry(con, countryId);    // ④ 국가 삭제

            // result > 0이면 국가가 실제로 지워진 경우
            // 이때 4개 삭제를 한꺼번에 확정
            // result == 0이면 국가가 안 지워진 경우
            // 이때 ①②③까지 포함해 전부 취소
            finishTransaction(con, result);
            return result;
        } catch (RuntimeException e) {
            throw rollbackFailure(con, e);
        } finally {
            close(con);
        }
    }

    private void finishTransaction(Connection con, int result) {
        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }
    }

    private RuntimeException rollbackFailure(Connection con, RuntimeException original) {
        try {
            rollback(con);
        } catch (RuntimeException rollbackFailure) {
            original.addSuppressed(rollbackFailure);
        }

        return original;
    }
}
