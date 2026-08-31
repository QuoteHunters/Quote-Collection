/*
 * 의도1: Favorite-001 추가, Favorite-002 목록 조회, Favorite-003 취소에 필요한 SQL을 bookmark 테이블에 실행한다.
 * 의도2: DAO는 DB 작업만 담당하며, 화면 입력은 BookmarkView에서, 업무 규칙과 트랜잭션은 BookmarkService에서 담당한다.
 */

package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.BookmarkDTO;
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

public class BookmarkDAO {

    // bookmark-query.xml 안의 SQL을 key-value 형태로 보관한다.
    private final Properties prop = new Properties();

    /*
     * DAO 객체가 만들어질 때 즐겨찾기 SQL XML 파일을 읽는다.
     * 이후 prop.getProperty("키 이름")으로 필요한 SQL을 가져온다.
     */
    public BookmarkDAO() {
        try {
            prop.loadFromXML(new FileInputStream(
                    "src/main/java/com/quotehunters/quotecollection/mapper/bookmark-query.xml"
            ));
        } catch (IOException e) {
            throw new RuntimeException("즐겨찾기 SQL 설정 파일을 불러오지 못했습니다.", e);
        }
    }

    /*
     * [Favorite-001·003] 한 회원이 한 명언을 이미 즐겨찾기에 저장했는지 확인한다.
     *
     * true  : 같은 member_id + quote_id 조합이 이미 있음 → Favorite-003 취소 대상일 수 있다.
     * false : 같은 조합이 없음 → Favorite-001 추가 대상일 수 있다.
     */
    public boolean existsBookmark(
            Connection con,
            int memberId,
            int quoteId
    ) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        String query = getQuery("existsBookmark");

        try {
            // XML에서 읽어 온 SELECT문을 실행할 준비를 한다.
            pstmt = con.prepareStatement(query);

            // SQL의 첫 번째 ?는 로그인 회원 번호, 두 번째 ?는 선택 명언 번호다.
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, quoteId);

            // SELECT COUNT(*) 실행 결과를 ResultSet으로 받는다.
            rset = pstmt.executeQuery();

            // COUNT(*) 결과는 정확히 한 행이다.
            if (rset.next()) {
                return rset.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            throw new RuntimeException("즐겨찾기 상태 조회 중 오류가 발생했습니다.", e);

        } finally {
            // DAO가 직접 만든 ResultSet과 PreparedStatement만 DAO가 닫는다.
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
    }

    /*
     * [Favorite-002] 로그인한 회원이 저장한 즐겨찾기 명언 목록을 최근 등록 순서로 조회한다.
     *
     * bookmark 테이블은 회원 번호와 명언 번호만 가지므로,
     * 화면에 필요한 명언 내용·인물명·주제명은 quote, person, theme 테이블과 JOIN해서 가져온다.
     * 그 조회 결과의 중심은 명언이므로 List<QuoteDTO>로 반환한다.
     */
    public List<QuoteDTO> selectFavoriteQuotesByMemberId(
            Connection con,
            int memberId
    ) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<QuoteDTO> favoriteQuoteList = new ArrayList<>();

        String query = getQuery("selectFavoriteQuotesByMemberId");

        try {
            // XML에서 읽어 온 JOIN SELECT문을 실행할 준비를 한다.
            pstmt = con.prepareStatement(query);

            // 목록의 첫 번째 ?에는 로그인한 회원 번호만 넣는다.
            pstmt.setInt(1, memberId);

            rset = pstmt.executeQuery();

            // 조회된 각 행을 QuoteDTO 한 개로 바꿔 목록에 차례로 담는다.
            while (rset.next()) {
                favoriteQuoteList.add(convertToQuote(rset));
            }

            return favoriteQuoteList;

        } catch (SQLException e) {
            throw new RuntimeException("즐겨찾기 목록 조회 중 오류가 발생했습니다.", e);

        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
    }

