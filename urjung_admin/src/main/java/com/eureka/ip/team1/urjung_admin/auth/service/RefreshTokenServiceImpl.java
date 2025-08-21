package com.eureka.ip.team1.urjung_admin.auth.service;

import org.springframework.stereotype.Service;

import com.eureka.ip.team1.urjung_admin.auth.dto.RefreshToken;
import com.eureka.ip.team1.urjung_admin.auth.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public RefreshToken save(String adminId, String refreshToken) {

        RefreshToken token = new RefreshToken(refreshToken, adminId);
        return refreshTokenRepository.save(token);
	}

}
