package com.eureka.ip.team1.urjung_admin.auth.service;

import com.eureka.ip.team1.urjung_admin.admin.dto.AdminDto;
import com.eureka.ip.team1.urjung_admin.auth.dto.AuthResultDto;
import com.eureka.ip.team1.urjung_admin.common.ApiResponse;

public interface AuthService {
	ApiResponse<AuthResultDto> login(String email, String password);
	ApiResponse<AuthResultDto> signup(AdminDto adminDto);
	ApiResponse<AuthResultDto> logout(String RefreshToken);
	
	
	ApiResponse<AuthResultDto> reissue(String oldRefreshToken);
}
