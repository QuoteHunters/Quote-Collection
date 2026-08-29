/*
 * 의도1: Member-001 회원가입에 필요한 SELECT와 INSERT SQL을 quote_user에 실행한다.
 * 의도2: DAO는 DB 작업만 담당, 화면 입력은 MemberView에서, 업무 규칙과 트랜잭션은 MemberService에서 담당한다.
 */

package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.common.JDBC;
import com.quotehunters.quotecollection.model.dto.MemberDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MemberDAO {

    // member-query.xml 안의 SQL을 key-value 형태로 보관한다.
    private final Properties prop = new Properties();

    /*
     * DAO 객체가 만들어질 때 XML 파일을 읽는다.
     * 이후 prop.getProperty("키 이름")으로 필요한 SQL을 가져온다.
     */
    public MemberDAO() {
        try {
            prop.loadFromXML(new FileInputStream(
                    "src/main/java/com/quotehunters/quotecollection/mapper/member-query.xml"
            ));
        } catch (IOException e) {
            throw new RuntimeException("회원 SQL 설정 파일을 불러오지 못했습니다.", e);
        }
    }

    /*
     * [Member-001] 입력한 아이디가 DB에 이미 존재하는지 확인한다.
     *
     * true  : 같은 user_id가 있음 → View가 아이디를 다시 받는다.
     * false : 같은 user_id가 없음 → 다음 가입 단계로 진행한다.
     */
    public boolean existsMemberByUserId(Connection con, String userId) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        String query = getQuery("existsMemberByUserId");

        try {
            // XML에서 읽어 온 SELECT문을 실행할 준비를 한다.
            pstmt = con.prepareStatement(query);

            // SQL의 첫 번째 ? 자리에 입력한 아이디를 넣는다.
            pstmt.setString(1, userId);

            // SELECT 실행 결과를 ResultSet으로 받는다.
            rset = pstmt.executeQuery();

            // COUNT(*) 결과는 정확히 한 행이어야 한다.
            if (rset.next()) {
                // COUNT가 1 이상이면 이미 같은 아이디가 있다.
                return rset.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            // View가 "아이디 중복"과 "DB 오류"를 구분할 수 있도록 예외를 위로 보낸다.
            throw new RuntimeException("회원 아이디 중복 조회 중 DB 오류가 발생했습니다.", e);

        } finally {
            // DAO가 직접 만든 ResultSet과 PreparedStatement만 DAO가 닫는다.
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
    }

    /*
     * [Member-001] MemberDTO에 담긴 새 회원 정보를 quote_user에 INSERT한다.
     *
     * 반환값은 INSERT된 행 수다.
     * 회원 한 명이 정상 저장되면 보통 1을 반환한다.
     */
    public int insertMember(Connection con, MemberDTO member) {

        PreparedStatement pstmt = null;

        String query = getQuery("insertMember");

        try {
            pstmt = con.prepareStatement(query);

            // INSERT문의 첫 번째 ?부터 MemberDTO 값을 순서대로 넣는다.
            pstmt.setString(1, member.getUserId());
            pstmt.setString(2, member.getUserPw());
            pstmt.setInt(3, member.getUserAuth());

            // executeUpdate()는 INSERT에 성공한 행 수를 int로 반환한다.
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("회원가입 DB 저장 중 오류가 발생했습니다.", e);

        } finally {
            // INSERT에는 ResultSet이 없으므로 PreparedStatement만 닫는다.
            JDBC.close(pstmt);
        }
    }

    /*
     * [Member-002 + Member-003] ID와 비밀번호가 모두 맞는 회원 한 명을 찾는다.
     *
     * 반환값 MemberDTO : 로그인에 성공한 회원의 번호, 아이디, 권한을 담은 객체다.
     * 반환값 null      : ID가 없거나 비밀번호가 다른 경우이다.
     *
     */
    public MemberDTO selectMemberByLoginInfo(
            Connection con,
            String userId,
            String userPw
    ) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        String query = getQuery("selectMemberByLoginInfo");

        try {
            // XML에서 읽어 온 로그인 SELECT문을 실행할 준비를 한다.
            pstmt = con.prepareStatement(query);

            // SQL의 첫 번째 ?에는 ID, 두 번째 ?에는 비밀번호를 순서대로 넣는다.
            pstmt.setString(1, userId);
            pstmt.setString(2, userPw);

            // SELECT 실행 결과를 ResultSet으로 받는다.
            rset = pstmt.executeQuery();

            // ID는 UNIQUE이므로 로그인 성공 결과는 최대 한 행이다.
            if (rset.next()) {
                // 조회한 DB 한 행을 Java의 MemberDTO 한 객체로 옮긴다.
                MemberDTO member = new MemberDTO();
                member.setMemberId(rset.getInt("member_id"));
                member.setUserId(rset.getString("user_id"));
                member.setUserAuth(rset.getInt("user_auth"));

                return member;
            }

            // ID 또는 비밀번호가 맞지 않으면 SQL 결과 행이 없으므로 null을 반환한다.
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("회원 로그인 조회 중 DB 오류가 발생했습니다.", e);

        } finally {
            // DAO가 직접 만든 ResultSet과 PreparedStatement만 DAO가 닫는다.
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
    }


    /*
     * [Member-004] 로그인한 회원 번호로 DB에 저장된 현재 비밀번호를 한 개 조회한다.
     *
     * 반환값 String : DB에 저장된 user_pw 문자열이다.
     * 반환값 null   : member_id와 일치하는 회원 행이 없다는 뜻이다.
     *
     * 이 메서드는 비밀번호를 화면에 출력하지 않는다.
     * Service가 반환된 값과 사용자가 입력한 현재 비밀번호를 안전하게 비교한다.
     */
    public String selectMemberPasswordByMemberId(
            Connection con,
            int memberId
    ) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        String query = getQuery("selectMemberPasswordByMemberId");

        try {
            // XML에서 읽어 온 현재 비밀번호 조회 SELECT문을 실행할 준비를 한다.
            pstmt = con.prepareStatement(query);

            // SQL의 첫 번째 ? 자리에 로그인 성공 때 받은 회원 번호를 넣는다.
            pstmt.setInt(1, memberId);

            // SELECT 실행 결과를 ResultSet으로 받는다.
            rset = pstmt.executeQuery();

            // member_id는 PRIMARY KEY이므로 결과는 최대 한 행이다.
            if (rset.next()) {
                return rset.getString("user_pw");
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("현재 비밀번호 조회 중 DB 오류가 발생했습니다.", e);

        } finally {
            // DAO가 직접 만든 ResultSet과 PreparedStatement만 DAO가 닫는다.
            JDBC.close(rset);
            JDBC.close(pstmt);
        }
    }

    /*
     * [Member-004] 로그인한 회원 한 명의 비밀번호를 새 비밀번호로 UPDATE한다.
     *
     * 반환값은 UPDATE된 행 수다.
     * member_id가 존재하는 회원 한 명을 정상 변경하면 보통 1을 반환한다.
     */
    public int updateMemberPassword(
            Connection con,
            int memberId,
            String newUserPw
    ) {

        PreparedStatement pstmt = null;

        String query = getQuery("updateMemberPassword");

        try {
            // XML에서 읽어 온 UPDATE문을 실행할 준비를 한다.
            pstmt = con.prepareStatement(query);

            // 첫 번째 ?에는 새 비밀번호, 두 번째 ?에는 로그인 회원 번호를 순서대로 넣는다.
            pstmt.setString(1, newUserPw);
            pstmt.setInt(2, memberId);

            // executeUpdate()는 실제로 변경된 행 수를 int로 반환한다.
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("비밀번호 변경 DB 처리 중 오류가 발생했습니다.", e);

        } finally {
            // UPDATE에는 ResultSet이 없으므로 PreparedStatement만 닫는다.
            JDBC.close(pstmt);
        }
    }


    // XML key를 잘못 썼을 때 원인을 명확하게 알려 주는 보조 메서드다.
    private String getQuery(String key) {
        String query = prop.getProperty(key);

        if (query == null) {
            throw new IllegalStateException(
                    "member-query.xml에 '" + key + "' SQL이 없습니다."
            );
        }

        return query;
    }
}
