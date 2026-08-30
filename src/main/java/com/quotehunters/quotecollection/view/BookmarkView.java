/*
 * 의도1: Favorite-001 명언 상세 화면에서 즐겨찾기 상태를 보여 주고, 아직 저장하지 않은 명언을 즐겨찾기에 추가한다.
 * 의도2: View는 입력·화면 흐름·결과 출력만 담당하며, DB 상태 확인과 INSERT는 Controller → Service → DAO에 맡긴다.
 *
 */

package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.BookmarkController;
import com.quotehunters.quotecollection.model.dto.BookmarkDTO;
import com.quotehunters.quotecollection.model.dto.MemberDTO;
import com.quotehunters.quotecollection.model.dto.QuoteDTO;

import java.util.List;
import java.util.Scanner;

public class BookmarkView {

    // Favorite-001 업무 처리를 요청할 Controller다.
    private final BookmarkController bookmarkController = new BookmarkController();

    // 숫자 메뉴 입력 검증을 재사용한다.
    private final ScannerView scannerView = new ScannerView();

    // 공통 성공 / 오류 출력 형식을 재사용한다.
    private final ResultView resultView = new ResultView();

    /*
     * [Favorite-001] 명언 상세에서 즐겨찾기 상태를 표시하고, 미등록 상태일 때만 추가 메뉴를 보여 준다.
     *
     * 즐겨찾기 추가가 성공하면 return하지 않고 while의 처음으로 돌아간다.
     * 따라서 같은 명언 상세를 다시 출력할 때 "즐겨찾기 등록됨" 상태가 갱신되어 보인다.
     */
    public void showQuoteDetailForFavorite(
            Scanner sc,
            MemberDTO loginMember,
            QuoteDTO selectedQuote
    ) {

        if (!hasValidLoginMember(loginMember) || !hasValidSelectedQuote(selectedQuote)) {
            resultView.errorMessage("로그인 회원 또는 선택한 명언 정보를 확인할 수 없습니다.");
            return;
        }

        while (true) {
            try {
                // 로그인 회원 번호와 선택 명언 번호로 현재 즐겨찾기 상태를 DB에서 확인한다.
                boolean isBookmarked = bookmarkController.isBookmarked(
                        loginMember.getMemberId(),
                        selectedQuote.getQuoteId()
                );

                printQuoteDetail(selectedQuote, isBookmarked);

                if (isBookmarked) {
                    // Favorite-002 취소는 아직 범위 밖이므로, 등록 상태에서는 뒤로가기만 제공한다.
                    System.out.println("0. 명언 목록으로");

                    int selectedMenu = scannerView.scannInt(sc, "번호 선택");

                    if (selectedMenu == 0) {
                        return;
                    }

                    resultView.errorMessage("이 명언은 이미 즐겨찾기에 등록되어 있습니다. 0을 입력해 돌아가세요.");
                    continue;
                }

                // 아직 즐겨찾기에 없는 명언일 때만 추가 메뉴를 보여 준다.
                System.out.println("1. 즐겨찾기 추가");
                System.out.println("0. 명언 목록으로");

                int selectedMenu = scannerView.scannInt(sc, "번호 선택");

                if (selectedMenu == 0) {
                    return;
                }

                if (selectedMenu != 1) {
                    resultView.errorMessage("1 또는 0만 입력해주세요.");
                    continue;
                }

                /*
                 * 회원 번호는 사용자가 직접 입력하지 않는다.
                 * 로그인 성공 MemberDTO의 memberId와 선택 QuoteDTO의 quoteId를 묶어 전달한다.
                 */
                BookmarkDTO bookmark = new BookmarkDTO(
                        loginMember.getMemberId(),
                        selectedQuote.getQuoteId()
                );

                // View → Controller → Service → DAO → INSERT 흐름을 시작한다.
                int result = bookmarkController.addBookmark(bookmark);

                if (result > 0) {
                    resultView.successMessage("즐겨찾기에 추가되었습니다.");
                    // 같은 명언 상세 화면을 다시 그려 "등록됨" 상태를 표시한다.
                    continue;
                }

                /*
                 * 상세 진입 뒤 다른 요청이 먼저 INSERT했을 수 있으므로 0일 때 상태를 다시 확인한다.
                 * 이미 등록됐다면 친절하게 중복 메시지를, 아니라면 일반 실패 메시지를 출력한다.
                 */
                if (bookmarkController.isBookmarked(
                        loginMember.getMemberId(),
                        selectedQuote.getQuoteId()
                )) {
                    resultView.errorMessage("이미 즐겨찾기에 등록된 명언입니다.");
                } else {
                    resultView.errorMessage("즐겨찾기 추가에 실패했습니다. 다시 시도해주세요.");
                }

            } catch (RuntimeException e) {
                /*
                 * DB 연결 자체가 끊긴 경우에는 상태 SELECT를 다시 실행할 수 없으므로
                 * 무한히 오류만 반복하지 않고 사용자가 재시도 또는 목록 복귀를 선택하게 한다.
                 */
                resultView.errorMessage("즐겨찾기 처리 중 DB 오류가 발생했습니다.");
                System.out.println("1. 다시 시도");
                System.out.println("0. 명언 목록으로");

                int selectedMenu = scannerView.scannInt(sc, "번호 선택");

                if (selectedMenu == 0) {
                    return;
                }

                if (selectedMenu != 1) {
                    resultView.errorMessage("1 또는 0만 입력해주세요.");
                }
            }
        }
    }

    // 전체 명언 목록에서 선택하기 쉽게 화면 순번과 명언 요약을 출력한다.
    private void printQuoteListForFavorite(List<QuoteDTO> quoteList) {

        System.out.println();
        System.out.println("======= 명언 탐색 / 전체 목록 =======");

        for (int i = 0; i < quoteList.size(); i++) {
            QuoteDTO quote = quoteList.get(i);

            System.out.println(
                    (i + 1) + ". "
                            + quote.getQuoteContent()
                            + " | 인물: " + quote.getPersonName()
                            + " | 주제: " + quote.getThemeName()
            );
        }

        System.out.println("===================================");
    }

    // 명언 상세 내용과 현재 즐겨찾기 상태를 함께 출력한다.
    private void printQuoteDetail(QuoteDTO selectedQuote, boolean isBookmarked) {

        System.out.println();
        System.out.println("=========== 명언 상세 ===========");
        System.out.println("명언 번호: " + selectedQuote.getQuoteId());
        System.out.println("명언: " + selectedQuote.getQuoteContent());
        System.out.println("인물: " + selectedQuote.getPersonName());
        System.out.println("주제: " + selectedQuote.getThemeName());
        System.out.println(
                "즐겨찾기 상태: " + (isBookmarked ? "등록됨" : "미등록")
        );
        System.out.println("===============================");
    }

    // 로그인에 성공했으며 memberId가 있는지 확인하는 보조 메서드다.
    private boolean hasValidLoginMember(MemberDTO loginMember) {
        return loginMember != null && loginMember.getMemberId() > 0;
    }

    // 명언 조회 결과에서 선택됐으며 quoteId가 있는지 확인하는 보조 메서드다.
    private boolean hasValidSelectedQuote(QuoteDTO selectedQuote) {
        return selectedQuote != null && selectedQuote.getQuoteId() > 0;
    }
}
