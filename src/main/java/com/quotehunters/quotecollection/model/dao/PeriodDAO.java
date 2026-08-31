package com.quotehunters.quotecollection.model.dao;

import com.quotehunters.quotecollection.model.dto.PeriodDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PeriodDAO {

    private final Properties prop = new Properties();

    public PeriodDAO() {
        try {
            prop.loadFromXML(new FileInputStream(
                    "src/main/java/com/quotehunters/quotecollection/mapper/period-query.xml"));
        } catch (IOException e) {
            throw new IllegalStateException("시대 SQL 파일을 불러오지 못했습니다.", e);
        }
    }

    // 전체 시대 목록 조회
    public List<PeriodDTO> selectAllPeriod(Connection con) {
        List<PeriodDTO> periodList = new ArrayList<>();
        String sql = prop.getProperty("selectAllPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                periodList.add(new PeriodDTO(
                        rs.getInt("period_id"),
                        rs.getString("period_name")
                ));
            }
        } catch (SQLException e) {
            throw databaseError("시대 목록 조회", e);
        }

        return periodList;
    }

    public PeriodDTO selectPeriodByName(Connection con, String periodName) {
        String sql = prop.getProperty("selectPeriodByName");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, periodName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new PeriodDTO(
                            rs.getInt("period_id"),
                            rs.getString("period_name")
                    );
                }
            }
        } catch (SQLException e) {
            throw databaseError("시대 중복 확인", e);
        }

        return null;
    }

    // 시대 등록
    public int insertPeriod(Connection con, String periodName) {
        String sql = prop.getProperty("insertPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, periodName);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw databaseError("시대 등록", e);
        }
    }

    // 시대명 수정
    public int updatePeriod(Connection con, PeriodDTO period) {
        String sql = prop.getProperty("updatePeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, period.getPeriodName());        // 1번 ? = 새 이름
            pstmt.setInt(2, period.getPeriodId());             // 2번 ? = 대상 번호
            return pstmt.executeUpdate();                      // UPDATE 실행
        } catch (SQLException e) {
            throw databaseError("시대 수정", e);
        }
    }

    // 해당 시대 소속 인물의 명언에 달린 즐겨찾기 삭제
    public int deleteBookmarkByPeriod(Connection con, int periodId) {
        String sql = prop.getProperty("deleteBookmarkByPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, periodId);
            return pstmt.executeUpdate();                      // 지운 즐겨찾기 개수
        } catch (SQLException e) {
            throw databaseError("시대 관련 즐겨찾기 삭제", e);
        }
    }

    // 해당 시대 소속 인물들의 명언 삭제
    public int deleteQuoteByPeriod(Connection con, int periodId) {
        String sql = prop.getProperty("deleteQuoteByPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, periodId);
            return pstmt.executeUpdate();                      // 지운 명언 개수
        } catch (SQLException e) {
            throw databaseError("시대 관련 명언 삭제", e);
        }
    }

    // 해당 시대 소속 인물 삭제
    public int deletePersonByPeriod(Connection con, int periodId) {
        String sql = prop.getProperty("deletePersonByPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, periodId);
            return pstmt.executeUpdate();                      // 지운 인물 수
        } catch (SQLException e) {
            throw databaseError("시대 관련 인물 삭제", e);
        }
    }

    // 시대 삭제
    public int deletePeriod(Connection con, int periodId) {
        String sql = prop.getProperty("deletePeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, periodId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw databaseError("시대 삭제", e);
        }
    }

    private IllegalStateException databaseError(String operation, SQLException e) {
        return new IllegalStateException(operation + " 중 오류가 발생했습니다.", e);
    }
}
