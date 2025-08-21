package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.controller;

import com.eureka.ip.team1.urjung_admin.auth.jwt.TokenProvider;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.RequestForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.ResponseForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.service.ForbiddenWordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForbiddenWordController.class)
@AutoConfigureMockMvc(addFilters = false)
class ForbiddenWordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForbiddenWordService forbiddenWordService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("금칙어 등록 성공")
    void testCreateForbiddenWord() throws Exception {
        RequestForbidden request = new RequestForbidden("욕설", "비속어입니다", "욕", "admin-uuid");
        ResponseForbidden response = new ResponseForbidden(UUID.randomUUID(), "욕설", "비속어입니다", "욕", LocalDateTime.now());

        given(forbiddenWordService.createForbidden(any(RequestForbidden.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/admin/forbidden")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("욕설"))
                .andExpect(jsonPath("$.wordDesc").value("비속어입니다"))
                .andExpect(jsonPath("$.wordClass").value("욕"));
    }

    @Test
    @DisplayName("금칙어 수정 성공")
    void testUpdateForbiddenWord() throws Exception {
        UUID id = UUID.randomUUID();
        RequestForbidden request = new RequestForbidden("변경된욕설", "수정설명", "업데이트", "admin-uuid");
        ResponseForbidden response = new ResponseForbidden(id, "변경된욕설", "수정설명", "업데이트", LocalDateTime.now());

        given(forbiddenWordService.updateForbidden(eq(id), any(RequestForbidden.class)))
                .willReturn(response);

        mockMvc.perform(put("/api/admin/forbidden/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("변경된욕설"))
                .andExpect(jsonPath("$.wordDesc").value("수정설명"))
                .andExpect(jsonPath("$.wordClass").value("업데이트"));
    }

    @Test
    @DisplayName("금칙어 삭제 성공")
    void testDeleteForbiddenWord() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/admin/forbidden/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("금칙어 전체 조회 성공")
    void testGetForbiddenWords() throws Exception {
        ResponseForbidden r1 = new ResponseForbidden(UUID.randomUUID(), "금칙어1", "설명1", "욕", LocalDateTime.now());
        ResponseForbidden r2 = new ResponseForbidden(UUID.randomUUID(), "금칙어2", "설명2", "비속어", LocalDateTime.now());

        given(forbiddenWordService.searchForbidden(null, null))
                .willReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/admin/forbidden"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].word").value("금칙어1"))
                .andExpect(jsonPath("$[1].word").value("금칙어2"));
    }

    @Test
    @DisplayName("금칙어 검색 필터 조회 성공")
    void testSearchForbiddenWordsWithFilter() throws Exception {
        ResponseForbidden r1 = new ResponseForbidden(UUID.randomUUID(), "꺼져", "설명", "욕", LocalDateTime.now());

        given(forbiddenWordService.searchForbidden("꺼", "욕"))
                .willReturn(List.of(r1));

        mockMvc.perform(get("/api/admin/forbidden")
                        .param("word", "꺼")
                        .param("wordClass", "욕"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].word").value("꺼져"));
    }

    @Test
    @DisplayName("금칙어 상세 조회 성공")
    void testGetForbiddenById() throws Exception {
        UUID id = UUID.randomUUID();
        ResponseForbidden response = new ResponseForbidden(id, "좆", "설명입니다", "비속어", LocalDateTime.now());

        given(forbiddenWordService.getForbiddenById(id))
                .willReturn(response);

        mockMvc.perform(get("/api/admin/forbidden/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("좆"))
                .andExpect(jsonPath("$.wordDesc").value("설명입니다"))
                .andExpect(jsonPath("$.wordClass").value("비속어"));
    }
}
