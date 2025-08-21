package com.eureka.ip.team1.urjung_admin.backoffice.user.entity;

import com.eureka.ip.team1.urjung_admin.backoffice.membership.entity.Membership;
import com.eureka.ip.team1.urjung_admin.backoffice.membership.repository.MembershipRepository;
import com.eureka.ip.team1.urjung_admin.backoffice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// 추후에 membership 조건 확인
@DataJpaTest
@Transactional
public class UserEntityTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    @DisplayName("User 저장 테스트 - UUID 자동 생성 및 Membership 연관 관계 확인")
    public void saveUserTestWithMembership() {
        // given
        Membership membership = Membership.builder()
                .membershipName("basic")
                .require(0)
                .giftDiscount(0.0)
                .build();
        membership = membershipRepository.save(membership);

        User user = User.builder()
                .name("유레카")
                .email("ureca@ureca.com")
                .password("test")
                .gender("M")
                .birth(LocalDate.of(1999, 1, 7))
                .membership(membership)
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        // UUID 자동 생성 확인
        assertThat(savedUser.getUserId()).isNotNull();
        // membership 생성 및 연관 관계 확인
        assertThat(savedUser.getMembership().getMembershipName()).isEqualTo("basic");
    }
}
