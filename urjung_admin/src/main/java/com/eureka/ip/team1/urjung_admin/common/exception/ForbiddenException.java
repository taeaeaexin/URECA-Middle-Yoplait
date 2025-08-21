package com.eureka.ip.team1.urjung_admin.common.exception;


import static com.eureka.ip.team1.urjung_admin.common.Message.FORBIDDEN;

// 권한이 없는 사용자 접근 => 403
public class ForbiddenException extends RuntimeException{
    public ForbiddenException() {
        super(FORBIDDEN);
    }
}
