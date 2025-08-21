package com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AiUserAnalysisResponseDto {

	private String summary;
	
	private Long totalMessages;
	
	private List<FrequentTopic> frequentTopics;
}
