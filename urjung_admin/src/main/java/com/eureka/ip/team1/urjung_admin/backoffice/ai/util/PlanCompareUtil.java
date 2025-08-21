package com.eureka.ip.team1.urjung_admin.backoffice.ai.util;

import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDetailDto;

import java.util.List;
import java.util.stream.Collectors;

// 현재 플랜과 비슷한 플랜 필터링
public class PlanCompareUtil {
    public static List<PlanDetailDto> findSimilarPlans(PlanDetailDto target, List<PlanDetailDto> allPlans) {
        // 필터 사용자 설정
        int priceRange = 5000;  // 가격 오차 범위 ±5,000원
        long dataRange = 5L;    // 데이터 용량 오차 범위 ±5GB

        return allPlans.stream()
                .filter(p -> !p.getId().equals(target.getId()))
                .filter(p -> Math.abs(p.getPrice() - target.getPrice()) <= priceRange)
                .filter(p -> Math.abs(p.getDataAmount() - target.getDataAmount()) <= dataRange)
                .collect(Collectors.toList());
    }
}
