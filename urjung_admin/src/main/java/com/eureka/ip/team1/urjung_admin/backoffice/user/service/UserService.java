package com.eureka.ip.team1.urjung_admin.backoffice.user.service;

import com.eureka.ip.team1.urjung_admin.backoffice.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(String sortBy, String keyword);
}
