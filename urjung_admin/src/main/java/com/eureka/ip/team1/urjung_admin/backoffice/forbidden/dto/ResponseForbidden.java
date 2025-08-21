package com.eureka.ip.team1.urjung_admin.backoffice.forbidden.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseForbidden {
    private UUID wordId;
    private String word;
    private String wordDesc;
    private String wordClass;
    private LocalDateTime wordUpdate;

}
