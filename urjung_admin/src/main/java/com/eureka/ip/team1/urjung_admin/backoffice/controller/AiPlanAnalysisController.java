package com.eureka.ip.team1.urjung_admin.backoffice.controller;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiPlanAnalysisResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.service.AiPlanAnalysisService;
import com.eureka.ip.team1.urjung_admin.common.ApiResponse;
import com.eureka.ip.team1.urjung_admin.common.enums.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/ai/plans")
public class AiPlanAnalysisController {
    private final AiPlanAnalysisService aiPlanAnalysisService;

    // 요금제 Ai분석 API
    @PostMapping("/{planId}")
    public ResponseEntity<ApiResponse<AiPlanAnalysisResponseDto>> analyze(@PathVariable String planId) {
        return aiPlanAnalysisService.getPlanAnalysis(planId)
                .map(result -> ResponseEntity.ok(
                        ApiResponse.<AiPlanAnalysisResponseDto>builder()
                                .result(Result.SUCCESS)
                                .data(result)
                                .message("SUCCESS")
                                .build()))
                .orElseGet(() -> ResponseEntity.internalServerError().body(
                        ApiResponse.<AiPlanAnalysisResponseDto>builder()
                                .result(Result.FAIL)
                                .message("FAIL")
                                .build()));
    }
}
