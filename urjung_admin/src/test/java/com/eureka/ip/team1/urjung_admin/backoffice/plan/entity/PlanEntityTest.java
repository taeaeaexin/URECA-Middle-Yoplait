package com.eureka.ip.team1.urjung_admin.backoffice.plan.entity;

import com.eureka.ip.team1.urjung_admin.backoffice.plan.repository.PlanRepository;
import com.eureka.ip.team1.urjung_admin.backoffice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Transactional
public class PlanEntityTest {
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Plan 저장 테스트 - UUID 자동 생성")
    public void savePlanTest() {
        // given
        Plan plan = Plan.builder()
                .name("Test Plan")
                .price(29_900)
                .description("Test Description")
                .dataAmount(100L)
                .callAmount(200L)
                .smsAmount(300L)
//                .createdAt(LocalDateTime.now()) // @PrePersist 때문에 필요 없음
                .build();

        // when
        Plan savedPlan = planRepository.save(plan);

        // then
        assertThat(savedPlan.getPlanId()).isNotNull();
        assertThat(savedPlan.getName()).isEqualTo("Test Plan");
        assertThat(savedPlan.getPrice()).isEqualTo(29_900);
        assertThat(savedPlan.getDescription()).isEqualTo("Test Description");
        assertThat(savedPlan.getDataAmount()).isEqualTo(100L);
        assertThat(savedPlan.getCallAmount()).isEqualTo(200L);
        assertThat(savedPlan.getSmsAmount()).isEqualTo(300L);
        assertThat(savedPlan.getCreatedAt()).isNotNull();
    }
}
