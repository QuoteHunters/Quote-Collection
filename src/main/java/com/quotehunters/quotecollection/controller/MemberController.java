/*
 * 의도1: MemberView의 회원가입 요청을 MemberService까지 전달하고 결과를 다시 View로 돌려준다.
 * 의도2: Controller는 중간 연결만 담당하며, 입력은 MemberView에서, SQL·Connection 처리는 Service/DAO에서 담당한다.
 */

package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.MemberDTO;
import com.quotehunters.quotecollection.model.service.MemberService;

public class MemberController {

    // 회원가입 업무와 DB 처리를 담당하는 Service 객체다.
    private final MemberService memberService = new MemberService();

    // View가 요청한 아이디 중복 검사를 Service에 전달한다.
    public boolean isUserIdDuplicated(String userId) {
        return memberService.isUserIdDuplicated(userId);
    }

    // View가 만든 회원가입 DTO를 Service에 전달한다.
    public int signUp(MemberDTO member) {
        return memberService.signUp(member);
    }
}
