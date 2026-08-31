/*
 * 의도1: MemberView의 회원 관련 요청을 MemberService까지 전달하고 결과를 다시 View로 돌려준다.
 * 의도2: Controller는 중간 연결만 담당하며, 입력은 MemberView에서, SQL·Connection 처리는 Service/DAO에서 담당한다.
 */

package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.MemberDTO;
import com.quotehunters.quotecollection.model.service.MemberService;

public class MemberController {

    // [Member-001~005] 회원 업무와 DB 처리를 담당하는 Service 객체다.
    private final MemberService memberService = new MemberService();

    // [Member-001] View가 요청한 아이디 중복 검사를 Service에 전달한다.
    public boolean isUserIdDuplicated(String userId) {
        return memberService.isUserIdDuplicated(userId);
    }

    // [Member-001] View가 만든 회원가입 DTO를 Service에 전달한다.
    public int signUp(MemberDTO member) {
        return memberService.signUp(member);
    }

    // [Member-002 + Member-003] View가 받은 ID와 비밀번호를 Service에 전달하고, 로그인 회원 DTO 또는 null을 돌려준다.
    public MemberDTO login(String userId, String userPw) {
        return memberService.login(userId, userPw);
    }

    // [Member-004 + Member-005] 현재 비밀번호가 로그인한 회원의 DB 비밀번호와 같은지 확인한다.
    public boolean isCurrentPasswordCorrect(int memberId, String currentUserPw) {
        return memberService.isCurrentPasswordCorrect(memberId, currentUserPw);
    }

    // [Member-004] 로그인한 회원의 새 비밀번호 저장을 Service에 요청하고 UPDATE 행 수를 반환한다.
    public int changePassword(int memberId, String newUserPw) {
        return memberService.changePassword(memberId, newUserPw);
    }

    // Member-005 회원탈퇴
    public boolean withdraw(MemberDTO member) {
        return memberService.withdraw(member);
    }
}
