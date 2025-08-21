package com.eureka.ip.team1.urjung_admin.backoffice.plan.service;


import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDetailDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanCreateDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDto;

import java.util.List;

// 요금제 목록 Service
public interface PlanService {
    // 요금제 목록 필터링
    List<PlanDto> getPlansSorted(String sortBy, String keyword);

    // 요금제 상세 페이지
    PlanDetailDto getPlanDetail(String planId);

    // 요금제 등록
    PlanCreateDto createPlan(PlanCreateDto planCreateDto);

    // 요금제 수정
    PlanCreateDto updatePlan(String planId, PlanCreateDto planCreateDto);

    // 요금제 삭제
    void deletePlan(String planId);

    // 요금제 상세 리스트
    List<PlanDetailDto> getAllPlans();

    // 요금제 2개 비교하기
//    List<PlanDetailDto> comparePlans(List<String> planIds);
}
