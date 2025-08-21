package com.eureka.ip.team1.urjung_admin.backoffice.plan.service;

import com.eureka.ip.team1.urjung_admin.backoffice.line.entity.Line;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDetailDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanCreateDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.entity.Plan;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.repository.PlanRepository;
import com.eureka.ip.team1.urjung_admin.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 요금제 목록 ServiceImpl

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    // 요금제 이름, 설명 통합 검색 & 정렬
    @Override
    public List<PlanDto> getPlansSorted(String sortBy, String keyword) {
        List<Plan> plans;

        if(keyword != null && !keyword.isEmpty()) {
            plans = planRepository.searchByNameOrDescription(keyword);
        }else {
            switch (sortBy) {
                case "nameDesc" -> plans = planRepository.findAllByOrderByNameDesc();
                case "nameAsc" -> plans = planRepository.findAllByOrderByNameAsc();
                case "priceDesc" -> plans = planRepository.findAllByOrderByPriceDesc();
                case "priceAsc" -> plans = planRepository.findAllByOrderByPriceAsc();
                case "dataDesc" -> plans = planRepository.findAllByOrderByDataAmountDesc();
                case "dataAsc" -> plans = planRepository.findAllByOrderByDataAmountAsc();
                case "callDesc" -> plans = planRepository.findAllByOrderByCallAmountDesc();
                case "callAsc" -> plans = planRepository.findAllByOrderByCallAmountAsc();
                case "smsAsc" -> plans = planRepository.findAllByOrderBySmsAmountAsc();
                case "smsDesc" -> plans = planRepository.findAllByOrderBySmsAmountDesc();
                case "userDesc" -> plans = planRepository.findAllByOrderByUserAmountDesc();
                case "userAsc" -> plans = planRepository.findAllByOrderByUserAmountAsc();
                default -> plans = planRepository.findAll(); // fallback
            }
        }

        return plans.stream()
                .map(this::convertToDto)
                .toList();
    }

    // PlanDto로 변환
    private PlanDto convertToDto(Plan plan) {
        return PlanDto.builder()
                .id(plan.getPlanId())
                .name(plan.getName())
                .price(plan.getPrice())
                .description(plan.getDescription())
                .dataAmount(plan.getDataAmount())
                .callAmount(plan.getCallAmount())
                .smsAmount(plan.getSmsAmount())
                .userAmount((int) plan.getLines().stream()
                        .filter(line -> line.getStatus() == Line.LineStatus.active)
                        .count())
                .build();
    }

    // PlanCreateDto로 변환
    private PlanCreateDto convertToCreateDto(Plan plan) {
        return PlanCreateDto.builder()
                .id(plan.getPlanId())
                .name(plan.getName())
                .price(plan.getPrice())
                .description(plan.getDescription())
                .dataAmount(plan.getDataAmount())
                .callAmount(plan.getCallAmount())
                .smsAmount(plan.getSmsAmount())
                .build();
    }

    // 요금제 상세 페이지 조회
    @Override
    public PlanDetailDto getPlanDetail(String planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("요금제를 찾을 수 없습니다."));

        return convertToDetailDto(plan);
    }

    // PlanDetailDto로 변환
    private PlanDetailDto convertToDetailDto(Plan plan) {
        // 가입자 점수 식
        double ratio = (double) plan.getLines().stream()
                .filter(line -> line.getStatus() == Line.LineStatus.active)
                .count() / plan.getLines().size();
        double logFactor = Math.log10(planRepository.count() + 1);
        double score = (ratio * logFactor * 1000) / 10.0;

        return PlanDetailDto.builder()
                .id(plan.getPlanId())
                .name(plan.getName())
                .price(plan.getPrice())
                .description(plan.getDescription())
                .dataAmount(plan.getDataAmount())
                .callAmount(plan.getCallAmount())
                .smsAmount(plan.getSmsAmount())
                .userAmount((int) plan.getLines().stream()
                        .filter(line -> line.getStatus() == Line.LineStatus.active)
                        .count())
                .score(score)
                .createdAt(plan.getCreatedAt())
                .build();
    }

    // 요금제 생성
    @Override
    public PlanCreateDto createPlan(PlanCreateDto planCreateDto){
        Plan plan = Plan.builder()
                .name(planCreateDto.getName())
                .price(planCreateDto.getPrice())
                .description(planCreateDto.getDescription())
                .dataAmount(planCreateDto.getDataAmount())
                .callAmount(planCreateDto.getCallAmount())
                .smsAmount(planCreateDto.getSmsAmount())
                .build();

        Plan saved = planRepository.save(plan);

        return convertToCreateDto(saved);
    }

    // 요금제 수정
    @Override
    public PlanCreateDto updatePlan(String planId, PlanCreateDto planCreateDto){
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("요금제를 찾을 수 없습니다."));

        plan.setName(planCreateDto.getName());
        plan.setPrice(planCreateDto.getPrice());
        plan.setDescription(planCreateDto.getDescription());
        plan.setDataAmount(planCreateDto.getDataAmount());
        plan.setCallAmount(planCreateDto.getCallAmount());
        plan.setSmsAmount(planCreateDto.getSmsAmount());

        Plan updated = planRepository.save(plan);

        return convertToCreateDto(updated);
    }

    // 요금제 삭제
    @Override
    public void deletePlan(String planId){
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("요금제를 찾을 수 없습니다."));

        long activeUserCount = plan.getLines().stream()
                .filter(line -> line.getStatus() == Line.LineStatus.active)
                .count();

        if (activeUserCount > 0) {
            throw new IllegalStateException("가입 중인 사용자가 있어 요금제를 삭제할 수 없습니다.");
        }

        planRepository.delete(plan);
    }

    // 모든 Detail Plan (AiPlanAnalysis 전용)
    @Override
    public List<PlanDetailDto> getAllPlans() {
        List<Plan> plans = planRepository.findAll();

        return plans.stream()
                .map(this::convertToDetailDto)
                .toList();
    }

    // 요금제 비교 페이지
//    @Override
//    public List<PlanDetailDto> comparePlans(List<String> planIds) {
//        List<Plan> plans = planRepository.findAllById(planIds);
//
//        // 예외 처리 (선택된 요금제가 부족할 경우)
//        if (plans.size() != planIds.size()) {
//            throw new NotFoundException("일부 요금제를 찾을 수 없습니다.");
//        }
//
//        // 변환 후 리턴
//        return plans.stream()
//                .map(plan -> PlanDetailDto.builder()
//                        .id(plan.getId())
//                        .name(plan.getName())
//                        .price(plan.getPrice())
//                        .description(plan.getDescription())
//                        .dataAmount(plan.getDataAmount())
//                        .callAmount(plan.getCallAmount())
//                        .smsAmount(plan.getSmsAmount())
//                        .createdAt(plan.getCreatedAt())
//                        .build())
//                .toList();
//    }
}
