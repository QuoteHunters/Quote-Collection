/*
    의도1: 회원 한 명의 정보를 한 덩어리로 담아 전달한다.
    의도2: DTO는 회원 정보만 담으며, 입력은 MemberView에서, SQL의 실행은 DAO에서 담당한다.
*/


package com.quotehunters.quotecollection.model.dto;

public class MemberDTO {

    // 회원관리의 ERD 설계에 따라 memberId, userId, userPw, userAuth의 정보를 담는다.
    private int memberId;
    private String userId;
    private String userPw;
    private int userAuth;

    // 일단 비어있는 회원 객체 한개를 만든다.
    public MemberDTO(){
    }

    // memberId는 AUTO_Increment이므로 여기서 받지 않는다.
    // 공개 회원가입 사용자는 userAuth 1로 저장되며, 그 값은 나중에 MemberService가 정한다.
    public MemberDTO(String userId, String userPw){
        this.userId = userId;
        this.userPw = userPw;
    }

    // MemberDTO에 저장된 회원 번호를 반환한다.
    // 전달받은 회원 번호를 MemberDTO의 memberId 필드에 저장한다.
    public int getMemberId(){ return memberId; }
    public void setMemberId(int memberId){ this.memberId = memberId; }


    // 현재 객체에 저장된 회원 아이디를 String 값으로 반환한다.
    // 전달받은 회원 아이디를 이 객체의 userId 필드에 저장한다.
    public String getUserId(){ return  userId; }
    public void setUserId(String userId){ this.userId = userId; }

    // 현재 객체에 저장된 비밀번호 문자열을 반환한다.
    // 전달받은 비밀번호 문자열을 이 객체의 userPw 필드에 저장한다.
    public String getUserPw(){ return userPw; }
    public void setUserPw(String userPw){ this.userPw = userPw; }

    // 현재 객체에 저장된 권한 숫자를 int 값으로 반환한다.
    // 전달받은 권한 숫자를 이 객체의 userAuth 필드에 저장한다.
    // 회원가입에서는 MemberService가 일반회원 값 1을 전달할 예정입니다.
    public int getUserAuth(){ return userAuth; }
    public void setUserAuth(int userAuth){ this.userAuth = userAuth; }

    // 확인용 코드
    @Override
    public String toString() {
        return "MemberDTO{"
                + "memberId=" + memberId
                + ", userId='" + userId + "'"
                + ", userPw='" + userPw + "'"
                + ", userAuth=" + userAuth
                + "}";
    }


}
