package com.eureka.ip.team1.urjung_admin.backoffice.plan.dto;

import lombok.*;

import java.time.LocalDateTime;

// 요금제 상세 페이지 Dto
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDetailDto {
    private String id;
    private String name;
    private int price;
    private String description;
    private Long dataAmount;
    private Long callAmount;
    private Long smsAmount;
    private String tag;
    private LocalDateTime createdAt;

    private int userAmount; // 가입자 수
    private String aiAnalysis; // ai 분석
    private double score; // 점수 (ai 분석에 이용)
}