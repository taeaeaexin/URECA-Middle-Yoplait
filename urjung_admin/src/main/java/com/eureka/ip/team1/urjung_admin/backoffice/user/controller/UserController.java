package com.eureka.ip.team1.urjung_admin.backoffice.user.controller;

import com.eureka.ip.team1.urjung_admin.backoffice.user.dto.UserDto;
import com.eureka.ip.team1.urjung_admin.backoffice.user.service.UserService;
import com.eureka.ip.team1.urjung_admin.common.ApiResponse;
import com.eureka.ip.team1.urjung_admin.common.enums.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 유저 이메일, 이름 통합 검색 & 정렬 API
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "emailAsc") String sortBy,
            @RequestParam(required = false) String keyword
    ) {
        List<UserDto> users = userService.getAllUsers(sortBy, keyword);

        return ResponseEntity.ok(
                ApiResponse.<List<UserDto>>builder()
                        .message("SUCCESS")
                        .result(Result.SUCCESS)
                        .data(users)
                        .build()
        );
    }
}
