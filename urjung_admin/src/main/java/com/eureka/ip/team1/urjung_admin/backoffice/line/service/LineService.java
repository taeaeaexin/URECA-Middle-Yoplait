package com.eureka.ip.team1.urjung_admin.backoffice.line.service;

import com.eureka.ip.team1.urjung_admin.backoffice.line.dto.LineDto;

import java.util.List;

public interface LineService {
    List<LineDto> getAllLinesByUserId(String userId);
}