    /*
     * [Favorite-002] 목록에서 선택한 명언의 최신 상세 정보를 조회한다.
     *
     * member_id와 quote_id를 동시에 조건으로 사용하므로,
     * 다른 회원의 즐겨찾기나 이미 취소된 명언은 상세 결과로 반환되지 않는다.
     *
     * 반환값
     * - QuoteDTO : 현재 로그인 회원의 즐겨찾기 안에 선택 명언이 있음
     * - null     : 명언이 삭제됐거나 즐겨찾기가 이미 취소됨
     */
    public QuoteDTO selectFavoriteQuoteDetail(
            Connection con,
            int memberId,
            int quoteId
    ) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        String query = getQuery("selectFavoriteQuoteDetail");

        try {
            pstmt = con.prepareStatement(query);

            // 첫 번째 ?는 로그인 회원 번호, 두 번째 ?는 목록에서 선택한 실제 명언 번호다.
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, quoteId);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                return convertToQuote(rset);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("즐겨찾기 명언 상세 조회 중 오류가 발생했습니다.", e);

        } finally {
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
    }

    /*
     * [Favorite-001] BookmarkDTO의 회원-명언 연결 정보를 bookmark 테이블에 INSERT한다.
     *
     * 반환값은 INSERT된 행 수다.
     * 즐겨찾기 한 건이 정상 저장되면 보통 1을 반환한다.
     */
    public int insertBookmark(Connection con, BookmarkDTO bookmark) {

        PreparedStatement pstmt = null;

        String query = getQuery("insertBookmark");

        try {
            // XML에서 읽어 온 INSERT문을 실행할 준비를 한다.
            pstmt = con.prepareStatement(query);

            // INSERT문의 첫 번째 ?에는 회원 번호, 두 번째 ?에는 명언 번호를 순서대로 넣는다.
            pstmt.setInt(1, bookmark.getMemberId());
            pstmt.setInt(2, bookmark.getQuoteId());

            // executeUpdate()는 INSERT에 성공한 행 수를 int로 반환한다.
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("즐겨찾기 저장 중 오류가 발생했습니다.", e);

        } finally {
            // INSERT에는 ResultSet이 없으므로 PreparedStatement만 닫는다.
            JDBC.close(pstmt);
        }
    }

    /*
     * [Favorite-003] BookmarkDTO의 회원-명언 연결 정보를 bookmark 테이블에서 삭제한다.
     *
     * DELETE의 WHERE 조건에 member_id와 quote_id를 모두 사용한다.
     * 따라서 같은 명언이라도 다른 회원이 저장한 즐겨찾기 행은 삭제하지 않는다.
     *
     * 반환값은 DELETE된 행 수다.
     * 정상적으로 취소한 한 건이면 보통 1을 반환한다.
     */
    public int deleteBookmark(Connection con, BookmarkDTO bookmark) {

        PreparedStatement pstmt = null;

        String query = getQuery("deleteBookmark");

        try {
            // XML에서 읽어 온 DELETE문을 실행할 준비를 한다.
            pstmt = con.prepareStatement(query);

            // 첫 번째 ?는 로그인 회원 번호, 두 번째 ?는 현재 명언 번호다.
            pstmt.setInt(1, bookmark.getMemberId());
            pstmt.setInt(2, bookmark.getQuoteId());

            // executeUpdate()는 DELETE에 성공한 행 수를 int로 반환한다.
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("즐겨찾기 취소 중 오류가 발생했습니다.", e);

        } finally {
            // DELETE에는 ResultSet이 없으므로 PreparedStatement만 닫는다.
            JDBC.close(pstmt);
        }
    }

    // Favorite-002의 JOIN SELECT 행을 화면용 QuoteDTO로 바꾼다.
    private QuoteDTO convertToQuote(ResultSet rset) throws SQLException {

        QuoteDTO quote = new QuoteDTO();

        quote.setQuoteId(rset.getInt("quote_id"));
        quote.setQuoteContent(rset.getString("quote_content"));
        quote.setPersonName(rset.getString("person_name"));
        quote.setThemeName(rset.getString("theme_name"));

        return quote;
    }

    // XML key를 잘못 썼을 때 원인을 명확하게 알려 주는 보조 메서드다.
    private String getQuery(String key) {
        String query = prop.getProperty(key);

        if (query == null) {
            throw new IllegalStateException(
                    "bookmark-query.xml에 '" + key + "' SQL이 없습니다."
            );
        }

        return query;
    }
}
