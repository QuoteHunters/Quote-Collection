/*
 * 의도1: Member-001 회원가입의 업무 규칙을 적용하고 DB Connection의 시작과 끝을 관리한다.
 * 의도2: Service는 USER 권한을 정하고 commit / rollback을 담당하며, SQL 실행은 MemberDAO에서 담당한다.
 */

package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dao.MemberDAO;
import com.quotehunters.quotecollection.model.dto.MemberDTO;

import java.sql.Connection;

public class MemberService {

    // 현재 DB 계약에서 1은 일반회원(USER) 권한이다.
    private static final int USER_AUTH = 1;

    // 실제 SQL 실행은 MemberDAO에 맡긴다.
    private final MemberDAO memberDAO = new MemberDAO();

    //[Member-001] 입력한 아이디가 이미 사용 중인지 확인한다.
    public boolean isUserIdDuplicated(String userId) {

        Connection con = getOpenConnection();

        try {
            return memberDAO.existsMemberByUserId(con, userId);

        } finally {
            // Connection을 만든 Service가 작업이 끝난 뒤 닫는다.
            JDBC.close(con);
        }
    }

    /*
     * [Member-001] 공개 회원가입을 처리한다.
     *
     * 사용자는 userAuth를 입력하지 않는다.
     * Service가 무조건 1(USER)을 넣고 INSERT한 뒤 결과에 따라 commit 또는 rollback한다.
     */
    public int signUp(MemberDTO member) {

        Connection con = getOpenConnection();

        try {
            // 공개 가입 회원은 항상 USER(1)로 저장한다.
            member.setUserAuth(USER_AUTH);

            // DAO가 INSERT하고, 성공한 행 수를 반환한다.
            int result = memberDAO.insertMember(con, member);

            if (result > 0) {
                // INSERT가 성공했으므로 DB 변경을 확정한다.
                JDBC.commit(con);
            } else {
                // 저장된 행이 없으면 DB 변경을 확정하지 않는다.
                JDBC.rollback(con);
            }

            return result;

        } catch (RuntimeException e) {
            // SQL 오류가 발생했을 때 열려 있는 트랜잭션을 되돌린다.
            JDBC.rollback(con);
            throw e;

        } finally {
            JDBC.close(con);
        }
    }

    /*
     * JDBC.getConnection()이 DB 연결 실패 시 null을 반환할 수 있다.
     */
    private Connection getOpenConnection() {
        Connection con = JDBC.getConnection();

        if (con == null) {
            throw new IllegalStateException("DB 연결을 만들지 못했습니다.");
        }

        return con;
    }
}
