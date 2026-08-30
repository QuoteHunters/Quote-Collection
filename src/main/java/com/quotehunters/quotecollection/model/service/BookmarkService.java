/*
 * 의도1: Favorite-001 즐겨찾기 추가의 중복 방지 규칙을 적용하고 DB Connection의 시작과 끝을 관리한다.
 * 의도2: Service는 중복 확인·트랜잭션·Connection 관리를 담당하며, SQL 실행은 BookmarkDAO에서 담당한다.
 */

package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.BookmarkDAO;
import com.quotehunters.quotecollection.model.dto.BookmarkDTO;

import java.sql.Connection;

public class BookmarkService {

    // 실제 SQL 실행은 BookmarkDAO에 맡긴다.
    private final BookmarkDAO bookmarkDAO = new BookmarkDAO();

    /*
     * [Favorite-001] 상세 화면에 표시할 현재 즐겨찾기 상태를 확인한다.
     * SELECT만 하므로 commit / rollback은 필요 없지만 Connection은 닫아야 한다.
     */
    public boolean isBookmarked(int memberId, int quoteId) {

        Connection con = getOpenConnection();

        try {
            return bookmarkDAO.existsBookmark(con, memberId, quoteId);

        } finally {
            // Connection을 만든 Service가 조회가 끝난 뒤 닫는다.
            JDBC.close(con);
        }
    }

    /*
     * [Favorite-001] 회원-명언 즐겨찾기 연결을 저장한다.
     *
     * 반환값 1 : 즐겨찾기 INSERT 성공
     * 반환값 0 : 이미 즐겨찾기에 있거나 INSERT된 행이 없음
     */
    public int addBookmark(BookmarkDTO bookmark) {

        Connection con = getOpenConnection();

        try {
            // INSERT 바로 전에 같은 회원-명언 조합이 이미 있는지 다시 확인한다.
            if (bookmarkDAO.existsBookmark(
                    con,
                    bookmark.getMemberId(),
                    bookmark.getQuoteId()
            )) {
                // DB를 바꾸지 않았으므로 확정할 것이 없다. 0을 반환해 View가 안내하게 한다.
                return 0;
            }

            // DAO가 INSERT를 실행하고, 실제로 저장한 행 수를 반환한다.
            int result = bookmarkDAO.insertBookmark(con, bookmark);

            if (result > 0) {
                // 즐겨찾기 한 건이 저장됐으므로 DB 변경을 확정한다.
                JDBC.commit(con);
            } else {
                // INSERT된 행이 없으면 기존 DB 상태를 유지한다.
                JDBC.rollback(con);
            }

            return result;

        } catch (RuntimeException e) {
            // SQL 오류가 발생했을 때 열린 트랜잭션을 되돌린다.
            JDBC.rollback(con);
            throw e;

        } finally {
            JDBC.close(con);
        }
    }

    /*
     * JDBC.getConnection()이 DB 연결 실패 시 null을 반환할 수 있다.
     * null을 DAO에 넘기기 전에 여기서 명확한 오류로 바꾼다.
     */
    private Connection getOpenConnection() {
        Connection con = JDBC.getConnection();

        if (con == null) {
            throw new IllegalStateException("DB 연결을 만들지 못했습니다.");
        }

        return con;
    }
}
