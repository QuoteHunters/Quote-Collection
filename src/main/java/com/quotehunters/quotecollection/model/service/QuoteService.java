package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.QuoteDAO;
import com.quotehunters.quotecollection.model.dto.QuoteDTO;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class QuoteService {

    // 실제 DB 조회 작업을 수행할 DAO 객체
    private QuoteDAO quoteDAO = new QuoteDAO();

    // 전체 명언 목록을 조회하여 Controller에 반환
    public List<QuoteDTO> selectAllQuotes() {

        // DB와 연결
        Connection con = JDBC.getConnection();

        // 생성한 Connection을 DAO에 전달하여 전체 명언 조회
        // DAO에서 조회 결과를 List<QuoteDTO> 형태로 반환한다.
        List<QuoteDTO> quoteList = quoteDAO.selectAllQuotes(con);

        // DB 연결 사용이 끝났으므로 Connection 종료
        JDBC.close(con);

        // 조회된 명언 목록을 Controller에 반환
        return quoteList;
    }
    public QuoteDTO selectTodayQuote(){
        Connection con = JDBC.getConnection();
        int quoteCount = quoteDAO.selectQuoteCount(con);
        if(quoteCount == 0){
            JDBC.close(con);
            return null;
        }
        int dayOfYear = LocalDate.now().getDayOfYear();
        int offset = (dayOfYear - 1) % quoteCount;

        QuoteDTO quote = quoteDAO.selectTodayQuote(con, offset);

        JDBC.close(con);

        return quote;
    }
    // 키워드 검색에 필요한 Connection을 관리하고 검색 결과를 반환한다.
    public List<QuoteDTO> searchQuotesByKeyword(String keyword) {

        Connection con = JDBC.getConnection();

        try {
            return quoteDAO.searchQuotesByKeyword(con, keyword);
        } finally {
            JDBC.close(con);
        }
    }
    // 인물 이름으로 명언을 검색하고 Connection을 반환한다.
    public List<QuoteDTO> searchQuotesByPerson(String personName) {

        Connection con = JDBC.getConnection();

        try {
            return quoteDAO.searchQuotesByPerson(con, personName);
        } finally {
            JDBC.close(con);
        }
    }

    // 인물 이름의 앞뒤 공백을 제거한 후 등록 대상 인물을 검색한다.
    public List<QuoteDTO> searchPersonsForQuoteRegistration(String personName) {

        Connection con = JDBC.getConnection();

        try {
            return quoteDAO.searchPersonsForQuoteRegistration(
                    con,
                    personName.trim()
            );
        } finally {
            JDBC.close(con);
        }
    }

    // 명언 등록에 사용할 전체 주제 목록을 조회한다.
    public List<QuoteDTO> selectThemesForQuoteRegistration() {

        Connection con = JDBC.getConnection();

        try {
            return quoteDAO.selectThemesForQuoteRegistration(con);
        } finally {
            JDBC.close(con);
        }
    }

    // 명언을 등록하고 결과에 따라 트랜잭션을 확정하거나 취소한다.
    public boolean insertQuote(QuoteDTO quote) {

        Connection con = JDBC.getConnection();

        try {
            int result = quoteDAO.insertQuote(con, quote);

            if (result > 0) {
                JDBC.commit(con);
                return true;
            }

            JDBC.rollback(con);
            return false;

        } catch (RuntimeException e) {
            JDBC.rollback(con);
            throw e;

        } finally {
            JDBC.close(con);
        }
    }

    // 선택한 인물이 가진 명언 목록을 조회한다.
    public List<QuoteDTO> selectQuotesByPersonIdForUpdate(
            int personId
    ) {

        Connection con = JDBC.getConnection();

        try {
            return quoteDAO.selectQuotesByPersonIdForUpdate(
                    con,
                    personId
            );

        } finally {
            JDBC.close(con);
        }
    }

    // 명언 내용을 수정하고 성공 여부에 따라 commit 또는 rollback한다.
    public boolean updateQuoteContent(QuoteDTO quote) {

        Connection con = JDBC.getConnection();

        try {
            int result = quoteDAO.updateQuoteContent(con, quote);

            if (result > 0) {
                JDBC.commit(con);
                return true;
            }

            JDBC.rollback(con);
            return false;

        } catch (RuntimeException e) {
            JDBC.rollback(con);
            throw e;

        } finally {
            JDBC.close(con);
        }
    }

    // 주제 이름 일부로 명언을 검색한다.
    public List<QuoteDTO> searchQuotesByTheme(String themeName) {

        Connection con = JDBC.getConnection();

        try {
            return quoteDAO.searchQuotesByTheme(
                    con,
                    themeName.trim()
            );

        } finally {
            JDBC.close(con);
        }
    }

    // 명언의 주제를 수정하고 트랜잭션을 처리한다.
    public boolean updateQuoteTheme(
            int quoteId,
            int themeId
    ) {

        Connection con = JDBC.getConnection();

        try {
            int result = quoteDAO.updateQuoteTheme(
                    con,
                    quoteId,
                    themeId
            );

            if (result > 0) {
                JDBC.commit(con);
                return true;
            }

            JDBC.rollback(con);
            return false;

        } catch (RuntimeException e) {
            JDBC.rollback(con);
            throw e;

        } finally {
            JDBC.close(con);
        }
    }






}