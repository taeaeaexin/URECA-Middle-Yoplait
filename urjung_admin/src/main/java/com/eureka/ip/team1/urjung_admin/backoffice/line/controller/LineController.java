package com.eureka.ip.team1.urjung_admin.backoffice.line.controller;

import com.eureka.ip.team1.urjung_admin.backoffice.line.dto.LineDto;
import com.eureka.ip.team1.urjung_admin.backoffice.line.service.LineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lines")
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<LineDto>> getAllLinesByUserId(@PathVariable String userId) {
        List<LineDto> lines = lineService.getAllLinesByUserId(userId);
        return ResponseEntity.ok(lines);
    }

}
