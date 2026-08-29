/*
 * 의도1: Member-001 회원가입 화면에서 아이디·비밀번호를 입력받고, 사용자에게 결과를 출력한다.
 * 의도2: View는 입력·검증·화면 흐름만 담당하며, 회원가입 SQL 실행은 Controller → Service → DAO의 흐름으로 진행한다.
 */


package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.MemberController;
import com.quotehunters.quotecollection.model.dto.MemberDTO;

import java.util.Scanner;

public class MemberView {

    // Member-001 업무 처리를 요청할 Controller.
    private final MemberController memberController = new MemberController();

    // 공통 입력 검증 메서드를 재사용한다.
    private final ScannerView scannerView = new ScannerView();

    // 공통 성공 / 오류 출력 형식을 재사용한다.
    private final ResultView resultView = new ResultView();

    // quote_user.user_id VARCHAR(30) 계약과 맞춘 최대 길이다.
    private static final int MAX_USER_ID_LENGTH = 30;

    // quote_user.user_pw VARCHAR(20) 계약과 맞춘 최대 길이다.
    private static final int MAX_USER_PW_LENGTH = 20;

    /*
     * [Member-001] 회원가입 전체 흐름이다.
     * 아이디 입력 → 중복 검사 → 비밀번호 / 확인 입력 → 가입 / 취소 → INSERT 결과 출력
     */
    public void signUp(Scanner sc) {

        System.out.println();
        System.out.println("========== 회원가입 ==========");
        System.out.println("각 입력 단계에서 0을 입력하면 회원 관리 메뉴로 돌아갑니다.");

        // 1. ID 길이와 DB 중복 검사를 통과한 아이디를 받는다.
        String userId = readAvailableUserId(sc);

        // null은 사용자가 0을 입력해 가입을 취소했다는 신호다.
        if (userId == null) {
            return;
        }

        // 2. 저장할 비밀번호와 확인 비밀번호가 같은지 검사한다.
        String userPw = readConfirmedPasswordForSignUp(sc);

        if (userPw == null) {
            return;
        }

        /*
         * 3. DB에 저장할 ID와 비밀번호만 MemberDTO에 담는다.
         * userAuth는 사용자가 입력하지 않고 Service가 1(USER)로 정한다.
         */
        MemberDTO member = new MemberDTO(userId, userPw);

        // 4. INSERT 전에 저장 예정 정보와 USER 권한을 보여 주고 확인받는다.
        printSignUpSummary(userId);

        if (!askSignUpConfirmation(sc)) {
            System.out.println("회원가입을 취소했습니다. DB에는 아무것도 저장하지 않았습니다.");
            return;
        }

        // 5. View → Controller → Service → DAO → INSERT 흐름을 시작한다.
        try {
            int result = memberController.signUp(member);

            if (result > 0) {
                resultView.successMessage("회원가입이 완료되었습니다.");
            } else {
                resultView.errorMessage("회원가입에 실패했습니다. 다시 시도해주세요.");
            }

        } catch (RuntimeException e) {
            // ID 중복과 구분되는 DB 오류를 화면에 안내한다.
            resultView.errorMessage("회원가입 중 DB 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }


    /*
     * [Member-002 + Member-003] 사용자와 관리자가 함께 쓰는 로그인 입력 화면이다.
     *
     * 이 메서드는 user_auth로 메뉴를 고르지 않는다.
     * 로그인에 성공한 MemberDTO를 Application에 반환하고,
     * Application이 user_auth == 0 / 1을 보고 관리자·사용자 메뉴를 고른다.
     *
     * 반환값 MemberDTO : ID와 비밀번호가 맞아 로그인에 성공했다는 뜻이다.
     * 반환값 null      : 사용자가 0을 입력해 로그인 화면을 취소했다는 뜻이다.
     */
    public MemberDTO login(Scanner sc) {

        System.out.println();
        System.out.println("=========== 로그인 ===========");
        System.out.println("각 입력 단계에서 0을 입력하면 회원 관리 메뉴로 돌아갑니다.");

        while (true) {
            // 1. DB 열 길이를 넘지 않는 아이디를 입력받는다.
            String userId = readTextWithinLength(
                    sc,
                    "아이디 입력 (0: 뒤로가기)",
                    MAX_USER_ID_LENGTH,
                    "아이디"
            );

            if (userId == null) {
                return null;
            }

            // 2. DB 열 길이를 넘지 않는 비밀번호를 입력받는다.
            String userPw = readTextWithinLength(
                    sc,
                    "비밀번호 입력 (0: 뒤로가기)",
                    MAX_USER_PW_LENGTH,
                    "비밀번호"
            );

            if (userPw == null) {
                return null;
            }

            try {
                // 3. View → Controller → Service → DAO → SELECT 로그인 조회를 요청한다.
                MemberDTO loginMember = memberController.login(userId, userPw);

                if (loginMember != null) {
                    // 4. 성공한 회원 정보를 Application에 반환한다.
                    resultView.successMessage(loginMember.getUserId() + "님, 로그인되었습니다.");
                    return loginMember;
                }

                // ID가 없거나 비밀번호가 달라도 같은 메시지만 보여 준다.
                resultView.errorMessage("아이디 또는 비밀번호가 올바르지 않습니다. 다시 입력해주세요.");

            } catch (RuntimeException e) {
                resultView.errorMessage("로그인 중 DB 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    }


    /*
     * 아이디 입력 전용 메서드다.
     *
     * 문자열 입력 → 30자 검사 → 중복 SELECT 순서를 모두 통과한 아이디만 반환한다.
     */
    private String readAvailableUserId(Scanner sc) {

        while (true) {
            String userId = readTextWithinLength(
                    sc,
                    "아이디 입력 (0: 뒤로가기)",
                    MAX_USER_ID_LENGTH,
                    "아이디"
            );

            if (userId == null) {
                return null;
            }

            try {
                if (memberController.isUserIdDuplicated(userId)) {
                    resultView.errorMessage("이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.");
                    continue;
                }

                return userId;

            } catch (RuntimeException e) {
                resultView.errorMessage("아이디 중복 확인 중 DB 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    }

    /*
     * 비밀번호와 비밀번호 확인값을 입력받는다.
     *
     * confirmUserPw는 두 값이 같은지 확인하는 지역변수다.
     * DB에도 MemberDTO에도 저장하지 않는다.
     */
    private String readConfirmedPasswordForSignUp(Scanner sc) {

        while (true) {
            String userPw = readTextWithinLength(
                    sc,
                    "비밀번호 입력 (0: 뒤로가기)",
                    MAX_USER_PW_LENGTH,
                    "비밀번호"
            );

            if (userPw == null) {
                return null;
            }

            String confirmUserPw = readTextWithinLength(
                    sc,
                    "비밀번호 확인 입력 (0: 뒤로가기)",
                    MAX_USER_PW_LENGTH,
                    "비밀번호 확인"
            );

            if (confirmUserPw == null) {
                return null;
            }

            if (!userPw.equals(confirmUserPw)) {
                resultView.errorMessage("비밀번호와 비밀번호 확인값이 다릅니다. 비밀번호부터 다시 입력해주세요.");
                continue;
            }

            return userPw;
        }
    }

    /*
     * ScannerView의 빈 문자열 검사 뒤에 DB 열 길이 검사를 더하는 공통 입력 메서드다.
     *
     * 반환값 null은 사용자가 0을 입력해 현재 회원가입을 취소했다는 뜻이다.
     */
    private String readTextWithinLength(
            Scanner sc,
            String prompt,
            int maxLength,
            String fieldName
    ) {

        while (true) {
            String input = scannerView.scannString(sc, prompt);

            // 이 프로젝트의 0은 현재 입력 단계에서 돌아가기 명령이다.
            if (input.equals("0")) {
                return null;
            }

            if (input.length() > maxLength) {
                resultView.errorMessage(
                        fieldName + "은(는) " + maxLength + "자 이하로 입력해주세요."
                );
                continue;
            }

            return input;
        }
    }

    // 가입 전에 "진행" 또는 "취소"를 받는다.
    private boolean askSignUpConfirmation(Scanner sc) {

        while (true) {
            System.out.println("위 정보로 가입하시겠습니까?");
            System.out.println("1. 가입");
            System.out.println("0. 취소");

            int choice = scannerView.scannInt(sc, "번호 선택");

            if (choice == 1) {
                return true;
            }

            if (choice == 0) {
                return false;
            }

            resultView.errorMessage("1 또는 0만 입력해주세요.");
        }
    }

    // 비밀번호 원문은 출력하지 않고, 입력이 완료되었다는 사실만 보여 준다.
    private void printSignUpSummary(String userId) {
        System.out.println();
        System.out.println("------- 가입 정보 확인 -------");
        System.out.println("아이디: " + userId);
        System.out.println("권한: USER (user_auth = 1)");
        System.out.println("비밀번호: 입력 완료");
        System.out.println("----------------------------");
    }
}
