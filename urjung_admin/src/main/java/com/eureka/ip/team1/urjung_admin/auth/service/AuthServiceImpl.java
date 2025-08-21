package com.eureka.ip.team1.urjung_admin.auth.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eureka.ip.team1.urjung_admin.admin.dto.AdminDto;
import com.eureka.ip.team1.urjung_admin.admin.entity.Admin;
import com.eureka.ip.team1.urjung_admin.admin.repository.AdminRepository;
import com.eureka.ip.team1.urjung_admin.auth.dto.AuthResultDto;
import com.eureka.ip.team1.urjung_admin.auth.dto.RefreshToken;
import com.eureka.ip.team1.urjung_admin.auth.dto.TokenDto;
import com.eureka.ip.team1.urjung_admin.auth.jwt.TokenProvider;
import com.eureka.ip.team1.urjung_admin.auth.repository.RefreshTokenRepository;
import com.eureka.ip.team1.urjung_admin.common.ApiResponse;
import com.eureka.ip.team1.urjung_admin.common.enums.Result;
import com.eureka.ip.team1.urjung_admin.common.exception.NotFoundException;
import com.eureka.ip.team1.urjung_admin.common.exception.UnAuthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
	
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;

    private final AdminRepository adminRepository;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
	
	@Override
	public ApiResponse<AuthResultDto> login(String email, String password) {
		AuthResultDto loginResultDto = new AuthResultDto();
		
		log.debug("login start");
		
		try {
			Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(email, password) );
			
			log.debug("create token");
			
	        Admin admin = adminRepository.findByEmail(email)
	                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
	        String adminId = admin.getAdminId();
	        
			TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

	        refreshTokenService.save(adminId, tokenDto.getRefreshToken());
	        
			log.debug("created token : " + tokenDto);
			
			log.debug("login end");
			
	        loginResultDto.setResult("success");
	        loginResultDto.setAccessToken(tokenDto.getAccessToken());
	        loginResultDto.setAccessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn());
	        loginResultDto.setRefreshToken(tokenDto.getRefreshToken());
	        
	        return ApiResponse.<AuthResultDto>builder()
	                .result(Result.SUCCESS)
	                .data(loginResultDto)
	                .message("Login successful")
	                .build();
		} catch(Exception e) {
			log.debug("login failed: ", e.getMessage());
			
	        return ApiResponse.<AuthResultDto>builder()
	                .result(Result.FAIL)
	                .message("Login failed: " + e.getMessage())
	                .build();
		}
		
	}

	@Override
	public ApiResponse<AuthResultDto> signup(AdminDto adminDto) {
	    log.debug("Signup start");

	    try {
	        Optional<Admin> existUser = adminRepository.findByEmail(adminDto.getEmail());
	        if (existUser.isPresent()) {
	            return ApiResponse.<AuthResultDto>builder()
	                    .result(Result.FAIL)
	                    .message("Email already exists")
	                    .build();
	        }
	        
	        Admin admin = Admin.builder()
	                .name(adminDto.getName())
	                .email(adminDto.getEmail())
	                .password(passwordEncoder.encode(adminDto.getPassword())) // 비밀번호 암호화
	                .build();

	        adminRepository.save(admin);
	        
	        System.out.println("admin :" + admin);
	        
	        AuthResultDto authResultDto = new AuthResultDto();
	        authResultDto.setResult("success");

	        log.debug("Signup successful");

	        return ApiResponse.<AuthResultDto>builder()
	                .result(Result.SUCCESS)
	                .data(authResultDto)
	                .message("Signup successful")
	                .build();
	    } catch (Exception e) {
	        log.error("Signup error: ", e);

	        return ApiResponse.<AuthResultDto>builder()
	                .result(Result.FAIL)
	                .message("An error occurred during signup")
	                .build();
	    }
	}

	@Override
	public ApiResponse<AuthResultDto> logout(String RefreshToken) {
	    try {
			RefreshToken saved = refreshTokenRepository.findById(RefreshToken)
					.orElseThrow(() -> new NotFoundException("불일치" + RefreshToken));
			
			refreshTokenRepository.delete(saved);
	        
	        AuthResultDto authResultDto = new AuthResultDto();
	        authResultDto.setResult("success");

	        log.debug("Logout successful");

	        return ApiResponse.<AuthResultDto>builder()
	                .result(Result.SUCCESS)
	                .data(authResultDto)
	                .message("Logout successful")
	                .build();
	    } catch (Exception e) {
	        log.error("Logout error: ", e);

	        return ApiResponse.<AuthResultDto>builder()
	                .result(Result.FAIL)
	                .message("An error occurred during logout")
	                .build();
	    }
	}
	
	@Override
	public ApiResponse<AuthResultDto> reissue(String oldRefreshToken) {
	    log.debug("Reissue start");

	    try {
			if (!tokenProvider.validateToken(oldRefreshToken)) {
				throw new UnAuthorizedException();
			}
			System.out.println("RefreshToken : " + oldRefreshToken);
			RefreshToken saved = refreshTokenRepository.findById(oldRefreshToken)
					.orElseThrow(() -> new NotFoundException("없는 토큰" + oldRefreshToken));
			
			String adminId = saved.getAdminId();
			
			String newAccessToken = tokenProvider.createAccessToken(adminId);
			String newRefreshToken = tokenProvider.createRefreshToken(adminId);
			
			refreshTokenRepository.delete(saved);
			refreshTokenRepository.save(new RefreshToken(newRefreshToken, adminId));
			
	        TokenDto token = TokenDto.builder()
					.accessToken(newAccessToken)
					.refreshToken(newRefreshToken)
					.accessTokenExpiresIn(tokenProvider.getAccessTokenValidDuration())
					.build();
	        
	        System.out.println("token :" + token);
	        
	        AuthResultDto authResultDto = new AuthResultDto();
	        authResultDto.setResult("success");

	        log.debug("Reissue successful");

	        return ApiResponse.<AuthResultDto>builder()
	                .result(Result.SUCCESS)
	                .data(authResultDto)
	                .message("Reissue successful")
	                .build();
	    } catch (Exception e) {
	        log.error("Reissue error: ", e);

	        return ApiResponse.<AuthResultDto>builder()
	                .result(Result.FAIL)
	                .message("An error occurred during reissue")
	                .build();
	    }
	    
	}

}
