package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestForbidden {
    private String word;
    private String wordDesc;
    private String wordClass;
    private String adminId; // 추후 수정 예정
}
