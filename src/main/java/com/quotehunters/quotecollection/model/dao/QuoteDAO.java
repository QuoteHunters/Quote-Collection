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

    // 명언 등록에 사용할 인물 후보를 이름 일부로 검색한다.
    public List<QuoteDTO> searchPersonsForQuoteRegistration(
            Connection con,
            String personName
    ) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<QuoteDTO> personList = new ArrayList<>();
        String query = prop.getProperty("searchPersonsForQuoteRegistration");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, personName);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                QuoteDTO person = new QuoteDTO();

                person.setPersonId(rset.getInt("person_id"));
                person.setPersonName(rset.getString("person_name"));
                person.setCountryName(rset.getString("country_name"));
                person.setPeriodName(rset.getString("period_name"));
                person.setFieldName(rset.getString("field_name"));

                personList.add(person);
            }

        } catch (SQLException e) {
            throw new RuntimeException("명언 등록용 인물 검색 중 오류가 발생했습니다.", e);

        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return personList;
    }

    // 명언 등록에 사용할 전체 주제 목록을 조회한다.
    public List<QuoteDTO> selectThemesForQuoteRegistration(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<QuoteDTO> themeList = new ArrayList<>();
        String query = prop.getProperty("selectThemesForQuoteRegistration");

        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                QuoteDTO theme = new QuoteDTO();

                theme.setThemeId(rset.getInt("theme_id"));
                theme.setThemeName(rset.getString("theme_name"));

                themeList.add(theme);
            }

        } catch (SQLException e) {
            throw new RuntimeException("명언 등록용 주제 조회 중 오류가 발생했습니다.", e);

        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        return themeList;
    }

    // 선택한 인물, 주제 및 명언 내용을 quote 테이블에 등록한다.
    public int insertQuote(Connection con, QuoteDTO quote) {

        PreparedStatement pstmt = null;

        int result;
        String query = prop.getProperty("insertQuote");

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, quote.getThemeId());
            pstmt.setInt(2, quote.getPersonId());
            pstmt.setString(3, quote.getQuoteContent());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("명언 등록 중 오류가 발생했습니다.", e);

        } finally {
            JDBC.close(pstmt);
        }

        return result;
    }

    // 선택한 인물 ID에 해당하는 명언 목록을 조회한다.
    public List<QuoteDTO> selectQuotesByPersonIdForUpdate(
            Connection con,
            int personId
    ) {

        String query =
                prop.getProperty("selectQuotesByPersonIdForUpdate");

        /*
         * 기존 공통 조회 메서드를 재사용한다.
         * 쿼리 결과에 quote_id, quote_content,
         * person_name, theme_name이 있으므로
         * 기존 convertToQuote()를 그대로 사용할 수 있다.
         */
        return selectQuoteList(con, query, personId);
    }

    // 선택한 명언의 내용만 수정하고 반영된 행 개수를 반환한다.
    public int updateQuoteContent(
            Connection con,
            QuoteDTO quote
    ) {

        PreparedStatement pstmt = null;

        String query = prop.getProperty("updateQuoteContent");

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, quote.getQuoteContent());
            pstmt.setInt(2, quote.getQuoteId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "명언 수정 중 오류가 발생했습니다.",
                    e
            );

        } finally {
            JDBC.close(pstmt);
        }
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

    // 주제 이름 일부로 명언 목록을 검색한다.
    public List<QuoteDTO> searchQuotesByTheme(
            Connection con,
            String themeName
    ) {

        String query = prop.getProperty("searchQuotesByTheme");

        return selectQuoteList(con, query, themeName);
    }

    // 선택한 명언의 주제 ID만 수정한다.
    public int updateQuoteTheme(
            Connection con,
            int quoteId,
            int themeId
    ) {

        PreparedStatement pstmt = null;

        String query = prop.getProperty("updateQuoteTheme");

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, themeId);
            pstmt.setInt(2, quoteId);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "명언 주제 수정 중 오류가 발생했습니다.",
                    e
            );

        } finally {
            JDBC.close(pstmt);
        }
    }
}