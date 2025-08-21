package com.eureka.ip.team1.urjung_admin.backoffice.membership.entity;

import com.eureka.ip.team1.urjung_admin.backoffice.membership.repository.MembershipRepository;
import com.eureka.ip.team1.urjung_admin.backoffice.user.entity.User;
import com.eureka.ip.team1.urjung_admin.backoffice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class MembershipEntityTest {
    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Membership 저장 테스트 - UUID 자동 생성")
    public void saveMembershipWithUserTest() {
        // given
        Membership membership = Membership.builder()
                .membershipName("basic")
                .require(0)
                .giftDiscount(0.0)
                .build();

        // when
        Membership savedMembership = membershipRepository.save(membership);

        // then
        // UUID 자동 생성 확인
        assertThat(savedMembership.getMembershipId()).isNotNull();
    }
}
