package com.eureka.ip.team1.urjung_admin.backoffice.ai.service;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiPlanAnalysisResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.GeminiResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.util.PlanPromptBuilder;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDetailDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.service.PlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiPlanAnalysisServiceImpl implements AiPlanAnalysisService {
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.base-url}")
    private String baseUrl;

    @Value("${gemini.api.model-name}")
    private String model;

    @Value("${gemini.api.method}")
    private String method;

    private final PlanService planService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Optional<AiPlanAnalysisResponseDto> getPlanAnalysis(String planId) {
        try {
            PlanDetailDto nowPlan = planService.getPlanDetail(planId);
            List<PlanDetailDto> allPlans = planService.getAllPlans();
            int totalUser = allPlans.stream()
                    .mapToInt(plan -> Optional.of(plan.getUserAmount()).orElse(0))
                    .sum();
            int totalPlan = allPlans.size();

            String prompt = PlanPromptBuilder.buildPrompt(nowPlan, allPlans, totalUser, totalPlan);
            Map<String, Object> body = buildRequestBody(prompt);

            log.info("Gemini 요금제 분석 요청 시작");
            return Optional.of(sendRequest(body));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("요금제 분석 실패", e);
        }
    }

    private AiPlanAnalysisResponseDto sendRequest(Map<String, Object> body) {
        try {
            String url = "%s/models/%s:%s?key=%s"
                    .formatted(baseUrl, model, method, apiKey);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<GeminiResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, GeminiResponseDto.class);

            String content = response.getBody()
                    .getCandidates().get(0)
                    .getContent().getParts().get(0)
                    .getText();

            // 로깅
//            log.warn("⭐⭐⭐️Gemini 응답 원문: {}", content.replace("\n", "\\n"));

            // 백틱 제거
            String json = stripCodeFence(content);

//            log.warn("⭐⭐⭐️스트립 후 응답: {}", json.replace("\n", "\\n"));

            return objectMapper.readValue(json, AiPlanAnalysisResponseDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gemini 응답 파싱 실패", e);
        }
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        return Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(Map.of("text", prompt))
                        )
                )
        );
    }

    // 백틱 제거
    private String stripCodeFence(String content) {
        if (content == null) return "";
        String s = content.trim();

        // ```json ~ ``` 제거
        if (s.startsWith("```")) {
            s = s.replaceFirst("^```[a-zA-Z]*\\R?", ""); // 시작 부분 제거
            s = s.replaceFirst("\\R?```$", "");         // 끝 부분 제거
            s = s.trim();
        }

        // { ... } 부분만 추출 (혹시 앞뒤에 텍스트가 있을 때도 대비)
        int start = s.indexOf('{');
        int end = s.lastIndexOf('}');
        if (start >= 0 && end > start) {
            s = s.substring(start, end + 1);
        }

        return s;
    }
}
