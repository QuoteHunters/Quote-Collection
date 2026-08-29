package com.quotehunters.quotecollection.model.service;

import com.quotehunters.quotecollection.model.dao.PeriodDAO;
import com.quotehunters.quotecollection.model.dto.PeriodDTO;

import java.sql.Connection;
import java.util.List;

import static com.quotehunters.quotecollection.common.JDBC.*;

public class PeriodService {
    private PeriodDAO periodDAO = new PeriodDAO();

    // [조회] 전체 시대 목록
    public List<PeriodDTO> allPeriods() {
        Connection con = getConnection();
        List<PeriodDTO> periods = periodDAO.selectAllPeriod(con);

        close(con);

        return periods;
    }

    // [중복확인] 이미 등록된 시대명인지 (있으면 true)
    public boolean existsPeriodName(String periodName) {
        Connection con = getConnection();
        boolean exists = false;

        try {

            exists = periodDAO.selectPeriodByName(con, periodName) != null;
        } finally {
            close(con);
        }

        return exists;
    }

    // [등록]
    public int insertPeriod(String periodName) {
        Connection con = getConnection();
        int result = periodDAO.insertPeriod(con, periodName);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }
        close(con);

        return result;
    }

    // [수정] 성공 1, 실패 0
    public int updatePeriod(int periodId, String periodName) {
        Connection con = getConnection();

        int result = periodDAO.updatePeriod(con, new PeriodDTO(periodId, periodName));

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }
        close(con);

        return result;
    }

    // [삭제] bookmark→quote→person→period 순서로 삭제
    // 성공하면 1, 실패하면 0 반환
    public int deletePeriod(int periodId) {
        Connection con = getConnection();
        int result = 0;

        try {
            periodDAO.deleteBookmarkByPeriod(con, periodId);   // ① 즐겨찾기 삭제
            periodDAO.deleteQuoteByPeriod(con, periodId);      // ② 명언 삭제
            periodDAO.deletePersonByPeriod(con, periodId);     // ③ 인물 삭제
            result = periodDAO.deletePeriod(con, periodId);    // ④ 시대 삭제

            if (result > 0) {                 // 시대가 실제로 지워졌으면
                commit(con);                  // 4개 삭제를 한꺼번에 확정
            } else {                          // 시대가 안 지워졌으면
                rollback(con);                // ①②③까지 포함해 전부 취소
            }
        } finally {
            close(con);
        }

        return result;
    }
}