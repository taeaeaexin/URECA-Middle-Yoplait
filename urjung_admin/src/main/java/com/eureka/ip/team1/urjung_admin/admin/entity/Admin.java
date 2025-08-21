package com.eureka.ip.team1.urjung_admin.admin.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="admin") // 임시 테이블 명
public class Admin {
	@Id
	@Column(updatable = false, nullable = false, length = 36)
	private String adminId;
    
	@Column(nullable = false)
	private String name;
    
	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;
	
    @PrePersist
    public void generateId() {
        this.adminId = UUID.randomUUID().toString();
    }
}
