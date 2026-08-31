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

    private Properties prop = new Properties();

    public PeriodDAO() {
        try {
            prop.loadFromXML(new FileInputStream(
                    "src/main/java/com/quotehunters/quotecollection/mapper/period-query.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 전체 시대 목록 조회
    public List<PeriodDTO> selectAllPeriod(Connection con) {
        List<PeriodDTO> periodList = new ArrayList<>();
        String sql = prop.getProperty("selectAllPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                PeriodDTO period = new PeriodDTO(
                        rs.getInt("period_id"),
                        rs.getString("period_name")
                );
                periodList.add(period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return periodList;

    }

    public PeriodDTO selectPeriodByName(Connection con, String periodName) {
        PeriodDTO period = null;
        String sql = prop.getProperty("selectPeriodByName");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, periodName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    period = new PeriodDTO(
                            rs.getInt("period_id"),
                            rs.getString("period_name")
                    );
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return period;



    }

    // 시대 등록
    public int insertPeriod(Connection con, String periodName) {
        int result = 0;
        String sql = prop.getProperty("insertPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, periodName);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 시대명 수정

    public int updatePeriod(Connection con, PeriodDTO period) {
        int result = 0;
        String sql = prop.getProperty("updatePeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, period.getPeriodName());        // 1번 ? = 새 이름
            pstmt.setInt(2, period.getPeriodId());             // 2번 ? = 대상 번호
            result = pstmt.executeUpdate();                    // UPDATE 실행
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    // 해당 시대 소속 인물의 명언에 달린 즐겨찾기 삭제
    public int deleteBookmarkByPeriod(Connection con, int periodId) {
        int result = 0;
        String sql = prop.getProperty("deleteBookmarkByPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, periodId);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;                                         // 지운 즐겨찾기 개수
    }

    // 해당 시대 소속 인물들의 명언 삭제
    public int deleteQuoteByPeriod(Connection con, int periodId) {
        int result = 0;
        String sql = prop.getProperty("deleteQuoteByPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, periodId);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;                                         // 지운 명언 개수
    }

    // 해당 시대 소속 인물 삭제
    public int deletePersonByPeriod(Connection con, int periodId) {
        int result = 0;
        String sql = prop.getProperty("deletePersonByPeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, periodId);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;                                         // 지운 인물 수
    }

    // 시대 삭제
    public int deletePeriod(Connection con, int periodId) {
        int result = 0;
        String sql = prop.getProperty("deletePeriod");

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, periodId);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}

