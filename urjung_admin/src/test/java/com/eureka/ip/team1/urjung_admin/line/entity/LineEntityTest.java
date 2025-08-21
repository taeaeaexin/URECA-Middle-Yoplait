package com.eureka.ip.team1.urjung_admin.line.entity;

import com.eureka.ip.team1.urjung_admin.backoffice.line.entity.Line;
import com.eureka.ip.team1.urjung_admin.backoffice.membership.entity.Membership;
import com.eureka.ip.team1.urjung_admin.backoffice.plan.entity.Plan;
import com.eureka.ip.team1.urjung_admin.backoffice.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class LineEntityTest {
    // Line Repository 없음
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("Line 저장 테스트 - EntityManager 사용")
    public void testLineEntity() {
        // Membership
        Membership membership = Membership.builder()
                .membershipName("basic")
                .require(0)
                .giftDiscount(0.0)
                .build();
        em.persist(membership);

        // User
        User user = User.builder()
                .name("유레카")
                .email("ureca@ureca.com")
                .password("password")
                .gender("M")
                .birth(LocalDate.of(1999, 1, 7))
                .membership(membership)
                .build();
        em.persist(user);

        // Plan
        Plan plan = Plan.builder()
                .name("Test Plan")
                .price(12_900)
                .description("Test Description")
                .dataAmount(100L)
                .callAmount(200L)
                .smsAmount(300L)
                .build();
        em.persist(plan);


        Line line = new Line();
        line.setUserId(user.getUserId());
        line.setPlanId(plan.getPlanId());
        line.setStatus(Line.LineStatus.active);
        line.setPhoneNumber("010-0101-0101");
        line.setDiscountedPrice("100");

        em.persist(line);

        // then
        assertThat(line.getLineId()).isNotNull();
        assertThat(line.getStatus()).isEqualTo(Line.LineStatus.active);
        assertThat(line.getPhoneNumber()).isEqualTo("010-0101-0101");
        assertThat(line.getUserId()).isEqualTo(user.getUserId());
        assertThat(line.getPlanId()).isEqualTo(plan.getPlanId());
    }
}
