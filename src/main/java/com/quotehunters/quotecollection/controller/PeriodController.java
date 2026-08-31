package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.PeriodDTO;
import com.quotehunters.quotecollection.model.service.PeriodService;

import java.util.List;

public class PeriodController {

    private PeriodService periodService = new PeriodService();   // 주방장 준비

    // [조회] 전체 시대 목록을 그대로 전달
    public List<PeriodDTO> allPeriods() {
        return periodService.allPeriods();
    }

    // [등록] 검증 → 통과 시 등록 → 결과 메시지 반환 (명세 Period-001의 Validation)
    public String registPeriod(String periodName) {

        // 검증 1: 빈 값 (null이거나, 공백만 친 경우) — View 1차 방어의 2차 안전망
        if (periodName == null || periodName.trim().isEmpty()) {
            return "시대명을 입력해주세요.";
        }

        // 검증 2: 글자 수 20자 제한 (★ Country의 30자와 다른 유일한 지점 — 명세서 30자 표기는 오타)
        if (periodName.length() > 20) {
            return "시대명은 20자를 초과할 수 없습니다.";
        }

        // 검증 3: 중복
        if (periodService.existsPeriodName(periodName)) {
            return "이미 등록된 시대명입니다.";
        }

        int result = periodService.insertPeriod(periodName);   // 검증 통과 → 등록 실행

        if (result > 0) {
            return "시대가 등록되었습니다.";
        } else {
            return "시대 등록에 실패했습니다.";
        }
    }

    // [수정] 검증 → 통과 시 수정 → 결과 메시지 반환 (명세 Period-003의 S5)
    public String modifyPeriod(int periodId, String periodName) {

        // 검증 1: 빈 값
        if (periodName == null || periodName.trim().isEmpty()) {
            return "시대명을 입력해주세요.";
        }

        // 검증 2: 글자 수 (20자)
        if (periodName.length() > 20) {
            return "시대명은 20자를 초과할 수 없습니다.";
        }

        // 검증 3: 중복
        if (periodService.existsPeriodName(periodName)) {
            return "이미 등록된 시대명입니다.";
        }

        int result = periodService.updatePeriod(periodId, periodName);   // 검증 통과 → 수정 실행

        if (result > 0) {
            return "시대가 수정되었습니다.";
        } else {
            return "시대 수정에 실패했습니다.";
        }
    }

    // [삭제] 연쇄 삭제 실행 (검증 없음 — 삭제 확인은 View의 1·2차 경고가 담당)
    public String removePeriod(int periodId) {
        int result = periodService.deletePeriod(periodId);   // 트랜잭션 삭제 실행

        if (result > 0) {
            return "시대가 삭제되었습니다.";      // 명세 S8-1의 완료 메시지
        } else {
            return "시대 삭제에 실패했습니다.";
        }
    }
}
