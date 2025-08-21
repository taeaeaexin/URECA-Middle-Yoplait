package com.eureka.ip.team1.urjung_admin.backoffice.plan.dto;

import lombok.*;

// 요금제 Dto
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {
    private String id;
    private String name;
    private int price;
    private String description;
    private Long dataAmount;
    private Long callAmount;
    private Long smsAmount;
    private String tag;

    private int userAmount; // 가입자 수
}
