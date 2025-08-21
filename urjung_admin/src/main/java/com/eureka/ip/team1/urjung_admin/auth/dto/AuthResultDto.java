package com.eureka.ip.team1.urjung_admin.auth.dto;

import com.eureka.ip.team1.urjung_admin.admin.dto.AdminDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResultDto {
	private String result;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;
	private AdminDto adminDto;
}
