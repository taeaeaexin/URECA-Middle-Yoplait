package com.eureka.ip.team1.urjung_admin.common.exception;


import static com.eureka.ip.team1.urjung_admin.common.Message.UNAUTHORIZED;

// 로그인 하지 않은 사용자가 api 요청할 경우 => 401
public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException() {
        super(UNAUTHORIZED);
    }
}
