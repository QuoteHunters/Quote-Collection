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
}