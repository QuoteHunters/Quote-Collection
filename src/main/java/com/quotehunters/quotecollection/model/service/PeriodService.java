package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.model.dao.PeriodDAO;
import com.quotehunters.quotecollection.model.dto.PeriodDTO;

import java.sql.Connection;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.close;
import static com.quotehunters.quotecollection.common.JDBC.commit;
import static com.quotehunters.quotecollection.common.JDBC.getConnection;
import static com.quotehunters.quotecollection.common.JDBC.rollback;

public class PeriodService {

    private final PeriodDAO periodDAO = new PeriodDAO();

    // [조회] 전체 시대 목록
    public List<PeriodDTO> allPeriods() {
        Connection con = openConnection();

        try {
            return periodDAO.selectAllPeriod(con);
        } finally {
            close(con);
        }
    }

    // [중복확인]
    public boolean existsPeriodName(String periodName) {
        Connection con = openConnection();

        try {
            return periodDAO.selectPeriodByName(con, periodName) != null;
        } finally {
            close(con);
        }
    }

    // [등록]
    public int insertPeriod(String periodName) {
        Connection con = openConnection();

        try {
            int result = periodDAO.insertPeriod(con, periodName);

            if (result > 0) {
                commit(con);
            } else {
                rollback(con);
            }

            return result;
        } catch (RuntimeException e) {
            throw rollbackFailure(con, e);
        } finally {
            close(con);
        }
    }

    // [수정] 수정된 행이 있으면 1, 대상이 없으면 0을 반환하고 SQL 오류는 예외로 전달
    public int updatePeriod(int periodId, String periodName) {
        Connection con = openConnection();

        try {
            int result = periodDAO.updatePeriod(con, new PeriodDTO(periodId, periodName));

            if (result > 0) {
                commit(con);
            } else {
                rollback(con);
            }

            return result;
        } catch (RuntimeException e) {
            throw rollbackFailure(con, e);
        } finally {
            close(con);
        }
    }

    // [삭제] bookmark→quote→person→period 순서로 삭제
    //
    public int deletePeriod(int periodId) {
        Connection con = openConnection();

        try {
            periodDAO.deleteBookmarkByPeriod(con, periodId);   // ① 즐겨찾기 삭제
            periodDAO.deleteQuoteByPeriod(con, periodId);      // ② 명언 삭제
            periodDAO.deletePersonByPeriod(con, periodId);     // ③ 인물 삭제
            int result = periodDAO.deletePeriod(con, periodId);    // ④ 시대 삭제

            if (result > 0) {                 // 시대가 실제로 지워졌으면
                commit(con);                  // 4개 삭제를 한꺼번에 확정
            } else {                          // 시대가 안 지워졌으면
                rollback(con);                // ①②③까지 포함해 전부 취소
            }

            return result;
        } catch (RuntimeException e) {
            throw rollbackFailure(con, e);
        } finally {
            close(con);
        }
    }

    private Connection openConnection() {
        Connection con = getConnection();

        if (con == null) {
            throw new IllegalStateException("데이터베이스 연결에 실패했습니다.");
        }

        return con;
    }

    private RuntimeException rollbackFailure(Connection con, RuntimeException original) {
        try {
            rollback(con);
        } catch (RuntimeException rollbackFailure) {
            original.addSuppressed(rollbackFailure);
        }

        return original;
    }
}
