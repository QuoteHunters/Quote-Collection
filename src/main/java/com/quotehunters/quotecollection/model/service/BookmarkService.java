package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.BookmarkDAO;
import com.quotehunters.quotecollection.model.dto.BookmarkDTO;
import com.quotehunters.quotecollection.model.dto.QuoteDTO;

import java.sql.Connection;
import java.util.List;

/*
 * 의도1: Favorite-001 추가의 중복 방지, Favorite-002 내 목록 조회, Favorite-003 취소의 소유 회원 확인 규칙을 적용하고 DB Connection의 시작과 끝을 관리한다.
 * 의도2: Service는 중복 확인·트랜잭션·Connection 관리를 담당하며, SQL 실행은 BookmarkDAO에서 담당한다.
 */
public class BookmarkService {

    // 실제 SQL 실행은 BookmarkDAO에 맡긴다.
    private final BookmarkDAO bookmarkDAO = new BookmarkDAO();

    /*
     * [Favorite-001·003] 상세 화면에 표시할 현재 즐겨찾기 상태를 확인한다.
     * SELECT만 하므로 commit / rollback은 필요 없지만 Connection은 닫는다.
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
     * [Favorite-002] 로그인한 회원의 즐겨찾기 명언 목록을 조회한다.
     * SELECT만 하므로 commit / rollback은 필요 없고, 조회가 끝나면 Connection을 닫는다.
     */
    public List<QuoteDTO> selectFavoriteQuotesByMemberId(int memberId) {

        Connection con = getOpenConnection();

        try {
            return bookmarkDAO.selectFavoriteQuotesByMemberId(con, memberId);

        } finally {
            JDBC.close(con);
        }
    }

    /*
     * [Favorite-002] 목록에서 선택한 명언의 최신 상세 정보를 조회한다.
     * memberId도 함께 넘겨 현재 로그인 회원의 즐겨찾기 안에 있는 명언만 조회한다.
     */
    public QuoteDTO selectFavoriteQuoteDetail(
            int memberId,
            int quoteId
    ) {

        Connection con = getOpenConnection();

        try {
            return bookmarkDAO.selectFavoriteQuoteDetail(
                    con,
                    memberId,
                    quoteId
            );

        } finally {
            JDBC.close(con);
        }
    }

    /*
     * [Favorite-001] 회원-명언 즐겨찾기 연결을 저장한다.
     *
     * View가 상세 화면에 들어올 때 중복 상태를 먼저 보여 주지만,
     * 다른 화면에서 이 Service를 호출해도 중복 저장되지 않도록 같은 Connection에서 한 번 더 확인한다.
     * DB의 UNIQUE(member_id, quote_id) 제약도 마지막으로 중복을 막는다.
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
     * [Favorite-003] 현재 로그인 회원의 즐겨찾기 연결 한 건을 취소한다.
     *
     * 먼저 같은 회원-명언 조합이 있는지 확인한다.
     * 그 뒤 DELETE도 member_id + quote_id 조건으로 실행하므로,
     * 다른 회원의 즐겨찾기를 삭제할 수 없다.
     *
     * 반환값 1 : 즐겨찾기 DELETE 성공
     * 반환값 0 : 이미 취소됐거나 DELETE된 행이 없음
     */
    public int cancelBookmark(BookmarkDTO bookmark) {

        Connection con = getOpenConnection();

        try {
            // 취소 버튼을 누르는 시점에도 이 회원의 즐겨찾기가 실제로 있는지 확인한다.
            if (!bookmarkDAO.existsBookmark(
                    con,
                    bookmark.getMemberId(),
                    bookmark.getQuoteId()
            )) {
                // 이미 다른 화면 또는 요청에서 취소됐다면 DB를 바꾸지 않고 0을 반환한다.
                return 0;
            }

            // DAO가 member_id + quote_id가 일치하는 행만 DELETE한다.
            int result = bookmarkDAO.deleteBookmark(con, bookmark);

            if (result > 0) {
                // 즐겨찾기 한 건이 삭제됐으므로 DB 변경을 확정한다.
                JDBC.commit(con);
            } else {
                // 삭제된 행이 없으면 DB 상태를 되돌린다.
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
