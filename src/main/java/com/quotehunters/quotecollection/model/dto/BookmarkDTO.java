/*
 * 의도1: Favorite-001 추가와 Favorite-003 취소에서 로그인한 회원 한 명과 선택한 명언 한 개의 즐겨찾기 연결 정보를 한 객체에 담아 전달한다.
 * 의도2: 실제 DB 테이블 이름이 bookmark이므로 Java 클래스도 BookmarkDTO로 이름을 맞춘다.
 */

package com.quotehunters.quotecollection.model.dto;

public class BookmarkDTO {

    // DB가 AUTO_INCREMENT로 자동 생성하는 즐겨찾기 고유 번호다.
    private int bookmarkId;

    // 로그인에 성공한 MemberDTO에서 가져오는 회원 고유 번호다.
    private int memberId;

    // 명언 상세에서 선택한 QuoteDTO에서 가져오는 명언 고유 번호다.
    private int quoteId;

    // ResultSet으로 조회해 DTO를 만들 때 사용할 수 있도록 기본 생성자를 둔다.
    public BookmarkDTO() {
    }

    /*
     * Favorite-001 INSERT와 Favorite-003 DELETE에 공통으로 쓰는 생성자다.
     * bookmarkId는 DB의 AUTO_INCREMENT가 만들고, DELETE는 memberId + quoteId 조건을 쓰므로 회원 번호와 명언 번호만 받는다.
     */
    public BookmarkDTO(int memberId, int quoteId) {
        this.memberId = memberId;
        this.quoteId = quoteId;
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    @Override
    public String toString() {
        // 비밀번호처럼 숨겨야 할 값은 없지만, 객체 확인용으로 ID 값만 사람이 읽기 좋게 출력한다.
        return "BookmarkDTO{"
                + "bookmarkId=" + bookmarkId
                + ", memberId=" + memberId
                + ", quoteId=" + quoteId
                + "}";
    }
}
