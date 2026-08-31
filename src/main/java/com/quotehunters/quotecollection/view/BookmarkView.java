/*
 * 의도1: Favorite-001 명언 상세의 추가, Favorite-002 My Page 목록·상세, Favorite-003 목록 상세의 취소 화면 흐름을 담당한다.
 * 의도2: View는 입력·화면 흐름·결과 출력만 담당하며, DB 상태 확인과 SELECT·INSERT·DELETE는 Controller → Service → DAO에 맡긴다.
 */

package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.BookmarkController;
import com.quotehunters.quotecollection.model.dto.BookmarkDTO;
import com.quotehunters.quotecollection.model.dto.MemberDTO;
import com.quotehunters.quotecollection.model.dto.QuoteDTO;

import java.util.List;
import java.util.Scanner;

public class BookmarkView {

    private static final String HEADER = "==========";
    private static final String LINE = "------------------------------";

    // Favorite-001·002·003 업무 처리를 요청할 Controller다.
    private final BookmarkController bookmarkController = new BookmarkController();

    // 숫자 메뉴 입력 검증을 재사용한다.
    private final ScannerView scannerView = new ScannerView();

    // 공통 성공 / 오류 출력 형식을 재사용한다.
    private final ResultView resultView = new ResultView();

    /*
     * [Favorite-001 준비 화면]
     * 명언 조회 기능이 반환한 List<QuoteDTO>에서 한 명언을 선택한다.
     *
     * 실제 팀 통합에서는 QuoteView/QuoteController의 명언 상세 흐름이 선택한 QuoteDTO를
     * showQuoteDetailForFavorite(...)에 넘기면 된다.
     */
    public QuoteDTO selectQuoteForFavorite(Scanner sc, List<QuoteDTO> quoteList) {

        if (quoteList == null || quoteList.isEmpty()) {
            resultView.errorMessage("선택할 명언이 없습니다.");
            return null;
        }

        printQuoteListForFavorite(quoteList);
        System.out.println("0. 이전 메뉴로");

        while (true) {
            int selectedNumber = scannerView.scannInt(sc, "상세로 볼 명언 번호 선택");

            if (selectedNumber == 0) {
                return null;
            }

            if (selectedNumber < 1 || selectedNumber > quoteList.size()) {
                resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
                continue;
            }

            // 화면의 1번은 List의 0번이므로 -1을 해서 실제 선택 객체를 반환한다.
            return quoteList.get(selectedNumber - 1);
        }
    }

