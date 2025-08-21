//package com.eureka.ip.team1.urjung_admin.backoffice.plan.controller;
//
//import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDetailDto;
//import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanCreateDto;
//import com.eureka.ip.team1.urjung_admin.backoffice.plan.service.PlanService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.MediaType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(PlanController.class)
//public class PlanControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private PlanService planService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // 요금제 목록 조회 조건
//    @Test
//    @DisplayName("요금제 목록 조회 성공 (정렬 파라미터 popular)")
//    void getPlans_success() throws Exception {
//        // given
//        PlanCreateDto plan1 = PlanCreateDto.builder()
//                .id("uuid-1")
//                .name("Plan 1")
//                .price(30000)
//                .description("Description 1")
//                .dataAmount(10000L)
//                .callAmount(500L)
//                .smsAmount(200L)
//                .build();
//
//        when(planService.getPlansSorted("popular")).thenReturn(Arrays.asList(plan1));
//
//        // when & then
//        mockMvc.perform(get("/api/admin/plans").param("sortBy", "popular"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("SUCCESS"))
//                .andExpect(jsonPath("$.data[0].id").value("uuid-1"))
//                .andExpect(jsonPath("$.data[0].name").value("Plan 1"));
//    }
//
//    @Test
//    @DisplayName("요금제 목록 조회 - 빈 리스트 반환 (정렬 파라미터 price)")
//    void getPlans_emptyList() throws Exception {
//        // given
//        when(planService.getPlansSorted("price")).thenReturn(Collections.emptyList());
//
//        // when & then
//        mockMvc.perform(get("/api/admin/plans").param("sortBy", "price"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("SUCCESS"))
//                .andExpect(jsonPath("$.data").isEmpty());
//    }
//
//    @Test
//    @DisplayName("요금제 상세 조회 성공")
//    void getPlanDetail_success() throws Exception {
//        // given
//        String planId = "uuid-1";
//
//        PlanDetailDto planDetail = PlanDetailDto.builder()
//                .id(planId)
//                .name("Plan 1")
//                .price(30000)
//                .description("Description 1")
//                .dataAmount(10000L)
//                .callAmount(500L)
//                .smsAmount(200L)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        when(planService.getPlanDetail(planId)).thenReturn(planDetail);
//
//        // when & then
//        mockMvc.perform(get("/api/admin/plans/{planId}", planId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("SUCCESS"))
//                .andExpect(jsonPath("$.data.id").value(planId))
//                .andExpect(jsonPath("$.data.name").value("Plan 1"))
//                .andExpect(jsonPath("$.data.price").value(30000));
//    }
//
//    @Test
//    @DisplayName("요금제 생성 성공")
//    void createPlans_success() throws Exception {
//        // given
//        PlanCreateDto requestDto = PlanCreateDto.builder()
//                .name("Plan 1")
//                .price(55000)
//                .description("Description 1")
//                .dataAmount(15000L)
//                .callAmount(1000L)
//                .smsAmount(300L)
//                .build();
//
//        PlanCreateDto responseDto = PlanCreateDto.builder()
//                .id("uuid-1")
//                .name("Plan 1")
//                .price(55000)
//                .description("Description 1")
//                .dataAmount(15000L)
//                .callAmount(1000L)
//                .smsAmount(300L)
//                .build();
//
//        when(planService.createPlan(any(PlanCreateDto.class))).thenReturn(responseDto);
//
//        // when & then
//        mockMvc.perform(post("/api/admin/plans")
//                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
//                        .content(new ObjectMapper().writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("SUCCESS"))
//                .andExpect(jsonPath("$.data.id").value("uuid-1"))
//                .andExpect(jsonPath("$.data.name").value("Plan 1"))
//                .andExpect(jsonPath("$.data.price").value(55000));
//    }
//
//    @Test
//    @DisplayName("요금제 수정 성공")
//    void updatePlans_success() throws Exception {
//        // given
//        String planId = "uuid-1";
//
//        PlanCreateDto requestDto = PlanCreateDto.builder()
//                .name("TestPlan 1")
//                .price(55000)
//                .description("Description 1")
//                .dataAmount(15000L)
//                .callAmount(1000L)
//                .smsAmount(300L)
//                .build();
//
//        PlanCreateDto responseDto = PlanCreateDto.builder()
//                .id("uuid-1")
//                .name("TestPlan 1")
//                .price(55000)
//                .description("Description 1")
//                .dataAmount(15000L)
//                .callAmount(1000L)
//                .smsAmount(300L)
//                .build();
//
//        when(planService.updatePlan(eq(planId), any(PlanCreateDto.class))).thenReturn(responseDto);
//
//        // when & then
//        mockMvc.perform(put("/api/admin/plans/{id}", planId)
//                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("SUCCESS"))
//                .andExpect(jsonPath("$.data.id").value("uuid-1"))
//                .andExpect(jsonPath("$.data.name").value("TestPlan 1"))
//                .andExpect(jsonPath("$.data.price").value(55000));
//    }
//
//    @Test
//    @DisplayName("요금제 삭제 성공")
//    void deletePlans_success() throws Exception {
//        // given
//        String planId = "uuid-1";
//
//        // when & then
//        mockMvc.perform(delete("/api/admin/plans/{planId}", planId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result").value("SUCCESS"));
//    }
//}
