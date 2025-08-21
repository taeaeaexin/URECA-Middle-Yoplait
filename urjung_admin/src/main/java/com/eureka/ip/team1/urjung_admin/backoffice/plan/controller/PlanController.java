package com.eureka.ip.team1.urjung_admin.backoffice.plan.controller;

import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDetailDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanCreateDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.dto.PlanDto;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.service.PlanService;
import com.eureka.ip.team1.urjung_admin.common.ApiResponse;
import com.eureka.ip.team1.urjung_admin.common.enums.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    // 요금제 목록 조회 필터 API
    @GetMapping
    public ResponseEntity<?> getPlans(
            @RequestParam(defaultValue = "") String sortBy,
            @RequestParam(required = false) String keyword
    ) {
        List<PlanDto> plans = planService.getPlansSorted(sortBy, keyword);

        return ResponseEntity.ok(
                ApiResponse.<List<PlanDto>>builder()
                        .message("SUCCESS")
                        .result(Result.SUCCESS)
                        .data(plans)
                        .build()
        );
    }

    // 특정 요금제 상세 조회 API
    @GetMapping("/{planId}")
    public ResponseEntity<ApiResponse<PlanDetailDto>> getPlanDetail(@PathVariable String planId) {
        PlanDetailDto planDetail = planService.getPlanDetail(planId);

        return ResponseEntity.ok(
                ApiResponse.<PlanDetailDto>builder()
                        .message("SUCCESS")
                        .result(Result.SUCCESS)
                        .data(planDetail)
                        .build()
            );
    }

    // 요금제 생성 API
    @PostMapping()
    public ResponseEntity<ApiResponse<PlanCreateDto>> createPlan(@RequestBody PlanCreateDto planCreateDto) {
        PlanCreateDto createPlan = planService.createPlan(planCreateDto);

        return ResponseEntity.ok(
                ApiResponse.<PlanCreateDto>builder()
                        .message("SUCCESS")
                        .result(Result.SUCCESS)
                        .data(createPlan)
                        .build()
        );
    }

    // 요금제 수정 API
    @PutMapping("/{planId}")
    public ResponseEntity<ApiResponse<PlanCreateDto>> updatePlan(@PathVariable String planId, @RequestBody PlanCreateDto planUpdateDto)  {
        PlanCreateDto updatedPlan = planService.updatePlan(planId, planUpdateDto);

        return ResponseEntity.ok(
                ApiResponse.<PlanCreateDto>builder()
                        .message("SUCCESS")
                        .result(Result.SUCCESS)
                        .data(updatedPlan)
                        .build()
        );
    }

    // 요금제 삭제 API
    @DeleteMapping("/{planId}")
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable String planId){
        try {
            planService.deletePlan(planId);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .message("SUCCESS")
                            .result(Result.SUCCESS)
                            .build()
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Void>builder()
                            .message(e.getMessage())
                            .result(Result.FAIL)
                            .build()
            );
        }
    }

    // 요금제 비교 API
//    @GetMapping("/compare")
//    public ResponseEntity<ApiResponse<List<PlanDetailDto>>> comparePlans(@RequestParam List<String> planIds) {
//        List<PlanDetailDto> plans = planService.comparePlans(planIds);
//        return ResponseEntity.ok(ApiResponse.<List<PlanDetailDto>>builder()
//                .result(Result.SUCCESS)
//                .data(plans)
//                .message("SUCCESS")
//                .build());
//    }
}