package com.eureka.ip.team1.urjung_admin.common.exception;

// 잘못된 입력값 => 400
public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String message) {
        super(message);
    }
}
