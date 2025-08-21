package com.eureka.ip.team1.urjung_admin.backoffice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.request.AiUserAnalysisRequestDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiUserAnalysisResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.FrequentTopic;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.service.AiUserAnalysisService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AiUserAnalysisController.class)
class AiUserAnalysisControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private AiUserAnalysisService aiUserAnalysisService;
	
	@Test 
	void getUserAnalysis_성공_Test() throws Exception {
		// given
		String userId = UUID.randomUUID().toString();
		AiUserAnalysisRequestDto request = new AiUserAnalysisRequestDto();
		request.setUserId(userId);
		
		AiUserAnalysisResponseDto response = createMockAiUserAnalysisResponseDto();
		
		when(aiUserAnalysisService.getUserAnalysis(userId))
			.thenReturn(Optional.of(response));
		
		mockMvc.perform(post("/api/admin/ai/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value("SUCCESS"))
				.andExpect(jsonPath("$.data.summary").value("이 사용자는 ~~ 요금제를 선호합니다."))
				.andExpect(jsonPath("$.data.totalMessages").value(2))
				.andExpect(jsonPath("$.data.frequentTopics[0].topic").value("요금제"));
	}
	
	@Test 
	void getUserAnalysis_실패_Test() throws Exception {
		// given
		String userId = UUID.randomUUID().toString();
		AiUserAnalysisRequestDto request = new AiUserAnalysisRequestDto();
		request.setUserId(userId);
		
		when(aiUserAnalysisService.getUserAnalysis(userId))
			.thenReturn(Optional.empty());
		
		mockMvc.perform(post("/api/admin/ai/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.result").value("FAIL"))
				.andExpect(jsonPath("$.message").value("사용자의 챗봇 대화 기록이 없습니다."));
	}
	
	private AiUserAnalysisResponseDto createMockAiUserAnalysisResponseDto() {
		List<FrequentTopic> frequentTopics = List.of(
				new FrequentTopic("요금제", 1L, 50.0, List.of("요금제")),
				new FrequentTopic("데이터 사용량", 1L, 50.0, List.of("요금제"))
		);
		
		AiUserAnalysisResponseDto aiUserAnalysisResponseDto =
				new AiUserAnalysisResponseDto("이 사용자는 ~~ 요금제를 선호합니다.", 2L, frequentTopics);
		
		return aiUserAnalysisResponseDto;
	}
	

}
