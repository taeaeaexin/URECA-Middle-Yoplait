package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.service;

import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.RequestForbidden;
import com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto.ResponseForbidden;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

public interface ForbiddenWordService {
    ResponseForbidden createForbidden(RequestForbidden requestForbidden);
    ResponseForbidden updateForbidden(UUID id, RequestForbidden request);
    void deleteForbidden(UUID id);
    List<ResponseForbidden> getForbidden();
    List<ResponseForbidden> searchForbidden(String word, String wordClass);
    ResponseForbidden getForbiddenById(UUID id);
}
