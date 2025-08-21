package com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiPlanAnalysisResponseDto {
//    // 요금제 추천 이유
//    private String recommendReason;
//
//    // 수정, 개선 부분
//    private String fixReason;
//
//    // 삭제, 통합의 이유
//    private String removeReason;
//
//    // 다른 요금제들과 비교
//    private String compare;
//
//    // 운영자/기획자에게 제안하는 전략
//    private String strategy;

    // 분석 상태 요약
    private String summary;        // 한 줄 요약
    private String decision;       // 유지 권장 / 통합 고려 / 혜택 개선 필요 등

    // 분석 항목별 인사이트
    private String strength;       // 강점 요약
    private String weakness;       // 약점 또는 보완 포인트
    private String redundancy;     // 내부 유사 요금제 중복 정보
    private String userFeedback;   // 가입자 수나 해지율 기반 인사이트

    // 전략 제안
    private String recommendedStrategy; // 개선 or 운영 전략 제안

    // 운영 우선순위
    private String priority;       // 낮음 / 중간 / 높음
}
