package com.eureka.ip.team1.urjung_admin.backoffice.plan.tag.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tag")
public class Tag {
    @Id
    @Column(name="tag_id", nullable = false, length = 36)
    private String tagId;

    @Column(name="tag_name", nullable = false)
    private String tagName;

    @Column(name="description")
    private String tagDescription;

    // plan_tag
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanTag> planTags = new ArrayList<>();

    // 생성 시 UUID 자동 세팅
    @PrePersist
    public void generateId() {
        this.tagId = UUID.randomUUID().toString();
    }
}
