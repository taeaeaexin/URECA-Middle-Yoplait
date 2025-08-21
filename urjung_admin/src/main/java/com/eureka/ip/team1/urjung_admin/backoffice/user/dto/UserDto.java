package com.eureka.ip.team1.urjung_admin.backoffice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

// 유저 Dto
@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String email;
    private String name;
    private int subscribedCount;
}
