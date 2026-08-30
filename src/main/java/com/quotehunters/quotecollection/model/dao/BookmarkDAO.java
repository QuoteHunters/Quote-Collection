/*
 * 의도1: Favorite-001의 즐겨찾기 상태 확인과 즐겨찾기 추가 SQL을 bookmark 테이블에 실행한다.
 * 의도2: DAO는 DB 작업만 담당하며, 화면 입력은 BookmarkView에서, 업무 규칙과 트랜잭션은 BookmarkService에서 담당한다.
 */

package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.BookmarkDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * [Favorite-001] 한 회원이 한 명언을 이미 즐겨찾기에 저장했는지 확인한다.
     *
     * true  : 같은 member_id + quote_id 조합이 이미 있음 → 추가 메뉴를 보여 주지 않는다.
     * false : 같은 조합이 없음 → 즐겨찾기 INSERT를 진행할 수 있다.
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
            throw new RuntimeException("즐겨찾기 상태 조회 중 DB 오류가 발생했습니다.", e);

        } finally {
            // DAO가 직접 만든 ResultSet과 PreparedStatement만 DAO가 닫는다.
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
            throw new RuntimeException("즐겨찾기 DB 저장 중 오류가 발생했습니다.", e);

        } finally {
            // INSERT에는 ResultSet이 없으므로 PreparedStatement만 닫는다.
            JDBC.close(pstmt);
        }
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
