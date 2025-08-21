package com.eureka.ip.team1.urjung_admin.backoffice.plan.tag.entity;

import com.eureka.ip.team1.urjung_admin.backoffice.plan.entity.Plan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class PlanTagEntityTest {
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("PlanTag 저장 테스트 - Plan과 Tag 연관 매핑 및 UUID 확인")
    public void savePlanTagTest() {
        // given
        Plan plan = Plan.builder()
                .name("Test Plan")
                .price(39000)
                .description("Test Description")
                .dataAmount(100L)
                .callAmount(1000L)
                .smsAmount(500L)
                .build();
        em.persist(plan);

        Tag tag = new Tag();
        tag.setTagName("Test Tag");
        tag.setTagDescription("Test Description");
        em.persist(tag);

        PlanTag planTag = new PlanTag();
        planTag.setPlan(plan);
        planTag.setTag(tag);

        // when
        em.persist(planTag);

        //then
        assertThat(planTag.getPlanTagId()).isNotNull();
        assertThat(planTag.getPlan().getName()).isEqualTo("Test Plan");
        assertThat(planTag.getTag().getTagName()).isEqualTo("Test Tag");
    }

}
