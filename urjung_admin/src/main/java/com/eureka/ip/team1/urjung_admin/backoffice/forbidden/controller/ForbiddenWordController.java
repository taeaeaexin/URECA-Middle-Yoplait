package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.controller;

import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.RequestForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.ResponseForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.service.ForbiddenWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/forbidden")
@RequiredArgsConstructor
public class ForbiddenWordController {

    private final ForbiddenWordService forbiddenWordService;

    @PostMapping    // 금칙어 추가
    public ResponseEntity<ResponseForbidden> createForbiddenWord(@RequestBody RequestForbidden requestForbidden) {
        ResponseForbidden response = forbiddenWordService.createForbidden(requestForbidden);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")    // 금칙어 업데이트
    public ResponseEntity<ResponseForbidden> updateForbiddenWord(@PathVariable UUID id, @RequestBody RequestForbidden requestForbidden) {
        ResponseForbidden response = forbiddenWordService.updateForbidden(id, requestForbidden);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")  // 금칙어 삭제
    public ResponseEntity<Void> deleteForbiddenWord(@PathVariable UUID id) {
        forbiddenWordService.deleteForbidden(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping // 금칙어 전체 조회, 검색, 필터링
    public ResponseEntity<List<ResponseForbidden>> getOrSearchForbiddenWords(
            @RequestParam(required = false) String word,
            @RequestParam(required = false) String wordClass) {
        List<ResponseForbidden> responses = forbiddenWordService.searchForbidden(word, wordClass);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")    // 금칙어 상세 페이지 보기
    public ResponseEntity<ResponseForbidden> getForbiddenDetail(@PathVariable UUID id) {
        ResponseForbidden response = forbiddenWordService.getForbiddenById(id);
        return ResponseEntity.ok(response);
    }

}
