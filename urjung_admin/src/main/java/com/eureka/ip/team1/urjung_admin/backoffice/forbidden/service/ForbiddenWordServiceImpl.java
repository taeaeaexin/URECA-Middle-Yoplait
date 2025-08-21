package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.service;

import com.eureka.ip.team1.urjung_admin.admin.entity.Admin;
import com.eureka.ip.team1.urjung_admin.admin.repository.AdminRepository;
import com.eureka.ip.team1.urjung_admin.auth.jwt.TokenProvider;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.RequestForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.ResponseForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.entity.ForbiddenWord;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.repository.ForbiddenWordRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForbiddenWordServiceImpl implements ForbiddenWordService {
    private final ForbiddenWordRepository forbiddenWordRepository;
    private final AdminRepository adminRepository;

    private final HttpServletRequest request; // 현재 request 주입
    private final TokenProvider tokenProvider; // JwtToken 파서

    private final RestTemplate restTemplate;
    @Value("${urjung.main-service-url}")
    private String mainServiceUrl;

    @Override
    public ResponseForbidden createForbidden(RequestForbidden requestForbidden) {
        ForbiddenWord fw = new ForbiddenWord();

        fw.setWord(requestForbidden.getWord());
        fw.setWordDesc(requestForbidden.getWordDesc());
        fw.setWordClass(requestForbidden.getWordClass());
        fw.setWordUpdate(LocalDateTime.now());

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
            String email = tokenProvider.getUsernameFromToken(token);
            email = email.trim();

            Admin admin = adminRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("관리자 정보를 찾을수 없습니다."));
            fw.setAdminId(admin.getAdminId());
        } else {
            throw new RuntimeException("유효한 Authorization 헤더가 없습니다.");
        }

        ForbiddenWord saved = forbiddenWordRepository.save(fw);

        try {
            restTemplate.postForEntity(mainServiceUrl + "/internal/forbidden/reload", null, String.class);
        }catch (Exception e) {
            log.warn("금칙어 갱신 API 호출 실패: {}", e.getMessage());
        }

        return toResponse(saved);
    }

    @Override
    public ResponseForbidden updateForbidden(UUID id, RequestForbidden request) {
        ForbiddenWord fw = forbiddenWordRepository.findById(id).orElseThrow(()
                -> new RuntimeException("금칙어를 찾을 수 없습니다.")
                );
        fw.setWord(request.getWord());
        fw.setWordDesc(request.getWordDesc());
        fw.setWordClass(request.getWordClass());
        fw.setWordUpdate(LocalDateTime.now());

        ForbiddenWord saved = forbiddenWordRepository.save(fw);

        try {
            restTemplate.postForEntity(mainServiceUrl + "/internal/forbidden/reload", null, String.class);
        }catch (Exception e) {
            log.warn("금칙어 갱신 API 호출 실패: {}", e.getMessage());
        }

        return toResponse(saved);
    }

    @Override
    public void deleteForbidden(UUID id) {
        forbiddenWordRepository.deleteById(id);

        try {
            restTemplate.postForEntity(mainServiceUrl + "/internal/forbidden/reload", null, String.class);
        }catch (Exception e) {
            log.warn("금칙어 갱신 API 호출 실패: {}", e.getMessage());
        }
    }

    @Override
    public List<ResponseForbidden> getForbidden() {
        return forbiddenWordRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private ResponseForbidden toResponse(ForbiddenWord fw) {
        return new ResponseForbidden(
                fw.getWordId(),
                fw.getWord(),
                fw.getWordDesc(),
                fw.getWordClass(),
                fw.getWordUpdate()
        );
    }

    @Override
    public List<ResponseForbidden> searchForbidden(String word, String wordClass) {
        List<ForbiddenWord> results;

        if (word != null && wordClass != null ) {
            results = forbiddenWordRepository.findByWordClassAndWord(word, wordClass);
        } else if (word != null) {
            results = forbiddenWordRepository.findByWordContaining(word);

        }else if (wordClass != null) {
            results = forbiddenWordRepository.findByWordClass(wordClass);
        }else {
            results = forbiddenWordRepository.findAll();
        }
        return results.stream().map(this::toResponse).toList();
    }

    @Override
    public ResponseForbidden getForbiddenById(UUID id) {
        ForbiddenWord fw = forbiddenWordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("금칙어를 찾을 수 없습니다."));
        return toResponse(fw);
    }
}
