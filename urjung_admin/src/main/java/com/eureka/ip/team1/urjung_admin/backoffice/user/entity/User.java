package com.eureka.ip.team1.urjung_admin.backoffice.user.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eureka.ip.team1.urjung_admin.backoffice.line.entity.Line;
import com.eureka.ip.team1.urjung_admin.backoffice.membership.entity.Membership;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id", updatable = false, nullable = false, length = 36)
	private String userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "membership_id", nullable = false)
	private Membership membership;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "birth", nullable = false)
	private LocalDate birth;

	// line
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Line> lines = new ArrayList<>();

	// 생성 시 UUID 자동 세팅
	@PrePersist
	public void generateId() {
		this.userId = UUID.randomUUID().toString();
	}
}
