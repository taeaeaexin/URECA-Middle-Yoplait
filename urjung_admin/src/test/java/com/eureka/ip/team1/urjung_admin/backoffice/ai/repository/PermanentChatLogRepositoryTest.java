package com.eureka.ip.team1.urjung_admin.backoffice.ai.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.document.PermanentChatLog;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.Content;
import com.eureka.ip.team1.urjung_admin.backoffice.ai.dto.Part;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class PermanentChatLogRepositoryTest {

	@Autowired
	private PermanentChatLogRepository permanentChatLogRepository;
	
    private List<String> createdTestIds = new ArrayList<>();

	
	private String userId1 = UUID.randomUUID().toString();
	private String userId2 = UUID.randomUUID().toString();
	private String sessionId1 = UUID.randomUUID().toString();
	private String sessionId2 = UUID.randomUUID().toString();
	private String sessionId3 = UUID.randomUUID().toString();
	private List<Content> userMessages = List.of(
			Content.createContent(
					"user",
					List.of(Part.createPart("hi"))
			)
	);
	
	private List<Content> botMessages = List.of(
			Content.createContent(
					"bot",
					List.of(Part.createPart("bye"))
			)
	);
	
	@BeforeEach
	void setUp() {
        List<PermanentChatLog> testLogs = List.of(
                PermanentChatLog.createChatLog(userId1, sessionId1, userMessages),
                PermanentChatLog.createChatLog(userId1, sessionId1, botMessages),
                PermanentChatLog.createChatLog(userId1, sessionId2, userMessages),
                PermanentChatLog.createChatLog(userId1, sessionId2, botMessages),
                PermanentChatLog.createChatLog(userId2, sessionId3, userMessages),
                PermanentChatLog.createChatLog(userId2, sessionId3, botMessages)
            );
            
            createdTestIds = permanentChatLogRepository.saveAll(testLogs)
                .stream()
                .map(PermanentChatLog::getId)
                .collect(Collectors.toList());
	}
	
	@AfterEach
	void cleanUp() {
		permanentChatLogRepository.deleteAllById(createdTestIds);
		createdTestIds.clear();
	}
	
	@Test
	void findAllByUserId_test() {
		List<PermanentChatLog> chatLog1 = permanentChatLogRepository.findAllByUserId(userId1);
		List<PermanentChatLog> chatLog2 = permanentChatLogRepository.findAllByUserId(userId2);
		
		// 유저 1
		assertEquals(userId1, chatLog1.get(0).getUserId());
		assertEquals(sessionId1, chatLog1.get(0).getSessionId());
		assertEquals(userMessages, chatLog1.get(0).getMessages());
		assertEquals(botMessages, chatLog1.get(1).getMessages());
		
		assertEquals(userId1, chatLog1.get(2).getUserId());
		assertEquals(sessionId2, chatLog1.get(2).getSessionId());
		assertEquals(userMessages, chatLog1.get(2).getMessages());
		assertEquals(botMessages, chatLog1.get(3).getMessages());
		
		for(PermanentChatLog chatLog : chatLog1) {
			log.info("userId : {}, sessionId : {}, messages : {}", chatLog.getUserId(), chatLog.getSessionId(), chatLog.getMessages());
		}
		
		// 유저 2
		assertEquals(userId2, chatLog2.get(0).getUserId());
		assertEquals(sessionId3, chatLog2.get(0).getSessionId());
		assertEquals(userMessages, chatLog2.get(0).getMessages());
		assertEquals(botMessages, chatLog2.get(1).getMessages());
		
		for(PermanentChatLog chatLog : chatLog2) {
			log.info("userId : {}, sessionId : {}, messages : {}", chatLog.getUserId(), chatLog.getSessionId(), chatLog.getMessages());
		}
	}

}
