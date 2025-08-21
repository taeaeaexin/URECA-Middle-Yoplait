package com.eureka.ip.team1.urjung_admin.backoffice.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.request.AiUserAnalysisRequestDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiUserAnalysisResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.service.AiUserAnalysisService;
import com.eureka.ip.team1.urjung_admin.common.ApiResponse;
import com.eureka.ip.team1.urjung_admin.common.enums.Result;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/ai")
public class AiUserAnalysisController {

	private final AiUserAnalysisService aiUserAnalysisService;
	
	@PostMapping("/users")
	public ResponseEntity<ApiResponse<AiUserAnalysisResponseDto>> getUserAnalysis(@RequestBody AiUserAnalysisRequestDto aiUserAnalysisRequestDto) {
		Optional<AiUserAnalysisResponseDto> userAnalysis = aiUserAnalysisService.getUserAnalysis(aiUserAnalysisRequestDto.getUserId());
		
		if(userAnalysis.isEmpty()) {
			return ResponseEntity.internalServerError().body(
					ApiResponse.<AiUserAnalysisResponseDto>builder()
						.result(Result.FAIL)
						.message("사용자의 챗봇 대화 기록이 없습니다.")
						.build()
			);
		} 
		else {
			return ResponseEntity.ok(
					ApiResponse.<AiUserAnalysisResponseDto>builder()
					.result(Result.SUCCESS)
					.data(userAnalysis.get())
					.build()
			);
		}
	}
}
