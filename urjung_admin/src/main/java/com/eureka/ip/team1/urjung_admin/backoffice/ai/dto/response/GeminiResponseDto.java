package com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response;

import java.util.List;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.Candidate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeminiResponseDto {
	
	private List<Candidate> candidates;
	
	// 팩토리 메소드
	public static GeminiResponseDto createGeminiResponseDto(List<Candidate> candidates) {
		GeminiResponseDto geminiResponseDto = new GeminiResponseDto();
		
		geminiResponseDto.candidates = candidates;
		
		return geminiResponseDto;
	}
	
}
