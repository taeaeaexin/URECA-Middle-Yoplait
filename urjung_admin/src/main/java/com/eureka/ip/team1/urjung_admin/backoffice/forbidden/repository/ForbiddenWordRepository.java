package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.repository;

import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.entity.ForbiddenWord;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.service.ForbiddenWordService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

public interface ForbiddenWordRepository extends JpaRepository<ForbiddenWord, UUID> {
    // 검색 : 단어 포함 여부
    List<ForbiddenWord> findByWordContaining(String word);

    // 필터 : 분류 기준
    List<ForbiddenWord> findByWordClass (String wordClass);

    // 검색 + 필터 동시
    List<ForbiddenWord> findByWordClassAndWord (String word, String wordClass);
}
