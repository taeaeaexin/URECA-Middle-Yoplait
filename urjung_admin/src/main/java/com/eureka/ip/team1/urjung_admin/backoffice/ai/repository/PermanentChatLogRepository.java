package com.eureka.ip.team1.urjung_admin.backoffice.ai.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eureka.ip.team1.urjung_admin.backoffice.ai.document.PermanentChatLog;

public interface PermanentChatLogRepository extends MongoRepository<PermanentChatLog, String>{

	List<PermanentChatLog> findAllByUserId(String userId);
	
}
