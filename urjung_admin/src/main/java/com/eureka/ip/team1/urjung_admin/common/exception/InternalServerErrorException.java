package com.eureka.ip.team1.urjung_admin.common.exception;


import static com.eureka.ip.team1.urjung_admin.common.Message.SERVER_ERROR;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException() {
        super(SERVER_ERROR);
    }
}
