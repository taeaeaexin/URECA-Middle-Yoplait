package com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FrequentTopic {
	
	private String topic;
	
	private Long frequency;
	
	private Double percentage;
	
	private List<String> examples;
	
}
