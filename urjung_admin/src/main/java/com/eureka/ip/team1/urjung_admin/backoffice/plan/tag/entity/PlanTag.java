package com.eureka.ip.team1.urjung_admin.backoffice.plan.tag.entity;

import com.eureka.ip.team1.urjung_admin.backoffice.plan.entity.Plan;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "plan_tag")
public class PlanTag {
    @Id
    @Column(name = "plan_tag_id", nullable = false, length = 36)
    private String planTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // 생성 시 UUID 자동 세팅
    @PrePersist
    public void generateId() {
        this.planTagId = UUID.randomUUID().toString();
    }
}