    /*
     * [Favorite-001] 명언 탐색에서 선택한 명언 상세에 즐겨찾기 추가 상태와 메뉴를 보여 준다.
     *
     * 추가 또는 취소가 성공하면 같은 명언 상세를 다시 보여 최신 즐겨찾기 상태를 확인한다.
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
                    System.out.println("이미 즐겨찾기에 등록된 명언입니다.");
                    System.out.println("1. 즐겨찾기 취소");
                    System.out.println("0. 이전 화면으로");

                    int selectedMenu = scannerView.scannInt(sc, "번호 선택");

                    if (selectedMenu == 0) {
                        return;
                    }

                    if (selectedMenu != 1) {
                        resultView.errorMessage("1 또는 0만 입력해주세요.");
                        continue;
                    }

                    if (!confirmBookmarkCancellation(sc)) {
                        continue;
                    }

                    BookmarkDTO bookmark = new BookmarkDTO(
                            loginMember.getMemberId(),
                            selectedQuote.getQuoteId()
                    );

                    int result = bookmarkController.cancelBookmark(bookmark);

                    if (result > 0) {
                        resultView.successMessage("즐겨찾기가 취소되었습니다.");
                        // 같은 명언 상세를 다시 출력해 미등록 상태를 보여 준다.
                        continue;
                    } else {
                        resultView.errorMessage(
                                "선택한 즐겨찾기가 없거나 이미 취소되었습니다. 이전 화면으로 돌아갑니다."
                        );
                    }

                    return;
                }

                // 아직 즐겨찾기에 없는 명언일 때만 추가 메뉴를 보여 준다.
                System.out.println("1. 즐겨찾기 추가");
                System.out.println("0. 이전 화면으로");

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
                    // 같은 명언 상세를 다시 출력해 등록 상태를 보여 준다.
                    continue;
                }

                /*
                 * 상세 진입 뒤 다른 요청이 먼저 INSERT했을 수 있으므로 0일 때 상태를 다시 확인한다.
                 * 이미 등록됐다면 중복 메시지를, 아니라면 일반 실패 메시지를 출력한다.
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
                 * 처리 오류가 난 경우에는 상태 조회를 무한히 반복하지 않고
                 * 사용자가 재시도 또는 이전 화면 복귀를 선택하게 한다.
                 */
                resultView.errorMessage("즐겨찾기 처리 중 오류가 발생했습니다.");
                if (!askRetryOrGoBack(sc, "0. 이전 화면으로")) {
                    return;
                }
            }
        }
    }

    /*
     * [Favorite-002] My Page에서 현재 로그인 회원의 즐겨찾기 목록을 조회하고,
     * 사용자가 번호를 고르면 그 명언의 최신 상세 화면으로 들어간다.
     *
     * 이 메서드의 while은 상세에서 돌아오거나 Favorite-003 취소가 끝날 때마다
     * 목록 SELECT를 다시 실행한다. 따라서 취소된 명언이 빠진 최신 목록이 출력된다.
     */
    public void showFavoriteList(Scanner sc, MemberDTO loginMember) {

        if (!hasValidLoginMember(loginMember)) {
            resultView.errorMessage("로그인 회원 정보를 확인할 수 없습니다.");
            return;
        }

        while (true) {
            List<QuoteDTO> favoriteQuoteList;

            try {
                // 로그인한 회원의 memberId로 자기 즐겨찾기 목록만 조회한다.
                favoriteQuoteList = bookmarkController.selectFavoriteQuotesByMemberId(
                        loginMember.getMemberId()
                );
            } catch (RuntimeException e) {
                // 빈 목록과 DB 오류를 같은 메시지로 처리하지 않는다.
                resultView.errorMessage("즐겨찾기 목록 조회 중 오류가 발생했습니다.");
                if (askRetryOrGoBack(sc, "0. My Page로")) {
                    continue;
                }
                return;
            }

            if (favoriteQuoteList.isEmpty()) {
                // Favorite-002: 목록이 비어 있는 것은 정상 결과이므로 My Page로 돌아간다.
                System.out.println("저장한 즐겨찾기가 없습니다.");
                return;
            }

            printFavoriteQuoteList(favoriteQuoteList);
            System.out.println("0. My Page로");

            int selectedNumber;

            // 범위 오류일 때 목록을 다시 출력하지 않고 번호만 다시 입력받는다.
            while (true) {
                selectedNumber = scannerView.scannInt(sc, "상세로 볼 즐겨찾기 번호 선택");

                if (selectedNumber == 0) {
                    return;
                }

                if (selectedNumber < 1 || selectedNumber > favoriteQuoteList.size()) {
                    resultView.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
                    continue;
                }

                break;
            }

            /*
             * 화면의 1번, 2번은 DB의 quote_id가 아니다.
             * List에서 고른 QuoteDTO 안의 실제 quoteId를 꺼내 최신 상세 SELECT에 사용한다.
             */
            QuoteDTO selectedFromList = favoriteQuoteList.get(selectedNumber - 1);
            QuoteDTO selectedQuote = null;

            while (selectedQuote == null) {
                try {
                    selectedQuote = bookmarkController.selectFavoriteQuoteDetail(
                            loginMember.getMemberId(),
                            selectedFromList.getQuoteId()
                    );
                } catch (RuntimeException e) {
                    resultView.errorMessage("즐겨찾기 명언 상세 조회 중 오류가 발생했습니다.");
                    if (askRetryOrGoBack(sc, "0. 즐겨찾기 목록으로")) {
                        continue;
                    }
                    break;
                }

                if (selectedQuote == null) {
                    resultView.errorMessage(
                            "선택한 즐겨찾기가 없거나 이미 취소되었습니다. 목록을 다시 조회합니다."
                    );
                    break;
                }
            }

            if (selectedQuote == null) {
                /*
                 * 상세 대상이 사라졌거나 사용자가 이전 화면을 선택하면
                 * while 처음으로 돌아가 최신 목록을 다시 조회한다.
                 */
                continue;
            }

            // 상세에서 0을 누르거나 Favorite-003 취소가 끝나면 이 메서드로 돌아와 목록을 재조회한다.
            showFavoriteQuoteDetailForCancel(sc, loginMember, selectedQuote);
        }
    }

    /*
     * [Favorite-003] Favorite-002 목록에서 선택한 즐겨찾기 명언의 상세를 보여 주고 취소를 처리한다.
     * 성공·대상 없음·취소 실행 뒤 0건 모두 return하여 부모 showFavoriteList(...)가 목록을 다시 조회하게 한다.
     */
    private void showFavoriteQuoteDetailForCancel(
            Scanner sc,
            MemberDTO loginMember,
            QuoteDTO selectedQuote
    ) {

        while (true) {
            try {
                /*
                 * 삭제 버튼을 보이기 직전에 memberId + quoteId 조합을 다시 확인한다.
                 * 이 확인은 현재 로그인 회원이 실제로 소유한 즐겨찾기만 취소 대상으로 삼는다.
                 */
                boolean isBookmarked = bookmarkController.isBookmarked(
                        loginMember.getMemberId(),
                        selectedQuote.getQuoteId()
                );

                if (!isBookmarked) {
                    resultView.errorMessage(
                            "선택한 즐겨찾기가 없거나 이미 취소되었습니다. 목록을 다시 조회합니다."
                    );
                    return;
                }

                printFavoriteQuoteDetail(selectedQuote);
                System.out.println("1. 즐겨찾기 취소");
                System.out.println("0. 즐겨찾기 목록으로");

                int selectedMenu = scannerView.scannInt(sc, "번호 선택");

                if (selectedMenu == 0) {
                    return;
                }

                if (selectedMenu != 1) {
                    resultView.errorMessage("1 또는 0만 입력해주세요.");
                    continue;
                }

                // Favorite-003 S3: 취소 실행 전에 한 번 더 사용자의 의사를 확인한다.
                if (!confirmBookmarkCancellation(sc)) {
                    // '아니오'를 고르면 DELETE 없이 같은 상세 화면을 다시 보여 준다.
                    continue;
                }

                /*
                 * 사용자는 회원 번호나 즐겨찾기 번호를 직접 입력하지 않는다.
                 * 로그인 MemberDTO의 memberId와 현재 상세 QuoteDTO의 quoteId를 BookmarkDTO에 담아 전달한다.
                 */
                BookmarkDTO bookmark = new BookmarkDTO(
                        loginMember.getMemberId(),
                        selectedQuote.getQuoteId()
                );

                // View → Controller → Service → DAO → DELETE 흐름을 시작한다.
                int result = bookmarkController.cancelBookmark(bookmark);

                if (result > 0) {
                    resultView.successMessage("즐겨찾기가 취소되었습니다.");
                } else {
                    // 영향 행이 0이면 성공 메시지를 출력하지 않고 최신 목록으로 돌아간다.
                    resultView.errorMessage(
                            "선택한 즐겨찾기가 없거나 이미 취소되었습니다. 목록을 다시 조회합니다."
                    );
                }

                // Favorite-003 S4: 성공·0건 결과 모두 부모 목록 화면으로 돌아가 SELECT를 다시 실행한다.
                return;

            } catch (RuntimeException e) {
                /*
                 * Service는 예외가 나면 rollback을 처리한다.
                 * View는 오류를 안내하고 재시도 또는 부모 목록 복귀를 선택하게 한다.
                 */
                resultView.errorMessage("즐겨찾기 취소 중 오류가 발생했습니다.");
                if (!askRetryOrGoBack(sc, "0. 즐겨찾기 목록으로")) {
                    return;
                }
            }
        }
    }

    // 오류 뒤 재시도 또는 이전 화면 복귀를 선택한다.
    private boolean askRetryOrGoBack(Scanner sc, String previousMenu) {
        while (true) {
            System.out.println("1. 다시 시도");
            System.out.println(previousMenu);

            int selectedMenu = scannerView.scannInt(sc, "번호 선택");

            if (selectedMenu == 1) {
                return true;
            }

            if (selectedMenu == 0) {
                return false;
            }

            resultView.errorMessage("1 또는 0만 입력해주세요.");
        }
    }

    /*
     * [Favorite-003 확인 화면]
     * true  : '예'를 골랐으므로 DELETE를 진행한다.
     * false : '아니오'를 골랐으므로 DELETE를 실행하지 않는다.
     */
    private boolean confirmBookmarkCancellation(Scanner sc) {

        while (true) {
            String answer = scannerView.scannString(
                    sc,
                    "즐겨찾기를 취소하시겠습니까? (예 / 아니오)"
            );

            if (answer.equals("예")) {
                return true;
            }

            if (answer.equals("아니오")) {
                return false;
            }

            resultView.errorMessage("예, 아니오 중 하나를 입력해주세요.");
        }
    }

    // Favorite-002 목록을 화면용 임시 번호, 명언, 인물, 주제 순서로 출력한다.
    private void printFavoriteQuoteList(List<QuoteDTO> favoriteQuoteList) {

        printHeader("내 즐겨찾기 명언");

        for (int i = 0; i < favoriteQuoteList.size(); i++) {
            QuoteDTO quote = favoriteQuoteList.get(i);

            System.out.println(
                    (i + 1) + ". "
                            + quote.getQuoteContent()
                            + " | 인물: " + quote.getPersonName()
                            + " | 주제: " + quote.getThemeName()
            );
        }

        System.out.println(LINE);
    }

    // Favorite-003 취소 전, 현재 선택한 즐겨찾기 명언의 상세를 출력한다.
    private void printFavoriteQuoteDetail(QuoteDTO selectedQuote) {

        printHeader("즐겨찾기 명언 상세");
        System.out.println("명언: " + selectedQuote.getQuoteContent());
        System.out.println("인물: " + selectedQuote.getPersonName());
        System.out.println("주제: " + selectedQuote.getThemeName());
        System.out.println("즐겨찾기 상태: 등록됨");
        System.out.println(LINE);
    }

    // 전체 명언 목록에서 선택하기 쉽게 화면 순번과 명언 요약을 출력한다.
    private void printQuoteListForFavorite(List<QuoteDTO> quoteList) {

        printHeader("명언 목록");

        for (int i = 0; i < quoteList.size(); i++) {
            QuoteDTO quote = quoteList.get(i);

            System.out.println(
                    (i + 1) + ". "
                            + quote.getQuoteContent()
                            + " | 인물: " + quote.getPersonName()
                            + " | 주제: " + quote.getThemeName()
            );
        }

        System.out.println(LINE);
    }

    // 명언 상세 내용과 현재 즐겨찾기 상태를 함께 출력한다.
    private void printQuoteDetail(QuoteDTO selectedQuote, boolean isBookmarked) {

        printHeader("명언 상세");
        System.out.println("명언: " + selectedQuote.getQuoteContent());
        System.out.println("인물: " + selectedQuote.getPersonName());
        System.out.println("주제: " + selectedQuote.getThemeName());
        System.out.println("즐겨찾기 상태: " + (isBookmarked ? "등록됨" : "미등록"));
        System.out.println(LINE);
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println(HEADER + " " + title + " " + HEADER);
    }

    // 로그인한 일반 회원이며 memberId가 있는지 확인하는 보조 메서드다.
    private boolean hasValidLoginMember(MemberDTO loginMember) {
        return loginMember != null
                && loginMember.getMemberId() > 0
                && loginMember.getUserAuth() == 1;
    }

    // 명언 조회 결과에서 선택됐으며 quoteId가 있는지 확인하는 보조 메서드다.
    private boolean hasValidSelectedQuote(QuoteDTO selectedQuote) {
        return selectedQuote != null && selectedQuote.getQuoteId() > 0;
    }
}
