package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.model.dao.CountryDAO;
import com.quotehunters.quotecollection.model.dto.CountryDTO;

import java.sql.Connection;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.*;

public class CountryService {
    private CountryDAO countryDAO = new CountryDAO();

    public List<CountryDTO> allCountries() {
        Connection con = getConnection();
        List<CountryDTO> countries = countryDAO.allCountries(con);

        close(con);

        return countries;
    }

    public boolean existsCountryName(String countryName) {
        Connection con = getConnection();
        boolean exists = false;

        try {
            exists = countryDAO.existsCountryName(con, countryName);
        } finally {
            close(con);
        }

        return exists;
    }

    public int insertCountry(String countryName) {
        Connection con = getConnection();
        int result = countryDAO.insertCountry(con, countryName);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }
        close(con);

        return result;
    }

    public int updateCountry(int countryId, String countryName) {
        Connection con = getConnection();
        int result = countryDAO.updateCountry(con, countryId, countryName);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }
        close(con);

        return result;
    }
    // [삭제] 연쇄 삭제 4단계를 하나의 트랜잭션으로 처리(bookmark→quote→person→country 순서로 삭제).
    // 성공하면 1, 실패하면 0 반환

    public int deleteCountry(int countryId) {
        Connection con = getConnection();
        int result = 0;

        try {
            countryDAO.deleteBookmarkByCountry(con, countryId);   // ① 즐겨찾기 삭제
            countryDAO.deleteQuoteByCountry(con, countryId);      // ② 명언 삭제
            countryDAO.deletePersonByCountry(con, countryId);     // ③ 인물 삭제
            result = countryDAO.deleteCountry(con, countryId);    // ④ 국가 삭제

            if (result > 0) {                 // 국가가 실제로 지워졌으면
                commit(con);                  // 4개 삭제를 한꺼번에 확정
            } else {                          // 국가가 안 지워졌으면
                rollback(con);                // ①②③까지 포함해 전부 취소
            }
        } finally {
            close(con);
        }

        return result;
    }


}