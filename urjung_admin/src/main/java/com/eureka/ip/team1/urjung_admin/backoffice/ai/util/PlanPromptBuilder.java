package com.eureka.ip.team1.urjung_admin.backoffice.ai.util;

import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDetailDto;

import java.util.List;

// 1. 현재 플랜과 비슷한 플랜을 PlanCompareUtil에서 필터링 -> 취소
// 1-1. 모든 요금제를 비교 대상으로 사용
// 2. 필터링 된 데이터를 가져와서 현재 플랜과 비교 분석
public class PlanPromptBuilder {
    public static String buildPrompt(PlanDetailDto nowPlan, List<PlanDetailDto> allPlans, int totalUser, int totalPlan) {
        StringBuilder comparisonList = new StringBuilder();
        for (int i = 0; i < allPlans.size(); i++) {
            PlanDetailDto p = allPlans.get(i);
            comparisonList.append(String.format("%d. %s / %d원 / %dMB / %d분 / %d건\n",
                    i + 1,
                    p.getName(),
                    p.getPrice(),
                    p.getDataAmount(),
                    p.getCallAmount(),
                    p.getSmsAmount()));
        }

        return String.format("""
                당신은 통신사의 요금제를 기획 및 운영하는 전문가입니다.
                다음은 우리 회사의 전체 요금제 목록입니다.
                그 중 하나의 요금제를 중심으로 자사 내부 요금제들과 비교 분석을 수행해주세요.
                
                [요금제 정보]
                - 이름: %s
                - 가격: %d원
                - 데이터: %dMB
                - 통화: %d분
                - 문자: %d건
                - 설명: %s
                - 가입자 점수 : %.1f점

                [비교 요금제 목록]
                %s
            
                [가입자 점수 해석 기준]
                100점 초과 | 매우 훌륭한 가입자 수
                80점 이상 | 매우 많은 가입자 수
                60~79점 | 많은 가입자 수
                40~59점 | 보통 수준의 가입자 수 (나쁘지 않음)
                20~39점 | 낮은 가입자 수
                20점 미만 | 매우 낮은 가입자 수
                Nan | 가입자 없음

                - 가입자 점수는 모든 상황을 고려하여 가입자 수를 상대적으로 나타낸 수치입니다
                - 가입자 점수를 말할때 가입자 수라고 말하세요 (점수 말하지 마세요)
                
                목표는 운영 전략 수립에 도움이 되는 정보를 항목별로 구조화하는 것입니다.
                아래 형식에 맞추어 구체적이고 수치 기반으로 작성해주세요:
                1. summary: 요금제의 상태를 한 줄로 요약해주세요. 예: “구성 유사 요금제가 많아 통합 필요”
                2. decision: 아래 중 하나를 골라 기입하고, 판단 기준도 간단히 써주세요 (유지 권장 / 통합 고려 / 혜택 개선 필요 / 제거 고려)
                3. strength: 이 요금제가 내부적으로 갖는 강점을 구체적으로 서술해주세요
                4. weakness: 이 요금제가 갖는 단점이나 부족한 요소를 지적해주세요
                5. redundancy: 자사 다른 요금제와 구성, 가격이 얼마나 유사한지, 어떤 요금제와 겹치는지를 설명해주세요
                6. userFeedback: 가입자 수 기반으로 유의미한 운영 인사이트를 서술해주세요 (점수가 몇점인지는 언급하지 마세요)
                7. recommendedStrategy: 기획자/운영자가 취할 수 있는 구체적 전략을 제안해주세요 (ex: 가격 인하, 부가 혜택 추가, 통합, 리네이밍 등)
                8. priority: 운영자 입장에서 이 요금제를 즉시 조정해야 할지 판단해주세요
                
                - 타 통신사 요금제와 비교하지 말고, 자사 요금제끼리 비교만 하세요.
                - 수치 기반으로 근거를 제시하면 좋습니다.
                - 반드시 위 8개 항목에 해당하는 응답만 구조화해서 반환해주세요.

                [응답 형식]
                {
                  "summary": "...",
                  "decision": "...",
                  "strength": "...",
                  "weakness": "...",
                  "redundancy": "...",
                  "userFeedback": "...",
                  "recommendedStrategy": "...",
                  "priority": "..."
                  }
                1. 응답 형식에 맞춰서 간단히(한 줄) 응답하세요.
                2. 반드시 주어진 데이터에서만 유효한 응답을 하세요. (없거나, 거짓 데이터를 상상하는것 금지)
                3. 데이터는 MB로 주어지지만, GB 단위로(1024로 나눠서) 피드백 할것
                4. 통화,데이터가 -1이면 무제한, 문자는 15000건이면 기본 제공으로 판단
                """,
                nowPlan.getName(),
                nowPlan.getPrice(),
                nowPlan.getDataAmount(),
                nowPlan.getCallAmount(),
                nowPlan.getSmsAmount(),
                nowPlan.getDescription(),
                nowPlan.getScore(),
                comparisonList
        );
    }
}
