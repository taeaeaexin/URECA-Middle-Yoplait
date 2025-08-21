package com.eureka.ip.team1.urjung_admin.admin.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.ip.team1.urjung_admin.admin.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String>{
	Optional<Admin> findByEmail(String email);
}
