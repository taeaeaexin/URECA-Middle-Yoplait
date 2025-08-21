package com.eureka.ip.team1.urjung_admin.backoffice.user.service;

import com.eureka.ip.team1.urjung_admin.backoffice.user.dto.UserDto;
import com.eureka.ip.team1.urjung_admin.backoffice.user.entity.User;
import com.eureka.ip.team1.urjung_admin.backoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

	// 유저 email, 이름 통합 검색 & 정렬
	@Override
	public List<UserDto> getAllUsers(String sortBy, String keyword) {
		List<User> users;

		if(keyword != null && !keyword.isEmpty()) {
			users = userRepository.searchByNameOrEmail(keyword);
		}else {
			switch (sortBy) {
				case "emailAsc" -> users = userRepository.findAllByOrderByEmailAsc();
				case "emailDesc" -> users = userRepository.findAllByOrderByEmailDesc();
				case "nameAsc" -> users = userRepository.findAllByOrderByNameAsc();
				case "nameDesc" -> users = userRepository.findAllByOrderByNameDesc();
				case "serviceAsc" -> users = userRepository.findAllByOrderServiceCountAsc();
				case "serviceDesc" -> users = userRepository.findAllByOrderServiceCountDesc();
				default -> users = userRepository.findAll(); // fallback
			}
		}

		return users.stream()
				.map(this::convertToDto)
				.toList();
	}

	private UserDto convertToDto(User user) {
		return UserDto.builder()
				.id(user.getUserId())
				.email(user.getEmail())
				.name(user.getName())
				.subscribedCount(user.getLines().size())
				.build();
	}
}
