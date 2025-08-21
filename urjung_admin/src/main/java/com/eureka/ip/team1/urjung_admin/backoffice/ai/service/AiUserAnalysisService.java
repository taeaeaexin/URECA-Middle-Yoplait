package com.eureka.ip.team1.urjung_admin.backoffice.ai.service;

import java.util.Optional;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiUserAnalysisResponseDto;

public interface AiUserAnalysisService {

	Optional<AiUserAnalysisResponseDto> getUserAnalysis(String userId);
	
}
