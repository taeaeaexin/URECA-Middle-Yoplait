package com.eureka.ip.team1.urjung_admin.backoffice.plan.entity;

// 요금제 전체 목록, 상세 조회
import com.eureka.ip.team1.urjung_admin.backoffice.line.entity.Line;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.tag.entity.PlanTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "plan")
public class Plan {
    @Id
    @Column(name = "plan_id", nullable = false, length = 36)
    private String planId;

    @Column(name = "plan_name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "description")
    private String description;

    @Column(name = "data_amount")
    private Long dataAmount;

    @Column(name = "call_amount")
    private Long callAmount;

    @Column(name = "sms_amount")
    private Long smsAmount;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    // Line
    @OneToMany(mappedBy = "plan",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Line> lines = new ArrayList<>();

    // PlanTag
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanTag> planTags = new ArrayList<>();

    // 생성 시 UUID 자동 세팅
    @PrePersist
    public void generateId() {
        this.planId = UUID.randomUUID().toString();
        this.createdAt = java.time.LocalDateTime.now();
    }
}