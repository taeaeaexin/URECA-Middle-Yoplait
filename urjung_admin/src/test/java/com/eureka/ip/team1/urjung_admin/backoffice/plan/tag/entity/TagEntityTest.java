package com.eureka.ip.team1.urjung_admin.backoffice.plan.tag.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class TagEntityTest {
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("Tag 저장 테스트 - UUID 자동 생성")
    public void saveTagTest() {
        // given
        Tag tag = new Tag();
        tag.setTagName("Test Tag");
        tag.setTagDescription("Test Description");

        // when
        em.persist(tag);

        //then
        assertThat(tag.getTagId()).isNotNull();
        assertThat(tag.getTagName()).isEqualTo("Test Tag");
        assertThat(tag.getTagDescription()).isEqualTo("Test Description");
        assertThat(tag.getPlanTags()).isEmpty();
    }
}
