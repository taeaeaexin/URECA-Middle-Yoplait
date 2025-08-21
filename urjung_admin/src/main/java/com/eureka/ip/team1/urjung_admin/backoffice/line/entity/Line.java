package com.eureka.ip.team1.urjung_admin.backoffice.line.entity;

import com.eureka.ip.team1.urjung_admin.backoffice.plan.entity.Plan;
import com.eureka.ip.team1.urjung_admin.backoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "line")
public class Line {
    @Id
    @Column(name = "line_id")
    private String lineId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "plan_id", nullable = false, updatable = false, length = 36)
    private String planId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('active', 'canceled') DEFAULT 'active'")
    private LineStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "discounted_price", nullable = false)
    private int discountedPrice;

    // Plan 연관 객체 (조회용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", insertable = false, updatable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // 생성 시 UUID 자동 세팅
    @PrePersist
    public void onCreate() {
        this.lineId = UUID.randomUUID().toString();
        this.startDate = LocalDateTime.now();
        this.status = LineStatus.active;
    }

    // Enum
    public enum LineStatus {
        active, canceled
    }
}