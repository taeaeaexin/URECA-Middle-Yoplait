package com.eureka.ip.team1.urjung_admin.backoffice.ai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.document.PermanentChatLog;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.Candidate;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.Content;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.Part;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiUserAnalysisResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.FrequentTopic;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.GeminiResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.repository.PermanentChatLogRepository;
import com.eureka.ip.team1.urjung_admin.common.exception.DatabaseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AiUserAnalysisServiceImplTest {

	@Mock
	private PermanentChatLogRepository permanentChatLogRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@InjectMocks
	private AiUserAnalysisServiceImpl aiUserAnalysisServiceImpl;
	
	private final String TEST_USER_ID = UUID.randomUUID().toString();
	private final String TEST_API_KEY = "test-api-key";
	private final String TEST_BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
	private final String TEST_MODEL = "gemini-2.0-flash";
	private final String TEST_METHOD = "generateContent";
	
	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(aiUserAnalysisServiceImpl, "apiKey", TEST_API_KEY);
		ReflectionTestUtils.setField(aiUserAnalysisServiceImpl, "baseUrl", TEST_BASE_URL);
		ReflectionTestUtils.setField(aiUserAnalysisServiceImpl, "model", TEST_MODEL);
		ReflectionTestUtils.setField(aiUserAnalysisServiceImpl, "method", TEST_METHOD);
		ReflectionTestUtils.setField(aiUserAnalysisServiceImpl, "restTemplate", restTemplate);
		ReflectionTestUtils.setField(aiUserAnalysisServiceImpl, "objectMapper", objectMapper);
	}
	
	@Test
	void getUserAnalysis_성공_Test() throws JsonProcessingException { 
		// given
		List<PermanentChatLog> chatLogList = getMockChatLog();
		
		when(permanentChatLogRepository.findAllByUserId(TEST_USER_ID))
			.thenReturn(chatLogList);
		
		String jsonResponse = "{\"summary\":\"이 사용자는 ~~ 요금제를 선호합니다.\",\"totalMessages\":2,\"frequentTopics\":[]}";

		GeminiResponseDto response = createMockGeminiResponseDto(jsonResponse);
		
		
		when(restTemplate.exchange(
				anyString(),
				eq(HttpMethod.POST),
				any(HttpEntity.class),
				eq(GeminiResponseDto.class)
		)).thenReturn(ResponseEntity.ok(response));
		
		AiUserAnalysisResponseDto analysisResponseDto = createMockAiUserAnalysisResponseDto();
		
		when(objectMapper.readValue(anyString(), eq(AiUserAnalysisResponseDto.class)))
			.thenReturn(analysisResponseDto);
		
		// when
		Optional<com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiUserAnalysisResponseDto> result 
		= aiUserAnalysisServiceImpl.getUserAnalysis(TEST_USER_ID);
		
		// then
		assertTrue(result.isPresent());
		assertEquals(analysisResponseDto, result.get());
        verify(permanentChatLogRepository).findAllByUserId(TEST_USER_ID);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(GeminiResponseDto.class));
        verify(objectMapper).readValue(anyString(), eq(AiUserAnalysisResponseDto.class));
		
	}
	
	@Test
	void getUserAnalysis_채팅_로그_X_Test() { 
		// given
		when(permanentChatLogRepository.findAllByUserId(TEST_USER_ID))
			.thenReturn(List.of());
		// when
		Optional<AiUserAnalysisResponseDto> userAnalysis = aiUserAnalysisServiceImpl.getUserAnalysis(TEST_USER_ID);
		
		// then
		assertTrue(userAnalysis.isEmpty());
	}
	
	@Test
	void getUserAnalysis_DB_에러_Test() { 
		// given
		when(permanentChatLogRepository.findAllByUserId(TEST_USER_ID))
			.thenThrow(new RuntimeException());
		
		// then
		assertThrows(
				DatabaseException.class,
				() -> aiUserAnalysisServiceImpl.getUserAnalysis(TEST_USER_ID)
		);
	}
	
	private List<PermanentChatLog> getMockChatLog() {
		List<Content> userMessages = List.of(
				Content.createContent(
						"user",
						List.of(Part.createPart("hi"))
				)
		);
		
		List<Content> botMessages = List.of(
				Content.createContent(
						"bot",
						List.of(Part.createPart("bye"))
				)
		);
		
		String sessionId1 = UUID.randomUUID().toString();
		String sessionId2 = UUID.randomUUID().toString();
		
		PermanentChatLog chatLog1 = 
				PermanentChatLog.createChatLog(TEST_USER_ID, sessionId1, userMessages);
		
		PermanentChatLog chatLog2 = 
				PermanentChatLog.createChatLog(TEST_USER_ID, sessionId2, botMessages);
		
		return new ArrayList<>(List.of(chatLog1, chatLog2));
	}
	
	private GeminiResponseDto createMockGeminiResponseDto(String jsonContent) {
		GeminiResponseDto geminiResponseDto = GeminiResponseDto.createGeminiResponseDto(
				List.of(Candidate.createCandidate(
						Content.createContent(
								"bot",
								List.of(Part.createPart(jsonContent))
						)
				))
		);
		
		return geminiResponseDto;
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
