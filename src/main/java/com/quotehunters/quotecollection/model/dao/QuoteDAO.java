package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.QuoteDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QuoteDAO {

    // XML에 작성된 SQL을 key-value 형태로 저장할 객체
    private final Properties prop = new Properties();

    public QuoteDAO() {
        try {
            // quote-query.xml 파일을 읽어서 prop에 SQL문을 저장
            prop.loadFromXML(new FileInputStream(
                    "src/main/java/com/quotehunters/quotecollection/mapper/quote-query.xml"
            ));
        } catch (IOException e) {
            throw new RuntimeException("SQL 설정 파일을 불러오는 중 오류가 발생했습니다.", e);
        }
    }

    // 전체 명언 조회
    public List<QuoteDTO> selectAllQuotes(Connection con) {

        String query = prop.getProperty("selectAllQuotes");

        return selectQuoteList(con, query);
    }

    // 전체 명언 개수 조회
    public int selectQuoteCount(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        int count = 0;

        String query = prop.getProperty("selectQuoteCount");

        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                count = rset.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("명언 개수 조회 중 오류가 발생했습니다.", e);

        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return count;
    }

    // 오늘의 명언 조회
    public QuoteDTO selectTodayQuote(Connection con, int offset) {

        String query = prop.getProperty("selectTodayQuote");

        return selectQuote(con, query, offset);
    }

    // 명언 내용 키워드 검색
    public List<QuoteDTO> searchQuotesByKeyword(
            Connection con,
            String keyword
    ) {

        String query = prop.getProperty("searchQuotesByKeyword");

        return selectQuoteList(con, query, keyword);
    }

    // 인물 이름 검색
    public List<QuoteDTO> searchQuotesByPerson(
            Connection con,
            String personName
    ) {

        String query = prop.getProperty("searchQuotesByPerson");

        return selectQuoteList(con, query, personName);
    }

    // 여러 개의 명언을 조회하는 공통 메서드
    private List<QuoteDTO> selectQuoteList(
            Connection con,
            String query,
            Object... params
    ) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<QuoteDTO> quoteList = new ArrayList<>();

        try {
            // SQL 실행 준비
            pstmt = con.prepareStatement(query);

            // SQL에 필요한 파라미터 설정
            setParameters(pstmt, params);

            // SELECT 실행
            rset = pstmt.executeQuery();

            // 조회 결과를 QuoteDTO로 변환하여 리스트에 저장
            while (rset.next()) {
                quoteList.add(convertToQuote(rset));
            }

        } catch (SQLException e) {
            throw new RuntimeException("명언 목록 조회 중 오류가 발생했습니다.", e);

        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return quoteList;
    }

    // 하나의 명언을 조회하는 공통 메서드
    private QuoteDTO selectQuote(
            Connection con,
            String query,
            Object... params
    ) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        QuoteDTO quote = null;

        try {
            // SQL 실행 준비
            pstmt = con.prepareStatement(query);

            // SQL에 필요한 파라미터 설정
            setParameters(pstmt, params);

            // SELECT 실행
            rset = pstmt.executeQuery();

            // 조회 결과가 존재하면 QuoteDTO로 변환
            if (rset.next()) {
                quote = convertToQuote(rset);
            }

        } catch (SQLException e) {
            throw new RuntimeException("명언 조회 중 오류가 발생했습니다.", e);

        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return quote;
    }

    // PreparedStatement의 ? 자리에 전달받은 값을 순서대로 설정
    private void setParameters(
            PreparedStatement pstmt,
            Object... params
    ) throws SQLException {

        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    // 현재 ResultSet 행을 QuoteDTO 객체로 변환
    private QuoteDTO convertToQuote(ResultSet rset) throws SQLException {

        QuoteDTO quote = new QuoteDTO();

        quote.setQuoteId(rset.getInt("quote_id"));
        quote.setQuoteContent(rset.getString("quote_content"));
        quote.setPersonName(rset.getString("person_name"));
        quote.setThemeName(rset.getString("theme_name"));

        return quote;
    }
}