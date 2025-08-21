package com.eureka.ip.team1.urjung_admin.common;

import com.eureka.ip.team1.urjung_admin.common.enums.Result;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private Result result;
    private T data;
    private String message;
}
