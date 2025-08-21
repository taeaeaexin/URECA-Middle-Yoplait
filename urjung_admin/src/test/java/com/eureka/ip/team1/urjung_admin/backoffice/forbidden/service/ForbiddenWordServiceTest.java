package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.service;

import com.eureka.ip.team1.urjung_admin.admin.entity.Admin;
import com.eureka.ip.team1.urjung_admin.admin.repository.AdminRepository;
import com.eureka.ip.team1.urjung_admin.auth.jwt.TokenProvider;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.RequestForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.ResponseForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.entity.ForbiddenWord;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.repository.ForbiddenWordRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ForbiddenWordServiceTest {

    @InjectMocks
    private ForbiddenWordServiceImpl forbiddenWordService;

    @Mock
    private ForbiddenWordRepository forbiddenWordRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // mainServiceUrl 수동 주입
        ReflectionTestUtils.setField(forbiddenWordService, "mainServiceUrl", "http://localhost:8080");
    }

    @Test
    @DisplayName("금칙어 등록 성공")
    void testCreateForbiddenWord() {
        // given
        String bearerToken = "Bearer dummy.token.value";
        String email = "test@test.com";

        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(tokenProvider.getUsernameFromToken("dummy.token.value")).thenReturn(email);

        Admin admin = new Admin();
        admin.setAdminId(UUID.randomUUID().toString());
        admin.setEmail(email);

        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(admin));

        RequestForbidden requestDto = new RequestForbidden();
        requestDto.setWord("씨발");
        requestDto.setWordDesc("욕설");
        requestDto.setWordClass("욕설");

        ForbiddenWord savedWord = new ForbiddenWord();
        savedWord.setAdminId(admin.getAdminId());
        savedWord.setWord(requestDto.getWord());
        savedWord.setWordDesc(requestDto.getWordDesc());
        savedWord.setWordClass(requestDto.getWordClass());
        savedWord.setWordUpdate(LocalDateTime.now());

        when(forbiddenWordRepository.save(any(ForbiddenWord.class))).thenReturn(savedWord);

        // when
        ResponseForbidden response = forbiddenWordService.createForbidden(requestDto);

        // then
        assertThat(response.getWord()).isEqualTo(requestDto.getWord());
        assertThat(response.getWordDesc()).isEqualTo(requestDto.getWordDesc());
        assertThat(response.getWordClass()).isEqualTo(requestDto.getWordClass());

        verify(restTemplate).postForEntity(contains("/internal/forbidden/reload"), isNull(), eq(String.class));
    }

    @Test
    @DisplayName("금칙어 등록 실패 - Authorization 헤더 없음")
    void testCreateForbidden_MissingAuthorizationHeader() {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        RequestForbidden requestDto = new RequestForbidden();
        requestDto.setWord("나쁜말");

        // when / then
        assertThatThrownBy(() -> forbiddenWordService.createForbidden(requestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("유효한 Authorization 헤더가 없습니다.");
    }

    @Test
    @DisplayName("금칙어 등록 실패 - Authorization 헤더 형식 오류")
    void testCreateForbidden_InvalidAuthorizationHeaderFormat() {
        // given
        when(request.getHeader("Authorization")).thenReturn("Token abc.def.ghi");  // Bearer 아님

        RequestForbidden requestDto = new RequestForbidden();
        requestDto.setWord("비속어");

        // when / then
        assertThatThrownBy(() -> forbiddenWordService.createForbidden(requestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("유효한 Authorization 헤더가 없습니다.");
    }

    @Test
    @DisplayName("금칙어 등록 - 금칙어 저장 후 reload API 실패해도 정상 저장됨")
    void testCreateForbidden_ReloadFailsButCreateSucceeds() {
        // given
        String token = "Bearer fake.jwt.token";
        String email = "admin@eureka.com";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(tokenProvider.getUsernameFromToken("fake.jwt.token")).thenReturn(email);

        Admin admin = new Admin();
        admin.setAdminId(UUID.randomUUID().toString());
        admin.setEmail(email);

        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(admin));

        ForbiddenWord saved = new ForbiddenWord();
        saved.setWord("욕설");
        saved.setWordDesc("심한 욕설");
        saved.setWordClass("비속어");
        saved.setWordUpdate(LocalDateTime.now());
        saved.setAdminId(admin.getAdminId());

        when(forbiddenWordRepository.save(any())).thenReturn(saved);

        // 예외 던지도록 설정
        doThrow(new RuntimeException("연결 실패")).when(restTemplate)
                .postForEntity(contains("/internal/forbidden/reload"), isNull(), eq(String.class));

        RequestForbidden requestDto = new RequestForbidden();
        requestDto.setWord("욕설");
        requestDto.setWordDesc("심한 욕설");
        requestDto.setWordClass("비속어");

        // when
        ResponseForbidden response = forbiddenWordService.createForbidden(requestDto);

        // then
        assertThat(response.getWord()).isEqualTo("욕설");
        assertThat(response.getWordDesc()).isEqualTo("심한 욕설");
        assertThat(response.getWordClass()).isEqualTo("비속어");
    }

    @Test
    @DisplayName("금칙어 수정 성공")
    void testUpdateForbidden() {
        UUID id = UUID.randomUUID();

        ForbiddenWord existing = new ForbiddenWord();
        existing.setWordId(id);
        existing.setWord("신발");
        existing.setWordDesc("기존 설명");
        existing.setWordClass("욕설");

        when(forbiddenWordRepository.findById(id)).thenReturn(Optional.of(existing));

        RequestForbidden request = new RequestForbidden();
        request.setWord("시발");
        request.setWordDesc("수정 설명");
        request.setWordClass("심한 욕설");

        when(forbiddenWordRepository.save(existing)).thenReturn(existing);

        ResponseForbidden response = forbiddenWordService.updateForbidden(id, request);

        assertThat(response.getWord()).isEqualTo("시발");
        assertThat(response.getWordDesc()).isEqualTo("수정 설명");
        assertThat(response.getWordClass()).isEqualTo("심한 욕설");

        verify(restTemplate).postForEntity(contains("/internal/forbidden/reload"), isNull(), eq(String.class));
    }

    @Test
    @DisplayName("금칙어 삭제 성공")
    void testDeleteForbidden() {
        UUID id = UUID.randomUUID();

        forbiddenWordService.deleteForbidden(id);

        verify(forbiddenWordRepository).deleteById(id);
        verify(restTemplate).postForEntity(contains("/internal/forbidden/reload"), isNull(), eq(String.class));
    }

    @Test
    @DisplayName("금칙어 삭제 시 갱신 API 호출 실패해도 정상 동작")
    void testDeleteForbidden_whenReloadFails_shouldStillDelete() {
        // given
        UUID id = UUID.randomUUID();

        // 예외 던지게 설정
        doThrow(new RuntimeException("연결 실패"))
                .when(restTemplate)
                .postForEntity(contains("/internal/forbidden/reload"), isNull(), eq(String.class));

        // when
        forbiddenWordService.deleteForbidden(id);

        // then
        verify(forbiddenWordRepository).deleteById(id);
    }

    @Test
    @DisplayName("금칙어 전체 조회 성공")
    void testGetForbidden() {
        ForbiddenWord w1 = new ForbiddenWord();
        w1.setWordId(UUID.randomUUID());
        w1.setWord("욕설1");
        w1.setWordDesc("설명1");
        w1.setWordClass("욕");

        ForbiddenWord w2 = new ForbiddenWord();
        w2.setWordId(UUID.randomUUID());
        w2.setWord("욕설2");
        w2.setWordDesc("설명2");
        w2.setWordClass("비속어");

        when(forbiddenWordRepository.findAll()).thenReturn(List.of(w1, w2));

        List<ResponseForbidden> list = forbiddenWordService.getForbidden();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getWord()).isEqualTo("욕설1");
        assertThat(list.get(1).getWord()).isEqualTo("욕설2");
    }

    @Test
    @DisplayName("금칙어 수정 실패 - 존재하지 않는 ID")
    void testUpdateForbidden_NotFound() {
        UUID id = UUID.randomUUID();
        when(forbiddenWordRepository.findById(id)).thenReturn(Optional.empty());

        RequestForbidden request = new RequestForbidden();
        request.setWord("없는 단어");

        assertThatThrownBy(() -> forbiddenWordService.updateForbidden(id, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("금칙어를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("금칙어 수정 시 갱신 API 호출 실패해도 정상 동작")
    void testUpdateForbidden_whenReloadFails_shouldStillReturnUpdatedResponse() {
        // given
        UUID id = UUID.randomUUID();

        ForbiddenWord fw = new ForbiddenWord();
        fw.setWordId(id);
        fw.setWord("기존");
        fw.setWordDesc("desc");
        fw.setWordClass("class");

        when(forbiddenWordRepository.findById(id)).thenReturn(Optional.of(fw));
        when(forbiddenWordRepository.save(fw)).thenReturn(fw);

        // 예외 던지게 설정
        doThrow(new RuntimeException("연결 실패"))
                .when(restTemplate)
                .postForEntity(contains("/internal/forbidden/reload"), isNull(), eq(String.class));

        RequestForbidden request = new RequestForbidden();
        request.setWord("수정");
        request.setWordDesc("업데이트됨");
        request.setWordClass("변경됨");

        // when
        ResponseForbidden response = forbiddenWordService.updateForbidden(id, request);

        // then
        assertThat(response.getWord()).isEqualTo("수정");
        assertThat(response.getWordDesc()).isEqualTo("업데이트됨");
        assertThat(response.getWordClass()).isEqualTo("변경됨");
    }

    @Test
    @DisplayName("금칙어 등록 실패 - 관리자 없음")
    void testCreateForbidden_AdminNotFound() {
        String bearerToken = "Bearer dummy.token.value";
        String email = "admin@test.com";

        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(tokenProvider.getUsernameFromToken("dummy.token.value")).thenReturn(email);
        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());

        RequestForbidden request = new RequestForbidden();
        request.setWord("금칙어");

        assertThatThrownBy(() -> forbiddenWordService.createForbidden(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("관리자 정보를 찾을수 없습니다.");
    }

    @Test
    @DisplayName("금칙어 검색 - 단어 + 분류")
    void testSearchForbidden_BothParams() {
        ForbiddenWord fw = new ForbiddenWord();
        fw.setWord("시발");
        fw.setWordClass("욕");

        when(forbiddenWordRepository.findByWordClassAndWord("시발", "욕"))
                .thenReturn(List.of(fw));

        List<ResponseForbidden> result = forbiddenWordService.searchForbidden("시발", "욕");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getWord()).isEqualTo("시발");
    }

    @Test
    @DisplayName("금칙어 검색 - 단어만")
    void testSearchForbidden_OnlyWord() {
        ForbiddenWord fw = new ForbiddenWord();
        fw.setWord("씨");

        when(forbiddenWordRepository.findByWordContaining("씨"))
                .thenReturn(List.of(fw));

        List<ResponseForbidden> result = forbiddenWordService.searchForbidden("씨", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getWord()).contains("씨");
    }

    @Test
    @DisplayName("금칙어 검색 - 분류만")
    void testSearchForbidden_OnlyClass() {
        ForbiddenWord fw = new ForbiddenWord();
        fw.setWord("개새끼");
        fw.setWordClass("욕설");

        when(forbiddenWordRepository.findByWordClass("욕설"))
                .thenReturn(List.of(fw));

        List<ResponseForbidden> result = forbiddenWordService.searchForbidden(null, "욕설");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getWordClass()).isEqualTo("욕설");
    }

    @Test
    @DisplayName("금칙어 검색 - 둘 다 없음")
    void testSearchForbidden_NullParams() {
        ForbiddenWord fw = new ForbiddenWord();
        fw.setWord("꺼져");

        when(forbiddenWordRepository.findAll())
                .thenReturn(List.of(fw));

        List<ResponseForbidden> result = forbiddenWordService.searchForbidden(null, null);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("금칙어 ID 조회 - 성공")
    void testGetForbiddenById_Success() {
        UUID id = UUID.randomUUID();

        ForbiddenWord fw = new ForbiddenWord();
        fw.setWordId(id);
        fw.setWord("좆");
        fw.setWordDesc("비속어");

        when(forbiddenWordRepository.findById(id))
                .thenReturn(Optional.of(fw));

        ResponseForbidden result = forbiddenWordService.getForbiddenById(id);

        assertThat(result.getWord()).isEqualTo("좆");
        assertThat(result.getWordId()).isEqualTo(id);
    }

    @Test
    @DisplayName("금칙어 ID 조회 - 실패")
    void testGetForbiddenById_NotFound() {
        UUID id = UUID.randomUUID();

        when(forbiddenWordRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> forbiddenWordService.getForbiddenById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("금칙어를 찾을 수 없습니다.");
    }
}
