package com.eureka.ip.team1.urjung_admin.backoffice.ai.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.document.PermanentChatLog;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.AiUserAnalysisResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response.GeminiResponseDto;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.repository.PermanentChatLogRepository;
import com.eureka.ip.team1.urjung_admin.common.exception.ChatBotException;
import com.eureka.ip.team1.urjung_admin.common.exception.DatabaseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiUserAnalysisServiceImpl implements AiUserAnalysisService {

	@Value("${gemini.api.key}")
	private String apiKey;

	@Value("${gemini.api.base-url}")
	private String baseUrl;

	@Value("${gemini.api.model-name}")
	private String model;

	@Value("${gemini.api.method}")
	private String method;

	private final PermanentChatLogRepository permanentChatLogRepository;

	private RestTemplate restTemplate = new RestTemplate();

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Optional<AiUserAnalysisResponseDto> getUserAnalysis(String userId) {
		try {
			List<PermanentChatLog> chatLogList = permanentChatLogRepository.findAllByUserId(userId);

			log.info("채팅 로그 {}", chatLogList);
			if(chatLogList.isEmpty()) {
				log.info("사용자의 채팅 기록이 없습니다.");
				return Optional.empty();
			}

			log.info("채팅 로그 {} 건 조회 완료", chatLogList.size());



			String prompt = createAnalysisPrompt(chatLogList);

			Map<String, Object> chatRequestBody = buildChatRequestBody(chatLogList, prompt);

			log.info("사용자 분석 완료");
			return Optional.of(sendRequest(chatRequestBody));

		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException("Mongo DB 접근 중, 예외가 발생하였습니다.");
		}
	}

	private AiUserAnalysisResponseDto sendRequest(Map<String, Object> chatRequestBody) {
		try {
			String url = "%s/models/%s:%s?key=%s"
					.formatted(
							baseUrl,
							model,
							method,
							apiKey
					);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(chatRequestBody, headers);

			GeminiResponseDto response = restTemplate.exchange(
					url,
					HttpMethod.POST,
					entity,
					GeminiResponseDto.class
			).getBody();

			String content = response.getCandidates()
					.get(0)
					.getContent()
					.getParts()
					.get(0)
					.getText();

			AiUserAnalysisResponseDto aiUserAnalysisResponseDto =
					objectMapper.readValue(content, AiUserAnalysisResponseDto.class);

			return aiUserAnalysisResponseDto;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ChatBotException();
		}
	}

	private String createAnalysisPrompt(List<PermanentChatLog> chatLogList) {
		return String.format("""
	            다음은 사용자의 통신사 요금제 상담 채팅 데이터입니다:

	            %s

	            위 데이터를 분석하여 다음을 JSON 형식으로 정확히 제공해주세요:

	            **중요: 반드시 통신사 요금제와 관련된 주제만 분석해주세요.**

	            **포함할 주제 범위:**
	            - 요금제 관련: 알뜰폰, 가성비 요금제, 무제한 요금제, 가족 요금제, 프리미엄 요금제 등
	            - 데이터 관련: 데이터 사용량, 데이터 절약, 무제한 데이터 등
	            - 통화/문자 관련: 통화 품질, 무제한 통화, 문자 서비스 등
	            - 부가서비스: 5G, 해외로밍, OTT 서비스, 결합상품 등
	            - 할인/혜택: 학생할인, 시니어할인, 가족할인 등
	            - 통신사 관련: SKT, KT, LG U+ 서비스 비교 등

	            **제외할 주제:**
	            - 일반적인 인사말이나 단순 질문
	            - 통신사와 무관한 기술 질문
	            - 프로그래밍, 개발 관련 질문
	            - 기타 통신 서비스와 관련 없는 내용

	            **분석 결과:**
	            1. summary: 사용자의 통신 서비스 이용 패턴에 대한 요약 (문자열)
	            2. totalMessages: 통신사 관련 사용자 메시지 수만 계산 (숫자)
	            3. frequentTopics: 통신사 관련 주제들만 추출 (배열)
	               - topic: 주제명 (예: "알뜰 요금제", "데이터 사용량")
	               - frequency: 언급 횟수 (숫자)
	               - percentage: 통신사 관련 대화에서 차지하는 비율 (숫자)
	               - examples: 해당 주제의 질문 예시들 (문자열 배열)

	            frequentTopics는 비율 순으로, 상위 3건만 추출해주세요.
	            
	            **만약 통신사 관련 질문이 없다면:**
	            - summary: "통신사 관련 질문이 발견되지 않았습니다."
	            - totalMessages: 0
	            - frequentTopics: [] (빈 배열)

	            응답은 반드시 올바른 JSON 형식이어야 합니다.
	            """, chatLogList);
	}

	private Map<String, Object> buildChatRequestBody(
			List<PermanentChatLog> chatLogList,
			String prompt
	) {
		Map<String, Object> requestBody = Map.of(
				"contents", List.of(
						Map.of(
								"role", "user",
								"parts", List.of(
										Map.of("text", prompt)
								)
						)
				),
				"generationConfig", Map.of(
						"response_mime_type", "application/json",
						"response_schema", getJsonSchema()
				)
		);

		return requestBody;
	}

	private Map<String, Object> getJsonSchema() {
		return Map.of(
				"type", "object",
				"properties", Map.of(
						"summary", Map.of(
								"type", "string",
								"description", "사용자의 AI 사용 패턴에 대한 전반적인 요약"
						),
						"totalMessages", Map.of(
								"type", "integer",
								"description", "총 메시지 수"
						),
						"frequentTopics", Map.of(
								"type", "array",
								"description", "자주 질문하는 주제들",
								"items", Map.of(
										"type", "object",
										"properties", Map.of(
												"topic", Map.of(
														"type", "string",
														"description", "주제명"
												),
												"frequency", Map.of(
														"type", "integer",
														"description", "언급 횟수"
												),
												"percentage", Map.of(
														"type", "number",
														"description", "전체 대화에서 차지하는 비율"
												),
												"examples", Map.of(
														"type", "array",
														"items", Map.of("type", "string"),
														"description", "해당 주제의 질문 예시들"
												)
										),
										"required", List.of("topic", "frequency", "percentage")
								)
						)
				),
				"required", List.of("summary", "totalMessages", "frequentTopics")
		);
	}

}