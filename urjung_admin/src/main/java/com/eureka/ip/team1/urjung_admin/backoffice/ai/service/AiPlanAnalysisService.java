package com.eureka.ip.team1.urjung_admin.backoffice.ai.service;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiPlanAnalysisResponseDto;

import java.util.Optional;

public interface AiPlanAnalysisService {
    Optional<AiPlanAnalysisResponseDto> getPlanAnalysis(String planId);
}
