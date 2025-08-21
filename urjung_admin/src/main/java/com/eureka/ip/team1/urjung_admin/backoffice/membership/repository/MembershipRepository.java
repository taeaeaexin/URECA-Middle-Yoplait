package com.eureka.ip.team1.urjung_admin.backoffice.membership.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.ip.team1.urjung_admin.backoffice.membership.entity.Membership;

public interface MembershipRepository extends JpaRepository<Membership, UUID>{
	Optional<Membership> findByMembershipName(String MembershipName);
}
