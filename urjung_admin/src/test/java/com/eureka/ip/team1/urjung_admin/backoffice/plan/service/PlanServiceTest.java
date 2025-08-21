//package com.eureka.ip.team1.urjung_admin.backoffice.plan.service;
//
//import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDetailDto;
//import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanCreateDto;
//import com.eureka.ip.team1.urjung_admin.backoffice.plan.entity.Plan;
//import com.eureka.ip.team1.urjung_admin.backoffice.plan.repository.PlanRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class PlanServiceTest {
//    @Mock
//    private PlanRepository planRepository;
//
//    @InjectMocks
//    private PlanServiceImpl planService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // 요금제 목록 필터링 조회 테스트
//    @Test
//    @DisplayName("getPlansSorted should return list of PlanDto when sortBy is priceAsc")
//    void getPlansSorted_shouldReturnPlanDtoList() {
//        // given
//        Plan plan1 = Plan.builder()
//                .planId("uuid-1")
//                .name("Plan 1")
//                .price(30000)
//                .description("Description 1")
//                .dataAmount(10000L)
//                .callAmount(500L)
//                .smsAmount(200L)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        Plan plan2 = Plan.builder()
//                .planId("uuid-2")
//                .name("Plan 2")
//                .price(50000)
//                .description("Description 2")
//                .dataAmount(20000L)
//                .callAmount(1000L)
//                .smsAmount(500L)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        Mockito.when(planRepository.findAllByOrderByPriceAsc()).thenReturn(Arrays.asList(plan1, plan2));
//
//        // when
//        List<PlanCreateDto> result = planService.getPlansSorted("priceAsc");
//
//        // then
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getId()).isEqualTo("uuid-1");
//        assertThat(result.get(1).getId()).isEqualTo("uuid-2");
//    }
//
//    @Test
//    @DisplayName("getPlansSorted should return list of PlanDto when sortBy is priceDesc")
//    void getPlansSorted_priceDesc_shouldReturnPlanDtoList() {
//        // given
//        Plan plan1 = Plan.builder().planId("uuid-1").name("Plan 1").price(30000).description("Desc 1").dataAmount(10000L).callAmount(500L).smsAmount(200L).createdAt(LocalDateTime.now()).build();
//        Plan plan2 = Plan.builder().planId("uuid-2").name("Plan 2").price(50000).description("Desc 2").dataAmount(20000L).callAmount(1000L).smsAmount(500L).createdAt(LocalDateTime.now()).build();
//
//        Mockito.when(planRepository.findAllByOrderByPriceDesc()).thenReturn(Arrays.asList(plan1, plan2));
//
//        // when
//        List<PlanCreateDto> result = planService.getPlansSorted("priceDesc");
//
//        // then
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getId()).isEqualTo("uuid-1");
//        assertThat(result.get(1).getId()).isEqualTo("uuid-2");
//    }
//
//    @Test
//    @DisplayName("getPlansSorted should return list of PlanDto when sortBy is dataAsc")
//    void getPlansSorted_dataAsc_shouldReturnPlanDtoList() {
//        // given
//        Plan plan1 = Plan.builder().planId("uuid-1").name("Plan 1").price(30000).description("Desc 1").dataAmount(10000L).callAmount(500L).smsAmount(200L).createdAt(LocalDateTime.now()).build();
//        Plan plan2 = Plan.builder().planId("uuid-2").name("Plan 2").price(50000).description("Desc 2").dataAmount(20000L).callAmount(1000L).smsAmount(500L).createdAt(LocalDateTime.now()).build();
//
//        Mockito.when(planRepository.findAllByOrderByDataAmountAsc()).thenReturn(Arrays.asList(plan1, plan2));
//
//        // when
//        List<PlanCreateDto> result = planService.getPlansSorted("dataAsc");
//
//        // then
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getId()).isEqualTo("uuid-1");
//        assertThat(result.get(1).getId()).isEqualTo("uuid-2");
//    }
//
//    @Test
//    @DisplayName("getPlansSorted should return list of PlanDto when sortBy is dataDesc")
//    void getPlansSorted_dataDesc_shouldReturnPlanDtoList() {
//        // given
//        Plan plan1 = Plan.builder().planId("uuid-1").name("Plan 1").price(30000).description("Desc 1").dataAmount(10000L).callAmount(500L).smsAmount(200L).createdAt(LocalDateTime.now()).build();
//        Plan plan2 = Plan.builder().planId("uuid-2").name("Plan 2").price(50000).description("Desc 2").dataAmount(20000L).callAmount(1000L).smsAmount(500L).createdAt(LocalDateTime.now()).build();
//
//        Mockito.when(planRepository.findAllByOrderByDataAmountDesc()).thenReturn(Arrays.asList(plan1, plan2));
//
//        // when
//        List<PlanCreateDto> result = planService.getPlansSorted("dataDesc");
//
//        // then
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getId()).isEqualTo("uuid-1");
//        assertThat(result.get(1).getId()).isEqualTo("uuid-2");
//    }
//
//    @Test
//    @DisplayName("getPlansSorted should return list of PlanDto when sortBy is popular")
//    void getPlansSorted_popular_shouldReturnPlanDtoList() {
//        // given
//        Plan plan1 = Plan.builder().planId("uuid-1").name("Plan 1").price(30000).description("Desc 1").dataAmount(10000L).callAmount(500L).smsAmount(200L).createdAt(LocalDateTime.now()).build();
//        Plan plan2 = Plan.builder().planId("uuid-2").name("Plan 2").price(50000).description("Desc 2").dataAmount(20000L).callAmount(1000L).smsAmount(500L).createdAt(LocalDateTime.now()).build();
//
//        Mockito.when(planRepository.findPopularPlans()).thenReturn(Arrays.asList(plan1, plan2));
//
//        // when
//        List<PlanCreateDto> result = planService.getPlansSorted("popular");
//
//        // then
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getId()).isEqualTo("uuid-1");
//        assertThat(result.get(1).getId()).isEqualTo("uuid-2");
//    }
//
//    @Test
//    @DisplayName("getPlansSorted should fallback to popular when sortBy is unknown")
//    void getPlansSorted_unknownSortBy_shouldFallbackToPopular() {
//        // given
//        Plan plan1 = Plan.builder().planId("uuid-1").name("Plan 1").price(30000).description("Desc 1").dataAmount(10000L).callAmount(500L).smsAmount(200L).createdAt(LocalDateTime.now()).build();
//        Plan plan2 = Plan.builder().planId("uuid-2").name("Plan 2").price(50000).description("Desc 2").dataAmount(20000L).callAmount(1000L).smsAmount(500L).createdAt(LocalDateTime.now()).build();
//
//        Mockito.when(planRepository.findPopularPlans()).thenReturn(Arrays.asList(plan1, plan2));
//
//        // when
//        List<PlanCreateDto> result = planService.getPlansSorted("unknownSort");
//
//        // then
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getId()).isEqualTo("uuid-1");
//        assertThat(result.get(1).getId()).isEqualTo("uuid-2");
//    }
//
//    // 요금제 상세 페이지 조회
//    @Test
//    @DisplayName("getPlanDetail should return PlanDetailDto when plan exists")
//    void getPlanDetail_shouldReturnPlanDetailDto() {
//        // given
//        String planId = "uuid-1";
//
//        Plan plan = Plan.builder()
//                .planId(planId)
//                .name("Plan 1")
//                .price(30000)
//                .description("Description 1")
//                .dataAmount(10000L)
//                .callAmount(500L)
//                .smsAmount(200L)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        Mockito.when(planRepository.findById(planId)).thenReturn(java.util.Optional.of(plan));
//
//        // when
//        PlanDetailDto result = planService.getPlanDetail(planId);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo(planId);
//        assertThat(result.getName()).isEqualTo("Plan 1");
//        assertThat(result.getPrice()).isEqualTo(30000);
//    }
//}
