package com.eureka.ip.team1.urjung_admin.auth.service;

import java.util.UUID;

import com.eureka.ip.team1.urjung_admin.auth.dto.RefreshToken;

public interface RefreshTokenService {
	RefreshToken save(String adminId, String refreshToken);
}
