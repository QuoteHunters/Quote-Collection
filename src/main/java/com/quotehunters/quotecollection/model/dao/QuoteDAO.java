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
    private Properties prop = new Properties();

    public QuoteDAO() {
        try {
            // quote-query.xml 파일을 읽어서 prop에 SQL문을 저장
            // 이후 key값을 이용해 필요한 SQL을 꺼내서 사용한다.
            prop.loadFromXML(new FileInputStream(
                    "src/main/java/com/quotehunters/quotecollection/mapper/quote-query.xml"
            ));
        } catch (IOException e) {
            // XML 파일을 읽지 못했을 경우 오류 출력
            e.printStackTrace();
        }
    }

    // DB에서 전체 명언을 조회하여 QuoteDTO 목록으로 반환
    // Connection은 Service에서 생성한 뒤 전달받는다.
    public List<QuoteDTO> selectAllQuotes(Connection con) {

        // SQL을 실행하기 위한 객체
        PreparedStatement pstmt = null;

        // SELECT 실행 결과를 저장할 객체
        ResultSet rset = null;

        // 조회된 명언들을 저장할 빈 리스트
        List<QuoteDTO> quoteList = new ArrayList<>();

        // XML에서 selectAllQuotes라는 key를 가진 SQL문을 가져온다.
        String query = prop.getProperty("selectAllQuotes");

        try {
            // 전달받은 DB 연결을 이용해 SQL 실행 준비
            pstmt = con.prepareStatement(query);

            // SELECT문 실행 후 결과를 ResultSet으로 받는다.
            rset = pstmt.executeQuery();

            // 조회 결과가 존재하는 동안 한 행씩 반복
            while (rset.next()) {

                // 현재 행의 데이터를 담을 QuoteDTO 객체 생성
                QuoteDTO quote = new QuoteDTO();

                // ResultSet의 각 컬럼 값을 DTO에 저장
                quote.setQuoteId(rset.getInt("quote_id"));
                quote.setQuoteContent(rset.getString("quote_content"));
                quote.setPersonName(rset.getString("person_name"));
                quote.setThemeName(rset.getString("theme_name"));

                // 완성된 QuoteDTO를 조회 결과 리스트에 추가
                quoteList.add(quote);
            }

        } catch (SQLException e) {
            // SQL 실행 중 문제가 발생한 경우 오류 출력
            e.printStackTrace();

        } finally {
            // 사용이 끝난 DB 자원을 반환
            JDBC.close(rset);
            JDBC.close(pstmt);
        }

        // 조회된 전체 명언 목록을 Service에 반환
        return quoteList;
    }
}