package com.eureka.ip.team1.urjung_admin.admin.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class AdminDto {
	private UUID adminId;
	private String name;
	private String email;
	private String password;
}
