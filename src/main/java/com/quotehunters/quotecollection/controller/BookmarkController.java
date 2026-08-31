/*
 * 의도1: BookmarkView의 즐겨찾기 상태 확인·추가·목록 조회·취소 요청을 BookmarkService까지 전달하고 결과를 다시 View로 돌려준다.
 * 의도2: Controller는 중간 연결만 담당하며, 입력은 BookmarkView에서, SQL·Connection 처리는 Service/DAO에서 담당한다.
 */

package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.BookmarkDTO;
import com.quotehunters.quotecollection.model.dto.QuoteDTO;
import com.quotehunters.quotecollection.model.service.BookmarkService;

import java.util.List;

public class BookmarkController {

    // 즐겨찾기 업무와 DB 처리를 담당하는 Service 객체다.
    private final BookmarkService bookmarkService = new BookmarkService();

    // 명언 상세 화면이 현재 회원의 즐겨찾기 상태를 확인하도록 Service에 요청한다.
    public boolean isBookmarked(int memberId, int quoteId) {
        return bookmarkService.isBookmarked(memberId, quoteId);
    }

    // Favorite-002: 현재 로그인 회원의 즐겨찾기 명언 목록을 Service에 요청한다.
    public List<QuoteDTO> selectFavoriteQuotesByMemberId(int memberId) {
        return bookmarkService.selectFavoriteQuotesByMemberId(memberId);
    }

    // Favorite-002: 목록에서 선택한 명언의 최신 상세 정보를 Service에 요청한다.
    public QuoteDTO selectFavoriteQuoteDetail(int memberId, int quoteId) {
        return bookmarkService.selectFavoriteQuoteDetail(memberId, quoteId);
    }

    // View가 만든 BookmarkDTO를 Service에 전달하고 INSERT된 행 수를 반환한다.
    public int addBookmark(BookmarkDTO bookmark) {
        return bookmarkService.addBookmark(bookmark);
    }

    // Favorite-003: View가 만든 BookmarkDTO를 Service에 전달하고 DELETE된 행 수를 반환한다.
    public int cancelBookmark(BookmarkDTO bookmark) {
        return bookmarkService.cancelBookmark(bookmark);
    }
}
